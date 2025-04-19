package com.xpressbox.controller;

import com.xpressbox.domain.Categoria;
import com.xpressbox.service.CategoriaService;
import com.xpressbox.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pruebas")
public class PruebasController {

    @Autowired
    private ProductoService productoService;
    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/listado")
    public String listado(Model model) {
        var lista = productoService.getProductos(false);
        model.addAttribute("productos", lista);
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        return "/pruebas/listado";
    }

    @GetMapping("/listado/{idCategoria}")
    public String listado(Model model, Categoria categoria) {
        categoria = categoriaService.getCategoria(categoria);
        var lista = categoria.getProductos();
        model.addAttribute("productos", lista);
        var categorias = categoriaService.getCategorias(false);
        model.addAttribute("categorias", categorias);
        return "/pruebas/listado";
    }

    @GetMapping("/listado2")
    public String listado2(Model model) {
        var lista = productoService.getProductos(false);
        model.addAttribute("productos", lista);
        return "/pruebas/listado2";
    }

    @PostMapping("query1")
    public String query1(@RequestParam("precioInf") double precioInf,@RequestParam("precioSup") double precioSup, Model model) {
        var lista = productoService.consultaAmpliada(precioInf, precioSup);
        model.addAttribute("productos", lista);
        model.addAttribute("precioInf",precioInf);
        model.addAttribute("precioSup",precioSup);
        return "/pruebas/listado2";
    }
    
    @PostMapping("query2")
    public String query2(@RequestParam("precioInf") double precioInf,@RequestParam("precioSup") double precioSup, Model model) {
        var lista = productoService.consultaJPQL(precioInf, precioSup);
        model.addAttribute("productos", lista);
        model.addAttribute("precioInf",precioInf);
        model.addAttribute("precioSup",precioSup);
        return "/pruebas/listado2";
    }
    
    @PostMapping("query3")
    public String query3(@RequestParam("precioInf") double precioInf,@RequestParam("precioSup") double precioSup, Model model) {
        var lista = productoService.consultaSQL(precioInf, precioSup);
        model.addAttribute("productos", lista);
        model.addAttribute("precioInf",precioInf);
        model.addAttribute("precioSup",precioSup);
        return "/pruebas/listado2";
    }
    
}
