package com.ayoam.orderservice.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "photo")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long photoId;
    @NotNull
    @Column(columnDefinition="TEXT")
    private String photo;
    @NotNull
    private String extension;
}
