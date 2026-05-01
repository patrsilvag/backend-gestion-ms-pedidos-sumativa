package com.example.pedidos.controllers;

import com.example.pedidos.dto.CompraRequest;
import com.example.pedidos.models.Pedido;
import com.example.pedidos.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos") // Cambiado de productos a pedidos
@CrossOrigin(origins = "http://localhost:4200")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService; // Usamos el nuevo PedidoService

    // Lista el historial de compras realizadas
    @GetMapping
    public ResponseEntity<List<Pedido>> listarHistorial() {
        return ResponseEntity.ok(pedidoService.listarTodos()); // 200 OK
    }

    // Obtiene un pedido específico por su ID de transacción
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id)); // 200 OK
    }

    // Este es el corazón del microservicio: Realizar la compra
    @PostMapping("/comprar")
    public ResponseEntity<?> crearPedido(@Valid @RequestBody CompraRequest request) {
        Pedido nuevoPedido = pedidoService.registrarPedido(request);

        return new ResponseEntity<>(
                Map.of("mensaje", "Pago realizado con éxito. Su pedido está en camino.", "pedidoId",
                        nuevoPedido.getId(), "productoId", nuevoPedido.getProductoId(), "cantidad",
                        nuevoPedido.getCantidad(), "fecha", nuevoPedido.getFechaPedido()),
                HttpStatus.CREATED); // 201 Created
    }

}
