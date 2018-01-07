package com.fp.stock.dto;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO implements Serializable {

    @NotBlank(message = "Email is required.")
    private String email;

    @NotBlank
    @Size(min = 4)
    @Pattern(regexp = "^[a-zA-Z0-9@#$%\\^&+=]{4,}$", message = "Password is too short or you use not allowed signs. Please use only letters, numbers and @#$%&+=^")
    private String password;

    @Min(value = 0, message = "Enter your financial resources. If you don't have any just set 0.")
    @Max(value = Integer.MAX_VALUE, message = "Enter valid financial resource value. Must be less than or equal to " + Integer.MAX_VALUE)
    @NotNull
    private BigDecimal financialResources;

    @Pattern(regexp = "^[A-Za-z]{1,}\\s[A-Za-z]{1,}[A-Za-z-\\s]*$", message = "Please enter your first and last name")
    private String name;

    private List<StockDTO> stocks;

    private List<UserStockDTO> userStocks;

    public void sortUserStocksByCode(){
        if(this.userStocks != null && this.userStocks.size() > 1){
            List<UserStockDTO> sorted = this.userStocks.stream()
                    .sorted((s1, s2) -> s1.getStock().getCode().compareTo(s2.getStock().getCode()))
                    .collect(Collectors.toList());
            this.userStocks = sorted;
        }
    }
}
