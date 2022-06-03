package xxrexraptorxx.particle_spawner.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import xxrexraptorxx.particle_spawner.main.ModBlocks;
import xxrexraptorxx.particle_spawner.main.ParticleSpawner;
import xxrexraptorxx.particle_spawner.utils.CreativeTab;

import javax.annotation.Nullable;
import java.util.List;

public class AdjustmentTool extends Item {

    public AdjustmentTool() {
        super(new Properties()
                .tab(CreativeTab.MOD_TAB)
                .stacksTo(1)
        );
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if (stack.hasTag())
            list.add(new TextComponent("Mode: " + stack.getTag().getString("mode").substring(0, 1).toUpperCase() + stack.getTag().getString("mode").substring(1)).withStyle(ChatFormatting.YELLOW));
        list.add(new TranslatableComponent("message.particle_spawner.tool.desc").withStyle(ChatFormatting.GRAY));
    }


    @Override
    public InteractionResult useOn(UseOnContext event) {
        Level level = event.getLevel();
        BlockPos pos = event.getClickedPos();
        Player player = event.getPlayer();

        if (level.getBlockState(pos).getBlock() != ModBlocks.PARTICLE.get()) {

            //TOOL MODE SWITCH
            level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);

            CompoundTag tag = new CompoundTag();
            ItemStack stack = event.getItemInHand();

            tag.putString("mode", cycleMode(stack));
            stack.setTag(tag);

            if (level.isClientSide)
                player.sendMessage(new TextComponent(ChatFormatting.YELLOW + "Mode: " + stack.getTag().getString("mode").substring(0, 1).toUpperCase() + stack.getTag().getString("mode").substring(1)), player.getUUID());

        }
            return super.useOn(event);
    }


    public static String cycleMode(ItemStack stack) {
        if (stack.hasTag()) {
            String mode = stack.getTag().getString("mode");

            switch (mode) {
                case "type": return "strength";
                case "strength": return "range";
                case "range": return "break";
                case "break": return "type";
                default:
                    ParticleSpawner.LOGGER.error("Unknown mode (" + mode + ") on cycle switch.");
                    return "break";
            }

        } else {
            CompoundTag tag = new CompoundTag();

            tag.putString("mode", cycleMode(stack));
            stack.setTag(tag);
        }
        return null;
    }
}
