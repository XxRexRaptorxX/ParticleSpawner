package xxrexraptorxx.particle_spawner.world;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

import java.util.UUID;

@Mod.EventBusSubscriber(modid = References.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Events {


    /** Update-Checker **/
    private static boolean hasShownUp = false;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Config.UPDATE_CHECKER.get()) {
            if (!hasShownUp && Minecraft.getInstance().screen == null) {
                if (VersionChecker.getResult(ModList.get().getModContainerById(References.MODID).get().getModInfo()).status() == VersionChecker.Status.OUTDATED ||
                        VersionChecker.getResult(ModList.get().getModContainerById(References.MODID).get().getModInfo()).status() == VersionChecker.Status.BETA_OUTDATED) {

                    TextComponent clickevent = new TextComponent(ChatFormatting.RED + "Click here to update!");
                    clickevent.withStyle(clickevent.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, References.URL)));

                    Minecraft.getInstance().player.sendMessage(new TextComponent(ChatFormatting.BLUE + "A newer version of " + ChatFormatting.YELLOW + References.NAME + ChatFormatting.BLUE + " is available!"), UUID.randomUUID());
                    Minecraft.getInstance().player.sendMessage(clickevent, UUID.randomUUID());

                    hasShownUp = true;
                } else if (VersionChecker.getResult(ModList.get().getModContainerById(References.MODID).get().getModInfo()).status() == VersionChecker.Status.FAILED) {
                    System.err.println(References.NAME + "'s version checker failed!");
                    hasShownUp = true;
                }
            }
        }
    }


    /** Adjustment Tool **/
    @SubscribeEvent
    public static void onInteract (PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        Level world = event.getWorld();
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();
        BlockState state = world.getBlockState(pos);
        FluidState fluidstate = player.getLevel().getFluidState(pos);

        if (stack.getItem() == ModItems.TOOL.get()) {

            if (state.getBlock() != ModBlocks.PARTICLE.get()) {

                //TOOL MODE SWITCH
                world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);

                CompoundTag tag = new CompoundTag();

                tag.putString("mode", cycleMode(stack));
                stack.setTag(tag);

                if(world.isClientSide) player.sendMessage(new TextComponent(ChatFormatting.YELLOW + "Mode: " + stack.getTag().getString("mode").substring(0, 1).toUpperCase() + stack.getTag().getString("mode").substring(1)), player.getUUID());


            //BLOCKSTATE CHANGE
            } else {
                world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);

                if (world.getBlockState(pos).getBlock() == ModBlocks.PARTICLE.get()) {
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
                            if (stack.getTag().getString("mode").equals("type") || !stack.hasTag()) {
                                ParticleBlock.refreshBlockStates(world, pos, state, +1, 0, 0);

                            } else if (stack.getTag().getString("mode").equals("strength")) {
                                ParticleBlock.refreshBlockStates(world, pos, state, 0, +1, 0);

                            } else if (stack.getTag().getString("mode").equals("range")) {
                                ParticleBlock.refreshBlockStates(world, pos, state, 0, 0, +1);

                            } else if (stack.getTag().getString("mode").equals("break")) {
                                world.destroyBlock(pos, true);

                            } else {
                                ParticleSpawner.LOGGER.error("Unknown Tool Mode: " + stack.getTag().getString("mode"));
                            }
                        }

                        if(world.isClientSide) {
                            player.sendMessage(new TextComponent(ChatFormatting.YELLOW + stack.getTag().getString("mode").substring(0, 1).toUpperCase() + stack.getTag().getString("mode").substring(1)
                                    +": " + state.getValue(ParticleBlock.getStateByName(stack.getTag().getString("mode")))), player.getUUID());
                        }
                    }
                }
            }
        }
    }


    private static String cycleMode(ItemStack stack) {
        if (stack.hasTag()) {
            String mode = stack.getTag().getString("mode");

            if(mode == "type") return "strength";
            if(mode == "strength") return "range";
            if(mode == "range") return "break";
            if(mode == "break") return "type";
        } else {
            return "type";
        }
        return "type";
    }
}