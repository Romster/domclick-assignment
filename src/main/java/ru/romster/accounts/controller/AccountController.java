package ru.romster.accounts.controller;

import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romster.accounts.controller.input.RequestedSum;
import ru.romster.accounts.error.NullParameterException;
import ru.romster.accounts.service.AccountService;
import ru.romster.accounts.service.model.Account;

/**
 * Created by n.romanov
 */
@RestController
@RequestMapping("/account")
@Api(value = "/account", description = "Main application controller")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    /**
     *
     */
    @GetMapping("/ping")
    @ApiOperation(value = "Just a ping")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "System is online")
    })
    public void ping() {

    }

    /**
     *
     */
    @GetMapping("/{accId}")
    @ApiOperation(value = "Retrieve an account via id", response = Account.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account was found"),
            @ApiResponse(code = 404, message = "Account was not found")})
    public ResponseEntity<Account> getAccount(
            @ApiParam(value = "Id of the account")
            @PathVariable("accId") Long accId) {

        Account account = accountService.getAccount(accId);
        return ResponseEntity.ok(account);
    }

    /**
     *
     */
    @PutMapping()
    @ApiOperation(value = "Create new account and return it's id", response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Account was successfully created")
    })
    public ResponseEntity<Long> create() {

        Long newAccount = accountService.createNewAccount();
        return ResponseEntity.ok(newAccount);
    }

    /**
     *
     */
    @PostMapping("/withdraw/{accountId}")
    @ApiOperation(value = "Withdraw given sum from account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The sum was successfully withdrawn"),
            @ApiResponse(code = 404, message = "Account was not found"),
            @ApiResponse(code = 400, message = "Illegal amount value, " +
                    "or there is not enough money on the account")
    })
    public void withdrawMoney(
            @ApiParam(value = "Id of the account")
            @PathVariable(name = "accountId") Long accId,

            @ApiParam(value = "Money to be withdrawn")
            @RequestBody RequestedSum requestedSum) {

        assertNotNull(requestedSum.getAmount(), "amount");
        accountService.withdraw(accId, requestedSum.getAmount());
    }


    /**
     *
     */
    @PostMapping("/deposit/{accountId}")
    @ApiOperation(value = "Deposit given sum to account")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The sum was successfully deposited"),
            @ApiResponse(code = 404, message = "Account was not found"),
            @ApiResponse(code = 400, message = "Illegal amount value")
    })
    public void depositMoney(
            @ApiParam(value = "Id of the account")
            @PathVariable(name = "accountId") Long accId,

            @ApiParam(value = "Money to be deposited")
            @RequestBody RequestedSum requestedSum) {

        assertNotNull(requestedSum.getAmount(), "amount");
        accountService.deposit(accId, requestedSum.getAmount());
    }

    /**
     *
     */
    @PostMapping("/transfer/{senderId}/{recipientId}")
    @ApiOperation(value = "Transfer given sum from one account to another")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "The sum was successfully transferred"),
            @ApiResponse(code = 404, message = "Account was not found"),
            @ApiResponse(code = 400, message = "Illegal amount value, " +
                    "or there is not enough money on the account")
    })
    public void transferMoney(
            @ApiParam(value = "Id of the \"sender\" account")
            @PathVariable(name = "senderId") Long fromId,
            @ApiParam(value = "Id of the \"recipient\" account")
            @PathVariable(name = "recipientId") Long toId,
            @ApiParam(value = "Money to be transfered")
            @RequestBody RequestedSum requestedSum) {

        assertNotNull(requestedSum.getAmount(), "amount");
        accountService.transfer(fromId, toId, requestedSum.getAmount());
    }


    private void assertNotNull(Object object, String name) {
        if (object == null) throw new NullParameterException(name);
    }

}
