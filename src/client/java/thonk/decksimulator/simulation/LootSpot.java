package thonk.decksimulator.simulation;

public class LootSpot {

    private final Dropper treasureDropper, emberDropper;
    private final String name;

    public LootSpot(String name, Dropper emberDropper, Dropper treasureDropper) {
        this.treasureDropper = treasureDropper;
        this.emberDropper = emberDropper;
        this.name = name;
    }

    public void triggerTreasure(PlayerInventory inv) {
        if (treasureDropper != null) {
            ItemType itemType = treasureDropper.drop();
            if (itemType != ItemType.NONE) {
                addLoot(itemType, inv);
            }
        }
    }

    public void triggerEmber(PlayerInventory inv) {
        if (emberDropper != null) {
            ItemType itemType = emberDropper.drop();
            if (itemType != ItemType.NONE) {
                addLoot(itemType, inv);
            }
        }
    }

    private void addLoot(ItemType itemType, PlayerInventory inv) {
        switch (itemType) {
            case EMBER -> inv.addEmbers(1);
            case CROWN -> inv.addCrowns(1);
            case COIN -> inv.addCoins(1);
            case LVLONEKEY -> inv.addLVLOneKeys(1);
            case LVLTWOKEY -> inv.addLVLTwoKeys(1);
            case LVLTHREEKEY -> inv.addLVLThreeKeys(1);
        }
    }

    public String getName() {
        return name;
    }
}
