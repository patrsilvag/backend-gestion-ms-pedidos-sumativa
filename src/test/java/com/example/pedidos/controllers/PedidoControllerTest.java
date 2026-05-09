package com.example.pedidos.controllers;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.pedidos.dto.CompraRequest;
import com.example.pedidos.dto.PedidoResponse;
import com.example.pedidos.exceptions.GlobalExceptionHandler;
import com.example.pedidos.exceptions.ResourceNotFoundException;
import com.example.pedidos.services.PedidoService;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("null")
class PedidoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PedidoService pedidoService;

    @InjectMocks
    private PedidoController pedidoController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pedidoController)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void testListarHistorial_Exitoso() throws Exception {
        PedidoResponse res = new PedidoResponse(1L, 10L, 2, LocalDateTime.now(), "PROCESADO");
        when(pedidoService.listarTodos()).thenReturn(Collections.singletonList(res));

        mockMvc.perform(get("/api/pedidos")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(10));
    }

    @Test
    void testObtenerPorId_NoEncontrado() throws Exception {
        when(pedidoService.buscarPorId(anyLong()))
                .thenThrow(new ResourceNotFoundException("No existe"));

        mockMvc.perform(get("/api/pedidos/99")).andExpect(status().isNotFound());
    }

    @Test
    void testCrearPedido_Exitoso() throws Exception {
        PedidoResponse res = new PedidoResponse(1L, 10L, 5, LocalDateTime.now(), "PROCESADO");
        when(pedidoService.registrarPedido(any(CompraRequest.class))).thenReturn(res);

        String json = "{\"productoId\":10, \"cantidad\":5}";

        mockMvc.perform(
                post("/api/pedidos/comprar").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.mensaje").exists());
    }

    @Test
    void testCrearPedido_ErrorValidacion() throws Exception {
        // Enviamos cantidad 0 para disparar @Min(1)
        String jsonInvalido = "{\"productoId\":10, \"cantidad\":0}";

        mockMvc.perform(post("/api/pedidos/comprar").contentType(MediaType.APPLICATION_JSON)
                .content(jsonInvalido)).andExpect(status().isBadRequest()); // Cubre
                                                                            // handleValidationErrors
    }

    @Test
    void testCrearPedido_AccesoDenegado() throws Exception {
        // Simulamos que el servicio lanza AccesoDenegadoException
        when(pedidoService.registrarPedido(any(CompraRequest.class))).thenThrow(
                new com.example.pedidos.exceptions.AccesoDenegadoException("No tienes permiso"));

        mockMvc.perform(post("/api/pedidos/comprar").contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":10, \"cantidad\":1}")).andExpect(status().isForbidden()); // Cubre
                                                                                                    // handleAccesoDenegado
    }

    @Test
    void testCrearPedido_RecursoDuplicado() throws Exception {
        // Simulamos conflicto (ej. pedido ya procesado)
        when(pedidoService.registrarPedido(any(CompraRequest.class))).thenThrow(
                new com.example.pedidos.exceptions.DuplicateResourceException("Pedido ya existe"));

        mockMvc.perform(post("/api/pedidos/comprar").contentType(MediaType.APPLICATION_JSON)
                .content("{\"productoId\":10, \"cantidad\":1}")).andExpect(status().isConflict()); // Cubre
                                                                                                   // handleDuplicateResource
    }

    @Test
    void testErrorGenerico() throws Exception {
        // Simulamos un error inesperado de servidor
        when(pedidoService.listarTodos()).thenThrow(new RuntimeException("Error de base de datos"));

        mockMvc.perform(get("/api/pedidos")).andExpect(status().isInternalServerError()); // Cubre
                                                                                          // handleGlobalException
    }
}
