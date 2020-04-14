package ch.uzh.ifi.seal.soprafs20.entity.game;


import ch.uzh.ifi.seal.soprafs20.constant.ErrorMsg;
import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;

import javax.persistence.*;
import java.util.EnumMap;
import java.util.Map;

@Entity
@Table(name = "RESOURCE_WALLET")
public class ResourceWallet {

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection
    private Map<ResourceType, Integer> resources;

    public ResourceWallet() {
        resources = new EnumMap<>(ResourceType.class);
    }

    public void addResource(ResourceType type, int amount) {
        int currentAmount = resources.get(type);
        resources.put(type, (currentAmount + amount));
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
}
