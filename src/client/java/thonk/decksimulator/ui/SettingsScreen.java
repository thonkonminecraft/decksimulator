package thonk.decksimulator.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import thonk.decksimulator.Settings;

public class SettingsScreen extends Screen {

    private final Screen parent;

    public SettingsScreen(Screen parent) {
        super(Component.literal("Settings"));
        this.parent = parent;
    }

    private record LabelEntry(String text, int x, int y) {
    }

    private final java.util.List<LabelEntry> labels = new java.util.ArrayList<>();

    @Override
    protected void init() {
        int cx = this.width / 2 - 95;
        int center = this.width / 2;
        int y = this.height / 2 - 90;
        int bw = 180, bh = 20, gap = 5, inputDigitSize = 10;

        addShuffle(cx, y, bw, bh, "Target Level",
                Settings.get()::tagTargetLevel,
                Settings.get()::nextTargetLevel);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 3 * inputDigitSize, 3, "LVL1 time to Door",
                Settings.get()::getLvlOneTimeToExit,
                Settings.get()::setLvlOneTimeToExit);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 3 * inputDigitSize, 3, "LVL1 time to Artifact",
                Settings.get()::getLvlOneTimeToArtifact,
                Settings.get()::setLvlOneTimeToArtifact);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 3 * inputDigitSize, 3, "LVL2 time to Door",
                Settings.get()::getLvlTwoTimeToExit,
                Settings.get()::setLvlTwoTimeToExit);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 3 * inputDigitSize, 3, "LVL2 time to Artifact",
                Settings.get()::getLvlTwoTimeToArtifact,
                Settings.get()::setLvlTwoTimeToArtifact);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 3 * inputDigitSize, 3, "LVL3 time to Door",
                Settings.get()::getLvlThreeTimeToExit,
                Settings.get()::setLvlThreeTimeToExit);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 3 * inputDigitSize, 3, "LVL3 time to Artifact",
                Settings.get()::getLvlThreeTimeToArtifact,
                Settings.get()::setLvlThreeTimeToArtifact);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 3 * inputDigitSize, 3, "LVL4 time to Artifact",
                Settings.get()::getLvlFourTimeToArtifact,
                Settings.get()::setLvlFourTimeToArtifact);
        y += bh + 2 * gap;

        this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> Minecraft.getInstance().setScreen(parent))
                .bounds(center - bw / 2, y, bw, bh)
                .build());

        cx = this.width / 2 + 95;
        y = this.height / 2 - 90;

        addShuffle(cx, y, bw, bh, "Artifact",
                Settings.get()::tagArtifactClause,
                Settings.get()::nextArtifactClause);
        y += bh + gap;

        addShuffle(cx, y, bw, bh, "Exit",
                Settings.get()::tagExitClause,
                Settings.get()::nextExitClause);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 2 * inputDigitSize, 2, "Adrenaline Rush Hits",
                Settings.get()::getAdrenalineRushInput,
                Settings.get()::setAdrenalineRushInput);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 2 * inputDigitSize, 2, "Feeding Frenzy Hits",
                Settings.get()::getFeedingFrenzyInput,
                Settings.get()::setFeedingFrenzyInput);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 2 * inputDigitSize, 2, "Frozen Tears Hits",
                Settings.get()::getFrozenTearsInput,
                Settings.get()::setFrozenTearsInput);
        y += bh + gap;

        addShuffle(cx, y, bw, bh, "Hopscotch Reward",
                Settings.get()::tagHopscotchInput,
                Settings.get()::nextHopscotchInput);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 2 * inputDigitSize, 2, "Ravenous Inclination Hits",
                Settings.get()::getRavenousInclinationInput,
                Settings.get()::setRavenousInclinationInput);
        y += bh + gap;

        addNumberInput(cx, y, bw, bh, 2 * inputDigitSize, 2, "Spirit Sense Hits",
                Settings.get()::getSpiritSenseInput,
                Settings.get()::setSpiritSenseInput);
    }

    private void addNumberInput(int cx, int y, int bw, int bh, int inputW, int maxLength, String label,
            java.util.function.Supplier<Integer> getVal, java.util.function.Consumer<Integer> save) {
        int labelW = bw - inputW - 2;
        int leftX = cx - bw / 2;
        labels.add(new LabelEntry(label + ":", leftX + labelW / 2, y + (bh - 8) / 2));
        int inputX = leftX + labelW;
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

    private void addShuffle(int cx, int y, int bw, int bh, String label,
                            java.util.function.Supplier<String> getVal, Runnable shuffle) {
        Button button = Button.builder(getLabel(label, getVal.get()),
            b -> {
                shuffle.run();
                b.setMessage(getLabel(label, getVal.get()));
            }
        ).bounds(cx - bw / 2, y, bw, bh).build();
        this.addRenderableWidget(button);
    }

    private Component getLabel(String label, String value) {
        int color = 0x55FF55;
        return Component.literal(label + ": ")
                .append(Component.literal(value).withStyle(style -> style.withColor(color)));
    }

    @Override
    public void render(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        this.renderBackground(gfx, mouseX, mouseY, delta);
        super.render(gfx, mouseX, mouseY, delta);
        gfx.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 110, 0xFFFFFF);
        for (LabelEntry l : labels) {
            gfx.drawCenteredString(this.font, l.text, l.x, l.y, 0xFFFFFF);
        }
    }
}
