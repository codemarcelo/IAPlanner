package com.planeer.iAPlanner.model.persistence.domains;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "TBL_AGENDAMENTO")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_AGENDAMENTO")
    private Long id; // Identificador único

    @Column(name = "DS_TITULO")
    private String title; // Título do agendamento

    @Column(name = "DS_DESCRICAO")
    private String description; // Descrição do agendamento

    @Column(name = "DH_AGENDAMENTO")
    private LocalDateTime dateTime; // Data e hora (ISO 8601)

    @Column(name = "DS_ENDERECO")
    private String localAddress;

    @Column(name = "DS_PONTO_REFERENCIA")
    private String referencePoint;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipantsEntity> participants; // Lista de participantes
}
