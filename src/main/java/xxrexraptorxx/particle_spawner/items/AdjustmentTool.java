package xxrexraptorxx.particle_spawner.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import xxrexraptorxx.particle_spawner.main.ParticleSpawner;
import xxrexraptorxx.particle_spawner.registry.ModBlocks;
import xxrexraptorxx.particle_spawner.registry.ModComponents;

import java.util.List;

public class AdjustmentTool extends Item {

    public AdjustmentTool() {
        super(new Properties()
                .stacksTo(1)
        );
    }


    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> list, TooltipFlag flag) {
        if (stack.has(ModComponents.MODE))
            list.add(Component.literal("Mode: " + stack.get(ModComponents.MODE).substring(0, 1).toUpperCase() + stack.get(ModComponents.MODE).substring(1)).withStyle(ChatFormatting.YELLOW));
        list.add(Component.translatable("message.particle_spawner.tool.desc").withStyle(ChatFormatting.GRAY));
    }


    @Override
    public InteractionResult useOn(UseOnContext event) {
        Level level = event.getLevel();
        BlockPos pos = event.getClickedPos();
        Player player = event.getPlayer();

        if (level.getBlockState(pos).getBlock() != ModBlocks.PARTICLE.get()) {

            //TOOL MODE SWITCH
            level.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK.value(), SoundSource.BLOCKS, 1.0f, 1.0f);

            ItemStack stack = event.getItemInHand();
            stack.set(ModComponents.MODE, cycleMode(stack));

            if (level.isClientSide)
                player.displayClientMessage(Component.literal(ChatFormatting.YELLOW + "Mode: " + stack.get(ModComponents.MODE).substring(0, 1).toUpperCase() + stack.get(ModComponents.MODE).substring(1)), true);
        }
            return super.useOn(event);
    }



    public static String cycleMode(ItemStack stack) {
        if (stack.has(ModComponents.MODE)) {
            String mode = stack.get(ModComponents.MODE);

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
            stack.set(ModComponents.MODE, "type");
            return "type";
        }
    }

}
