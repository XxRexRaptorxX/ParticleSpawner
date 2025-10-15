package xxrexraptorxx.particle_spawner.main;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xxrexraptorxx.magmacore.main.ModRegistry;
import xxrexraptorxx.particle_spawner.registry.CreativeModeTabs;
import xxrexraptorxx.particle_spawner.registry.ModBlocks;
import xxrexraptorxx.particle_spawner.registry.ModComponents;
import xxrexraptorxx.particle_spawner.registry.ModItems;
import xxrexraptorxx.particle_spawner.utils.Config;

/**
 * @author XxRexRaptorxX (RexRaptor)
 * @projectPage <a href="https://www.curseforge.com/minecraft/mc-mods/particle-spawner">...</a>
 **/
@Mod(References.MODID)
public class ParticleSpawner {

    public static final Logger LOGGER = LogManager.getLogger();


    public ParticleSpawner(IEventBus bus, ModContainer container) {
        ModItems.init(bus);
        ModBlocks.init(bus);
        ModComponents.init(bus);
        CreativeModeTabs.init(bus);

        Config.init(container);
        ModRegistry.register(References.MODID, References.NAME, References.URL);
    }

}
