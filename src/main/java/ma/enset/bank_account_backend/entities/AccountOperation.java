package ma.enset.bank_account_backend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date operationDate;

    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    private String description;

    @ManyToOne
    private BankAccount bankAccount;
}