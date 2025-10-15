package xxrexraptorxx.particle_spawner.world;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import xxrexraptorxx.magmacore.utils.FormattingHelper;
import xxrexraptorxx.particle_spawner.blocks.ParticleBlock;
import xxrexraptorxx.particle_spawner.items.AdjustmentTool;
import xxrexraptorxx.particle_spawner.main.ParticleSpawner;
import xxrexraptorxx.particle_spawner.main.References;
import xxrexraptorxx.particle_spawner.registry.ModBlocks;
import xxrexraptorxx.particle_spawner.registry.ModComponents;
import xxrexraptorxx.particle_spawner.registry.ModItems;

import java.util.List;

@EventBusSubscriber(modid = References.MODID)
public class Events {

    /** Adjustment Tool **/
    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        BlockState state = world.getBlockState(pos);
        FluidState fluidstate = player.level().getFluidState(pos);

        // Test if adjustment tool is in hand and particle spawner is clicked
        if (stack.getItem() == ModItems.TOOL.get() && state.getBlock() == ModBlocks.PARTICLE.get()) {

            world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 1.0f, 1.0f);

            // Sets a mode if no tag is present
            if (!stack.has(ModComponents.MODE)) {
                AdjustmentTool.cycleMode(stack);
            }

            // Power the block on first use
            if (!state.getValue(ParticleBlock.POWERED)) {
                world.setBlock(pos,
                        ModBlocks.PARTICLE.get().defaultBlockState().setValue(ParticleBlock.POWERED, true)
                                .setValue(ParticleBlock.PARTICLE_STRENGTH, state.getValue(ParticleBlock.PARTICLE_STRENGTH))
                                .setValue(ParticleBlock.WATERLOGGED, fluidstate.getType() == Fluids.WATER),
                        11);
            }

            // Modes
            if (state.getValue(ParticleBlock.POWERED)) {
                String mode = stack.get(ModComponents.MODE);

                if (player.isShiftKeyDown()) { // subtract mode
                    switch (mode) {
                        case "type" :
                            ParticleBlock.refreshBlockStates(world, pos, state, -1, 0, 0);
                            break;
                        case "strength" :
                            ParticleBlock.refreshBlockStates(world, pos, state, 0, -1, 0);
                            break;
                        case "range" :
                            ParticleBlock.refreshBlockStates(world, pos, state, 0, 0, -1);
                            break;
                    }
                } else { // add mode
                    switch (mode) {
                        case "break" :
                            ItemEntity drop = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, new ItemStack(ModBlocks.PARTICLE.get()));
                            world.destroyBlock(pos, false);
                            world.addFreshEntity(drop);
                            break;
                        case "type" :
                            ParticleBlock.refreshBlockStates(world, pos, state, +1, 0, 0);
                            break;
                        case "strength" :
                            ParticleBlock.refreshBlockStates(world, pos, state, 0, +1, 0);
                            break;
                        case "range" :
                            ParticleBlock.refreshBlockStates(world, pos, state, 0, 0, +1);
                            break;
                        default :
                            ParticleSpawner.LOGGER.error("Unknown Tool Mode: " + mode);
                    }
                }

                if (world.isClientSide()) {
                    player.displayClientMessage(
                            Component.literal(
                                    ChatFormatting.YELLOW + mode.substring(0, 1).toUpperCase() + mode.substring(1) + ": " + state.getValue(ParticleBlock.getStateByName(mode))),
                            true);
                }
            }
        }
    }


    @SubscribeEvent
    public static void addingToolTips(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        List<Component> list = event.getToolTip();

        if (BuiltInRegistries.BLOCK.getKey(ModBlocks.PARTICLE.get()).getPath().equals(BuiltInRegistries.ITEM.getKey(item).getPath())) {
            list.add(FormattingHelper.setMessageComponent(References.MODID, "spawner.desc", ChatFormatting.GRAY));
        }
    }


}
