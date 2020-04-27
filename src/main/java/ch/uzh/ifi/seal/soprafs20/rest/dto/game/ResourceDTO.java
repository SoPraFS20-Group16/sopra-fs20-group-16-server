package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;
import ch.uzh.ifi.seal.soprafs20.entity.game.ResourceWallet;

import java.util.EnumMap;

public class ResourceDTO extends EnumMap<ResourceType, Integer> {

    public ResourceDTO(ResourceWallet wallet) {
        super(ResourceType.class);
        this.setWallet(wallet);
    }

    private void setWallet(ResourceWallet wallet) {
        for (ResourceType type : ResourceType.values()) {
            this.put(type, wallet.getResourceAmount(type));
        }
    }
}
