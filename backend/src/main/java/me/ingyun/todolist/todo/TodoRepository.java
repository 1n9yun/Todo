package me.ingyun.todolist.todo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {
    Todo findByOwner(String username);
    ArrayList<Todo> findAllByOwner(String username);
}
