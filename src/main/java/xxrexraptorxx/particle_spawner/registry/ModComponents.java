package xxrexraptorxx.particle_spawner.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import xxrexraptorxx.particle_spawner.main.References;

import java.util.function.UnaryOperator;

public class ModComponents {

    private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.createDataComponents(DeferredRegister.DataComponents, References.MODID);

    public static void init(IEventBus bus) {
        DATA_COMPONENT_TYPES.register(bus);
    }


    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> MODE = register("mode", builder -> builder.persistent(Codec.STRING));


    public  static <T>DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return DATA_COMPONENT_TYPES.register(name, () -> builderOperator.apply(DataComponentType.builder()).build());
    }

}
