package ma.enset.bank_account_backend.services;

import ma.enset.bank_account_backend.dtos.BankAccountDTO;
import ma.enset.bank_account_backend.dtos.CustomerDTO;
import ma.enset.bank_account_backend.entities.CurrentAccount;
import ma.enset.bank_account_backend.entities.Customer;
import ma.enset.bank_account_backend.entities.SavingAccount;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        return Customer.builder()
                .id(customerDTO.getId())
                .name(customerDTO.getName())
                .email(customerDTO.getEmail())
                .build();
    }

    public BankAccountDTO fromCurrentAccount(CurrentAccount currentAccount) {
        return BankAccountDTO.builder()
                .id(currentAccount.getId())
                .balance(currentAccount.getBalance())
                .createdAt(currentAccount.getCreatedAt())
                .status(currentAccount.getStatus())
                .customer(fromCustomer(currentAccount.getCustomer()))
                .type("CurrentAccount")
                .build();
    }

    public BankAccountDTO fromSavingAccount(SavingAccount savingAccount) {
        return BankAccountDTO.builder()
                .id(savingAccount.getId())
                .balance(savingAccount.getBalance())
                .createdAt(savingAccount.getCreatedAt())
                .status(savingAccount.getStatus())
                .customer(fromCustomer(savingAccount.getCustomer()))
                .type("SavingAccount")
                .build();
    }
}