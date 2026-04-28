package com.example.pedidos.repositories;

import com.example.pedidos.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Método útil para que el usuario vea su historial de compras
    List<Pedido> findByProductoId(Long productoId);
}
