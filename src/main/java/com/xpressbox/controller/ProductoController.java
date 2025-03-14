package com.xpressbox.controller;

import com.xpressbox.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping("/listado")
    public String listado(
        @RequestParam(required = false) Double peso, 
        @RequestParam(required = false) String unidad,
        Model model) {
        
        // Mostrar la lista de productos
        var lista = productoService.getProductos(false);
        model.addAttribute("productos", lista);
        model.addAttribute("totalproductos", lista.size());

        // Si se reciben los parámetros de peso y unidad, realizar el cálculo
        if (peso != null && unidad != null) {
            double pesoKg = peso;
            double pesoLb = peso;

            // Si la unidad es kg, convertimos a libras
            if (unidad.equals("kg")) {
                pesoLb = peso * 2.20462; // Convertir kg a lb
            } else if (unidad.equals("lb")) {
                pesoKg = peso / 2.20462; // Convertir lb a kg
            }

            // Cálculo del costo base en colones: 4000 colones por libra
            double costoBase = pesoLb * 4000;
            double impuesto = 0.13;
            double costoTotal = costoBase * (1 + impuesto);

            // Redondear a 2 decimales usando BigDecimal para los valores
            BigDecimal bdPesoKg = new BigDecimal(pesoKg).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bdPesoLb = new BigDecimal(pesoLb).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bdCostoBase = new BigDecimal(costoBase).setScale(2, RoundingMode.HALF_UP);
            BigDecimal bdCostoTotal = new BigDecimal(costoTotal).setScale(2, RoundingMode.HALF_UP);
            model.addAttribute("pesoKg", bdPesoKg);
            model.addAttribute("pesoLb", bdPesoLb);
            model.addAttribute("costoBase", bdCostoBase);
            model.addAttribute("costoTotal", bdCostoTotal);
        }

        return "/producto/listado";
    }
}
