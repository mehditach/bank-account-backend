package ma.enset.bank_account_backend.web;

import lombok.AllArgsConstructor;
import ma.enset.bank_account_backend.security.config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private ma.enset.bank_account_backend.security.services.AccountService accountService;

    @PostMapping("/auth/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        String token = jwtUtil.generateToken(username);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", username);
        return response;
    }

    @PostMapping("/auth/change-password")
    public Map<String, String> changePassword(@RequestBody Map<String, String> request) {
        String username = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication().getName();

        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        accountService.changePassword(username, oldPassword, newPassword);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Mot de passe modifie avec succes");
        return response;
    }
}