package xxrexraptorxx.particle_spawner.world;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import xxrexraptorxx.particle_spawner.blocks.ParticleBlock;
import xxrexraptorxx.particle_spawner.items.AdjustmentTool;
import xxrexraptorxx.particle_spawner.main.ModBlocks;
import xxrexraptorxx.particle_spawner.main.ModItems;
import xxrexraptorxx.particle_spawner.main.ParticleSpawner;
import xxrexraptorxx.particle_spawner.main.References;
import xxrexraptorxx.particle_spawner.utils.Config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

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

                    MutableComponent url = Component.literal(ChatFormatting.GREEN + "Click here to update!");
                    url.withStyle(url.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, References.URL)));

                    Minecraft.getInstance().player.displayClientMessage(Component.literal(ChatFormatting.BLUE + "A newer version of " + ChatFormatting.YELLOW + References.NAME + ChatFormatting.BLUE + " is available!"), false);
                    Minecraft.getInstance().player.displayClientMessage(url, false);

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

        //test if adjustment tool is in hand and particle spawner is clicked
        if (stack.getItem() == ModItems.TOOL.get() && state.getBlock() == ModBlocks.PARTICLE.get()) {

            world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);

            //sets a mode if no tag is present
            if (!stack.hasTag()) {
                AdjustmentTool.cycleMode(stack);
            }

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



    /**
     * Distributes the supporter rewards on first join.
     */
    @SubscribeEvent
    public static void SupporterRewards(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getPlayer();
        Level world = player.getLevel();

        if (Config.PATREON_REWARDS.get()) {

            try {
                URL SUPPORTER_URL = new URL("https://raw.githubusercontent.com/XxRexRaptorxX/Patreons/main/Supporter");
                URL PREMIUM_SUPPORTER_URL = new URL("https://raw.githubusercontent.com/XxRexRaptorxX/Patreons/main/Premium%20Supporter");
                URL ELITE_URL = new URL("https://raw.githubusercontent.com/XxRexRaptorxX/Patreons/main/Elite");

                //test if a player already has rewards
                if (!player.getInventory().contains(new ItemStack(Items.PAPER))) {

                    ServerPlayer serverPlayer = (ServerPlayer) player;
                    //test if player joins the first time
                    if (serverPlayer.getStats().getValue(Stats.CUSTOM, Stats.PLAY_TIME) < 5) {

                        //test if player is supporter
                        if (SupporterCheck(SUPPORTER_URL, player)) {

                            ItemStack certificate = new ItemStack(Items.PAPER).setHoverName((Component.literal("Thank you for supporting me in my work!").withStyle(ChatFormatting.GOLD).append(Component.literal(" - XxRexRaptorxX").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GREEN))));

                            CompoundTag ownerNBT = new CompoundTag();
                            ItemStack reward = new ItemStack(Items.PLAYER_HEAD);
                            ownerNBT.putString("SkullOwner", player.getName().getString());
                            reward.setTag(ownerNBT);

                            player.getLevel().playSound((Player) null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, world.random.nextFloat() * 0.15F + 0.8F);
                            player.addItem(reward);
                            player.addItem(certificate);
                        }

                        //test if player is premium supporter
                        if (SupporterCheck(PREMIUM_SUPPORTER_URL, player)) {
                            ItemStack reward = new ItemStack(Items.DIAMOND_SWORD, 1).setHoverName(Component.literal("Rex's Night Sword").withStyle(ChatFormatting.DARK_GRAY));
                            reward.enchant(Enchantments.MENDING, 1);
                            reward.enchant(Enchantments.SHARPNESS, 3);
                            player.addItem(reward);
                        }

                        //test if player is elite
                        if (SupporterCheck(ELITE_URL, player)) {
                            player.addItem(new ItemStack(Items.NETHER_STAR).setHoverName(Component.literal("Elite Star")));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Tests if a player is a supporter
     *
     * @param url url to a file that contains the supporter names
     * @param player ingame player
     * @return true/false
     */
    private static boolean SupporterCheck(URL url, Player player) {
        try {
            Scanner scanner = new Scanner(url.openStream());
            List<String> supporterList = scanner.tokens().toList();

            for (String name: supporterList) {
                //test if player is in supporter list
                if (player.getName().getString().equals(name)) {
                    return true;
                }
            }

            scanner.close();

        } catch (MalformedURLException e) {
            ParticleSpawner.LOGGER.error("Supporter list URL not found! >>" + url);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


}
