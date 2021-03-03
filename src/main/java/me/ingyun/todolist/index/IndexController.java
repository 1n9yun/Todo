package me.ingyun.todolist.index;

import me.ingyun.todolist.todo.TodoController;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class IndexController {

    @GetMapping("/api")
    public RepresentationModel index(){
        var index = new RepresentationModel<>();
        index.add(linkTo(TodoController.class).withRel("todos"));
//        ...


        return index;
    }
}
