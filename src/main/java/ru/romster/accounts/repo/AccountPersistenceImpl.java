package ru.romster.accounts.repo;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.romster.accounts.error.AccountNotFoundException;
import ru.romster.accounts.error.IllegalSumException;
import ru.romster.accounts.error.NotEnoughMoneyException;
import ru.romster.accounts.repo.mapper.AccountMapper;
import ru.romster.accounts.repo.model.DBAccount;
import ru.romster.accounts.service.model.Account;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

/**
 * Created by n.romanov
 */
@Repository
@Transactional
public class AccountPersistenceImpl implements AccountPersistence {

    private final EntityManager entityManager;
    private final AccountRepository accountRepository;

    public AccountPersistenceImpl(EntityManager entityManager,
                                  AccountRepository accountRepository) {
        this.entityManager = entityManager;
        this.accountRepository = accountRepository;
    }


    @Override
    public Long createNewAccount() {
        DBAccount account = new DBAccount();
        account.setBalance(BigDecimal.ZERO);

        entityManager.persist(account);

        return account.getId();
    }


    @Override
    public Account getAccount(Long id) {
        return AccountMapper.fromEntity(findOrThrow(id));
    }

    @Override
    public void withdraw(Long fromId, BigDecimal sum) {
        assertSumIsNotLessThanZero(sum);

        DBAccount account = findOrThrow(fromId);

        BigDecimal result = account.getBalance().subtract(sum);
        if (result.compareTo(BigDecimal.ZERO) >= 0) {
            account.setBalance(result);
        } else {
            throw new NotEnoughMoneyException(fromId);
        }
    }

    @Override
    public void deposit(Long toId, BigDecimal sum) {
        assertSumIsNotLessThanZero(sum);

        DBAccount account = findOrThrow(toId);

        BigDecimal result = account.getBalance().add(sum);
        account.setBalance(result);
    }

    @Override
    public void transfer(Long fromId, Long toId, BigDecimal sum) {
        assertSumIsNotLessThanZero(sum);

        DBAccount from = findOrThrow(fromId);
        DBAccount to = findOrThrow(toId);

        BigDecimal fromBalance = from.getBalance().subtract(sum);
        BigDecimal toBalance = to.getBalance().add(sum);

        if (fromBalance.compareTo(BigDecimal.ZERO) >= 0) {
            from.setBalance(fromBalance);
            to.setBalance(toBalance);
        } else {
            throw new NotEnoughMoneyException(fromId);
        }

    }

    private DBAccount findOrThrow(Long id) {
        DBAccount account = accountRepository.findOne(id);
        if (account == null) throw new AccountNotFoundException(id);
        return account;
    }

    private void assertSumIsNotLessThanZero(BigDecimal sum)  {
        if (sum.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalSumException(sum);
    }
}
