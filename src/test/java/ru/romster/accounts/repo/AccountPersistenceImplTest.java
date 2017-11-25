package ru.romster.accounts.repo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;
import ru.romster.accounts.error.AccountNotFoundException;
import ru.romster.accounts.error.IllegalSumException;
import ru.romster.accounts.error.NotEnoughMoneyException;
import ru.romster.accounts.service.model.Account;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by n.romanov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
         AccountPersistenceImpl.class
})
@DataJpaTest
@EnableJpaRepositories(basePackages = "ru.romster.accounts.repo")
@EntityScan(basePackages = "ru.romster.accounts.repo")
public class AccountPersistenceImplTest {

    @Autowired
    private AccountPersistence accountPersistence;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void createAndFindAccount() throws Exception {
        Long newAccountId = accountPersistence.createNewAccount();

        flushAndClear();

        Account account = accountPersistence.getAccount(newAccountId);

        assertNotNull(newAccountId);
        assertEquals(newAccountId, account.getId());
        assertEquals("Sum == 0", 0, BigDecimal.ZERO.compareTo(account.getBalance()));
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void withdrawNotEnough() throws Exception {
        Long accId = accountPersistence.createNewAccount();

        flushAndClear();

        accountPersistence.withdraw(accId, new BigDecimal(100));
    }

    @Test(expected = IllegalSumException.class)
    public void withdrawIllegalSum() throws Exception {
        Long accId = accountPersistence.createNewAccount();

        flushAndClear();

        accountPersistence.withdraw(accId, new BigDecimal(-100));
    }


    @Test(expected = IllegalSumException.class)
    public void depositIllegalSum() throws Exception {
        Long accId = accountPersistence.createNewAccount();

        flushAndClear();

        accountPersistence.deposit(accId, new BigDecimal(-100));
    }

    @Test
    public void depositAndWithdraw() throws Exception {
        Long accId = accountPersistence.createNewAccount();
        flushAndClear();

        accountPersistence.deposit(accId, new BigDecimal("100"));
        flushAndClear();
        {
            Account account = accountPersistence.getAccount(accId);
            assertEquals(0, new BigDecimal(100).compareTo(account.getBalance()));
        }

        accountPersistence.withdraw(accId, new BigDecimal(40));
        flushAndClear();
        {
            Account account = accountPersistence.getAccount(accId);
            assertEquals(0, new BigDecimal(60).compareTo(account.getBalance()));
        }
    }


    @Test(expected = IllegalSumException.class)
    public void transferIllegalSum() {
        Long fromId = accountPersistence.createNewAccount();
        Long toId = accountPersistence.createNewAccount();
        flushAndClear();

        accountPersistence.transfer(fromId, toId, new BigDecimal(-100));
    }

    @Test(expected = NotEnoughMoneyException.class)
    public void transferNotEnough() {
        Long fromId = accountPersistence.createNewAccount();
        Long toId = accountPersistence.createNewAccount();
        flushAndClear();

        accountPersistence.transfer(fromId, toId, new BigDecimal(100));
    }

    @Test(expected = AccountNotFoundException.class)
    public void transferNotFoundTo() {
        Long fromId = accountPersistence.createNewAccount();
        flushAndClear();

        accountPersistence.transfer(fromId, -99999999L, new BigDecimal(100));
    }

    @Test
    public void transfer() {
        Long fromId = accountPersistence.createNewAccount();
        Long toId = accountPersistence.createNewAccount();
        flushAndClear();

        accountPersistence.deposit(fromId, new BigDecimal("100"));
        flushAndClear();

        accountPersistence.transfer(fromId, toId, new BigDecimal(40.50));
        flushAndClear();

        Account fromAccount = accountPersistence.getAccount(fromId);
        assertEquals(0, new BigDecimal(59.50).compareTo(fromAccount.getBalance()));

        Account toAccount = accountPersistence.getAccount(toId);
        assertEquals(0, new BigDecimal(40.50).compareTo(toAccount.getBalance()));
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

}