package com.retaileatshop.controller.main;

import com.retaileatshop.model.Cart;
import com.retaileatshop.model.Product;
import com.retaileatshop.model.Stat;
import com.retaileatshop.model.enums.Category;
import com.retaileatshop.model.enums.Role;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Attributes extends Main {

    protected void AddAttributes(Model model) {
        model.addAttribute("user", getUser());
    }

    protected void AddAttributesStats(Model model) {
        AddAttributes(model);
        List<Product> products = new ArrayList<>();
        if (getUser().getRole() == Role.SELLER) {
            products = getUser().getProducts();
        } else if(getUser().getRole() == Role.ADMIN) {
            products = productRepo.findAll();
        }
        AtomicInteger profitPrice = new AtomicInteger();
        products.forEach(product -> profitPrice.addAndGet(product.getStat().getPrice()));
        model.addAttribute("profitPrice", profitPrice.intValue());
        model.addAttribute("products", products);

        String[] topQuantityName = new String[5];
        int[] topQuantityNumber = new int[5];
        List<Stat> stats = new ArrayList<>();
        for (Product i : products) {
            stats.add(i.getStat());
        }
        stats.sort(Comparator.comparing(Stat::getQuantity));
        Collections.reverse(stats);
        for (int i = 0; i < stats.size(); i++) {
            if (i == 5) break;
            topQuantityName[i] = stats.get(i).getProduct().getName();
            topQuantityNumber[i] = stats.get(i).getQuantity();
        }
        model.addAttribute("topQuantityName", topQuantityName);
        model.addAttribute("topQuantityNumber", topQuantityNumber);
    }

    protected void AddAttributesCart(Model model) {
        AddAttributes(model);
        List<Cart> cartList = cartRepo.findAllByUser(getUser().getId());
        int total = 0;
        for (Cart i : cartList) {
            total += i.getQuantity() * i.getProduct().getPrice();
        }
        model.addAttribute("carts", cartList);
        model.addAttribute("total", total);
    }


    protected void AddAttributesProductAdd(Model model) {
        AddAttributes(model);
        model.addAttribute("categories", Category.values());
    }

    protected void AddAttributesProductEdit(Model model, Long id) {
        AddAttributes(model);
        model.addAttribute("categories", Category.values());
        model.addAttribute("product", productRepo.getReferenceById(id));
    }

    protected void AddAttributesUsers(Model model) {
        AddAttributes(model);
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("roles", Role.values());
    }

    protected void AddAttributesProducts(Model model) {
        AddAttributes(model);
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("categories", Category.values());
    }

    protected void AddAttributesProductsCategory(Model model, Category category) {
        AddAttributes(model);
        model.addAttribute("products", productRepo.findAllByCategory(category));
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", category);
    }

    protected void AddAttributesProductsSearch(Model model, Category category, String search, String desk) {
        AddAttributes(model);
        System.out.println(desk);
        if (desk.equals("cheap")) {
            model.addAttribute("products", productRepo.findAllByCategoryAndNameContainingOrderByPriceAsc(category, search));
        } else {
            model.addAttribute("products", productRepo.findAllByCategoryAndNameContainingOrderByPriceDesc(category, search));
        }
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", category);
        model.addAttribute("input", search);
    }
}
