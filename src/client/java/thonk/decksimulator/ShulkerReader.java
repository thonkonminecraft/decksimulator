package thonk.decksimulator;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import thonk.decksimulator.simulation.Card;

import java.util.ArrayList;

public class ShulkerReader {

    public static ArrayList<Card> readDeckBox(ItemStack held) {
        ArrayList<Card> cards = new ArrayList<>();
        ItemContainerContents contents = held.get(DataComponents.CONTAINER);
        if (contents != null) {
            for (ItemStack s : contents.nonEmptyItems()) {
                String raw = s.getHoverName().getString();
                String cleaned = raw.replaceAll("[^A-Za-z0-9]", "");
                for (int x = 0; x < s.getCount(); x++) {
                    Card newCard = CardFactory.create(cleaned);
                    if (newCard != null) cards.add(newCard);
                }
            }
        }
        return cards;
    }

    public static ArrayList<String> describeDeckBox(ItemStack held) {
        ArrayList<String> cards = new ArrayList<>();
        ItemContainerContents contents = held.get(DataComponents.CONTAINER);
        if (contents != null) {
            for (ItemStack s : contents.nonEmptyItems()) {
                String raw = s.getHoverName().getString();
                String cleaned = raw.replaceAll("[^A-Za-z0-9]", "");
                if (CardFactory.check(cleaned)) cards.add(raw + " x " + s.getCount());
            }
        }
        return cards;
    }
}
