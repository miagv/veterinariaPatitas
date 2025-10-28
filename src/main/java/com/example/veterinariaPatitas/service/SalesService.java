/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.example.veterinariaPatitas.service;


import com.example.veterinariaPatitas.model.Product;
import com.example.veterinariaPatitas.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Service
public class SalesService {

    private final double IGV_RATE = 0.18;

    @Autowired
    private ProductRepository productRepository;

   
    public List<Product> getInitialProducts() {
        return productRepository.findAll();
    }

   
    public void calculateTotals(List<Product> cart, Model model) {
        double subtotal = cart.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        double tax = subtotal * IGV_RATE;
        double total = subtotal + tax;

        model.addAttribute("subtotal", subtotal);
        model.addAttribute("tax", tax); // IGV
        model.addAttribute("total", total);
    }

   
    public String addProductToCart(int productId, int quantity, List<Product> cart) {
        Optional<Product> productOpt = productRepository.findById(productId);

        if (productOpt.isEmpty()) {
            return "Error: Producto no encontrado.";
        }

        Product product = productOpt.get();

        if (quantity <= 0 || quantity > product.getStock()) {
            return "Error: Cantidad inválida o stock insuficiente.";
        }

        
        Optional<Product> existingItemOpt = cart.stream()
                .filter(item -> item.getId() == productId)
                .findFirst();

        if (existingItemOpt.isPresent()) {
            Product existingItem = existingItemOpt.get();
            int newTotalQty = existingItem.getQuantity() + quantity;

            if (newTotalQty > product.getStock()) {
                return "Error: La cantidad total excede el stock disponible.";
            }

            existingItem.setQuantity(newTotalQty);
        } else {
            Product newItem = new Product(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStock(),
                    product.getUnit()
            );
            newItem.setQuantity(quantity);
            cart.add(newItem);
        }

        return "Producto agregado al carrito con éxito.";
    }

   
    public List<Product> finalizeSale(List<Product> cart) {

        for (Product cartItem : cart) {
            Optional<Product> productOpt = productRepository.findById(cartItem.getId());

            productOpt.ifPresent(dbProduct -> {
                int newStock = dbProduct.getStock() - cartItem.getQuantity();
                dbProduct.setStock(newStock);
                productRepository.save(dbProduct); 
            });
        }

       
        return new ArrayList<>(cart);
    }
}


