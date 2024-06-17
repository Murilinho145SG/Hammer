package com.murilinho145.maysstorage.impl.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

public enum LevelHarvest {
    WOODEN(1),
    STONE(2),
    IRON(3),
    DIAMOND(4),
    NETHERITE(5);

    private final int level;

    private LevelHarvest(int level) {
        this.level = level;
    }

    public static int getLevel(TagKey<Block> tag) {
        if (tag.equals(BlockTags.NEEDS_STONE_TOOL)) {
            return STONE.level;
        } else if (tag.equals(BlockTags.NEEDS_IRON_TOOL)) {
            return IRON.level;
        } else if (tag.equals(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return DIAMOND.level;
        } else if (tag.equals(Tags.Blocks.NEEDS_NETHERITE_TOOL)) {
            return NETHERITE.level;
        } else {
            return WOODEN.level;
        }
    }

    public static boolean getLevel(TagKey<Block> tag, boolean name) {
        if (tag.equals(BlockTags.NEEDS_STONE_TOOL)) {
            return true;
        } else if (tag.equals(BlockTags.NEEDS_IRON_TOOL)) {
            return true;
        } else if (tag.equals(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return true;
        } else if (tag.equals(Tags.Blocks.NEEDS_NETHERITE_TOOL)) {
            return true;
        } else {
            return true;
        }
    }
}
