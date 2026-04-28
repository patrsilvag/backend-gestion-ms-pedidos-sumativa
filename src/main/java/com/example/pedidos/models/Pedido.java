package com.example.pedidos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "PEDIDOS")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId; // Referencia al ID del MS-Productos

    @NotNull(message = "La cantidad es obligatoria")
    @Min(1)
    private Integer cantidad;

    private LocalDateTime fechaPedido = LocalDateTime.now();

    private String estado = "PROCESADO";
}
