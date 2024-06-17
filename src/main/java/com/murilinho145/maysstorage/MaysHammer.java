package com.murilinho145.maysstorage;

import com.mojang.logging.LogUtils;
import com.murilinho145.maysstorage.impl.RegistryImpl;
import com.murilinho145.maysstorage.impl.command.GetTag;
import com.murilinho145.maysstorage.impl.primitives.RegistryTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(MaysHammer.MODID)
public class MaysHammer {
    public static final String MODID = "mayshammer";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MaysHammer() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryImpl.registry().register(modEventBus);
        RegistryTabs.registry().register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        //modEventBus.addListener(this::addCreative);
    }

/*    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            for (RegistryObject<Item> entry : RegistryImpl.registry().getEntries()) {
                event.accept(entry);
            }
        }
    }*/

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        GetTag.register(event.getDispatcher());
    }

}
