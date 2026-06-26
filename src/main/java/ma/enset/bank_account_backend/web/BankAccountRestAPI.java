package ma.enset.bank_account_backend.web;

import ma.enset.bank_account_backend.dtos.AccountOperationDTO;
import lombok.AllArgsConstructor;
import ma.enset.bank_account_backend.dtos.BankAccountDTO;
import ma.enset.bank_account_backend.dtos.CustomerDTO;
import ma.enset.bank_account_backend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountRestAPI {

    private BankAccountService bankAccountService;

    // ===== Customers =====

    @GetMapping("/customers")
    public List<CustomerDTO> listCustomers() {
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        return bankAccountService.getCustomer(id);
    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    // ===== Bank Accounts =====

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{id}")
    public BankAccountDTO getAccount(@PathVariable String id) {
        return bankAccountService.getBankAccount(id);
    }

    @PostMapping("/accounts/current/{customerId}")
    public BankAccountDTO saveCurrentAccount(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") double initialBalance,
            @RequestParam(defaultValue = "0") double overDraft) {
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping("/accounts/saving/{customerId}")
    public BankAccountDTO saveSavingAccount(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") double initialBalance,
            @RequestParam(defaultValue = "0") double interestRate) {
        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
    }

    // ===== Operations =====

    @PostMapping("/accounts/{accountId}/debit")
    public void debit(
            @PathVariable String accountId,
            @RequestParam double amount,
            @RequestParam(defaultValue = "") String description) {
        bankAccountService.debit(accountId, amount, description);
    }

    @PostMapping("/accounts/{accountId}/credit")
    public void credit(
            @PathVariable String accountId,
            @RequestParam double amount,
            @RequestParam(defaultValue = "") String description) {
        bankAccountService.credit(accountId, amount, description);
    }

    @PostMapping("/accounts/transfer")
    public void transfer(
            @RequestParam String accountIdSource,
            @RequestParam String accountIdDestination,
            @RequestParam double amount) {
        bankAccountService.transfer(accountIdSource, accountIdDestination, amount);
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> accountHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }


}