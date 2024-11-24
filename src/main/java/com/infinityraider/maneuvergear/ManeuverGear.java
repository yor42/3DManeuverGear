package com.infinityraider.maneuvergear;
import com.infinityraider.infinitylib.config.IModConfiguration;
import com.infinityraider.infinitylib.config.ModConfiguration;
import com.infinityraider.maneuvergear.handler.ConfigurationHandler;
import com.infinityraider.maneuvergear.init.EntityRegistry;
import com.infinityraider.maneuvergear.init.ItemRegistry;
import com.infinityraider.maneuvergear.network.*;
import com.infinityraider.maneuvergear.proxy.IProxy;
import com.infinityraider.maneuvergear.reference.Names;
import com.infinityraider.maneuvergear.reference.Reference;
import com.infinityraider.infinitylib.InfinityMod;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.proxy.base.IProxyBase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        guiFactory = Reference.GUI_FACTORY_CLASS,
        dependencies = "required-after:infinitylib;after:"+ Names.Mods.baubles
)
public class ManeuverGear extends InfinityMod {
    @Mod.Instance(Tags.MOD_ID)
    public static ManeuverGear instance;

    @SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
    public static IProxy proxy;

    @Override
    public IProxyBase proxy() {
        return proxy;
    }

    @Override
    public String getModId() {
        return Tags.MOD_ID;
    }

    @Override
    public IModConfiguration getConfiguration() {
        return ModConfiguration.getInstance();
    }

    @Override
    public Object getModBlockRegistry() {
        return this;
    }

    @Override
    public Object getModItemRegistry() {
        return ItemRegistry.getInstance();
    }

    @Override
    public Object getModEntityRegistry() {
        return EntityRegistry.getInstance();
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        wrapper.registerMessage(MessageBoostUsed.class);
        wrapper.registerMessage(MessageDartAnchored.class);
        wrapper.registerMessage(MessageEquipManeuverGear.class);
        wrapper.registerMessage(MessageManeuverGearEquipped.class);
        wrapper.registerMessage(MessageSpawnSteamParticles.class);
    }
}
