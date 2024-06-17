package com.murilinho145.maysstorage;

import com.mojang.logging.LogUtils;
import com.murilinho145.maysstorage.impl.RegistryImpl;
import com.murilinho145.maysstorage.impl.command.GetTag;
import com.murilinho145.maysstorage.impl.primitives.RegistryTabs;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(MaysStorage.MODID)
public class MaysStorage {
    public static final String MODID = "maysstorage";
    public static final Logger LOGGER = LogUtils.getLogger();

    public MaysStorage() {
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
