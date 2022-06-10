package dev.schmarrn.schmagie.world.dimension;

import dev.schmarrn.schmagie.Schmagie;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class NetworkDimension {
	public static final RegistryKey<World> NETWORK_DIMENSION = RegistryKey.of(
			Registry.WORLD_KEY,
			new Identifier(Schmagie.MOD_ID, "network")
	);

	public static final RegistryKey<DimensionType> NETWORK_DIMENSION_TYPE = RegistryKey.of(
			Registry.DIMENSION_TYPE_KEY, NETWORK_DIMENSION.getValue()
	);

	public static void init() {

	}
}
