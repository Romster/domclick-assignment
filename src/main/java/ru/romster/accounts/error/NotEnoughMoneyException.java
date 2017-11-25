package ru.romster.accounts.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by n.romanov
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotEnoughMoneyException extends RuntimeException {
    private Long id;

    public Long getId() {
        return id;
    }

    public NotEnoughMoneyException(Long id) {
        super("Account [" + id + "] doesn't contain enough money for the operation");
        this.id = id;
    }
}
