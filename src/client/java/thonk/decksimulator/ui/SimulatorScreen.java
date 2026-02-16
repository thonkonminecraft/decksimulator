package thonk.decksimulator.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import thonk.decksimulator.simulation.Card;
import thonk.decksimulator.Settings;
import thonk.decksimulator.ShulkerReader;
import thonk.decksimulator.simulation.Simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class SimulatorScreen extends Screen {
    private final Settings settings;

    private final Screen parent;

    private MultiLineTextBox deckDescription;

    private MultiLineTextBox simulationResults;

    private final Simulation simulation = new Simulation();

    public SimulatorScreen(Screen parent) {
        super(Component.literal("Deck Simulator"));
        this.parent = parent;
        settings = Settings.get();
    }

    @Override
    protected void init() {

        int cx = this.width / 2 - 95;
        int y = this.height / 2 - 110;
        int bw = 180, bh = 20, gap = 5;
        int ew = 180, eh = 180;

        this.addRenderableWidget(Button.builder(Component.literal("Read Deck"), b -> readDeck())
                .bounds(cx - bw / 2, y, bw, bh)
                .build());
        y += bh + gap;

        deckDescription = new MultiLineTextBox(cx - ew / 2, y, ew, eh, Component.literal("Deck Description"));
        this.addRenderableWidget(deckDescription);
        deckDescription.setText("Hold a deck and click above to load!");
        y += eh + gap;

        this.addRenderableWidget(Button.builder(Component.literal("Settings"), b -> Minecraft.getInstance().setScreen(new SettingsScreen(this)))
                .bounds(cx - bw / 2, y, bw, bh)
                .build());

        cx = this.width / 2 + 95;
        y = this.height / 2 - 110;

        this.addRenderableWidget(Button.builder(Component.literal("Simulate"), b -> simulate())
                .bounds(cx - bw / 2, y, bw - 37, bh)
                .build());
        addNumberInput(y, cx - bw / 2 + bw - 32, bh, 32, 4, "No of Simulations",
                settings::getNoSimulations,
                settings::setNoSimulations);
        y += bh + gap;

        simulationResults = new MultiLineTextBox(cx - ew / 2, y, ew, eh, Component.literal("Simulation Results"));
        this.addRenderableWidget(simulationResults);
        y += eh + gap;

        this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> onClose())
                .bounds(cx - bw / 2, y, bw, bh)
                .build());
    }

    public void readDeck() {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack held = player.getMainHandItem();
        if (!(held.getItem() instanceof BlockItem bi) || !(bi.getBlock() instanceof ShulkerBoxBlock)) {
            deckDescription.setText("Invalid Held Item: Please hold a deck!");
            return;
        }

        ArrayList<Card> deck = ShulkerReader.readDeckBox(held);
        if (deck.isEmpty() || deck.size() > 40) {
            deckDescription.setText("Deck is invalid size!");
            return;
        }

        ArrayList<String> descriptionArray = ShulkerReader.describeDeckBox(held);
        deckDescription.setLines(descriptionArray);

        simulation.loadDeck(deck);

        simulationResults.setLines(Arrays.asList("Deck Loaded! Size: " + deck.size(), "Click above to simulate!"));
    }

    private void addNumberInput(int y, int inputX, int bh, int inputW, int maxLength, String label,
                                java.util.function.Supplier<Integer> getVal, java.util.function.Consumer<Integer> save) {
        EditBox box = new EditBox(this.font, inputX, y, inputW, bh, Component.literal(label));
        box.setValue(String.valueOf(getVal.get()));
        box.setFilter(s -> s.isEmpty() || s.matches("\\d*"));
        box.setMaxLength(maxLength);
        box.setResponder(text -> {
            if (text == null || text.isEmpty()) return;
            try {
                int parsed = Integer.parseInt(text);
                if (parsed != getVal.get()) {
                    save.accept(parsed);
                }
            } catch (NumberFormatException ignored) {
            }
        });
        this.addRenderableWidget(box);
    }

    public void simulate() {
        if (!simulation.isDeckLoaded()) {
            simulationResults.setText("Load a deck first!");
        } else {
            simulation.startSimulation(simulationResults);
        }
    }

    @Override
    public void onClose() {
        if (this.parent != null) {
            this.minecraft.setScreen(this.parent);
        } else {
            super.onClose();
        }
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        super.render(gfx, mouseX, mouseY, delta);
        gfx.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 130, 0xFFFFFFFF);
    }
}
