package thonk.decksimulator.simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dropper {

    private final List<ItemType> dropper;
    private final int firstNum, firstDen;

    public Dropper (int firstNum, int firstDen,
                    int embers, int coins, int crowns, int lvlonekey, int lvltwokey, int lvlthreekey) {
        this.firstNum = firstNum;
        this.firstDen = firstDen;
        dropper = new ArrayList<>();

        for (int i = 0; i < embers; i++) {
            dropper.add(ItemType.EMBER);
        }
        for (int i = 0; i < coins; i++) {
            dropper.add(ItemType.COIN);
        }
        for (int i = 0; i < crowns; i++) {
            dropper.add(ItemType.CROWN);
        }
        for (int i = 0; i < lvlonekey; i++) {
            dropper.add(ItemType.LVLONEKEY);
        }
        for (int i = 0; i < lvltwokey; i++) {
            dropper.add(ItemType.LVLTWOKEY);
        }
        for (int i = 0; i < lvlthreekey; i++) {
            dropper.add(ItemType.LVLTHREEKEY);
        }
    }

    public ItemType drop() {
        if (shouldDrop()) {
            Random r = new Random();
            return dropper.get(r.nextInt(dropper.size()));
        } else {
            return ItemType.NONE;
        }
    }

    private boolean shouldDrop() {
        return randomize(firstNum, firstDen);
    }

    private boolean randomize(int numerator, int denominator) {
        Random r = new Random();
        if (r.nextInt(denominator) < numerator) {
            return true;
        } else {
            return r.nextInt(denominator - 1) < numerator;
        }
    }
}
