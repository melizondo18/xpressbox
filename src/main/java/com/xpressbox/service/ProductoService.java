package com.xpressbox.service;

import com.xpressbox.domain.Producto;
import com.xpressbox.repository.ProductoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activos) {
        var lista = productoRepository.findAll();
        return lista;
    }

    @Transactional(readOnly = true)
    public Producto getProducto(Producto producto) {
        return productoRepository.findById(producto.getIdProducto()).orElse(null);
    }

    @Transactional
    public void delete(Producto producto) {
        productoRepository.delete(producto);
    }

    @Transactional
    public void save(Producto producto) {
        productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaAmpliada(double precioInf, double precioSup) {
        return productoRepository.findByPrecioBetweenOrderByPrecio(precioInf, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaJPQL(double precioInf, double precioSup) {
        return productoRepository.consultaJPQL(precioInf, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaSQL(double precioInf, double precioSup) {
        return productoRepository.consultaSQL(precioInf, precioSup);
    }

    @Transactional(readOnly = true)
    public List<Producto> consultaPorPrecio(double precio) {
        return productoRepository.findByPrecio(precio);
    }
}