package xxrexraptorxx.particle_spawner.registry;


import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import xxrexraptorxx.particle_spawner.items.AdjustmentTool;
import xxrexraptorxx.particle_spawner.main.References;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(References.MODID);

    public static void init(IEventBus bus) {
        ITEMS.register(bus);
    }


    public static final DeferredItem<AdjustmentTool> TOOL = ITEMS.register("adjustment_tool", AdjustmentTool::new);


    public static ResourceKey<Item> itemId(String name) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(References.MODID, name));
    }
}
