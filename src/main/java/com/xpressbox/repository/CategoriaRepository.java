package com.xpressbox.repository;

import com.xpressbox.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria,Long>{
    
}
