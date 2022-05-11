package xxrexraptorxx.particle_spawner.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import xxrexraptorxx.particle_spawner.main.ModItems;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;


public class ParticleBlock extends Block {

	protected static final VoxelShape CUSTOM_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final IntegerProperty PARTICLE_TYPE = IntegerProperty.create("type", 0, 10);
	public static final IntegerProperty PARTICLE_STRENGTH = IntegerProperty.create("strength", 0, 10);
	public static final IntegerProperty PARTICLE_RANGE = IntegerProperty.create("range", 0, 10);




	public ParticleBlock() {
		super(Properties.of(Material.AIR)
				.noCollission()
				.noOcclusion()
				.strength(-1.0F, 3600000.0F)
				.sound(SoundType.STONE)
		);

		this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false).setValue(PARTICLE_TYPE, 0).setValue(PARTICLE_STRENGTH, 0).setValue(PARTICLE_RANGE, 0));
	}


	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
		super.animateTick(state, level, pos, random);

		if(state.getValue(POWERED)) {
			for (int i = 0; i < state.getValue(PARTICLE_STRENGTH); i++) {
				level.addParticle(ParticleTypes.FLAME, true, (double) pos.getX() + state.getValue(PARTICLE_RANGE) + random.nextDouble(), (double) pos.getY() + state.getValue(PARTICLE_RANGE) + random.nextDouble(), (double) pos.getZ() + state.getValue(PARTICLE_RANGE) + random.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		}
	}


	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> list, TooltipFlag pFlag) {
		//list.add(new TranslatableComponent("message.minetraps.spike.desc").withStyle(ChatFormatting.GRAY));
	}


	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide) {
			if(player.getUseItem().getItem().getRegistryName().toString() == ModItems.TOOL.get().getRegistryName().toString()) {

				world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundSource.BLOCKS, 1.0f, 1.0f);
				world.setBlock(pos, this.defaultBlockState().setValue(PARTICLE_STRENGTH, state.getValue(PARTICLE_STRENGTH) + 1), 11);
			}
		}

		return InteractionResult.sidedSuccess(world.isClientSide);
	}


	//Shapes

	@Override
	public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return Shapes.empty();
	}


	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return CUSTOM_SHAPE;
	}


	//Blockstates

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(POWERED);
		pBuilder.add(PARTICLE_TYPE);
		pBuilder.add(PARTICLE_STRENGTH);
		pBuilder.add(PARTICLE_RANGE);
	}


	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(POWERED, false).setValue(PARTICLE_TYPE, 0).setValue(PARTICLE_STRENGTH, 1).setValue(PARTICLE_RANGE, 0);
	}


}