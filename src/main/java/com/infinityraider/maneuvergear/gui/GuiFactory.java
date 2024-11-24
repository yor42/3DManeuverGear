package com.infinityraider.maneuvergear.gui;

import com.infinityraider.maneuvergear.Tags;
import com.infinityraider.maneuvergear.handler.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class GuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraftInstance) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new ManeuverGearGuiConfig(parentScreen);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    public static class ManeuverGearGuiConfig extends GuiConfig {
        public ManeuverGearGuiConfig(GuiScreen guiScreen) {
            super(guiScreen,getConfigElements(), Tags.MOD_ID, false, false,
                    GuiConfig.getAbridgedConfigPath(ConfigurationHandler.getInstance().config.toString()));
        }

        private static List<IConfigElement> getConfigElements() {
            List<IConfigElement> list = new ArrayList<>();
            for(ConfigurationHandler.Categories category : ConfigurationHandler.Categories.values()) {
                list.add(new DummyConfigElement.DummyCategoryElement(
                            category.getDescription(),
                            category.getLangKey(),
                            new ConfigElement(ConfigurationHandler.getInstance().config.getCategory(category.getName())).getChildElements()));
            }
            return list;
        }
    }
}
