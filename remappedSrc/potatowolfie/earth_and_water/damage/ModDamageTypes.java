package potatowolfie.earth_and_water.damage;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import potatowolfie.earth_and_water.EarthWater;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> BATTLE_AXE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "battle_axe"));
    public static final ResourceKey<DamageType> WHIP = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "whip"));
    public static final ResourceKey<DamageType> EARTH_CHARGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "earth_charge"));
    public static final ResourceKey<DamageType> WATER_CHARGE = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "water_charge"));
    public static final ResourceKey<DamageType> SPIKED_SHIELD = ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, "spiked_shield"));
}