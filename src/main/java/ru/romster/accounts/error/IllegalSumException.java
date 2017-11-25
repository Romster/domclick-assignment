package ru.romster.accounts.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

/**
 * Created by n.romanov
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalSumException extends ApplicationException {
    private BigDecimal sum;

    public BigDecimal getSum() {
        return sum;
    }

    public IllegalSumException(BigDecimal sum) {
        super("Illegal sum " + sum);
        this.sum = sum;
    }
}
