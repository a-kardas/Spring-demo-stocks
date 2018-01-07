package com.fp.stock.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="USER_STOCK")
@Data
public class UserStock implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name="user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name="stock_id", nullable = false)
    private Long stockId;

    @NotNull
    @Column(name="amount", nullable = false)
    private int amount;

    @OneToOne
    @JoinColumn(name = "stock_id", insertable = false, updatable = false)
    private Stock stock;

}
