package xxrexraptorxx.particle_spawner.main;

import net.minecraftforge.eventbus.api.IEventBus;
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

    public ParticleSpawner() {

        ModBlocks.init();
        Config.init();

        IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
    }
}