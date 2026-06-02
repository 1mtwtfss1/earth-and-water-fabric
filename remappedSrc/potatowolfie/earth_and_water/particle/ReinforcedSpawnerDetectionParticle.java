package potatowolfie.earth_and_water.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class ReinforcedSpawnerDetectionParticle extends SingleQuadParticle {
    private final SpriteSet spriteProvider;
    private static final int field_47460 = 8;

    protected ReinforcedSpawnerDetectionParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scale, SpriteSet spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0, spriteProvider.first());
        this.spriteProvider = spriteProvider;
        this.friction = 0.96F;
        this.gravity = 0.0F;
        this.speedUpWhenYMotionIsBlocked = velocityY > 0;
        this.xd *= 0.0;
        this.yd = 0.119;
        this.zd *= 0.0;
        this.xd += velocityX;
        this.yd += velocityY;
        this.zd += velocityZ;
        this.quadSize *= 0.75F * scale;
        this.lifetime = 19;
        this.setSpriteFromAge(spriteProvider);
        this.hasPhysics = true;
    }

    public SingleQuadParticle.Layer getLayer() {
        return Layer.OPAQUE;
    }

    public int getLightColor(float tint) {
        return 240;
    }

    public SingleQuadParticle.FacingCameraMode getFacingCameraMode() {
        return FacingCameraMode.LOOKAT_Y;
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.spriteProvider);
    }

    public float getQuadSize(float tickProgress) {
        return this.quadSize * Mth.clamp(((float)this.age + tickProgress) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public Factory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, RandomSource random) {
            return new ReinforcedSpawnerDetectionParticle(clientWorld, d, e, f, 0.0, 0.0, 0.0, 1.5F, this.spriteProvider);
        }
    }
}