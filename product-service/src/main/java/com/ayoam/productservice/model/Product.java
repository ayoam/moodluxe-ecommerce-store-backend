package com.ayoam.productservice.model;

import com.ayoam.productservice.persistance.ProductEntityListener;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="Product")
@EntityListeners(ProductEntityListener.class)

public class Product {
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @SequenceGenerator(name="idp_generator",initialValue = 1432485,allocationSize = 1,sequenceName = "product_idp_seq")
    @GeneratedValue
            (strategy = GenerationType.SEQUENCE,generator = "product_generator")
    @SequenceGenerator(name="product_generator", sequenceName = "product_seq",initialValue = 5000,allocationSize=50)
    private Long idp;
    @NotNull
    private String libelle;
    @NotNull
    @Column(columnDefinition="TEXT")
    private String description;
    @NotNull
    private Double price;
    private Double compareToPrice;
    @NotNull
    private int quantity;
    @NotNull
    private Boolean active;

    private Long ordersCount=0L;

    @ManyToMany
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categoryList = new ArrayList<>();

    @ManyToOne
    private Brand brand;

    @OneToMany
    private List<Photo> photoList = new ArrayList<>();


    public void addPhoto(Photo photo){
        this.photoList.add(photo);
    }
}
