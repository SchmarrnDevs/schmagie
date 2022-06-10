package dev.schmarrn.schmagie.world.biom;

import dev.schmarrn.schmagie.Schmagie;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.OverworldBiomeCreator;

public class NetworkBiome {
	private static final RegistryKey<Biome> NETWORK_BIOME = RegistryKey.of(
			Registry.BIOME_KEY,
			new Identifier(Schmagie.MOD_ID, "network")
	);

	public static void init() {
		Registry.register(BuiltinRegistries.BIOME, NETWORK_BIOME.getValue(), OverworldBiomeCreator.createTheVoid());
	}
}
