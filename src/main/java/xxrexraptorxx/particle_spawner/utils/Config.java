package xxrexraptorxx.particle_spawner.utils;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class Config {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_WORLD = "world";

    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue UPDATE_CHECKER;
    public static ForgeConfigSpec.BooleanValue WORLD_GENERATION;
    public static ForgeConfigSpec.IntValue SANDSTONE_VEIN_RARITY;
    public static ForgeConfigSpec.IntValue CLAY_VEIN_RARITY;
    public static ForgeConfigSpec.IntValue DEEPSLATE_VEIN_RARITY;
    public static ForgeConfigSpec.IntValue SAND_VEIN_RARITY;
    public static ForgeConfigSpec.IntValue COBBLESTONE_VEIN_RARITY;
    public static ForgeConfigSpec.IntValue STONE_VEIN_RARITY;
    public static ForgeConfigSpec.IntValue SILVERFISH_BLOCK_VEIN_RARITY;
    public static ForgeConfigSpec.BooleanValue GENERATE_QUICKSAND;
    public static ForgeConfigSpec.BooleanValue GENERATE_MUD;


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

        builder.comment("World").push(CATEGORY_WORLD);
        WORLD_GENERATION = builder.comment("Activate the world generation features of Enhanced Nature ").define("world_generation", true);
        CLAY_VEIN_RARITY = builder.comment("The rarity of Clay veins (veins per chunk), 0 = no veins").defineInRange("clay_vein_rarity", 10, 0, 100);
        SAND_VEIN_RARITY = builder.comment("The rarity of Sand veins (veins per chunk), 0 = no veins").defineInRange("sand_vein_rarity", 10, 0, 100);
        SANDSTONE_VEIN_RARITY = builder.comment("The rarity of Sandstone veins (veins per chunk), 0 = no veins").defineInRange("sandstone_vein_rarity", 10, 0, 100);
        COBBLESTONE_VEIN_RARITY = builder.comment("The rarity of Cobblestone and Cobbled Deepslate veins (veins per chunk), 0 = no veins").defineInRange("cobblestone_vein_rarity", 10, 0, 100);
        STONE_VEIN_RARITY = builder.comment("The rarity of stone veins in deepslate (veins per chunk), 0 = no veins").defineInRange("stone_vein_rarity", 10, 0, 100);
        DEEPSLATE_VEIN_RARITY = builder.comment("The rarity of Clay veins (veins per chunk), 0 = no veins").defineInRange("deepslate_vein_rarity", 1, 0, 100);
        SILVERFISH_BLOCK_VEIN_RARITY = builder.comment("The rarity of Silverfish block veins (veins per chunk), 0 = no veins").defineInRange("silverfish_vein_rarity", 5, 0, 100);
        GENERATE_QUICKSAND = builder.comment("Activate/Deactivate Quicksand generation in your world").define("quicksand_generation", true);
        GENERATE_MUD = builder.comment("Activate/Deactivate Mud generation in your world").define("mud_generation", true);
        COMMON_CONFIG = builder.build();
    }


}