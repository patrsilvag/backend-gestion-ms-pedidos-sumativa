package com.example.pedidos.services;

import com.example.pedidos.exceptions.ResourceNotFoundException;
import com.example.pedidos.models.Pedido;
import com.example.pedidos.repositories.PedidoRepository;
import com.example.pedidos.dto.CompraRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NonNull;

@Slf4j
@Service
@SuppressWarnings({"null", "all"})
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * Lista todo el historial de pedidos realizados.
     */
    public List<Pedido> listarTodos() {
        log.info("Service: Obteniendo historial de pedidos");
        return pedidoRepository.findAll();
    }

    /**
     * Busca un pedido específico por su ID de transacción.
     */
    public Pedido buscarPorId(@NonNull Long id) { 
        log.info("Service: Buscando pedido ID: {}", id);
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
    }

    /**
     * Registra un nuevo pedido en la base de datos. Aquí es donde ocurrirá la magia de la
     * integración más adelante.
     */
    public Pedido registrarPedido(CompraRequest request) {
        log.info("Service: Procesando registro de pedido para Producto ID: {}",
                request.getProductoId());

        // 1. Creamos la instancia de Pedido usando el patrón Builder o manual
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setProductoId(request.getProductoId());
        nuevoPedido.setCantidad(request.getCantidad());
        nuevoPedido.setFechaPedido(LocalDateTime.now());
        nuevoPedido.setEstado("PROCESADO");

        // 2. Guardamos en nuestra base de datos de Pedidos
        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);

        log.info("Service: Pedido ID {} guardado exitosamente", pedidoGuardado.getId());

        return pedidoGuardado;
    }
}
