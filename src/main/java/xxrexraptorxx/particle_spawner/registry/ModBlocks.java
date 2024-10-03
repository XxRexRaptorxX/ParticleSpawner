package xxrexraptorxx.particle_spawner.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import xxrexraptorxx.particle_spawner.blocks.ParticleBlock;
import xxrexraptorxx.particle_spawner.main.References;

public class ModBlocks {


    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(References.MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(References.MODID);

    public static void init(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
    }


    public static final DeferredBlock<ParticleBlock> PARTICLE = BLOCKS.register("particle_spawner", ParticleBlock::new);
    public static final DeferredItem<Item> PARTICLE_BLOCKITEM = ITEMS.register("particle_spawner", () -> new BlockItem(PARTICLE.get(), new Item.Properties()));

}
