package potatowolfie.earth_and_water.advancement;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class MobLockHandler {

    public static void grantDeactivateSpawnerAdvancement(ServerPlayer player) {
        MinecraftServer server = player.level().getServer();
        if (server == null) return;

        Identifier advId = Identifier.fromNamespaceAndPath("earth-and-water", "mob_lock");
        AdvancementHolder advancement = server.getAdvancements().get(advId);

        if (advancement != null) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            if (!progress.isDone()) {
                player.getAdvancements().award(advancement, "mob_lock");
            }
        }
    }
}