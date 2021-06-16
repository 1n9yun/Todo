package me.ingyun.todolist.todo;

import me.ingyun.todolist.account.Account;
import me.ingyun.todolist.account.CurrentUser;
import me.ingyun.todolist.common.ErrorsResource;
import me.ingyun.todolist.common.ListResource;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/todo", produces = MediaTypes.HAL_JSON_VALUE)
public class TodoController {

    private final TodoService todoService;
    private final TodoValidator todoValidator;

    public TodoController(TodoService todoService, TodoValidator todoValidator) {
        this.todoService = todoService;
        this.todoValidator = todoValidator;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public ResponseEntity getTodos(@CurrentUser Account currentUser){
        ArrayList<Todo> todos = todoService.findAllByOwner(currentUser.getEmail());

        ListResource<Todo> todoListResource = new ListResource<>(todos);
        WebMvcLinkBuilder linkBuilder = linkTo(TodoController.class);

        todoListResource.add(linkBuilder.withSelfRel());
        todoListResource.add(Link.of("/docs/index.html#resources-todos-query").withRel("profile"));

        return ResponseEntity.ok(todoListResource);
    }

    @PostMapping
    public ResponseEntity createTodo(@RequestBody @Valid TodoDto todoDto,
                                     Errors errors,
                                     @CurrentUser Account currentUser
                                     ){
        if(errors.hasErrors()) return ErrorsResource.badRequest(errors);
        this.todoValidator.validate(todoDto, errors);
        if(errors.hasErrors()) return ErrorsResource.badRequest(errors);

        TodoResource todoResource = new TodoResource(todoService.createTodo(todoDto, currentUser));

        WebMvcLinkBuilder linkBuilder = linkTo(TodoController.class).slash(todoResource.getTodo().getId());
        URI createdUri = linkBuilder.toUri();

        todoResource.add(Link.of("/docs/index.html#resources-todos-create").withRel("profile"));
        todoResource.add(linkBuilder.withRel("query-todos"));
        todoResource.add(linkBuilder.withRel("update-todos"));
        todoResource.add(linkBuilder.withRel("delete-todos"));

        return ResponseEntity.created(createdUri).body(todoResource);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTodo(@PathVariable Integer id,
                                  @CurrentUser Account currentUser
    ){
        Optional<Todo> todo = todoService.findById(id);
        if(todo.isEmpty()) return ResponseEntity.notFound().build();
        if(!todo.get().getOwner().equals(currentUser.getEmail())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        TodoResource todoResource = new TodoResource(todo.get());

        WebMvcLinkBuilder linkBuilder = linkTo(TodoController.class).slash(id);
        todoResource.add(Link.of("/docs/index.html/#resources-todos-list").withRel("profile"));
        todoResource.add(linkBuilder.withRel("update-todo"));
        todoResource.add(linkBuilder.withRel("delete-todo"));

        return ResponseEntity.ok(todoResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTodo(@PathVariable Integer id,
                                     @RequestBody @Valid TodoDto todoDto,
                                     Errors errors,
                                     @CurrentUser Account currentUser){
        if(errors.hasErrors()) return ErrorsResource.badRequest(errors);
        todoValidator.validate(todoDto, errors);
        if(errors.hasErrors()) return ErrorsResource.badRequest(errors);

        Optional<Todo> todo = todoService.findById(id);
        if(todo.isEmpty()) return ResponseEntity.notFound().build();
        String owner = todo.get().getOwner();
        if(!owner.equals(todoDto.getOwner()) || !owner.equals(currentUser.getEmail()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        TodoResource todoResource = new TodoResource(todoService.updateTodo(todoDto, id));

        WebMvcLinkBuilder linkBuilder = linkTo(TodoController.class).slash(id);
        todoResource.add(Link.of("/docs/index.html#resources-todos-update").withRel("profile"));
        todoResource.add(linkBuilder.withRel("query-todo"));
        todoResource.add(linkBuilder.withRel("delete-todo"));

        return ResponseEntity.ok(todoResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteTodo(@PathVariable Integer id,
                                     @CurrentUser Account currentUser
    ){
        Optional<Todo> todo = todoService.findById(id);
        if(todo.isEmpty()) return ResponseEntity.notFound().build();
        if(!todo.get().getOwner().equals(currentUser.getEmail())) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
