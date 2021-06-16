package me.ingyun.todolist.config;

import me.ingyun.todolist.account.Account;
import me.ingyun.todolist.account.AccountRole;
import me.ingyun.todolist.account.AccountService;
import me.ingyun.todolist.common.AppProperties;
import me.ingyun.todolist.todo.CycleType;
import me.ingyun.todolist.todo.Todo;
import me.ingyun.todolist.todo.TodoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() { return new ModelMapper(); }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {

            @Autowired
            AccountService accountService;

            @Autowired
            TodoRepository todoRepository;

            @Autowired
            AppProperties appProperties;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account admin = Account.builder()
                        .email(appProperties.getAdminUsername())
                        .password(appProperties.getAdminPassword())
                        .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                        .build();
                Account user = Account.builder()
                        .email(appProperties.getUserUsername())
                        .password(appProperties.getUserPassword())
                        .roles(Set.of(AccountRole.USER))
                        .build();

                accountService.saveAccount(admin);
                accountService.saveAccount(user);

//                Todo todo = Todo.builder()
//                        .owner(appProperties.getUserUsername())
//                        .title("Test 1")
//                        .cycle(CycleType.DAY)
//                        .cycleDetail(2)
//                        .build();
//                todoRepository.save(todo);
            }
        };
    }
}
