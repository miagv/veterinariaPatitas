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
    
    // =======================================================
    // === MÉTODOS DE GESTIÓN (USADOS POR ProductManagementController) ===
    // =======================================================

    /**
     * CORRECCIÓN: Método para exponer la búsqueda de producto por ID al controlador.
     * @param id ID del producto.
     * @return Producto opcional.
     */
    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    /**
     * Guarda un producto nuevo o actualiza uno existente (incluye stock).
     * @param product El objeto Producto a guardar/actualizar.
     * @return El producto guardado.
     */
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * Añade stock al producto existente.
     * @param productId ID del producto.
     * @param addedStock Cantidad de stock a añadir.
     * @return El producto actualizado.
     */
    public Optional<Product> addStock(int productId, int addedStock) {
        Optional<Product> productOpt = productRepository.findById(productId);

        productOpt.ifPresent(product -> {
            if (addedStock > 0) {
                int newStock = product.getStock() + addedStock;
                product.setStock(newStock);
                productRepository.save(product);
            }
        });
        
        return productOpt;
    }
    
    /**
     * Elimina un producto por ID.
     * @param productId ID del producto a eliminar.
     */
    public void deleteProduct(int productId) {
        productRepository.deleteById(productId);
    }
}