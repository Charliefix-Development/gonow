package org.romawertq.gonow;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class ExitCommand {

    public static int execute(CommandContext<FabricClientCommandSource> context) {
        MinecraftClient client = MinecraftClient.getInstance();
        FabricClientCommandSource source = context.getSource();

        // 1. Отправляем сообщение
        source.sendFeedback(Text.literal("Завершаем работу..."));

        // 2. Попробуем остановить запись ReplayMod
        if (isReplayModLoaded()) {
            stopReplayModRecording(source);
        } else {
            source.sendFeedback(Text.literal("ReplayMod не обнаружен — пропускаем остановку записи."));
        }

        // 3. Запланировать корректное завершение игры
        client.scheduleStop();

        return Command.SINGLE_SUCCESS;
    }

    private static boolean isReplayModLoaded() {
        return net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("replaymod");
    }

    private static void stopReplayModRecording(FabricClientCommandSource source) {
        try {
            // Получаем instance ReplayMod
            Class<?> replayModClass = Class.forName("com.replaymod.replay.ReplayMod");
            Field instanceField = replayModClass.getDeclaredField("instance");
            Object replayModInstance = instanceField.get(null);

            if (replayModInstance == null) return;

            // Получаем ReplayHandler
            Method getHandler = replayModInstance.getClass().getMethod("getReplayHandler");
            Object replayHandler = getHandler.invoke(replayModInstance);

            if (replayHandler == null) {
                source.sendFeedback(Text.literal("ReplayMod: запись не активна."));
                return;
            }

            // Вызываем stopRecording()
            Method stopMethod = replayHandler.getClass().getMethod("stopRecording");
            stopMethod.invoke(replayHandler);

            source.sendFeedback(Text.literal("✅ Запись ReplayMod остановлена."));

        } catch (ClassNotFoundException e) {
            source.sendFeedback(Text.literal("❌ ReplayMod: класс не найден."));
        } catch (NoSuchFieldException | NoSuchMethodException e) {
            source.sendFeedback(Text.literal("❌ ReplayMod: API изменился."));
        } catch (IllegalAccessException e) {
            source.sendFeedback(Text.literal("❌ ReplayMod: доступ запрещён."));
        } catch (InvocationTargetException e) {
            source.sendFeedback(Text.literal("❌ ReplayMod: ошибка при остановке: " + e.getCause().getMessage()));
        } catch (Exception e) {
            source.sendFeedback(Text.literal("❌ Неизвестная ошибка с ReplayMod."));
        }
    }
}