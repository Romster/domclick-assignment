package ru.romster.accounts.repo.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by n.romanov
 */
@Entity(name = "Account")
@Table(name = "account")
@Data
public class DBAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Version
    @Column(name = "version")
    Long version;
}
