package com.shopify.ims.service;

import com.shopify.ims.exception.InventoryNotExistException;
import com.shopify.ims.model.Group;
import com.shopify.ims.model.Inventory;
import com.shopify.ims.repository.GroupRepository;
import com.shopify.ims.repository.InventoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Group testGroup;
    private Inventory testInventory;

    @BeforeEach
    void setUp() {
        testGroup = new Group();
        testGroup.setName("test group");

        testInventory = new Inventory();
        testInventory.setName("test inventory");

        testGroup.getInventories().add(testInventory);

        testInventory.getGroups().add(testGroup);
    }

    @Test
    void getInventoryList() {
        List<Inventory> inventories = Collections.singletonList(testInventory);

        when(inventoryRepository.findAll()).thenReturn(inventories);

        List<Inventory> returnedList = inventoryService.getInventoryList();

        assertEquals(returnedList.size(), 1);
        assertEquals(returnedList.get(0).getName(), testInventory.getName());
    }

    @Test
    void delete() {
        when(inventoryRepository.existsById(1L)).thenReturn(true);
        when(inventoryRepository.getById(1L)).thenReturn(testInventory);

        inventoryService.delete(Collections.singletonList(1L));

        verify(inventoryRepository, times(1)).delete(testInventory);
    }

    @Test
    void add() {
        inventoryService.add(testInventory);

        verify(inventoryRepository, times(1)).save(testInventory);
    }

    @Test
    void update() {
        when(inventoryRepository.existsById(1L)).thenReturn(true);
        when(inventoryRepository.getById(1L)).thenReturn(testInventory);

        inventoryService.update(1L, testInventory);

        verify(inventoryRepository, times(1)).save(testInventory);
    }

    @Test
    void assignInventoryToGroups() {
        when(inventoryRepository.existsById(1L)).thenReturn(true);
        when(inventoryRepository.getById(1L)).thenReturn(testInventory);
        when(groupRepository.findById(1L)).thenReturn(Optional.of(testGroup));

        inventoryService.assignInventoryToGroups(1L, Collections.singletonList(1L));

        verify(groupRepository, times(1)).save(testGroup);
    }

    @Test
    void getInventoryById() {
        when(inventoryRepository.existsById(1L)).thenReturn(true);
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));

        Inventory returnedInventory = inventoryService.getInventoryById(1L);

        assertEquals(returnedInventory.getName(), testInventory.getName());
    }

    @Test
    void getInventoryById_throwsInventoryNotExistException() {
        when(inventoryRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(InventoryNotExistException.class, () -> inventoryService.getInventoryById(1L));
    }

    @Test
    void getGroupsByInventoryId() {
        when(inventoryRepository.existsById(1L)).thenReturn(true);
        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(testInventory));

        List<Group> returnedGroups = inventoryService.getGroupsByInventoryId(1L);

        assertEquals(returnedGroups.size(), testInventory.getGroups().size());
    }

    @Test
    void getGroupsByInventoryId_throwsInventoryNotExistException() {
        when(inventoryRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(InventoryNotExistException.class, () -> inventoryService.getGroupsByInventoryId(1L));
    }
}