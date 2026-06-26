package ma.enset.bank_account_backend;

import ma.enset.bank_account_backend.dtos.BankAccountDTO;
import ma.enset.bank_account_backend.dtos.CustomerDTO;
import ma.enset.bank_account_backend.security.services.AccountService;
import ma.enset.bank_account_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@SpringBootApplication
public class BankAccountBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankAccountBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BankAccountService bankAccountService, AccountService accountService) {
		return args -> {

			// creer des clients via le service
			Stream.of("Mehdi", "Sara", "Yassine").forEach(name -> {
				CustomerDTO customer = CustomerDTO.builder()
						.name(name)
						.email(name.toLowerCase() + "@email.com")
						.build();
				bankAccountService.saveCustomer(customer);
			});

			// creer des comptes pour chaque client via le service
			bankAccountService.listCustomers().forEach(customer -> {
				bankAccountService.saveCurrentBankAccount(Math.random() * 9000, 9000, customer.getId());
				bankAccountService.saveSavingBankAccount(Math.random() * 9000, 5.5, customer.getId());
			});

			// ajouter des operations sur chaque compte
			List<BankAccountDTO> accounts = bankAccountService.bankAccountList();
			accounts.forEach(account -> {
				for (int i = 0; i < 3; i++) {
					if (Math.random() > 0.5) {
						bankAccountService.credit(account.getId(), new Random().nextDouble() * 1000, "Credit test");
					} else {
						try {
							bankAccountService.debit(account.getId(), new Random().nextDouble() * 500, "Debit test");
						} catch (Exception e) {
							System.out.println("Solde insuffisant pour " + account.getId());
						}
					}
				}
			});

			// afficher tous les comptes
			accounts.forEach(account -> {
				System.out.println("=====================================");
				System.out.println(account.getId());
				System.out.println(account.getBalance());
				System.out.println(account.getType());
				System.out.println(account.getCustomer().getName());
			});

			// creer des utilisateurs et roles pour la securite
			accountService.addNewRole("USER");
			accountService.addNewRole("ADMIN");

			accountService.addNewUser("admin", "1234", "admin@bank.com");
			accountService.addNewUser("user1", "1234", "user1@bank.com");

			accountService.addRoleToUser("admin", "USER");
			accountService.addRoleToUser("admin", "ADMIN");
			accountService.addRoleToUser("user1", "USER");
		};

	}
}