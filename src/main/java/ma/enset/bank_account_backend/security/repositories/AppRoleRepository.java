package ma.enset.bank_account_backend.security.repositories;

import ma.enset.bank_account_backend.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {

    AppRole findByRoleName(String roleName);
}