package thonk.decksimulator;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import thonk.decksimulator.ui.SimulatorScreen;

public class DeckSimulator implements ClientModInitializer {

	public static KeyMapping openSimulatorKey;

	@Override
	public void onInitializeClient() {

		openSimulatorKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.decksimulator.openSimulator",
				GLFW.GLFW_KEY_H,
				"category.decksimulator.keys"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openSimulatorKey.consumeClick()) {
				client.setScreen(new SimulatorScreen(client.screen));
			}
		});
	}
}
