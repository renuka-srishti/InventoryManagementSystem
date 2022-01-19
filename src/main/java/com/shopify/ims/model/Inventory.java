package com.shopify.ims.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Inventory extends AbstractPersistable<Long> {
    private String name;
    private String description;
    private long quantity;
    private double pricePerItem;

    @ManyToMany
    @JoinTable(
            name = "inventory_group",
            joinColumns = @JoinColumn(name = "inventory_id"),
            inverseJoinColumns = @JoinColumn(name = "named_group_id"))
    private Set<Group> groups = new HashSet<>();
}
