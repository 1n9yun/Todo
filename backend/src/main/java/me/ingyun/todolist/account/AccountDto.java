package me.ingyun.todolist.account;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class AccountDto {

    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;

}
