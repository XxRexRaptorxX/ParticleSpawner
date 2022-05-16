package xxrexraptorxx.particle_spawner.utils;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import xxrexraptorxx.particle_spawner.main.ParticleSpawner;

public class ParticleHelper {

    public static SimpleParticleType getParticleById(Integer id) {
   /**      TODO
    ArrayList<SimpleParticleType> list = new ArrayList<>();

        for (SimpleParticleType particle : ForgeRegistries.PARTICLE_TYPES) {
            list.add(particle);
        }

        return list.get(id);
    }   **/

        switch (id) {
            case 1:
                return ParticleTypes.FLAME;

            case 2:
                return ParticleTypes.AMBIENT_ENTITY_EFFECT;

            case 3:
                return ParticleTypes.ANGRY_VILLAGER;

            case 4:
                return ParticleTypes.ASH;

            case 5:
                return ParticleTypes.BUBBLE;

            case 6:
                return ParticleTypes.BUBBLE_COLUMN_UP;

            case 7:
                return ParticleTypes.BUBBLE_POP;

            case 8:
                return ParticleTypes.CAMPFIRE_COSY_SMOKE;

            case 9:
                return ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;

            case 10:
                return ParticleTypes.CLOUD;

            case 11:
                return ParticleTypes.COMPOSTER;

            case 12:
                return ParticleTypes.CRIMSON_SPORE;

            case 13:
                return ParticleTypes.CRIT;

            case 14:
                return ParticleTypes.CURRENT_DOWN;

            case 15:
                return ParticleTypes.DAMAGE_INDICATOR;

            case 16:
                return ParticleTypes.DOLPHIN;

            case 17:
                return ParticleTypes.DRAGON_BREATH;

            case 18:
                return ParticleTypes.EFFECT;

            case 19:
                return ParticleTypes.ELDER_GUARDIAN;

            case 20:
                return ParticleTypes.ELECTRIC_SPARK;

            case 21:
                return ParticleTypes.ENCHANTED_HIT;

            case 22:
                return ParticleTypes.END_ROD;

            case 23:
                return ParticleTypes.EXPLOSION;

            case 24:
                return ParticleTypes.EXPLOSION_EMITTER;

            case 25:
                return ParticleTypes.FALLING_HONEY;

            case 26:
                return ParticleTypes.FALLING_NECTAR;

            case 27:
                return ParticleTypes.FALLING_LAVA;

            case 28:
                return ParticleTypes.FALLING_WATER;

            case 29:
                return ParticleTypes.FALLING_OBSIDIAN_TEAR;

            case 30:
                return ParticleTypes.FALLING_SPORE_BLOSSOM;

            default:
                ParticleSpawner.LOGGER.error("Unknown Particle");
                return ParticleTypes.CLOUD;
        }
    }

}
