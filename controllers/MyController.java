package com.suraj.carrercraft.elasticsearchdemo.controllers;

import com.suraj.carrercraft.elasticsearchdemo.model.Product.Product;
import com.suraj.carrercraft.elasticsearchdemo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MyController {

    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public String addProduct(@RequestBody Product product) {
        System.out.println(product.getId());
        System.out.println(product.getName());
        System.out.println(product.getCategory());
        System.out.println(product.getDescription());
        System.out.println("------------------------------------");

        productService.saveProduct(product);
        return "Success";
    }

    @GetMapping("/search")
    public List<Product> fuzzySearch(@RequestParam String keyword) {
        List<Product> list = productService.fuzzySearch(keyword);
        for(Product product : list) {
            System.out.println(product.getId());
            System.out.println(product.getName());
            System.out.println(product.getCategory());
            System.out.println(product.getDescription());
            System.out.println("------------------------------------");
        }

        return list;
    }

    @GetMapping("/multiSearch")
    public List<Product> multiSearch(@RequestParam String keyword) {
        List<Product> list = productService.searchByKeyword(keyword);

        for(Product product : list) {
            System.out.println(product.getId());
            System.out.println(product.getName());
            System.out.println(product.getCategory());
            System.out.println(product.getDescription());
            System.out.println("------------------------------------");
        }

        return list;
    }

}
