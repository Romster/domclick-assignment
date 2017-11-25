package ru.romster.accounts.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.romster.accounts.repo.model.DBAccount;

/**
 * Created by n.romanov
 */
public interface AccountRepository extends JpaRepository<DBAccount, Long> {
}
