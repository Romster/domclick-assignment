package ru.romster.accounts.service;

import org.springframework.stereotype.Service;
import ru.romster.accounts.repo.AccountPersistence;
import ru.romster.accounts.service.model.Account;

import java.math.BigDecimal;

/**
 * Created by n.romanov
 */
@Service
public class AccountServiceImpl implements AccountService {

    private AccountPersistence accountPersistence;

    public AccountServiceImpl(AccountPersistence accountPersistence) {
        this.accountPersistence = accountPersistence;
    }

    @Override
    public Long createNewAccount() {
        return accountPersistence.createNewAccount();
    }

    @Override
    public Account getAccount(Long id) {
        return accountPersistence.getAccount(id);
    }

    @Override
    public void withdraw(Long from, BigDecimal sum) {
        accountPersistence.withdraw(from, sum);
    }

    @Override
    public void deposit(Long to, BigDecimal sum) {
        accountPersistence.deposit(to, sum);
    }

    @Override
    public void transfer(Long from, Long to, BigDecimal sum) {
        accountPersistence.transfer(from, to, sum);
    }
}
