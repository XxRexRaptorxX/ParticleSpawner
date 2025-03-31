package xxrexraptorxx.particle_spawner.world;

import com.mojang.authlib.GameProfile;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.VersionChecker;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import xxrexraptorxx.particle_spawner.blocks.ParticleBlock;
import xxrexraptorxx.particle_spawner.items.AdjustmentTool;
import xxrexraptorxx.particle_spawner.main.ParticleSpawner;
import xxrexraptorxx.particle_spawner.main.References;
import xxrexraptorxx.particle_spawner.registry.ModBlocks;
import xxrexraptorxx.particle_spawner.registry.ModComponents;
import xxrexraptorxx.particle_spawner.registry.ModItems;
import xxrexraptorxx.particle_spawner.utils.Config;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = References.MODID, bus = EventBusSubscriber.Bus.GAME)
public class Events {

    /** Update-Checker **/
    private static boolean hasShownUp = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        if (Config.UPDATE_CHECKER != null && Config.UPDATE_CHECKER.get()) {

            if (!hasShownUp && Minecraft.getInstance().screen == null) {
                var player = Minecraft.getInstance().player;
                if (player == null) return;

                var modContainer = ModList.get().getModContainerById(References.MODID).orElse(null);

                if (modContainer != null) {
                    var versionCheckResult = VersionChecker.getResult(modContainer.getModInfo());

                    if (versionCheckResult.status() == VersionChecker.Status.OUTDATED || versionCheckResult.status() == VersionChecker.Status.BETA_OUTDATED) {
                        MutableComponent url = Component.literal(ChatFormatting.GREEN + "Click here to update!")
                                .withStyle(style -> style.withClickEvent(new ClickEvent.OpenUrl(URI.create(References.URL))));

                        player.displayClientMessage(Component.literal(ChatFormatting.BLUE + "A newer version of " + ChatFormatting.YELLOW + References.NAME + ChatFormatting.BLUE + " is available!"), false);
                        player.displayClientMessage(url, false);

                        hasShownUp = true;

                    } else if (versionCheckResult.status() == VersionChecker.Status.FAILED) {
                        ParticleSpawner.LOGGER.error(References.NAME + "'s version checker failed!");
                        hasShownUp = true;
                    }
                }
            }
        }
    }


    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    /**
     * Distributes supporter rewards on first login.
     */
    @SubscribeEvent
    public static void SupporterRewards(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        Level level = player.level();

        if (Config.PATREON_REWARDS.get()) {
            // Check if the player already has rewards
            if (!player.getInventory().contains(new ItemStack(Items.PAPER))) {
                if (player instanceof ServerPlayer serverPlayer) { // Ensure the player is a ServerPlayer
                    // Check if the player is logging in for the first time
                    if (serverPlayer.getStats().getValue(Stats.CUSTOM, Stats.PLAY_TIME) < 5) {

                        // Perform supporter checks asynchronously
                        CompletableFuture.runAsync(() -> {
                            if (SupporterCheck(URI.create("https://raw.githubusercontent.com/XxRexRaptorxX/Patreons/main/Supporter"), player)) {
                                giveSupporterReward(player, level);
                            }
                            if (SupporterCheck(URI.create("https://raw.githubusercontent.com/XxRexRaptorxX/Patreons/main/Premium%20Supporter"), player)) {
                                givePremiumSupporterReward(player, level);
                            }
                            if (SupporterCheck(URI.create("https://raw.githubusercontent.com/XxRexRaptorxX/Patreons/main/Elite"), player)) {
                                giveEliteReward(player);
                            }
                        });
                    }
                }
            }
        }
    }


    /**
     * Checks if the player is in the supporter list from the given URI.
     *
     * @param uri URI to a file containing supporter names
     * @param player The in-game player
     * @return true if the player is a supporter, otherwise false
     */
    private static boolean SupporterCheck(URI uri, Player player) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            // Parse supporter list
            List<String> supporterList = List.of(response.body().split("\\R")); // Split lines
            return supporterList.contains(player.getName().getString());

        } catch (Exception e) {
            ParticleSpawner.LOGGER.error("Failed to fetch or process supporter list from URI: {}", uri, e);
            return false;
        }
    }


    private static void giveSupporterReward(Player player, Level level) {
        ItemStack certificate = new ItemStack(Items.PAPER);
        certificate.set(DataComponents.CUSTOM_NAME, Component.literal("Thank you for supporting me in my work!").withStyle(ChatFormatting.GOLD)
                .append(Component.literal(" - XxRexRaptorxX").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GREEN)));

        ItemStack reward = new ItemStack(Items.PLAYER_HEAD);
        var profile = new GameProfile(player.getUUID(), player.getName().getString());
        reward.set(DataComponents.PROFILE, new ResolvableProfile(profile));

        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, level.random.nextFloat() * 0.15F + 0.8F);
        player.getInventory().add(reward);
        player.getInventory().add(certificate);
    }

    private static void givePremiumSupporterReward(Player player, Level level) {
        ItemStack reward = new ItemStack(Items.DIAMOND_SWORD, 1);
        Registry<Enchantment> enchantmentsRegistry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);

        reward.enchant(enchantmentsRegistry.getOrThrow(Enchantments.MENDING), 1);
        reward.enchant(enchantmentsRegistry.getOrThrow(Enchantments.SHARPNESS), 3);
        reward.set(DataComponents.ENCHANTMENTS, reward.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY));
        player.getInventory().add(reward);
    }

    private static void giveEliteReward(Player player) {
        ItemStack star = new ItemStack(Items.NETHER_STAR);

        star.set(DataComponents.CUSTOM_NAME, Component.literal("Elite Star"));
        player.getInventory().add(star);
    }


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
                world.setBlock(pos, ModBlocks.PARTICLE.get().defaultBlockState()
                        .setValue(ParticleBlock.POWERED, true)
                        .setValue(ParticleBlock.PARTICLE_STRENGTH, state.getValue(ParticleBlock.PARTICLE_STRENGTH))
                        .setValue(ParticleBlock.WATERLOGGED, fluidstate.getType() == Fluids.WATER), 11);
            }

            // Modes
            if (state.getValue(ParticleBlock.POWERED)) {
                String mode = stack.get(ModComponents.MODE);

                if (player.isShiftKeyDown()) {  // subtract mode
                    switch (mode) {
                        case "type":
                            ParticleBlock.refreshBlockStates(world, pos, state, -1, 0, 0);
                            break;
                        case "strength":
                            ParticleBlock.refreshBlockStates(world, pos, state, 0, -1, 0);
                            break;
                        case "range":
                            ParticleBlock.refreshBlockStates(world, pos, state, 0, 0, -1);
                            break;
                    }
                } else {  // add mode
                    switch (mode) {
                        case "break":
                            ItemEntity drop = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D, new ItemStack(ModBlocks.PARTICLE.get()));
                            world.destroyBlock(pos, false);
                            world.addFreshEntity(drop);
                            break;
                        case "type":
                            ParticleBlock.refreshBlockStates(world, pos, state, +1, 0, 0);
                            break;
                        case "strength":
                            ParticleBlock.refreshBlockStates(world, pos, state, 0, +1, 0);
                            break;
                        case "range":
                            ParticleBlock.refreshBlockStates(world, pos, state, 0, 0, +1);
                            break;
                        default:
                            ParticleSpawner.LOGGER.error("Unknown Tool Mode: " + mode);
                    }
                }

                if (world.isClientSide) {
                    player.displayClientMessage(Component.literal(ChatFormatting.YELLOW + mode.substring(0, 1).toUpperCase() + mode.substring(1)
                            + ": " + state.getValue(ParticleBlock.getStateByName(mode))), true);
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
            list.add(Component.translatable("message." + References.MODID + ".spawner.desc").withStyle(ChatFormatting.GRAY));
        }
    }


}
