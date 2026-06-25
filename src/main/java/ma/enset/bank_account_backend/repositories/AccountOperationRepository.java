package ma.enset.bank_account_backend.repositories;

import ma.enset.bank_account_backend.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {

    List<AccountOperation> findByBankAccountId(String accountId);
}