package ru.romster.accounts.repo.mapper;

import ru.romster.accounts.repo.model.DBAccount;
import ru.romster.accounts.service.model.Account;

/**
 * Created by n.romanov
 */
public class AccountMapper {

    public static Account fromEntity(DBAccount from) {
        if(from == null) return null;

        Account to = new Account();
        to.setId(from.getId());
        to.setBalance(from.getBalance());

        return to;
    }

}
