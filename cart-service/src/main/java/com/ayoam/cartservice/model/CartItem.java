package com.ayoam.cartservice.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity(name="cart_item")
@NoArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;
    @NotNull
    private Long productId;
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    private Photo mainPhoto;
    @NotNull
    private String libelle;
    @NotNull
    private Double price;
    @NotNull
    private int quantity;
    @NotNull
    private int productStock;

    private boolean isAvailable=true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
