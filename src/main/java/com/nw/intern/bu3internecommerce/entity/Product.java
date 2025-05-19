package com.nw.intern.bu3internecommerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String code;
    private String name;
    private Double mrpPrice;
    private Double sellingPrice;
    private int discountPercentage;
    @Column(length = 1000)
    private String description;
    @Min(0)
    private Integer quantity;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> color;

    @OneToMany(fetch = FetchType.EAGER)
    private Collection<Image> imageUrls = new HashSet<>();

    private int numRating;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> sizes;

    @JsonBackReference
    @ManyToOne
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
}
