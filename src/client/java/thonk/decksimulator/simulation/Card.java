package thonk.decksimulator.simulation;

public class Card {

    private final int clankBlock;
    private final int hazardBlock;

    private final int clank;
    private final int hazard;

    private final int emberQueue;
    private final int treasureQueue;

    private final int recycle;

    private final int speed;
    private final int jump;
    private final int regeneration;

    private final int cardShop;

    private final boolean recyclable;

    private final boolean effect;

    private final boolean permanent;

    private final String name;

    public Card(int clankBlock, int hazardBlock, int clank, int hazard, int emberQueue, int treasureQueue, int recycle,
                int speed, int jump, int regeneration, int cardShop, boolean recyclable, boolean effect, boolean permanent, String name) {
        this.clankBlock = clankBlock;
        this.hazardBlock = hazardBlock;
        this.clank = clank;
        this.hazard = hazard;
        this.emberQueue = emberQueue;
        this.treasureQueue = treasureQueue;
        this.recycle = recycle;
        this.speed = speed;
        this.jump = jump;
        this.regeneration = regeneration;
        this.cardShop = cardShop;
        this.recyclable = recyclable;
        this.effect = effect;
        this.permanent = permanent;
        this.name = name;
    }

    public int getClankBlock() { return clankBlock; }
    public int getHazardBlock() { return hazardBlock; }
    public int getClank() { return clank; }
    public int getHazard() { return hazard; }
    public int getEmberQueue() { return emberQueue; }
    public int getTreasureQueue() { return treasureQueue; }
    public int getRecycle() { return recycle; }
    public int getSpeed() { return speed; }
    public int getJump() { return jump; }
    public int getRegeneration() { return regeneration; }
    public int getCardShop() { return cardShop; }

    public boolean isRecyclable() { return recyclable; }

    public boolean hasEffect() { return effect; }

    public boolean isPermanent() { return permanent; }

    public String getName() { return name; }
}
