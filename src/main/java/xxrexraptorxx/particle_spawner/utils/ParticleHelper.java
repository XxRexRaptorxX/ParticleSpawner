package xxrexraptorxx.particle_spawner.utils;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import xxrexraptorxx.particle_spawner.main.ParticleSpawner;

public class ParticleHelper {

    public static ParticleOptions getParticleById(Integer id) {
        /**
         * TODO ArrayList<SimpleParticleType> list = new ArrayList<>();
         *
         * for (SimpleParticleType particle : ForgeRegistries.PARTICLE_TYPES) { list.add(particle); }
         *
         * return list.get(id); }
         **/

        switch (id) {
            case 1 :
                return ParticleTypes.FLAME;

            case 2 :
                return ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER_OMINOUS;

            case 3 :
                return ParticleTypes.ANGRY_VILLAGER;

            case 4 :
                return ParticleTypes.ASH;

            case 5 :
                return ParticleTypes.BUBBLE;

            case 6 :
                return ParticleTypes.BUBBLE_COLUMN_UP;

            case 7 :
                return ParticleTypes.BUBBLE_POP;

            case 8 :
                return ParticleTypes.CAMPFIRE_COSY_SMOKE;

            case 9 :
                return ParticleTypes.CAMPFIRE_SIGNAL_SMOKE;

            case 10 :
                return ParticleTypes.CLOUD;

            case 11 :
                return ParticleTypes.COMPOSTER;

            case 12 :
                return ParticleTypes.CRIMSON_SPORE;

            case 13 :
                return ParticleTypes.CRIT;

            case 14 :
                return ParticleTypes.CURRENT_DOWN;

            case 15 :
                return ParticleTypes.DAMAGE_INDICATOR;

            case 16 :
                return ParticleTypes.DOLPHIN;

            case 17 :
                return ParticleTypes.FIREFLY;

            case 18 :
                return ParticleTypes.FISHING;

            case 19 :
                return ParticleTypes.SQUID_INK;

            case 20 :
                return ParticleTypes.ELECTRIC_SPARK;

            case 21 :
                return ParticleTypes.ENCHANTED_HIT;

            case 22 :
                return ParticleTypes.END_ROD;

            case 23 :
                return ParticleTypes.EXPLOSION;

            case 24 :
                return ParticleTypes.EXPLOSION_EMITTER;

            case 25 :
                return ParticleTypes.FALLING_HONEY;

            case 26 :
                return ParticleTypes.FALLING_NECTAR;

            case 27 :
                return ParticleTypes.FALLING_LAVA;

            case 28 :
                return ParticleTypes.FALLING_WATER;

            case 29 :
                return ParticleTypes.FALLING_OBSIDIAN_TEAR;

            case 30 :
                return ParticleTypes.FALLING_SPORE_BLOSSOM;

            case 31 :
                return ParticleTypes.FIREWORK;

            case 32 :
                return ParticleTypes.PALE_OAK_LEAVES;

            case 33 :
                return ParticleTypes.GLOW;

            case 34 :
                return ParticleTypes.GLOW_SQUID_INK;

            case 35 :
                return ParticleTypes.HAPPY_VILLAGER;

            case 36 :
                return ParticleTypes.HEART;

            case 37 :
                return ParticleTypes.NOTE;

            case 38 :
                return ParticleTypes.CHERRY_LEAVES;

            case 39 :
                return ParticleTypes.LAVA;

            case 40 :
                return ParticleTypes.PORTAL;

            case 41 :
                return ParticleTypes.POOF;

            case 42 :
                return ParticleTypes.RAIN;

            case 43 :
                return ParticleTypes.LARGE_SMOKE;

            case 44 :
                return ParticleTypes.SMALL_FLAME;

            case 45 :
                return ParticleTypes.SMOKE;

            case 46 :
                return ParticleTypes.SNEEZE;

            case 47 :
                return ParticleTypes.SNOWFLAKE;

            case 48 :
                return ParticleTypes.SOUL;

            case 49 :
                return ParticleTypes.SOUL_FIRE_FLAME;

            case 50 :
                return ParticleTypes.SWEEP_ATTACK;

            case 52 :
                return ParticleTypes.UNDERWATER;

            case 53 :
                return ParticleTypes.WARPED_SPORE;

            case 54 :
                return ParticleTypes.WAX_OFF;

            case 55 :
                return ParticleTypes.WAX_ON;

            case 56 :
                return ParticleTypes.WHITE_ASH;

            case 57 :
                return ParticleTypes.WITCH;

            case 58 :
                return ParticleTypes.ENCHANT;

            default :
                ParticleSpawner.LOGGER.error("Unknown Particle");
                return ParticleTypes.CLOUD;
        }
    }

}
