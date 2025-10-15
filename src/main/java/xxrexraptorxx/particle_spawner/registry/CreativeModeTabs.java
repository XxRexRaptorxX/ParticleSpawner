package xxrexraptorxx.particle_spawner.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xxrexraptorxx.particle_spawner.main.References;

public class CreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, References.MODID);

    public static void init(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }


    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register(References.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + References.MODID + "_tab")).icon(ModBlocks.PARTICLE::toStack).displayItems((params, output) -> {
                output.accept(ModBlocks.PARTICLE.get());
                output.accept(ModItems.TOOL.get());
            }).build());
}
