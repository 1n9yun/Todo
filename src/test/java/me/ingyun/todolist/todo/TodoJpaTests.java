package me.ingyun.todolist.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class TodoJpaTests {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TodoRepository repository;

//    CRUD는 당연히 되는 무의미한 테스트인 것 같은데.. 연관관계에서 함 생각해보자
    @Test
    void getTodoByOwner(){
        this.entityManager.persist(Todo.builder()
                .owner("1n9yun")
                .title("test")
                .build()
        );
        Todo todo = this.repository.findByOwner("1n9yun");

        assertEquals("1n9yun", todo.getOwner());
        assertEquals("test", todo.getTitle());
    }

    @Test
    void insertTodo(){
        Todo todo = Todo.builder()
                .owner("1n9yun")
                .title("test")
                .description("test description")
                .build();

        this.repository.save(todo);

        Todo found = this.repository.findByOwner("1n9yun");

        assertAll(
                () -> assertNotNull(found.getId()),
                () -> assertEquals("test", found.getTitle()),
                () -> assertEquals("test description", found.getDescription())
        );
    }

    @Test
    void updateTodo(){
        Todo todo = this.entityManager.persist(Todo.builder()
                .owner("1n9yun")
                .title("update test")
                .build()
        );
        todo.setTitle("update test done");
        Todo update = this.repository.save(todo);

        assertEquals(todo.getTitle(), update.getTitle());
    }

    @Test
    void deleteTodo(){
        Integer id = this.entityManager.persist(Todo.builder()
                .owner("1")
                .title("test")
                .build()
        ).getId();
        this.repository.deleteById(id);
        assertFalse(this.repository.findById(id).isPresent());
    }
}
