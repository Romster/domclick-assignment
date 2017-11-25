package ru.romster.accounts.controller.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Created by n.romanov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestedSum {

    @JsonProperty("amount")
    private BigDecimal amount;

}
