package thonk.decksimulator.simulation;

public class PlayerInventory {

    private int embers, crowns, coins, LVLOneKeys, LVLTwoKeys, LVLThreeKeys;

    public PlayerInventory() {

    }

    public void addEmbers(int a) { embers += a; }
    public void addCrowns(int a) { crowns += a; }
    public void addCoins(int a) { coins += a; }
    public void addLVLOneKeys(int a) { LVLOneKeys += a; }
    public void addLVLTwoKeys(int a) { LVLTwoKeys += a; }
    public void addLVLThreeKeys(int a) { LVLThreeKeys += a; }

    public void subEmbers(int a) { embers -= a; }
    public void subCrowns(int a) { crowns -= a; }
    public void subCoins(int a) { coins -= a; }
    public void subLVLOneKeys(int a) { LVLOneKeys -= a; }
    public void subLVLTwoKeys(int a) { LVLTwoKeys -= a; }
    public void subLVLThreeKeys(int a) { LVLThreeKeys -= a; }

    public int getEmbers() { return embers; }
    public int getCrowns() { return crowns; }
    public int getCoins() { return coins; }
    public int getLVLOneKeys() { return LVLOneKeys; }
    public int getLVLTwoKeys() { return LVLTwoKeys; }
    public int getLVLThreeKeys() { return LVLThreeKeys; }
}
