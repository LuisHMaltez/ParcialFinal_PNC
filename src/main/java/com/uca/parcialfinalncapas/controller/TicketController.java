package com.uca.parcialfinalncapas.controller;

import com.uca.parcialfinalncapas.dto.request.TicketCreateRequest;
import com.uca.parcialfinalncapas.dto.request.TicketUpdateRequest;
import com.uca.parcialfinalncapas.dto.response.GeneralResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponse;
import com.uca.parcialfinalncapas.dto.response.TicketResponseList;
import com.uca.parcialfinalncapas.exceptions.BadTicketRequestException;
import com.uca.parcialfinalncapas.service.TicketService;
import com.uca.parcialfinalncapas.utils.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {
    private TicketService ticketService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<GeneralResponse> getAllTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isTech = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_TECH"));
        String correo = authentication.getName();
        List<TicketResponseList> tickets;
        if (isTech) {
            tickets = ticketService.getAllTickets();
        } else {
            // Buscar usuarioId por correo
            // ...
            // Obtener usuarioId
            User usuario = userRepository.findByCorreo(correo).orElse(null);
            if (usuario == null) {
                return ResponseBuilderUtil.buildResponse("Usuario no encontrado", HttpStatus.NOT_FOUND, null);
            }
            tickets = ticketService.getTicketsByUsuarioId(usuario.getId());
        }
        return ResponseBuilderUtil.buildResponse("Tickets obtenidos correctamente",
                tickets.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK,
                tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneralResponse> getTicketById(@PathVariable Long id) {
        TicketResponse ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            throw new BadTicketRequestException("Ticket no encontrado");
        }
        return ResponseBuilderUtil.buildResponse("Ticket found", HttpStatus.OK, ticket);
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> createTicket(@Valid @RequestBody TicketCreateRequest ticket) {
        // Solo USER puede crear tickets
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isUser = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
        if (!isUser) {
            return ResponseBuilderUtil.buildResponse("Solo usuarios pueden crear tickets", HttpStatus.FORBIDDEN, null);
        }
        TicketResponse createdTicket = ticketService.createTicket(ticket);
        return ResponseBuilderUtil.buildResponse("Ticket creado correctamente", HttpStatus.CREATED, createdTicket);
    }

    @PutMapping
    public ResponseEntity<GeneralResponse> updateTicket(@Valid @RequestBody TicketUpdateRequest ticket) {
        // Solo TECH puede actualizar estado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isTech = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_TECH"));
        if (!isTech) {
            return ResponseBuilderUtil.buildResponse("Solo técnicos pueden actualizar el estado", HttpStatus.FORBIDDEN, null);
        }
        TicketResponse updatedTicket = ticketService.updateTicket(ticket);
        return ResponseBuilderUtil.buildResponse("Ticket actualizado correctamente", HttpStatus.OK, updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseBuilderUtil.buildResponse("Ticket eliminado correctamente", HttpStatus.OK, null);
    }
}
