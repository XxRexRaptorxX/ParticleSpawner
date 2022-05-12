package xxrexraptorxx.particle_spawner.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import xxrexraptorxx.particle_spawner.utils.CreativeTab;

import javax.annotation.Nullable;
import java.util.List;

public class AdjustmentTool extends Item {

    public AdjustmentTool() {
        super(new Properties()
                .tab(CreativeTab.MOD_TAB)
        );
    }


    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if(stack.hasTag()) list.add(new TextComponent("Mode: " + stack.getTag().getString("mode").substring(0, 1).toUpperCase() + stack.getTag().getString("mode").substring(1)).withStyle(ChatFormatting.YELLOW));
        list.add(new TranslatableComponent("message.particle_spawner.tool.desc").withStyle(ChatFormatting.GRAY));
    }

}
