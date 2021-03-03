package me.ingyun.todolist.common;

import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;

public class ListResource<T> extends RepresentationModel {

    private ArrayList<T> content;

    public ListResource(ArrayList<T> content){
        this.content = content;
    }

    public ArrayList<T> getContent(){
        return this.content;
    }
}
