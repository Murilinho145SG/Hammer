package com.murilinho145.maysstorage.impl;

import com.murilinho145.maysstorage.MaysHammer;
import com.murilinho145.maysstorage.impl.items.hammer.Hammer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Collection;

public class RegistryImpl {
    public static final DeferredRegister<Item> DEFAULT_REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, MaysHammer.MODID);
    private static boolean AlreadyRegisteredItems = false;

    public static DeferredRegister<Item> registry() {
        if (!AlreadyRegisteredItems) {
            RegistryObject<Item> STONE_HAMMER = DEFAULT_REGISTRY.register("stone_hammer", () -> new Hammer(Tiers.STONE,3, 250));
            RegistryObject<Item> IRON_HAMMER = DEFAULT_REGISTRY.register("iron_hammer", () -> new Hammer(Tiers.IRON, 5, 500));
            RegistryObject<Item> DIAMOND_HAMMER = DEFAULT_REGISTRY.register("diamond_hammer", () -> new Hammer(Tiers.DIAMOND,7, 1350));
            AlreadyRegisteredItems = true;
        }
        return DEFAULT_REGISTRY;
    }

    public static Collection<RegistryObject<Item>> getEntries() {
        return DEFAULT_REGISTRY.getEntries();
    }

    public static Item getItem(String id) {
        return DEFAULT_REGISTRY.getEntries().stream().filter(i -> i.getId().getPath().equals(id)).findFirst().get().get();
    }
}
