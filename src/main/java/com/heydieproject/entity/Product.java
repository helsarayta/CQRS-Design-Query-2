package com.heydieproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_query")
public class Product {
    @Id
    private Long id;
    private String productName;
    private String description;
    private Integer quantity;
    private Double price;
    private String productImg;
    private String createdAt;
    private String updatedAt;
}
