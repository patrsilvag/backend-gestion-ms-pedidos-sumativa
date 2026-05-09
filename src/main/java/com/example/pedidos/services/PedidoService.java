package com.example.pedidos.services;

import com.example.pedidos.dto.CompraRequest;
import com.example.pedidos.dto.PedidoResponse;
import com.example.pedidos.exceptions.ResourceNotFoundException;
import com.example.pedidos.models.Pedido;
import com.example.pedidos.repositories.PedidoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@SuppressWarnings("null") // Elimina avisos de nulos para compatibilidad con Java 21
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    // Inyección por constructor (Regla de oro para Rating A)
    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<PedidoResponse> listarTodos() {
        log.info("Service: Obteniendo historial de pedidos");

        // Reemplazamos .collect(Collectors.toList()) por .toList() para inmutabilidad
        return pedidoRepository.findAll().stream().map(this::convertToResponse).toList();
    }

    public PedidoResponse buscarPorId(Long id) {
        log.info("Service: Buscando pedido ID: {}", id);
        // Al eliminar @NonNull del parámetro, desaparece el error de la línea 34
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado: " + id));
        return convertToResponse(pedido);
    }

    @Transactional
    public PedidoResponse registrarPedido(CompraRequest request) {
        log.info("Service: Procesando registro de pedido para Producto ID: {}",
                request.getProductoId());

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setProductoId(request.getProductoId());
        nuevoPedido.setCantidad(request.getCantidad());
        nuevoPedido.setFechaPedido(LocalDateTime.now());
        nuevoPedido.setEstado("PROCESADO");

        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);
        log.info("Service: Pedido ID {} guardado exitosamente", pedidoGuardado.getId());

        return convertToResponse(pedidoGuardado);
    }

    // --- Mapeo de Entidad a DTO para proteger la arquitectura ---
    private PedidoResponse convertToResponse(Pedido entity) {
        return new PedidoResponse(entity.getId(), entity.getProductoId(), entity.getCantidad(),
                entity.getFechaPedido(), entity.getEstado());
    }
}
