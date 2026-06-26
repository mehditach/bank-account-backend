package ma.enset.bank_account_backend.security.services;

import ma.enset.bank_account_backend.security.entities.AppRole;
import ma.enset.bank_account_backend.security.entities.AppUser;

public interface AccountService {

    AppUser addNewUser(String username, String password, String email);

    AppRole addNewRole(String roleName);

    void addRoleToUser(String username, String roleName);

    AppUser loadUserByUsername(String username);
}