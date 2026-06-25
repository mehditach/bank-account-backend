package ma.enset.bank_account_backend.repositories;

import ma.enset.bank_account_backend.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}