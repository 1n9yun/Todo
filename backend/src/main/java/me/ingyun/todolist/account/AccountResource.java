package me.ingyun.todolist.account;

import org.springframework.hateoas.RepresentationModel;

public class AccountResource extends RepresentationModel {

    private Account account;

    public AccountResource(Account account){
        this.account = account;
    }

    public Account getAccount(){
        return this.account;
    }

}
