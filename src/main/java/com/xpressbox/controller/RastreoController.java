package com.xpressbox.controller;

import com.xpressbox.domain.Categoria;
import com.xpressbox.service.CategoriaService;
import com.xpressbox.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/rastreo")
public class RastreoController {
    
    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/buscar")
    public String listado(Model model) {
        var lista = productoService.getProductos(false);
        model.addAttribute("productos", lista);
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        return "/rastreo/buscar";
    }

    @GetMapping("/buscar/{idCategoria}")
    public String listado(Model model, Categoria categoria) {
        categoria = categoriaService.getCategoria(categoria);
        var lista = categoria.getProductos();
        model.addAttribute("productos", lista);
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        return "/rastreo/buscar";
    }

    @GetMapping("/listado2")
    public String listado2(Model model) {
        var lista = productoService.getProductos(false);
        model.addAttribute("productos", lista);
        return "/rastreo/buscar2";
    }

    @PostMapping("resultado_busqueda")
    public String query4(@RequestParam("precio") double precio, Model model) {
        var lista = productoService.consultaPorPrecio(precio);
        model.addAttribute("productos", lista);
        model.addAttribute("precio", precio);
        return "/rastreo/buscar2";
    }
}
