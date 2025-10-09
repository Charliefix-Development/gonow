package org.romawertq.gonow;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;

public class ExampleModClient implements ClientModInitializer {
	public static final String MOD_ID = "gonow";

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(new ClientCommandRegistrationCallback() {
			@Override
			public void register(CommandDispatcher<FabricClientCommandSource> commandDispatcher, CommandRegistryAccess commandRegistryAccess) {
				commandDispatcher.register(ClientCommandManager.literal("gonow").executes(ExitCommand::execute));
			}
		});
	}
}