
package com.tienda.service;

import com.tienda.domain.Producto;
import com.tienda.repository.ProductoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Transactional(readOnly=true)
    public List<Producto> getProductos (boolean activos){
        var lista = productoRepository.findAll();
        
        return lista;
    }
    
    // Se escriben los metodos de CRUD Read Update Delete
    
    @Transactional(readOnly=true)
    public Producto getProducto(Producto producto){
        
        return productoRepository.findById(producto.getIdProducto()).orElse(null);
    }
    
    //Eliminar el registro del ID
    @Transactional
    public void delete(Producto producto){
        productoRepository.delete(producto);
    }
    
    //Guarda el registro del ID, 
    //Si producto.idproducto esta vacio se inserta un registro
    //Si producto.idProducto tiene algo se modifica el registro
    
    @Transactional
    public void save(Producto producto){
        productoRepository.save(producto);
    }
}
