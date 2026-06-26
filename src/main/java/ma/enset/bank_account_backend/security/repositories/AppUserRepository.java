package ma.enset.bank_account_backend.security.repositories;

import ma.enset.bank_account_backend.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);
}