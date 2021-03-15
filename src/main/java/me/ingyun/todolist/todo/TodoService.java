package me.ingyun.todolist.todo;

import me.ingyun.todolist.account.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final ModelMapper modelMapper;

    public TodoService(TodoRepository todoRepository, ModelMapper modelMapper) {
        this.todoRepository = todoRepository;
        this.modelMapper = modelMapper;
    }

    public Todo createTodo(TodoDto todoDto, Account currentUser){
        todoDto.setOwner(currentUser.getEmail());
        return todoRepository.save(modelMapper.map(todoDto, Todo.class));
    }

    public Todo updateTodo(TodoDto todoDto, Integer id){
        Todo todo = modelMapper.map(todoDto, Todo.class);
        todo.setId(id);
        return todoRepository.save(todo);
    }

    public void deleteTodo(Integer id){
        todoRepository.deleteById(id);
    }

    public ArrayList<Todo> findAllByOwner(String owner){
        return todoRepository.findAllByOwner(owner);
    }

    public Optional<Todo> findById(Integer id){
        return todoRepository.findById(id);
    }
}
