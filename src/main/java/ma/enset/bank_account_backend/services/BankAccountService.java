package ma.enset.bank_account_backend.services;
import ma.enset.bank_account_backend.dtos.AccountOperationDTO;
import ma.enset.bank_account_backend.dtos.BankAccountDTO;
import ma.enset.bank_account_backend.dtos.CustomerDTO;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> listCustomers();

    CustomerDTO getCustomer(Long customerId);

    BankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId);

    BankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId);

    List<BankAccountDTO> bankAccountList();

    BankAccountDTO getBankAccount(String accountId);

    void debit(String accountId, double amount, String description);

    void credit(String accountId, double amount, String description);

    void transfer(String accountIdSource, String accountIdDestination, double amount);

    List<AccountOperationDTO> accountHistory(String accountId);

    java.util.Map<String, Object> getDashboardStats();
}