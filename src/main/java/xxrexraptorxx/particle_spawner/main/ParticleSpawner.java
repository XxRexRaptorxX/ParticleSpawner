package xxrexraptorxx.particle_spawner.main;

import net.neoforged.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xxrexraptorxx.particle_spawner.registry.CreativeModeTabs;
import xxrexraptorxx.particle_spawner.registry.ModBlocks;
import xxrexraptorxx.particle_spawner.registry.ModItems;
import xxrexraptorxx.particle_spawner.utils.Config;

/**
 * @author XxRexRaptorxX (RexRaptor)
 * @projectPage https://www.curseforge.com/minecraft/mc-mods/particle-spawner
 **/
@Mod(References.MODID)
public class ParticleSpawner {

    public static final Logger LOGGER = LogManager.getLogger();


    public ParticleSpawner() {
        Config.init();
        ModItems.init();
        ModBlocks.init();
        CreativeModeTabs.init();
    }

}