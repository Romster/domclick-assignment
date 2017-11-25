package ru.romster.accounts.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by n.romanov
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NullParameterException extends ApplicationException {
    private String argumentName;

    public String getArgumentName() {
        return argumentName;
    }

    public NullParameterException(String argumentName) {
        super(argumentName + " can not be null");
        this.argumentName = argumentName;
    }
}
