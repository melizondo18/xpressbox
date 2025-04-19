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

    @Transactional(readOnly = true)
    public List<Categoria> getCategorias(boolean activos) {
        var lista = categoriaRepository.findAll();
        //Falta algo xd
        return lista;
    }

    //Metodos del CRUD Create Read Update Delete
    @Transactional(readOnly = true)
    public Categoria getCategoria(Categoria categoria) {
        return categoriaRepository.findById(categoria.getIdCategoria()).orElse(null);
    }

    @Transactional
    public void delete(Categoria categoria) {
        //Elimina el registro que contiene el id igual a lo pasado en categoria.getIdCategoria()
        categoriaRepository.delete(categoria);
    }

    @Transactional
    public void save(Categoria categoria) {
        //Si categoria.IdCategoria esta vacio... se inserta un registro
        //Si categoria.idCategoria tiene algo... se modifica ese registro
        
        categoriaRepository.save(categoria);
    }
}
