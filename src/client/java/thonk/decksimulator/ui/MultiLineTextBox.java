package thonk.decksimulator.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class MultiLineTextBox extends AbstractWidget {
    private final Minecraft mc = Minecraft.getInstance();
    private final Font font = mc.font;

    private final int pad = 6;

    private double scrollY = 0.0;
    private double maxScroll = 0.0;

    private Component rawText = Component.empty();
    private final List<FormattedCharSequence> wrapped = new ArrayList<>();

    private int trackLeft, trackRight, trackTop, trackBottom, thumbTop, thumbHeight;
    private boolean draggingThumb = false;
    private int dragStartMouseY;
    private double dragStartScrollY;

    public MultiLineTextBox(int x, int y, int width, int height, Component title) {
        super(x, y, width, height, title);
        this.active = true;
        this.visible = true;
        this.setFocused(false);
    }

    public void setText(Component text) {
        this.rawText = (text == null) ? Component.empty() : text;
        rewrapFromRaw();
    }

    public void setText(String text) {
        setText(Component.literal(text == null ? "" : text));
    }

    public void setLines(List<String> lines) {
        this.rawText = Component.empty();
        wrapped.clear();
        int contentWidth = Math.max(1, getInnerWidth());
        for (String s : lines) {
            wrapped.addAll(font.split(Component.literal(s), contentWidth));
        }
        recomputeScroll();
    }

    private void rewrapFromRaw() {
        wrapped.clear();
        int contentWidth = Math.max(1, getInnerWidth());
        wrapped.addAll(font.split(this.rawText, contentWidth));
        recomputeScroll();
    }

    private int getInnerWidth() { return this.getWidth() - 2 * pad; }
    private int getInnerHeight() { return this.getHeight() - 2 * pad; }
    private int lineHeightPx() {
        int lineGap = 2;
        return font.lineHeight + lineGap; }

    private void recomputeScroll() {
        int contentHeight = Math.max(0, wrapped.size() * lineHeightPx());
        int viewHeight = Math.max(1, getInnerHeight());
        maxScroll = Math.max(0, contentHeight - viewHeight);
        scrollY = Math.max(0.0, Math.min(scrollY, maxScroll));
    }

    @Override
    protected void renderWidget(GuiGraphics gfx, int mouseX, int mouseY, float delta) {
        int x1 = getX(), y1 = getY();
        int x2 = x1 + getWidth(), y2 = y1 + getHeight();

        gfx.fill(x1, y1, x2, y2, 0xAA000000);

        int border = 0xFFFFFFFF;
        gfx.fill(x1, y1, x2, y1 + 1, border);
        gfx.fill(x1, y2 - 1, x2, y2, border);
        gfx.fill(x1, y1, x1 + 1, y2, border);
        gfx.fill(x2 - 1, y1, x2, y2, border);

        int cx1 = x1 + pad, cy1 = y1 + pad, cx2 = x2 - pad, cy2 = y2 - pad;

        gfx.enableScissor(cx1, cy1, cx2, cy2);

        int lh = lineHeightPx();
        int firstLine = (int) Math.floor(scrollY / lh);
        double firstLineOffset = -(scrollY - firstLine * lh);

        int y = cy1 + (int) firstLineOffset;
        int maxY = cy2;

        for (int i = firstLine; i < wrapped.size() && y < maxY; i++) {
            gfx.drawString(font, wrapped.get(i), cx1, y, 0xFFFFFF, false);
            y += lh;
        }

        gfx.disableScissor();

        drawScrollbar(gfx);
    }

    private void drawScrollbar(GuiGraphics gfx) {
        trackLeft = trackRight = trackTop = trackBottom = thumbTop = thumbHeight = 0;

        if (maxScroll <= 0) return;

        int x1 = getX(), y1 = getY();
        int x2 = x1 + getWidth(), y2 = y1 + getHeight();

        int barW = 5;
        trackLeft = x2 - pad - barW;
        trackRight = x2 - pad;
        trackTop = y1 + pad;
        trackBottom = y2 - pad;

        gfx.fill(trackLeft, trackTop, trackRight, trackBottom, 0x33000000);

        int viewH = getInnerHeight();
        int contentH = viewH + (int) maxScroll;

        int minThumb = 12;
        thumbHeight = Math.max(minThumb, (int) Math.round((viewH * viewH) / (double) contentH));

        int scrollRange = Math.max(1, viewH - thumbHeight);
        thumbTop = trackTop + (int) Math.round(scrollRange * (scrollY / maxScroll));

        gfx.fill(trackLeft, thumbTop, trackRight, thumbTop + thumbHeight, 0x88FFFFFF);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
        if (!isMouseOver(mouseX, mouseY)) return false;
        if (maxScroll <= 0) return false;

        double amount = -deltaY * lineHeightPx() * 3;
        scrollY = clamp(scrollY + amount, 0.0, maxScroll);
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isMouseOver(mouseX, mouseY)) return false;

        if (inside((int) mouseX, (int) mouseY, trackLeft, trackTop, trackRight, trackBottom) && maxScroll > 0) {
            if (inside((int) mouseX, (int) mouseY, trackLeft, thumbTop, trackRight, thumbTop + thumbHeight)) {
                draggingThumb = true;
                dragStartMouseY = (int) mouseY;
                dragStartScrollY = scrollY;
                return true;
            }
            int viewH = getInnerHeight();
            double page = Math.max(1, viewH - lineHeightPx());
            scrollY = clamp(scrollY + ((mouseY < thumbTop) ? -page : page), 0.0, maxScroll);
            return true;
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragDX, double dragDY) {
        if (!draggingThumb || maxScroll <= 0) return false;

        int viewH = getInnerHeight();
        int scrollRange = Math.max(1, viewH - thumbHeight);

        double dy = mouseY - dragStartMouseY;
        double frac = dy / scrollRange;
        scrollY = clamp(dragStartScrollY + frac * maxScroll, 0.0, maxScroll);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        draggingThumb = false;
        return false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {
        narration.add(net.minecraft.client.gui.narration.NarratedElementType.TITLE, this.getMessage());
    }

    private static boolean inside(int x, int y, int left, int top, int right, int bottom) {
        return x >= left && x < right && y >= top && y < bottom;
    }

    private static double clamp(double v, double lo, double hi) {
        return (v < lo) ? lo : Math.min(v, hi);
    }
}
