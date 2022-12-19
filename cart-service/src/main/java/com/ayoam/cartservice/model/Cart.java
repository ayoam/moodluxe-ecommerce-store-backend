package com.ayoam.cartservice.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity(name="cart")
@NoArgsConstructor
public class Cart{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;
    @NotNull
    private Long customerId;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderColumn
    private List<CartItem> cartItemList = new ArrayList<>();


    public void addToCart(CartItem cartItem){
        this.cartItemList.add(cartItem);
    }

    public void removeFromCart(CartItem cartItem){
        this.cartItemList.remove(cartItem);
    }
}
