package xxrexraptorxx.particle_spawner.items;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.Level;
import org.lwjgl.system.CallbackI;
import xxrexraptorxx.particle_spawner.main.References;
import xxrexraptorxx.particle_spawner.utils.CreativeTab;

import java.util.UUID;

public class AdjustmentTool extends Item {

    public AdjustmentTool() {
        super(new Properties()
                .tab(CreativeTab.MOD_TAB)
        );
    }

/**             => moved to events
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            if(player.isShiftKeyDown()) {

                CompoundTag tag = new CompoundTag();
                ItemStack stack = new ItemStack(this);

                tag.putString("mode", "strength"); //new TranslatableComponent("").getString()


                stack.setTag(tag);

                Minecraft.getInstance().player.sendMessage(new TextComponent(ChatFormatting.YELLOW + stack.getTag().getString("mode")), UUID.randomUUID());
            }
        }

        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide);
    }**/
}
