package com.example.pedidos.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoResponse {
    private Long id;
    private Long productoId;
    private Integer cantidad;
    private LocalDateTime fechaPedido;
    private String estado;
}
