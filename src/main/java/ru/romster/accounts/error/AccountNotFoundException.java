package ru.romster.accounts.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by n.romanov
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class AccountNotFoundException extends ApplicationException {

    private Long id;

    public Long getId() {
        return id;
    }

    public AccountNotFoundException(Long id) {
        super("Account [" + id + "] doesn't exist");
        this.id = id;
    }
}
