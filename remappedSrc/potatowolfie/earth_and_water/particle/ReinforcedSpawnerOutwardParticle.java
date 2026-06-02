package potatowolfie.earth_and_water.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class ReinforcedSpawnerOutwardParticle extends SimpleAnimatedParticle {

    ReinforcedSpawnerOutwardParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
        super(world, x, y, z, spriteProvider, 0.0125F);
        this.xd = velocityX;
        this.yd = 0.0;
        this.zd = velocityZ;
        this.gravity = 0.0F;
        this.quadSize *= 0.75F;
        this.lifetime = 19;
        this.setFadeColor(15916745);
        this.setSpriteFromAge(spriteProvider);
    }

    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().move(dx, dy, dz));
        this.setLocationFromBoundingbox();
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;
        private int particleIndex = 0;
        private int totalParticles = 0;
        private static final double SPEED = 0.17;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, RandomSource random) {
            if (particleIndex == 0) {
                totalParticles = 12 + random.nextInt(6);
            }

            double angle = (2 * Math.PI * particleIndex) / totalParticles;
            double velX = Math.cos(angle) * SPEED;
            double velZ = Math.sin(angle) * SPEED;

            particleIndex = (particleIndex + 1) % totalParticles;

            return new ReinforcedSpawnerOutwardParticle(clientWorld, d, e, f, velX, 0, velZ, this.spriteProvider);
        }
    }
}