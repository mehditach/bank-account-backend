package ma.enset.bank_account_backend.services;
import ma.enset.bank_account_backend.dtos.AccountOperationDTO;
import lombok.AllArgsConstructor;
import ma.enset.bank_account_backend.dtos.BankAccountDTO;
import ma.enset.bank_account_backend.dtos.CustomerDTO;
import ma.enset.bank_account_backend.entities.*;
import ma.enset.bank_account_backend.repositories.AccountOperationRepository;
import ma.enset.bank_account_backend.repositories.BankAccountRepository;
import ma.enset.bank_account_backend.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl bankAccountMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        Customer customer = bankAccountMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return bankAccountMapper.fromCustomer(savedCustomer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> bankAccountMapper.fromCustomer(customer))
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));
        return bankAccountMapper.fromCustomer(customer);
    }

    @Override
    public BankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        CurrentAccount currentAccount = CurrentAccount.builder()
                .id(UUID.randomUUID().toString())
                .balance(initialBalance)
                .createdAt(new Date())
                .status(AccountStatus.CREATED)
                .overDraft(overDraft)
                .customer(customer)
                .build();

        CurrentAccount savedAccount = bankAccountRepository.save(currentAccount);
        return bankAccountMapper.fromCurrentAccount(savedAccount);
    }

    @Override
    public BankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        SavingAccount savingAccount = SavingAccount.builder()
                .id(UUID.randomUUID().toString())
                .balance(initialBalance)
                .createdAt(new Date())
                .status(AccountStatus.CREATED)
                .interestRate(interestRate)
                .customer(customer)
                .build();

        SavingAccount savedAccount = bankAccountRepository.save(savingAccount);
        return bankAccountMapper.fromSavingAccount(savedAccount);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        return bankAccountRepository.findAll().stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount) {
                return bankAccountMapper.fromSavingAccount((SavingAccount) bankAccount);
            } else {
                return bankAccountMapper.fromCurrentAccount((CurrentAccount) bankAccount);
            }
        }).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        if (bankAccount instanceof SavingAccount) {
            return bankAccountMapper.fromSavingAccount((SavingAccount) bankAccount);
        } else {
            return bankAccountMapper.fromCurrentAccount((CurrentAccount) bankAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        if (bankAccount.getBalance() < amount) {
            throw new RuntimeException("Solde insuffisant");
        }

        AccountOperation accountOperation = AccountOperation.builder()
                .type(OperationType.DEBIT)
                .amount(amount)
                .description(description)
                .operationDate(new Date())
                .bankAccount(bankAccount)
                .build();
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Compte introuvable"));

        AccountOperation accountOperation = AccountOperation.builder()
                .type(OperationType.CREDIT)
                .amount(amount)
                .description(description)
                .operationDate(new Date())
                .bankAccount(bankAccount)
                .build();
        accountOperationRepository.save(accountOperation);

        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) {
        debit(accountIdSource, amount, "Virement vers " + accountIdDestination);
        credit(accountIdDestination, amount, "Virement depuis " + accountIdSource);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        List<AccountOperation> operations = accountOperationRepository.findByBankAccountId(accountId);
        return operations.stream().map(op -> AccountOperationDTO.builder()
                .id(op.getId())
                .operationDate(op.getOperationDate())
                .amount(op.getAmount())
                .type(op.getType())
                .description(op.getDescription())
                .build()
        ).collect(Collectors.toList());
    }
}