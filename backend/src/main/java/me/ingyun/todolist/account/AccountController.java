package me.ingyun.todolist.account;

import me.ingyun.todolist.common.ErrorsResource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/user")
public class AccountController {

    private final ModelMapper modelMapper;
    private final AccountService accountService;

    public AccountController(ModelMapper modelMapper, AccountService accountService) {
        this.modelMapper = modelMapper;
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity createAccount(@RequestBody @Valid AccountDto accountDto,
                                        Errors errors
                                        ){
        if(errors.hasErrors()) return ErrorsResource.badRequest(errors);
        if(accountService.findByEmail(accountDto.getEmail()).isPresent()) return ResponseEntity.status(HttpStatus.CONFLICT).build();

        Account account = modelMapper.map(accountDto, Account.class);
        account.setRoles(Set.of(AccountRole.USER));
        account = accountService.saveAccount(account);

        WebMvcLinkBuilder linkBuilder = linkTo(AccountController.class).slash(account.getId());

        AccountResource accountResource = new AccountResource(account);
        accountResource.add(linkBuilder.withSelfRel());
        accountResource.add(Link.of("/docs/index.html#resources-accounts-create").withRel("profile"));

        return ResponseEntity.created(linkBuilder.toUri()).body(accountResource);
    }

}
