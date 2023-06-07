package com.retaileatshop.model;

import com.retaileatshop.model.enums.Category;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Product {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, updatable = false)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Category category;
    private int price;
    private int quantity;
    private int weight;
    private String description;
    private String img;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts;
    @OneToOne(cascade = CascadeType.ALL)
    private Stat stat;
    @ManyToOne(cascade = CascadeType.ALL)
    private User owner;

    public Product(String name, Category category, int price, int quantity, int weight, String description, User owner) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.weight = weight;
        this.description = description;
        this.carts = new ArrayList<>();
        this.owner = owner;
    }

    public void addCart(Cart cart) {
        carts.add(cart);
        cart.setProduct(this);
    }

    public void removeCart(Cart cart) {
        carts.remove(cart);
        cart.setProduct(null);
    }
}
