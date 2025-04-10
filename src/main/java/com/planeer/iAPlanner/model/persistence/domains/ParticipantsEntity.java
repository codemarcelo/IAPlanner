package com.planeer.iAPlanner.model.persistence.domains;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "TBL_PARTICIPANTES")
public class ParticipantsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PARTICIPANTE")
    private Long id; // Identificador único

    @Column(name = "NM_NOME", nullable = false)
    private String name; // Nome do participante

    @Column(name = "DS_TELEFONE", nullable = false)
    private String phone; // Telefone do participante

    @Column(name = "DS_EMAIL", nullable = false)
    private String email; // Email do participante

    @ManyToOne
    @JoinColumn(name = "ID_AGENDAMENTO", nullable = false)
    private ScheduleEntity schedule; // Agendamento associado
}