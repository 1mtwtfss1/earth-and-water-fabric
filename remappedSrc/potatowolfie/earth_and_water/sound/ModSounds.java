package potatowolfie.earth_and_water.sound;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import potatowolfie.earth_and_water.EarthWater;

public class ModSounds {
    public static final SoundEvent BATTLE_AXE_DASH = registerSoundEvent("battle_axe_dash");
    public static final SoundEvent BORE_AMBIENT = registerSoundEvent("bore_ambient");
    public static final SoundEvent BORE_HURT = registerSoundEvent("bore_hurt");
    public static final SoundEvent BORE_DEATH = registerSoundEvent("bore_death");
    public static final SoundEvent BRINE_AMBIENT = registerSoundEvent("brine_ambient");
    public static final SoundEvent BRINE_UNDERWATER_AMBIENT = registerSoundEvent("brine_underwater_ambient");
    public static final SoundEvent BRINE_DEATH = registerSoundEvent("brine_death");
    public static final SoundEvent BRINE_UNDERWATER_DEATH = registerSoundEvent("brine_underwater_death");

    private static SoundEvent registerSoundEvent(String name) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT, Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, name),
                SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(EarthWater.MOD_ID, name)));
    }

    public static void registerSounds() {
        EarthWater.LOGGER.info("Registering Mod Sounds for " + EarthWater.MOD_ID);
    }
}