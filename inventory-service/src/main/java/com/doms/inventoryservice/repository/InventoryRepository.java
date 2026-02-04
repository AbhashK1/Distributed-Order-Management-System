package com.doms.inventoryservice.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doms.inventoryservice.model.Inventory;

@Repository
public interface InventoryRepository
        extends JpaRepository<Inventory, UUID> {
}
