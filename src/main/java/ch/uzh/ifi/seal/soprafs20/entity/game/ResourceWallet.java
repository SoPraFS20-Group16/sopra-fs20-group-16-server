package ch.uzh.ifi.seal.soprafs20.entity.game;

import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.PlayerConstants;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "RESOURCE_WALLET")
public class ResourceWallet implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    private final Map<ResourceType, Integer> resources;

    public ResourceWallet() {
        resources = new EnumMap<>(ResourceType.class);

        int initAmount = PlayerConstants.INIT_RESOURCE_AMOUNT;

        for (ResourceType type : ResourceType.values()) {
            resources.put(type, initAmount);
        }
    }

    public void addResource(ResourceType type, int amount) {
        int updatedAmount = resources.get(type) + amount;
        resources.put(type, updatedAmount);
    }

    public int getResourceAmount(ResourceType type) {
        return resources.get(type);
    }

    public void removeResource(ResourceType type, int amount) {
        int currentAmount = resources.get(type);
        currentAmount = currentAmount - amount;

        resources.put(type, currentAmount);

        if (currentAmount < 0) {
            throw new IllegalStateException(ErrorMsg.NO_NEGATIVE_RESOURCES);
        }
    }

    public List<ResourceType> getAllTypes() {
        return new ArrayList<>(resources.keySet());
    }
}
