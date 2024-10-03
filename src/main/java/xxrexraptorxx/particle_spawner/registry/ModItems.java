package xxrexraptorxx.particle_spawner.registry;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import xxrexraptorxx.particle_spawner.items.AdjustmentTool;
import xxrexraptorxx.particle_spawner.main.References;

public class ModItems {

    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(References.MODID);

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }


    public static final DeferredItem<AdjustmentTool> TOOL = ITEMS.register("adjustment_tool", AdjustmentTool::new);

}