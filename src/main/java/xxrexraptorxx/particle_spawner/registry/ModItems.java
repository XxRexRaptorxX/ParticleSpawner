package xxrexraptorxx.particle_spawner.registry;


import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import xxrexraptorxx.particle_spawner.items.AdjustmentTool;
import xxrexraptorxx.particle_spawner.main.References;

public class ModItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(References.MODID);


    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


    public static final DeferredItem<AdjustmentTool> TOOL = ITEMS.register("adjustment_tool", AdjustmentTool::new);

}