package xxrexraptorxx.particle_spawner.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_BLOCKS = "blocks";

    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue UPDATE_CHECKER;
    public static ForgeConfigSpec.BooleanValue PATREON_REWARDS;

    public static ForgeConfigSpec.BooleanValue ALWAYS_RENDER_PARTICLES;
    public static Integer PARTICLE_SPAWNER_STRENGTH_MAX_VALUE = 10;         //TODO wip
    public static Integer PARTICLE_SPAWNER_RANGE_MAX_VALUE = 10;            //TODO wip
    public static Integer PARTICLE_SPAWNER_TYPE_MAX_VALUE = 58;         //TODO wip




    public static void init() {
        initClient();
        initCommon();

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_CONFIG);
    }


    public static void initClient() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("General").push(CATEGORY_GENERAL);
        UPDATE_CHECKER = builder.comment("Activate the Update-Checker").define("update-checker", true);
        builder.pop();

        CLIENT_CONFIG = builder.build();
    }


    public static void initCommon() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("General").push(CATEGORY_GENERAL);
        PATREON_REWARDS = builder.comment("Enables ingame rewards on first spawn for Patreons").define("patreon_rewards", true);
        builder.pop();

        builder.comment("Blocks").push(CATEGORY_BLOCKS);
        ALWAYS_RENDER_PARTICLES = builder.comment("true = ignores the graphic settings and spawn always all particles").define("always_render_particles", true);
        //PARTICLE_SPAWNER_RANGE_MAX_VALUE = builder.comment("The max value of particle range from the Particle Spawner").defineInRange("particle_spawner_range_max_value", 10, 1, 100); TODO -> broken
        //PARTICLE_SPAWNER_STRENGTH_MAX_VALUE = builder.comment("The max value of particle strength from the Particle Spawner").defineInRange("particle_spawner_strength_max_value", 10, 1, 100);
        //PARTICLE_SPAWNER_TYPE_MAX_VALUE = builder.comment("The The max value of particle type from the Particle Spawner [Don't change this value unless you have added new particles!]").defineInRange("particle_spawner_type_max_value", 58, 1, 100);
        builder.pop();

        COMMON_CONFIG = builder.build();
    }


}