package ch.uzh.ifi.seal.soprafs20.rest.dto.game;

import ch.uzh.ifi.seal.soprafs20.constant.ResourceType;

import java.util.Map;

public class ResourceDTO {

    private Map<ResourceType, Integer> resources;

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public void setResources(Map<ResourceType, Integer> resources) {
        this.resources = resources;
    }
}
