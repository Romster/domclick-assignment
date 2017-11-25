package ru.romster.accounts.service;

import lombok.NonNull;
import ru.romster.accounts.service.model.Account;

import java.math.BigDecimal;

/**
 * Created by n.romanov
 */
public interface AccountService {

    Long createNewAccount();

    Account getAccount(@NonNull Long id);

    void withdraw(@NonNull Long from,
                  @NonNull BigDecimal sum);

    void deposit(@NonNull Long to,
                 @NonNull BigDecimal sum);

    void transfer(@NonNull Long from,
                  @NonNull Long to,
                  @NonNull BigDecimal sum);

}
