/**/
package com.tienda.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping ("/categoria")
public class CategoriaController {
    
    @GetMapping ("/listado")
    public String listado(Model model){
        model.addAttribute("nombre: ","Lex Voorhout");
        model.addAtribute(Mi edad es: [[${edad}]]);
        
        return "/categoria/listado";
    }
}
