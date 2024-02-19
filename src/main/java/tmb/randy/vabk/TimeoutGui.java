package tmb.randy.vabk;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.io.IOException;

public class TimeoutGui extends GuiScreen {

    private GuiNumericField killCooldownField;
    private GuiNumericField loadBowCooldownField;

    private static final int BUTTON_SAVE = 0;
    private static final int COOLDOWN_1 = 1;
    private static final int COOLDOWN_2 = 2;

    private Configuration config;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        drawString(fontRendererObj, "Cooldown zwischen Kills:", this.width / 2 - 50, this.height / 2 - 45, 0xFFFFFF);
        drawString(fontRendererObj, "Ladezeit für Bogen:", this.width / 2 - 50, this.height / 2 - 5, 0xFFFFFF);


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.killCooldownField.mouseClicked(mouseX, mouseY, mouseButton);
        this.loadBowCooldownField.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == BUTTON_SAVE) {

            int killCooldown = killCooldownField.getValue();
            int loadBowCooldown = loadBowCooldownField.getValue();

            config.get("settings", "killCooldown", 200).set(killCooldown);
            config.get("settings", "loadBowCooldown", 40).set(loadBowCooldown);

            config.save();
            Mod.getSharedInstance().setKillCooldown(killCooldown);
            Mod.getSharedInstance().setBowLoadCooldown(loadBowCooldown);
            this.mc.displayGuiScreen(null);
        }

        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();

        config = new Configuration(new File(Minecraft.getMinecraft().mcDataDir, "vabk.cfg"));
        config.load();

        int cooldown1 = config.getInt("killCooldown", "settings", 200, 0, Integer.MAX_VALUE, "Kill cooldown");
        int cooldown2 = config.getInt("loadBowCooldown", "settings", 40, 0, Integer.MAX_VALUE, "Load bow cooldown");

        killCooldownField = new GuiNumericField(this.fontRendererObj, COOLDOWN_1, this.width / 2 - 50, this.height / 2 - 30);
        killCooldownField.setMinimum(1);
        killCooldownField.setEnabled(true);
        killCooldownField.isFocused();
        killCooldownField.setSteps(5);
        killCooldownField.setValue(cooldown1);

        loadBowCooldownField = new GuiNumericField(this.fontRendererObj, COOLDOWN_2, this.width / 2 - 50, this.height / 2 + 10);
        loadBowCooldownField.setMinimum(1);
        loadBowCooldownField.setEnabled(true);
        loadBowCooldownField.setSteps(2);
        loadBowCooldownField.setValue(cooldown2);

        this.buttonList.add(killCooldownField);
        this.buttonList.add(loadBowCooldownField);
        this.buttonList.add(new GuiButton(BUTTON_SAVE, this.width / 2 - 50, this.height / 2 + 40, 100, 20, "Speichern"));

        config.save();
    }

    @Override
    public void updateScreen() {
        this.killCooldownField.updateCursorCounter();
        this.loadBowCooldownField.updateCursorCounter();
        super.updateScreen();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        config.save();
        super.onGuiClosed();
    }
}