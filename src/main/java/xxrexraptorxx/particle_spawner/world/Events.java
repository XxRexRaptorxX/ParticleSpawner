package xxrexraptorxx.particle_spawner.world;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import xxrexraptorxx.particle_spawner.blocks.ParticleBlock;
import xxrexraptorxx.particle_spawner.main.ModBlocks;
import xxrexraptorxx.particle_spawner.main.ModItems;
import xxrexraptorxx.particle_spawner.main.ParticleSpawner;
import xxrexraptorxx.particle_spawner.main.References;
import xxrexraptorxx.particle_spawner.utils.Config;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Events {


    /** Update-Checker **/
    private static boolean hasShownUp = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Config.UPDATE_CHECKER.get()) {
            if (!hasShownUp && Minecraft.getInstance().screen == null) {
                if (VersionChecker.getResult(ModList.get().getModContainerById(References.MODID).get().getModInfo()).status() == VersionChecker.Status.OUTDATED ||
                        VersionChecker.getResult(ModList.get().getModContainerById(References.MODID).get().getModInfo()).status() == VersionChecker.Status.BETA_OUTDATED ) {

                    Minecraft.getInstance().player.sendSystemMessage(Component.literal(ChatFormatting.BLUE + "A newer version of " + ChatFormatting.YELLOW + References.NAME + ChatFormatting.BLUE + " is available!"));
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal(ChatFormatting.GRAY + References.URL));

                    hasShownUp = true;

                } else if (VersionChecker.getResult(ModList.get().getModContainerById(References.MODID).get().getModInfo()).status() == VersionChecker.Status.FAILED) {
                    ParticleSpawner.LOGGER.error(References.NAME + "'s version checker failed!");
                    hasShownUp = true;

                }
            }
        }
    }


    /** Adjustment Tool **/
    @SubscribeEvent
    public static void onInteract(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        Level world = event.getWorld();
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();
        BlockState state = world.getBlockState(pos);
        FluidState fluidstate = player.getLevel().getFluidState(pos);

        if (stack.getItem() == ModItems.TOOL.get() && state.getBlock() == ModBlocks.PARTICLE.get()) {

            world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);

            //Power the block on first use
            if (state.getValue(ParticleBlock.POWERED).booleanValue() == false) {
                world.setBlock(pos, ModBlocks.PARTICLE.get().defaultBlockState().setValue(ParticleBlock.POWERED, true).setValue(ParticleBlock.PARTICLE_STRENGTH, state.getValue(ParticleBlock.PARTICLE_STRENGTH)).setValue(ParticleBlock.WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER)), 11);
            }

            //Modes
            if (state.getValue(ParticleBlock.POWERED).booleanValue() == true) {

                if (player.isShiftKeyDown()) {      //subtract mode
                    if (stack.getTag().getString("mode").equals("type") || !stack.hasTag()) {
                        ParticleBlock.refreshBlockStates(world, pos, state, -1, 0, 0);

                    } else if (stack.getTag().getString("mode").equals("strength")) {
                        ParticleBlock.refreshBlockStates(world, pos, state, 0, -1, 0);

                    } else if (stack.getTag().getString("mode").equals("range")) {
                        ParticleBlock.refreshBlockStates(world, pos, state, 0, 0, -1);
                    }

                } else {    //add mode
                    if (stack.getTag().getString("mode").equals("break")) {
                        ItemEntity drop = new ItemEntity(world, (double) pos.getX() + 0.5D, (double) pos.getY() + 1.5D, (double) pos.getZ() + 0.5D, new ItemStack(ModBlocks.PARTICLE.get()));
                        world.destroyBlock(pos, false);
                        world.addFreshEntity(drop);

                    } else if (stack.getTag().getString("mode").equals("type") || !stack.hasTag()) {
                        ParticleBlock.refreshBlockStates(world, pos, state, +1, 0, 0);

                    } else if (stack.getTag().getString("mode").equals("strength")) {
                        ParticleBlock.refreshBlockStates(world, pos, state, 0, +1, 0);

                    } else if (stack.getTag().getString("mode").equals("range")) {
                        ParticleBlock.refreshBlockStates(world, pos, state, 0, 0, +1);

                    } else {
                        ParticleSpawner.LOGGER.error("Unknown Tool Mode: " + stack.getTag().getString("mode"));
                    }
                }

                if (world.isClientSide) {
                    player.displayClientMessage(Component.literal(ChatFormatting.YELLOW + stack.getTag().getString("mode").substring(0, 1).toUpperCase() + stack.getTag().getString("mode").substring(1)
                            + ": " + state.getValue(ParticleBlock.getStateByName(stack.getTag().getString("mode")))), true);
                }
            }
        }
    }


}
