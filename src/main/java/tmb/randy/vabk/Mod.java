package tmb.randy.vabk;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

@net.minecraftforge.fml.common.Mod(modid = Mod.MODID, version = Mod.VERSION)
public class Mod {
    public static final String MODID = "vabk";
    public static final String VERSION = "1.0";
    private final String chatPrefix = "§6[§8§lV§7§lA§c§lB§4§lK§6]§f ";

    private int killCooldown = 45;
    private int bowLoadCooldown = 30;
    private int cooldown = 0;
    private final Logger logger = LogManager.getLogger();
    private GuiTextField chatInputField;

    private static Mod sharedInstance;

    private boolean active;

    private TimeoutGui settingsGui = new TimeoutGui();

    public static Mod getSharedInstance() {
        return sharedInstance;
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        KeyBindings.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        Mod.sharedInstance = this;
        MinecraftForge.EVENT_BUS.register(Mod.this);
    }

    @SubscribeEvent
    public void tick(TickEvent event) {
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null)
            return;

        if (event.phase == TickEvent.Phase.START && active) {
            cooldown++;

            if (cooldown >= (killCooldown + bowLoadCooldown)) {
                cooldown = 0;
                shoot();
            } else if (cooldown == bowLoadCooldown) {
                startUsingBow();
            }
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (event.gui instanceof GuiChat) {
            try {
                Field field = GuiChat.class.getDeclaredField("inputField");
                field.setAccessible(true);
                chatInputField = (GuiTextField) field.get(event.gui);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public void onKeyInput(GuiScreenEvent.KeyboardInputEvent.Pre event) {

/*
        if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) && chatInputField != null) {
            String chatInput = chatInputField.getText();
            if (chatInput.equalsIgnoreCase("vabk")) {
                toggleActive();
                chatInputField.setText("");
            } else if (chatInput.equalsIgnoreCase("/vabk settings")) {
                //showSettingsGui();
                chatInputField.setText("");
            }
        }

 */

    }

    @SubscribeEvent
    public void keyInput(InputEvent.KeyInputEvent event) {
        if(KeyBindings.toggleVabkAction.isPressed() && chatInputField == null) {
            toggleActive();
        } else if(KeyBindings.openSettingsAction.isPressed() && chatInputField == null) {
            showSettingsGui();
        }
    }

    public void toggleActive() {
        active = !active;
        sendChatMessage(active ? "§aAKTIV" : "§4INAKTIV");
    }

    private void log(String string) {
        logger.log(Level.INFO, string);
    }

    private void sendChatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(chatPrefix + message));
    }

    private void startUsingBow() {
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null)
            return;

        Minecraft.getMinecraft().thePlayer.inventory.currentItem = 2;
        ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();

        if (heldItem != null && heldItem.getItem() instanceof ItemBow)
            Minecraft.getMinecraft().playerController.sendUseItem(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, heldItem);
    }

    private void shoot() {
        if(Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null)
            return;

        ItemStack heldItem = Minecraft.getMinecraft().thePlayer.getHeldItem();

        if (heldItem != null && heldItem.getItem() instanceof ItemBow)
            Minecraft.getMinecraft().playerController.onStoppedUsingItem(Minecraft.getMinecraft().thePlayer);

        Minecraft.getMinecraft().thePlayer.inventory.currentItem = 0;
    }

    private void showSettingsGui() { Minecraft.getMinecraft().displayGuiScreen(settingsGui); }

    public void setKillCooldown(int killCooldown) { this.killCooldown = killCooldown; }

    public void setBowLoadCooldown(int bowLoadCooldown) { this.bowLoadCooldown = bowLoadCooldown; }
}
