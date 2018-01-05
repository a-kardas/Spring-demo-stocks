package com.fp.stock.dto;

import com.fp.stock.model.UserStock;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UserDTO implements Serializable {

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 4)
    //@Pattern(regexp = "^[a-zA-Z0-9@#$%^&+=](?=\\S+$){4,}$")
    private String password;

    //@Min(value = 0)
    private BigDecimal financialResources;

    //@NotBlank
    private String name;

    private List<StockDTO> stocks;

    private List<UserStockDTO> userStocks;
}
