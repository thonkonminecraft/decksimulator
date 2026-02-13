package thonk.decksimulator.simulation;

import java.util.List;

public class Level {

    private final String name;

    private final List<LootSpot> locations;

    public Level(List<LootSpot> locations, String name) {
        this.locations = locations;
        this.name = name;
    }

    public void triggerTreasure(PlayerInventory inv) {
        for (LootSpot l : locations) {
            l.triggerTreasure(inv);
        }
    }

    public void triggerEmber(PlayerInventory inv) {
        for (LootSpot l : locations) {
            l.triggerEmber(inv);
        }
    }

    public String getName() {
        return name;
    }
}
