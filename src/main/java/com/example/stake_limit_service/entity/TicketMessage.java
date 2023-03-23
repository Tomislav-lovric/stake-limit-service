package com.example.stake_limit_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TicketMessage")
@Table(name = "ticket_messages")
public class TicketMessage {

    @Id
    //user adds id himself, so we don't need auto generation
//    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
            name = "id",
            updatable = false
    )
    private UUID id;

    @ManyToOne
    @JoinColumn(
            name = "device_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "device_id_fk"
            )
    )
    private Device device;

    @Column(
            name = "stake",
            nullable = false
    )
    private Double stake;

    @Column(
            name = "created_at",
            nullable = false
    )
    private LocalDateTime createdAt;
}
