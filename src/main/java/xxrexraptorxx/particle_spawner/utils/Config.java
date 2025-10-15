package xxrexraptorxx.particle_spawner.utils;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_BLOCKS = "blocks";

    public static ModConfigSpec CLIENT_CONFIG;
    public static ModConfigSpec COMMON_CONFIG;

    public static ModConfigSpec.BooleanValue UPDATE_CHECKER;
    public static ModConfigSpec.BooleanValue PATREON_REWARDS;

    public static ModConfigSpec.BooleanValue ALWAYS_RENDER_PARTICLES;
    public static Integer PARTICLE_SPAWNER_STRENGTH_MAX_VALUE = 10; // TODO wip
    public static Integer PARTICLE_SPAWNER_RANGE_MAX_VALUE = 10; // TODO wip
    public static Integer PARTICLE_SPAWNER_TYPE_MAX_VALUE = 58; // TODO wip


    public static void init(ModContainer container) {
        initCommon();
        initClient();

        container.registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
        container.registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
    }


    public static void initClient() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("General").push(CATEGORY_GENERAL);
        UPDATE_CHECKER = builder.comment("Activate the Update-Checker").define("update-checker", true);
        builder.pop();

        CLIENT_CONFIG = builder.build();
    }


    public static void initCommon() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("General").push(CATEGORY_GENERAL);
        PATREON_REWARDS = builder.comment("Enables ingame rewards on first spawn for Patreons").define("patreon_rewards", true);
        builder.pop();

        builder.comment("Blocks").push(CATEGORY_BLOCKS);
        ALWAYS_RENDER_PARTICLES = builder.comment("true = ignores the graphic settings and spawn always all particles").define("always_render_particles", true);
        // PARTICLE_SPAWNER_RANGE_MAX_VALUE = builder.comment("The max value of particle range from the Particle Spawner").defineInRange("particle_spawner_range_max_value", 10, 1,
        // 100); TODO -> broken
        // PARTICLE_SPAWNER_STRENGTH_MAX_VALUE = builder.comment("The max value of particle strength from the Particle
        // Spawner").defineInRange("particle_spawner_strength_max_value", 10, 1, 100);
        // PARTICLE_SPAWNER_TYPE_MAX_VALUE = builder.comment("The The max value of particle type from the Particle Spawner [Don't change this value unless you have added new
        // particles!]").defineInRange("particle_spawner_type_max_value", 58, 1, 100);
        builder.pop();

        COMMON_CONFIG = builder.build();
    }


}
