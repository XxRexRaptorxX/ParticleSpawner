package xxrexraptorxx.particle_spawner.main;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xxrexraptorxx.particle_spawner.utils.Config;

/**
 * @author XxRexRaptorxX (RexRaptor)
 * @projectPage https://www.curseforge.com/minecraft/mc-mods/particle-spawner
 **/
@Mod(References.MODID)
public class ParticleSpawner {

    public static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation CREATIVE_TAB = new ResourceLocation(References.MODID, "tab");

    public ParticleSpawner() {
        Mod.EventBusSubscriber.Bus.MOD.bus().get().register(ParticleSpawner.class);
        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();

        Config.init();
        ModBlocks.init();
        ModItems.init();

    }


    //Creative Tab
    @SubscribeEvent
    public static void registerTabs(final CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(CREATIVE_TAB, (cf) -> cf.icon(() -> new ItemStack(ModBlocks.PARTICLE.get()))
                .title(Component.translatable("itemGroup." + References.MODID + "_tab")).displayItems((flagSet, output, ifSth) -> {
                    output.accept(ModBlocks.PARTICLE.get());
                    output.accept(ModItems.TOOL.get());
                })
        );
    }

}