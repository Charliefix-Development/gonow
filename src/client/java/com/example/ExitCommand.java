package com.example;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ExitCommand {
    public static int execute(CommandContext<FabricClientCommandSource> context) {
        // Отправляем сообщение перед выходом
        context.getSource().sendFeedback(Text.literal("Закрываем Minecraft..."));

        // Получаем экземпляр клиента
        MinecraftClient client = MinecraftClient.getInstance();

        // Закрываем игру корректно
        client.scheduleStop();

        return Command.SINGLE_SUCCESS;
    }
}