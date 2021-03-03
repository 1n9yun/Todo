package me.ingyun.todolist.todo;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class TodoResource extends RepresentationModel {
    @JsonUnwrapped
    private Todo todo;

    public TodoResource(Todo todo){
        this.todo = todo;

        add(linkTo(TodoController.class).slash(todo.getId()).withSelfRel());
    }
    public Todo getTodo(){
        return this.todo;
    }
}
