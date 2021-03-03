package me.ingyun.todolist.account;

import me.ingyun.todolist.common.AppProperties;
import me.ingyun.todolist.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.notification.RunListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTests extends BaseTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AccountService accountService;

    @Autowired
    AppProperties appProperties;

    @Test
    @DisplayName("userSignUp - 성공적으로 회원가입하는 테스트")
    public void userSignUp() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                .email("newuser@user.com")
                .password("newuser")
                .build();

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto))
                .accept(MediaTypes.HAL_JSON)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("account").exists())
                .andDo(document("create-account",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("link to docs")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                                headerWithName(HttpHeaders.ACCEPT).description("accept headeR")
                        ),
                        requestFields(
                                fieldWithPath("email").description("email"),
                                fieldWithPath("password").description("password")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                                headerWithName(HttpHeaders.LOCATION).description("location header")
                        ),
                        responseFields(
                                fieldWithPath("account.id").description("created account's id"),
                                fieldWithPath("account.email").description("created account's email"),
                                fieldWithPath("account.roles").description("created account's roles"),
                                fieldWithPath("account.id").description("created account's id"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.profile.href").description("link to docs")
                        )
                ))
                ;
    }

    @Test
    @DisplayName("createAccount - 중복 이메일일 때 409 CONFLICT")
    public void createAccountConflict() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                .email(appProperties.getUserUsername())
                .password(appProperties.getUserPassword())
                .build();

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto))
                .accept(MediaTypes.HAL_JSON)
        )
                .andExpect(status().isConflict())
        ;
    }


    @Test
    @DisplayName("createAccount - 빈 입력일 때 400 Bad Request")
    public void createAccountEmptyInput() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                .build();

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto))
                .accept(MediaTypes.HAL_JSON)
        )
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("createAccount - username이 Email 형식이 아닐 때 400 Bad Request")
    public void createAccountWrongInput() throws Exception {
        AccountDto accountDto = AccountDto.builder()
                .email("i'm not email")
                .password("test")
                .build();

        mockMvc.perform(post("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(accountDto))
                .accept(MediaTypes.HAL_JSON)
        )
                .andExpect(status().isBadRequest())
        ;
    }
}
