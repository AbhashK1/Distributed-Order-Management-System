package com.doms.inventoryservice.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter @Setter
public class Inventory {

    @Id
    private UUID productId;

    private int availableQuantity;
}
