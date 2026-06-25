package ma.enset.bank_account_backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.enset.bank_account_backend.entities.OperationType;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
    private String description;
}