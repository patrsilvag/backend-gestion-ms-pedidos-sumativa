package com.example.pedidos.controllers;

import com.example.pedidos.dto.CompraRequest;
import com.example.pedidos.dto.PedidoResponse;
import com.example.pedidos.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
// ✅ Se actualizan los orígenes para incluir el dominio de Docker
@CrossOrigin(origins = { "http://mi-app-docker", "http://localhost:4200", "http://localhost" })
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    // ✅ Permite /api/pedidos y /api/pedidos/ para evitar error 500 en Nginx
    @GetMapping({ "", "/" })
    public ResponseEntity<List<PedidoResponse>> listarHistorial() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.buscarPorId(id));
    }

    @PostMapping("/comprar")
    public ResponseEntity<Map<String, Object>> crearPedido(
            @Valid @RequestBody CompraRequest request) {
        PedidoResponse nuevoPedido = pedidoService.registrarPedido(request);

        Map<String, Object> respuesta = Map.of("mensaje", "Pago realizado con éxito. Su pedido está en camino.",
                "pedidoId",
                nuevoPedido.getId(), "productoId", nuevoPedido.getProductoId(), "cantidad",
                nuevoPedido.getCantidad(), "fecha", nuevoPedido.getFechaPedido());

        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }
}