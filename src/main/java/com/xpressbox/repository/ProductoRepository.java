
package com.xpressbox.repository;

import com.xpressbox.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository <Producto, Long>{
    
}
