package com.xpressbox.service;

import com.xpressbox.domain.Factura;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.xpressbox.domain.Item;
import com.xpressbox.domain.Usuario;
import com.xpressbox.domain.Venta;
import com.xpressbox.repository.FacturaRepository;
import com.xpressbox.repository.ProductoRepository;
import com.xpressbox.repository.UsuarioRepository;
import com.xpressbox.repository.VentaRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class ItemService {

    //Se usa una variable de session para guardar el carrito de compras.
    @Autowired
    private HttpSession session;

    //Crea un item en la variable de la session y si la variable no existe se crea tambien.
    public void save(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        //Se valida si el variable ya existia en la session

        if (lista == null) {
            //Si no esta se crea.
            lista = new ArrayList<>();
        }
        //Se busca en la lista si el producto ya existe.
        boolean existe = false;
        for (Item i : lista) {
            if (i.getIdProducto() == item.getIdProducto()) {
                existe = true;
                if (Objects.equals(i.getIdProducto(), item.getIdProducto())) {
                    if (i.getCantidad() < i.getExistencias()) {
                        //Aun se puede comprar.
                        i.setCantidad(i.getCantidad() + 1);
                    }
                    break;
                }
            }
        }
        if (!existe) { //Si no está el producto en la lista, se agrega.
            item.setCantidad(1);
            lista.add(item);
        }
        session.setAttribute("listaItems", lista);
    }

    //Recupera un item de la lista y si la variable no esta retorna null.
    public Item getItem(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        //Se valida si el variable ya existia en la session

        if (lista == null) {
            return null;
        }
        //Se busca en la lista si el producto.
        for (Item i : lista) {
            if (i.getIdProducto() == item.getIdProducto()) {
                return i;
            }
        }
        return null;
    }

    //Total de veta de compra acutal en el carrito.
    public double getTotal() {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        //Se valida si el variable ya existia en la session

        if (lista == null) {
            return 0;
        }
        //Se recorrela lista de producto.
        double total = 0;
        for (Item i : lista) {
            total += i.getCantidad() * i.getPrecio();
        }
        return total;
    }

    //retorna la lista completa.
    public List<Item> getItems() {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        return lista;
    }

    //Elimina un item de la variable de la session
    public void delete(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        //Se valida si el variable ya existia en la session

        if (lista != null) {
            //Se busca en la lista si el producto ya existe.
            boolean existe = false;
            var posicion = -1;
            for (Item i : lista) {
                posicion++;
                if (Objects.equals(i.getIdProducto(), item.getIdProducto())) {
                    existe = true;
                    break;
                }
            }

            if (existe) {
                lista.remove(posicion);
                session.setAttribute("listaItems", lista);
            }
        }
    }

    //actualiza un item de la cantidad de la session
    public void update(Item item) {
        @SuppressWarnings("unchecked")
        List<Item> lista = (List) session.getAttribute("listaItems");
        //Se valida si el variable ya existia en la session

        if (lista != null) {
            //Se busca en la lista si el producto ya existe.
            boolean existe = false;
            for (Item i : lista) {
                if (Objects.equals(i.getIdProducto(), item.getIdProducto())) {
                    i.setCantidad(item.getCantidad());
                    break;
                }
            }
        }
    }

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private VentaRepository ventaRepository;

    public void facturar() {
        //Se debe recuperar el usuario autenticado y obtener su idUsuario
        String username = "";
        var principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else {
            if (principal != null) {
                username = principal.toString();
            }
        }

        if (username.isBlank()) {
            System.out.println("username en blanco...");
            return;
        }

        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            System.out.println("Usuario no existe en usuarios...");
            return;
        }

        //Se debe registrar la factura incluyendo el usuario
        Factura factura = new Factura(usuario.getIdUsuario());
        factura = facturaRepository.save(factura);

        //Se debe registrar las ventas de cada producto -actualizando existencias-
        @SuppressWarnings("unchecked")
        List<Item> listaItems = (List) session.getAttribute("listaItems");
        if (listaItems != null) {
            double total = 0;
            for (Item i : listaItems) {
                var producto = productoRepository.getReferenceById(i.getIdProducto());
                if (producto.getExistencias() >= i.getCantidad()) {
                    Venta venta = new Venta(factura.getIdFactura(),
                            i.getIdProducto(),
                            i.getPrecio(),
                            i.getCantidad());
                    ventaRepository.save(venta);
                    producto.setExistencias(producto.getExistencias() - i.getCantidad());
                    productoRepository.save(producto);
                    total += i.getCantidad() * i.getPrecio();
                }
            }

            //Se debe registrar el total de la venta en la factura
            factura.setTotal(total);
            facturaRepository.save(factura);

            //Se debe limpiar el carrito la lista...
            listaItems.clear();
        }
    }
}
