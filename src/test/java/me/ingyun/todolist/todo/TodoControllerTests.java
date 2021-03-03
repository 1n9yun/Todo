package me.ingyun.todolist.todo;

import com.fasterxml.jackson.core.type.TypeReference;
import me.ingyun.todolist.account.Account;
import me.ingyun.todolist.account.AccountRole;
import me.ingyun.todolist.common.AppProperties;
import me.ingyun.todolist.common.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TodoControllerTests extends BaseTest {
    //    @MockBean
    @Autowired
    TodoRepository todoRepository;

    @Autowired
    AppProperties appProperties;

    @BeforeEach
    public void setUp(){
        Todo todo = Todo.builder()
                .owner(appProperties.getUserUsername())
                .title("Test 1")
                .startDate(LocalDate.of(2021, 3, 1))
                .endDate(LocalDate.of(2021, 6, 6))
                .build();
        todo = this.todoRepository.save(todo);
        basicId = todo.getId();
    }
    private Integer basicId = 0;

    private String getUserAccessToken() throws Exception { return getAccessToken(AccountRole.USER); }
    private String getAdminAccessToken() throws Exception { return getAccessToken(AccountRole.ADMIN); }
    private String getAccessToken(AccountRole role) throws Exception {
        var perform = mockMvc.perform(post("/oauth/token")
                .with(httpBasic(appProperties.getClientId(), appProperties.getClientSecret()))
                .param("username", role.equals(AccountRole.ADMIN) ? appProperties.getAdminUsername() : appProperties.getUserUsername())
                .param("password", role.equals(AccountRole.ADMIN) ? appProperties.getAdminPassword() : appProperties.getUserPassword())
                .param("grant_type", "password")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());

        var responseBody = perform.andReturn().getResponse().getContentAsString();
        Map<String, Object> map = objectMapper.readValue(responseBody, new TypeReference<>() {});
        return map.get("token_type") + " " + map.get("access_token");
    }

    @Test
    @DisplayName("createTodo - 정상적으로 Todo를 만드는 테스트")
    public void createTodo() throws Exception {
        TodoDto todoDto = TodoDto.builder()
                .title("test")
                .startDate(LocalDate.of(2021, 2, 21))
                .endDate(LocalDate.of(2021, 2, 23))
                .build();
//        mocking된 respository는 결과로 null을 반납하며 아래와 같이 반환 값을 설정해줘야 한다. (with MockBean)
//        Mockito.when(todoRepository.save(todo)).thenReturn(todo);

        mockMvc.perform(
                post("/api/todo")
                        .header(HttpHeaders.AUTHORIZATION, getUserAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(todoDto))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andDo(document("create-todo",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to docs"),
                                linkWithRel("query-todos").description("link to query todos"),
                                linkWithRel("update-todos").description("link to update todos"),
                                linkWithRel("delete-todos").description("link to delete todos")
//                                ...
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("owner").description("Owner of Todo"),
                                fieldWithPath("completed").description("if completed or not of Todo"),
                                fieldWithPath("title").description("Title of Todo"),
                                fieldWithPath("description").description("Description of Todo"),
                                fieldWithPath("startDate").description("StartDate of Todo"),
                                fieldWithPath("endDate").description("EndDate of Todo"),
                                fieldWithPath("cycle").description("Cycle of Todo"),
                                fieldWithPath("cycleDetail").description("CycleDetail of Todo"),
                                fieldWithPath("largeCategory").description("LargeCategory of Todo"),
                                fieldWithPath("mediumCategory").description("MediumCategory of Todo")
                        ),

                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("ContentType Header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of Todo"),
                                fieldWithPath("owner").description("Owner of Todo"),
                                fieldWithPath("created").description("createdDate of Todo"),
                                fieldWithPath("completed").description("if completed or not of Todo"),
                                fieldWithPath("title").description("Title of Todo"),
                                fieldWithPath("description").description("Description of Todo"),
                                fieldWithPath("startDate").description("StartDate of Todo"),
                                fieldWithPath("endDate").description("EndDate of Todo"),
                                fieldWithPath("cycle").description("Cycle of Todo"),
                                fieldWithPath("cycleDetail").description("CycleDetail of Todo"),
                                fieldWithPath("largeCategory").description("LargeCategory of Todo"),
                                fieldWithPath("mediumCategory").description("MediumCategory of Todo"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to docs"),
                                fieldWithPath("_links.query-todos.href").description("link to query todos"),
                                fieldWithPath("_links.update-todos.href").description("link to update todos"),
                                fieldWithPath("_links.delete-todos.href").description("link to delete todos")
                        )
                ))
        ;
    }

    @Test
    @DisplayName("createTodo - 빈 입력으로인한 400 Bad Request")
    public void createTodoEmptyInput() throws Exception {
        TodoDto todoDto = TodoDto.builder().build();

        this.mockMvc.perform(post(
                "/api/todo")
                .header(HttpHeaders.AUTHORIZATION, getUserAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(todoDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                ;
    }

    @ParameterizedTest
    @DisplayName("createTodo - 잘못된 입력으로 인한 400 Bad Request")
    @CsvSource(value = {
            "test, MONTH, NIL, NIL",
            "test, NIL, 2021-08-21, 2021-07-21"
    }, nullValues = "NIL")
//    public void createTodoWrongInput(@AggregateWith(TodoDtoAggregator.class) TodoDto todoDto) throws Exception {
    public void createTodoWrongInput(ArgumentsAccessor accessor) throws Exception {
        TodoDto todoDto = TodoDto.builder()
                .title(accessor.getString(0))
                .cycle(accessor.get(1, CycleType.class))
                .startDate(accessor.get(2, LocalDate.class))
                .endDate(accessor.get(3, LocalDate.class))
                .build();

        this.mockMvc.perform(post(
                "/api/todo")
                .header(HttpHeaders.AUTHORIZATION, getUserAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoDto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errors[0].objectName").exists())
                .andExpect(jsonPath("errors[0].field").exists())
                .andExpect(jsonPath("errors[0].defaultMessage").exists())
                .andExpect(jsonPath("errors[0].code").exists())
                .andExpect(jsonPath("errors[0].rejectedValue").exists())
                .andDo(print())
                ;
    }

    @Test
    @DisplayName("queryTodo - 정상적으로 하나의 Todo를 조회하는 테스트")
    public void queryTodo() throws Exception {
        System.out.println(todoRepository.findAll().get(0).getId());
        mockMvc.perform(get("/api/todo/{id}", basicId)
                .header(HttpHeaders.AUTHORIZATION, getUserAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andDo(document("query-todo",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to docs"),
                                linkWithRel("update-todo").description("link to update a todo"),
                                linkWithRel("delete-todo").description("link to delete a todo")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type header"),
                                headerWithName(HttpHeaders.ACCEPT).description("accept header")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of this todo"),
                                fieldWithPath("owner").description("owner of this todo"),
                                fieldWithPath("created").description("created date of this todo"),
                                fieldWithPath("completed").description("if completed or not this todo"),
                                fieldWithPath("startDate").description("start date of this todo"),
                                fieldWithPath("endDate").description("end date of this todo"),
                                fieldWithPath("title").description("title of this todo"),
                                fieldWithPath("description").description("description of this todo"),
                                fieldWithPath("cycle").description("cycle type of this todo"),
                                fieldWithPath("cycleDetail").description("cycleDetail of this todo per cycleType"),
                                fieldWithPath("largeCategory").description("largeCategory of this todo"),
                                fieldWithPath("mediumCategory").description("mediumCategory of this todo"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to docs"),
                                fieldWithPath("_links.update-todo.href").description("link to update a todo"),
                                fieldWithPath("_links.delete-todo.href").description("link to delete a todo")
                        )
                ));
    }

    @Test
    @DisplayName("queryTodo - 존재하지 않는 Todo에 404 Not Found")
    public void queryTodoNotFound() throws Exception{
        mockMvc.perform(get("/api/todo/{id}", -1))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("queryTodo - 다른 사람의 Todo를 조회할 때 403 FORBIDDEN")
    public void queryTodoForbidden() throws Exception {
        mockMvc.perform(get("/api/todo/{id}", basicId)
                .header(HttpHeaders.AUTHORIZATION, getAdminAccessToken())
        )
                .andDo(print())
                .andExpect(status().isForbidden())
                ;
    }

    @Test
    @DisplayName("queryTodos - 정상적으로 자신의 모든 Todo를 조회")
    public void queryTodos() throws Exception {
        mockMvc.perform(get("/api/todo")
                .header(HttpHeaders.AUTHORIZATION, getUserAccessToken())
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("content").exists())
                .andDo(document("query-todos",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to docs")
//                                ...
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        responseFields(
                                fieldWithPath("content[].id").description("identifier of this todo"),
                                fieldWithPath("content[].owner").description("owner of this todo"),
                                fieldWithPath("content[].created").description("created date of this todo"),
                                fieldWithPath("content[].completed").description("if completed or not this todo"),
                                fieldWithPath("content[].startDate").description("start date of this todo"),
                                fieldWithPath("content[].endDate").description("end date of this todo"),
                                fieldWithPath("content[].title").description("title of this todo"),
                                fieldWithPath("content[].description").description("description of this todo"),
                                fieldWithPath("content[].cycle").description("cycle type of this todo"),
                                fieldWithPath("content[].cycleDetail").description("cycleDetail of this todo per cycleType"),
                                fieldWithPath("content[].largeCategory").description("largeCategory of this todo"),
                                fieldWithPath("content[].mediumCategory").description("mediumCategory of this todo"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to docs")
                        )
                ));

    }

    @Test
    @DisplayName("updateTodo - 정상적으로 Todo를 수정하는 테스트")
    public void updateTodo() throws Exception{
        Todo todo = todoRepository.findById(basicId).get();
        TodoDto todoDto = modelMapper.map(todo, TodoDto.class);
        todoDto.setTitle("Test 2");

        mockMvc.perform(put("/api/todo/{id}", todo.getId())
                .header(HttpHeaders.AUTHORIZATION, getUserAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(todoDto))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(todo.getId()))
                .andExpect(jsonPath("title").value(todoDto.getTitle()))
                .andDo(document("update-todo",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to docs"),
                                linkWithRel("query-todo").description("link to query a todo"),
                                linkWithRel("delete-todo").description("link to delete a todo")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type header"),
                                headerWithName(HttpHeaders.ACCEPT).description("accept header")
                        ),

                        requestFields(
                                fieldWithPath("owner").description("Owner of Todo"),
                                fieldWithPath("completed").description("if completed or not of Todo"),
                                fieldWithPath("title").description("Title of Todo"),
                                fieldWithPath("description").description("Description of Todo"),
                                fieldWithPath("startDate").description("StartDate of Todo"),
                                fieldWithPath("endDate").description("EndDate of Todo"),
                                fieldWithPath("cycle").description("Cycle of Todo"),
                                fieldWithPath("cycleDetail").description("CycleDetail of Todo"),
                                fieldWithPath("largeCategory").description("LargeCategory of Todo"),
                                fieldWithPath("mediumCategory").description("MediumCategory of Todo")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content-type header")
                        ),
                        responseFields(
                                fieldWithPath("id").description("identifier of this todo"),
                                fieldWithPath("owner").description("owner of this todo"),
                                fieldWithPath("created").description("created date of this todo"),
                                fieldWithPath("completed").description("if completed or not this todo"),
                                fieldWithPath("startDate").description("start date of this todo"),
                                fieldWithPath("endDate").description("end date of this todo"),
                                fieldWithPath("title").description("title of this todo"),
                                fieldWithPath("description").description("description of this todo"),
                                fieldWithPath("cycle").description("cycle type of this todo"),
                                fieldWithPath("cycleDetail").description("cycleDetail of this todo per cycleType"),
                                fieldWithPath("largeCategory").description("largeCategory of this todo"),
                                fieldWithPath("mediumCategory").description("mediumCategory of this todo"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to docs"),
                                fieldWithPath("_links.query-todo.href").description("link to query a todo"),
                                fieldWithPath("_links.delete-todo.href").description("link to delete a todo")
                        )
                ));
    }

    @Test
    @DisplayName("updateTodo - 없는 Todo를 수정할 때 404 Not Found")
    public void updateTodoNotFound() throws Exception {
        TodoDto todoDto = modelMapper.map(todoRepository.findById(basicId).get(), TodoDto.class);
        todoDto.setTitle("Update Test");

        mockMvc.perform(put("/api/todo/{id}", -1)
                .header(HttpHeaders.AUTHORIZATION, getUserAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(todoDto))
        )
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("updateTodo - 다른 사람의 Todo를 수정할 때 403 FORBIDDEN")
    public void updateTodoForbidden() throws Exception {
        TodoDto todoDto = modelMapper.map(todoRepository.findById(basicId).get(), TodoDto.class);
        todoDto.setTitle("Update Test");

        mockMvc.perform(put("/api/todo/{id}", basicId)
                .header(HttpHeaders.AUTHORIZATION, getAdminAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(todoDto))
        )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("deleteTodo - 정상적으로 Todo를 삭제하는 테스트")
    public void deleteTodo() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", basicId)
                .header(HttpHeaders.AUTHORIZATION, getUserAccessToken())
        )
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete-todo"
                ))
        ;

        assertTrue(todoRepository.findById(basicId).isEmpty());
    }

    @Test
    @DisplayName("deleteTodo - 없는 Todo를 삭제할 때 404 Not Found")
    public void deleteTodoNotFound() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", -1)
                .header(HttpHeaders.AUTHORIZATION, getUserAccessToken())
        )
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @DisplayName("deleteTodo - 다른 회원의 Todo를 삭제할 때 403 FORBIDDEN")
    public void deleteTodoForbidden() throws Exception {
        mockMvc.perform(delete("/api/todo/{id}", basicId)
                .header(HttpHeaders.AUTHORIZATION, getAdminAccessToken())
        )
                .andDo(print())
                .andExpect(status().isForbidden())
        ;
    }
}
