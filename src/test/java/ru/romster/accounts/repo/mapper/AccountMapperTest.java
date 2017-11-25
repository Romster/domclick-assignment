package ru.romster.accounts.repo.mapper;

import org.junit.Test;
import ru.romster.accounts.repo.model.DBAccount;
import ru.romster.accounts.service.model.Account;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by n.romanov
 */
public class AccountMapperTest {
    @Test
    public void fromNull() throws Exception {
        assertNull(AccountMapper.fromEntity(null));
    }

    @Test
    public void fromEntity() throws Exception {
        DBAccount dbAccount = new DBAccount();
        dbAccount.setId(1L);
        dbAccount.setBalance(new BigDecimal(100.10));

        Account account = AccountMapper.fromEntity(dbAccount);
        assertEquals(dbAccount.getId(), account.getId());
        assertEquals(dbAccount.getBalance(), account.getBalance());

    }

}