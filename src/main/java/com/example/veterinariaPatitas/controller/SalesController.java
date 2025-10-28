package com.example.veterinariaPatitas.controller;

import com.example.veterinariaPatitas.model.Product;
import com.example.veterinariaPatitas.service.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"cart", "products"}) // Mantiene carrito y productos en sesión
public class SalesController {

    private final SalesService salesService;

    // Inyección del servicio
    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    /**
     * lista productos
     */
    @ModelAttribute("products")
    public List<Product> initializeProducts() {
        return salesService.getInitialProducts();
    }

    /**
     * carrito vacio
     */
    @ModelAttribute("cart")
    public List<Product> initializeCart() {
        return new ArrayList<>();
    }

    /**
     * pagina simulador
     */
    @GetMapping("/simulador_ventas")
    public String simuladorVentas(@ModelAttribute("cart") List<Product> cart, Model model) {
        salesService.calculateTotals(cart, model);
        return "simulador_ventas";
    }

    /**
     * añade al carrito
     */
    @PostMapping("/add_to_cart")
    public String addToCart(@RequestParam int productId, @RequestParam int quantity,
                            @ModelAttribute("products") List<Product> products,
                            @ModelAttribute("cart") List<Product> cart,
                            Model model) {

        // CORREGIDO (Línea 62): Solo se pasan 3 argumentos (productId, quantity, cart). 
        // Se eliminó la lista 'products' extra.
        String message = salesService.addProductToCart(productId, quantity, cart); 
        model.addAttribute("message", message);

        return "redirect:/simulador_ventas";
    }

    /**
     * elimina un producto
     */
    @PostMapping("/remove_from_cart")
    public String removeFromCart(@RequestParam int productId,
                                 @ModelAttribute("cart") List<Product> cart,
                                 Model model) {

        cart.removeIf(item -> item.getId() == productId);
        model.addAttribute("message", "Producto eliminado del carrito.");
        return "redirect:/simulador_ventas";
    }

    /**
     * vacio
     */
    @PostMapping("/clear_cart")
    public String clearCart(@ModelAttribute("cart") List<Product> cart, Model model) {
        cart.clear();
        model.addAttribute("message", "El carrito ha sido vaciado.");
        return "redirect:/simulador_ventas";
    }

    /**
     * finalizada venta
     */
    @PostMapping("/finalize_sale")
    public String finalizeSale(@ModelAttribute("products") List<Product> products,
                               @ModelAttribute("cart") List<Product> cart,
                               SessionStatus status, Model model) {

        if (cart.isEmpty()) {
            model.addAttribute("message", "Error: El carrito está vacío.");
            return "redirect:/simulador_ventas";
        }

        // descuenta del stock
        // CORREGIDO (Línea 106): Solo se pasa 1 argumento ('cart'). 
        // Se eliminó la lista 'products' extra.
        List<Product> finalCart = salesService.finalizeSale(cart);

        //
        salesService.calculateTotals(finalCart, model);

        // Agregar carrito final para boleta
        model.addAttribute("finalCart", finalCart);

        // Limpiar carrito en sesión
        cart.clear();

        model.addAttribute("message", "¡Venta confirmada!.");
        return "simulador_ventas";
    }
}