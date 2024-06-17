package com.murilinho145.maysstorage.impl.items.hammer;

import com.murilinho145.maysstorage.MaysStorage;
import com.murilinho145.maysstorage.impl.items.LevelHarvest;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hammer extends PickaxeItem {
    private int range;
    private int radius;
    private final int durability;
    private final Tiers tier;
    private final List<LevelBlock> tagBlockBreak = new ArrayList<>();
    private HammerMode mode;

    public Hammer(Tiers tiers, int range, int durability) {
        super(tiers, 1, 1, new Properties().durability(durability));
        this.range = range;
        this.radius = (range - 1) / 2;
        this.durability = durability;
        this.tier = tiers;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        CompoundTag tag = itemstack.getOrCreateTag();

        if (!tag.contains("mode")) {
            tag.putInt("mode", 1);
            itemstack.setTag(tag);
        } else {
            this.mode = HammerMode.getMode(tag.getInt("mode"));
        }
        System.out.println(this.mode);

        if (this.mode == HammerMode.ANGULAR) {
            Level level = player.getCommandSenderWorld();
            Direction facing = player.getDirection();
            Vec3 lookVec = player.getLookAngle();

            double angleY = Math.toDegrees(
                    Math.atan2(
                            lookVec.y,
                            Math.sqrt(
                                    lookVec.x * lookVec.x + lookVec.z * lookVec.z
                            )
                    )
            );

            boolean lookingUp = angleY < -45;
            boolean lookingDown = angleY > 45;

            if (!level.isClientSide && player != null) {
                BlockPos[][] relativePositions = new BlockPos[range][range];

                for (int i = -radius; i <= radius; i++) {
                    for (int j = -radius; j <= radius; j++) {
                        BlockPos blockPos;
                        if (lookingDown || lookingUp) {
                            blockPos = pos.offset(i, 0, j);
                        } else {
                            blockPos = switch (facing) {
                                case NORTH -> pos.offset(i, j, 0);
                                case SOUTH -> pos.offset(-i, j, 0);
                                case WEST -> pos.offset(0, j, i);
                                case EAST -> pos.offset(0, j, -i);
                                default -> pos.offset(i, 0, j);
                            };
                        }
                        relativePositions[i + radius][j + radius] = blockPos;
                    }
                }

                for (int i = 0; i < range; i++) {
                    for (int j = 0; j < range; j++) {
                        BlockPos currentPos = relativePositions[i][j];
                        BlockState blockState = level.getBlockState(currentPos);

                        if (!player.isCreative()) {
                            if (blockState.is(BlockTags.MINEABLE_WITH_PICKAXE)) {
                                List<TagKey<Block>> tags = blockState.getTags().toList();
                                if (isSpecificTag(tags)) {
                                    tagBlockBreak.add(new LevelBlock(blockState, tags.stream().filter(this::isSpecificTag).findFirst().get(), currentPos));
                                } else {
                                    tagBlockBreak.add(new LevelBlock(blockState, currentPos));
                                }
                            }

                            for (LevelBlock blockLevel : tagBlockBreak) {
                                if (LevelHarvest.getLevel(blockLevel.tagKey) <= LevelHarvest.getLevel(tier.getTag())) {
                                    level.destroyBlock(blockLevel.pos, true);
                                }
                            }

                            tagBlockBreak.clear();
                        } else {
                            level.destroyBlock(currentPos, false);
                        }
                    }
                }

                itemstack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
            }
        }
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (level.isClientSide || player == null) return InteractionResult.FAIL;
        if (context.getPlayer().isCrouching()) {
            CompoundTag tag = stack.getOrCreateTag();

            if (tag.contains("mode")) {
                int mode = tag.getInt("mode");

                if (HammerMode.getMode(mode) == HammerMode.ANGULAR) {
                    context.getPlayer().sendSystemMessage(Component.literal("Hammer mode has been changed to ").append(Component.literal("FACING").withStyle(ChatFormatting.GREEN)));
                    tag.putInt("mode", 1);
                    this.mode = HammerMode.FACING;
                } else {
                    context.getPlayer().sendSystemMessage(Component.literal("Hammer mode has been changed to ").append(Component.literal("ANGULAR").withStyle(ChatFormatting.GREEN)));
                    tag.putInt("mode", 2);
                    this.mode = HammerMode.ANGULAR;
                }
            } else {
                context.getPlayer().sendSystemMessage(Component.literal("Hammer mode has been changed to ").append(Component.literal("ANGULAR").withStyle(ChatFormatting.GREEN)));
                tag.putInt("mode", 2);
                this.mode = HammerMode.ANGULAR;
            }
            stack.setTag(tag);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    private boolean isSpecificTag(List<TagKey<Block>> tags) {
        return tags.contains(BlockTags.NEEDS_STONE_TOOL) ||
                tags.contains(BlockTags.NEEDS_IRON_TOOL) ||
                tags.contains(BlockTags.NEEDS_DIAMOND_TOOL) ||
                tags.contains(Tags.Blocks.NEEDS_NETHERITE_TOOL) ||
                tags.contains(Tags.Blocks.NEEDS_WOOD_TOOL);
    }

    private boolean isSpecificTag(TagKey<Block> tags) {
        return tags.equals(BlockTags.NEEDS_STONE_TOOL) ||
                tags.equals(BlockTags.NEEDS_IRON_TOOL) ||
                tags.equals(BlockTags.NEEDS_DIAMOND_TOOL) ||
                tags.equals(Tags.Blocks.NEEDS_NETHERITE_TOOL) ||
                tags.equals(Tags.Blocks.NEEDS_WOOD_TOOL);
    }

    private BlockPos getRightPos(BlockPos pos, Direction facing) {
        switch (facing) {
            case NORTH:
                return pos.east();
            case SOUTH:
                return pos.west();
            case WEST:
                return pos.north();
            case EAST:
                return pos.south();
            default:
                return pos;
        }
    }

    private BlockPos getLeftPos(BlockPos pos, Direction facing) {
        switch (facing) {
            case NORTH:
                return pos.west();
            case SOUTH:
                return pos.east();
            case WEST:
                return pos.south();
            case EAST:
                return pos.north();
            default:
                return pos;
        }
    }

    public int getDurability() {
        return durability;
    }

    public int getRange() {
        return range;
    }

    public void upgradeRange(int range) {
        if (!limitRange.isEmpty()) {
            int actualRange = this.range + range;
            if (actualRange < getLimitRange(getTier())) {
                this.range = actualRange;
            } else {
                this.range = getLimitRange(getTier());
            }
        }
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        if (!limitRadius.isEmpty()) {
            int actualRadius = this.radius + radius;
            if (actualRadius < getLimitRadius(getTier())) {
                this.radius = actualRadius;
            } else {
                this.radius = getLimitRadius(getTier());
            }
        }
    }

    private Map<Tier, Integer> limitRange = new HashMap<Tier, Integer>();
    private Map<Tier, Integer> limitRadius = new HashMap<Tier, Integer>();

    private void setLimitRadius() {
        limitRadius.put(Tiers.STONE, 3);
        limitRadius.put(Tiers.IRON, 6);
        limitRadius.put(Tiers.DIAMOND, 9);
    }

    private void setLimitRange() {
        limitRange.put(Tiers.STONE, 12);
        limitRange.put(Tiers.IRON, 24);
        limitRange.put(Tiers.DIAMOND, 36);
    }

    private int getLimitRadius(Tier tier) {
        return limitRadius.get(tier);
    }

    private int getLimitRange(Tier tier) {
        return limitRange.get(tier);
    }

    public class LevelBlock {
        private final BlockState state;
        private final TagKey<Block> tagKey;
        private final BlockPos pos;

        public LevelBlock(BlockState state, TagKey<Block> tagKey, BlockPos pos) {
            this.state = state;
            this.tagKey = tagKey;
            this.pos = pos;
        }

        public LevelBlock(BlockState state, BlockPos pos) {
            this(state, Tags.Blocks.NEEDS_WOOD_TOOL, pos);
        }

        public BlockState getState() {
            return state;
        }

        public TagKey<Block> getTagKey() {
            return tagKey == null ? Tags.Blocks.NEEDS_WOOD_TOOL : tagKey;
        }

        public BlockPos getPos() {
            return pos;
        }

        @Override
        public String toString() {
            return "LevelBlock(" +
                    "state=" + state +
                    ", tagKey=" + tagKey +
                    ", pos=" + pos +
                    ')';
        }
    }
}
