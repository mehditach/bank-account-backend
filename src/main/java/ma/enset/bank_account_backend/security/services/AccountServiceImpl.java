package ma.enset.bank_account_backend.security.services;

import lombok.AllArgsConstructor;
import ma.enset.bank_account_backend.security.entities.AppRole;
import ma.enset.bank_account_backend.security.entities.AppUser;
import ma.enset.bank_account_backend.security.repositories.AppRoleRepository;
import ma.enset.bank_account_backend.security.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String username, String password, String email) {
        AppUser appUser = AppUser.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(new ArrayList<>())
                .build();
        return appUserRepository.save(appUser);
    }

    @Override
    public AppRole addNewRole(String roleName) {
        AppRole appRole = AppRole.builder()
                .roleName(roleName)
                .build();
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        AppUser appUser = appUserRepository.findByUsername(username);
        AppRole appRole = appRoleRepository.findByRoleName(roleName);
        appUser.getRoles().add(appRole);
    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public void changePassword(String username, String oldPassword, String newPassword) {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new RuntimeException("Utilisateur introuvable");
        }

        if (!passwordEncoder.matches(oldPassword, appUser.getPassword())) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        appUser.setPassword(passwordEncoder.encode(newPassword));
        appUserRepository.save(appUser);
    }
}