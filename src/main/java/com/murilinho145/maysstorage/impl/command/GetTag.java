package com.murilinho145.maysstorage.impl.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class GetTag {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("gettag")
                .requires(source -> source.hasPermission(2))
                .executes(context -> execute(context.getSource())));
    }

    private static int execute(CommandSourceStack source) {
        if (source.getEntity() instanceof ServerPlayer player) {
            ItemStack item = player.getMainHandItem();
            String itemName = item.getDisplayName().getString();
            player.sendSystemMessage(
                    Component
                            .literal(itemName + ": " + item.getTag().getAsString()).withStyle(ChatFormatting.GREEN));
        }
        return 1;
    }
}
