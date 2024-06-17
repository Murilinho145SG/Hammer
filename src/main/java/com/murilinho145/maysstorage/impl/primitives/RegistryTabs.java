package com.murilinho145.maysstorage.impl.primitives;

import com.murilinho145.maysstorage.MaysStorage;
import com.murilinho145.maysstorage.impl.RegistryImpl;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RegistryTabs {
    private static boolean AlreadyRegisteredTab = false;
    public static final DeferredRegister<CreativeModeTab> DEFAULT_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MaysStorage.MODID);


    public static DeferredRegister<CreativeModeTab> registry() {
        if (!AlreadyRegisteredTab) {
            RegistryObject<CreativeModeTab> TAB = DEFAULT_TAB.register("tab", () -> CreativeModeTab.builder()
                    .title(Component.literal("Hammer"))
                    .displayItems((p,o) -> {
                        for (RegistryObject<Item> entry : RegistryImpl.DEFAULT_REGISTRY.getEntries()) {
                            o.accept(entry.get());
                        }
                    })
                    .icon(() -> RegistryImpl.getItem("iron_hammer").getDefaultInstance())
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .build());
            AlreadyRegisteredTab = true;
        }
        return DEFAULT_TAB;
    }
}
