package tmb.randy.vabk;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class KeyBindings {

    public static KeyBinding toggleVabkAction;
    public static KeyBinding openSettingsAction;

    public static void init() {
        toggleVabkAction = new KeyBinding("Toggle VABK", Keyboard.KEY_J, "VABK");
        openSettingsAction = new KeyBinding("Open settings", Keyboard.KEY_K, "VABK");
        ClientRegistry.registerKeyBinding(toggleVabkAction);
        ClientRegistry.registerKeyBinding(openSettingsAction);
    }
}