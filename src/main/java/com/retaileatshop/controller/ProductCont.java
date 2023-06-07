package com.retaileatshop.controller;

import com.retaileatshop.controller.main.Attributes;
import com.retaileatshop.model.Product;
import com.retaileatshop.model.Stat;
import com.retaileatshop.model.enums.Category;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequestMapping("/product")
public class ProductCont extends Attributes {
    @GetMapping("/all")
    String products(Model model) {
        AddAttributesProducts(model);
        return "products";
    }

    @GetMapping("/{category}")
    String productsCategory(Model model, @PathVariable Category category) {
        AddAttributesProductsCategory(model, category);
        return "products";
    }

    @PostMapping("/search")
    String productsSearch(Model model, @RequestParam Category category, @RequestParam String search, @RequestParam String desk) {
        AddAttributesProductsSearch(model, category, search, desk);
        return "products";
    }

    @GetMapping("/add")
    String productAdd(Model model) {
        AddAttributesProductAdd(model);
        return "productAdd";
    }

    @PostMapping("/add")
    String productAddNew(Model model, @RequestParam MultipartFile img, @RequestParam String name, @RequestParam Category category, @RequestParam int quantity, @RequestParam int price, @RequestParam int weight, @RequestParam String description) {
        Product product = productRepo.saveAndFlush(new Product(name, category, price, quantity, weight, description, getUser()));

        if (img != null && !Objects.requireNonNull(img.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            String res = "";
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = uuidFile + "_" + img.getOriginalFilename();
                    img.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (IOException e) {
                model.addAttribute("message", "Не удалось загрузить изображение");
                AddAttributesProductAdd(model);
                return "productAdd";
            }

            product.setImg(res);
            productRepo.save(product);
        }

        Stat stat = statRepo.saveAndFlush(new Stat(product));
        product.setStat(stat);
        productRepo.save(product);
        return "redirect:/product/all";
    }

    @GetMapping("/edit/{id}")
    String productEdit(Model model, @PathVariable Long id) {
        AddAttributesProductEdit(model, id);
        return "productEdit";
    }

    @PostMapping("/edit/{id}")
    String productEditOld(@PathVariable Long id, Model model, @RequestParam MultipartFile img, @RequestParam String name, @RequestParam Category category, @RequestParam int quantity, @RequestParam int price, @RequestParam int weight, @RequestParam String description) {
        Product product = productRepo.getReferenceById(id);

        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setWeight(weight);
        product.setDescription(description);

        if (img != null && !Objects.requireNonNull(img.getOriginalFilename()).isEmpty()) {
            String uuidFile = UUID.randomUUID().toString();
            boolean createDir = true;
            String res = "";
            try {
                File uploadDir = new File(uploadImg);
                if (!uploadDir.exists()) createDir = uploadDir.mkdir();
                if (createDir) {
                    res = uuidFile + "_" + img.getOriginalFilename();
                    img.transferTo(new File(uploadImg + "/" + res));
                }
            } catch (IOException e) {
                model.addAttribute("message", "Не удалось загрузить изображение");
                AddAttributesProductAdd(model);
                return "productEdit";
            }

            product.setImg(res);
            productRepo.save(product);
        }

        productRepo.save(product);

        return "redirect:/product/all";
    }

    @GetMapping("/delete/{id}")
    String productDelete(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/product/all";
    }

}
