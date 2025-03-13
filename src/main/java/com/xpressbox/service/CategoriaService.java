
package com.xpressbox.service;

import com.xpressbox.domain.Categoria;
import com.xpressbox.repository.CategoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class CategoriaService {
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Transactional(readOnly=true)
    public List<Categoria> getCategorias (boolean activos){
        var lista = categoriaRepository.findAll();
        
        return lista;
    }
    
    // Se escriben los metodos de CRUD Read Update Delete
    
    @Transactional(readOnly=true)
    public Categoria getCategoria(Categoria categoria){
        
        return categoriaRepository.findById(categoria.getIdCategoria()).orElse(null);
    }
    
    //Eliminar el registro del ID
    @Transactional
    public void delete(Categoria categoria){
        categoriaRepository.delete(categoria);
    }
    
    //Guarda el registro del ID, 
    //Si categoria.idcategoria esta vacio se inserta un registro
    //Si categoria.idCategoria tiene algo se modifica el registro
    
    @Transactional
    public void save(Categoria categoria){
        categoriaRepository.save(categoria);
    }
}
