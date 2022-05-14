package xxrexraptorxx.particle_spawner.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import xxrexraptorxx.particle_spawner.main.ModBlocks;
import xxrexraptorxx.particle_spawner.utils.Config;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;


public class ParticleBlock extends Block {

	protected static final VoxelShape CUSTOM_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final IntegerProperty PARTICLE_TYPE = IntegerProperty.create("type", 1, 10);
	public static final IntegerProperty PARTICLE_STRENGTH = IntegerProperty.create("strength", 1, 10);
	public static final IntegerProperty PARTICLE_RANGE = IntegerProperty.create("range", 1, 10);


	public ParticleBlock() {
		super(Properties.of(Material.AIR)
				.noCollission()
				.noOcclusion()
				.strength(-1.0F, 3600000.0F)
				.sound(SoundType.STONE)
		);

		this.registerDefaultState(this.defaultBlockState().setValue(POWERED, false).setValue(PARTICLE_TYPE, 1).setValue(PARTICLE_STRENGTH, 1).setValue(PARTICLE_RANGE, 1));
	}

	/**
	 * Allows easy setting of new blockstates
	 */
	public static void refreshBlockStates(Level level, BlockPos pos, BlockState state, Integer typeChange, Integer strengthChange, Integer rangeChange) {
		if(state.getBlock() == ModBlocks.PARTICLE.get()) {

			//calculate the new blockstate value
			int type = state.getValue(PARTICLE_TYPE) + typeChange;
			int strength = state.getValue(PARTICLE_STRENGTH) + strengthChange;
			int range = state.getValue(PARTICLE_RANGE) + rangeChange;

			//test if new value is higher than the max value, and reset it if to high
			if (type > Config.PARTICLE_SPAWNER_TYPE_MAX_VALUE.get()) 			type = type - Config.PARTICLE_SPAWNER_TYPE_MAX_VALUE.get() + 1;
			if (strength > Config.PARTICLE_SPAWNER_STRENGTH_MAX_VALUE.get()) 	strength = strength - Config.PARTICLE_SPAWNER_STRENGTH_MAX_VALUE.get() + 1;
			if (range > Config.PARTICLE_SPAWNER_RANGE_MAX_VALUE.get()) 			range = range - Config.PARTICLE_SPAWNER_RANGE_MAX_VALUE.get() + 1;

			//update the block with the new values
			level.setBlock(pos, ModBlocks.PARTICLE.get().defaultBlockState().setValue(ParticleBlock.POWERED, true).setValue(ParticleBlock.PARTICLE_TYPE, type).setValue(ParticleBlock.PARTICLE_STRENGTH, strength).setValue(ParticleBlock.PARTICLE_RANGE, range), 11);
		}
	}


	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
		super.animateTick(state, level, pos, random);

		if(state.getValue(POWERED)) {
			for (int i = 0; i < state.getValue(PARTICLE_STRENGTH); i++) {
				level.addParticle(ParticleTypes.FLAME, false, (double) pos.getX() + random.nextDouble(state.getValue(PARTICLE_RANGE) - (state.getValue(PARTICLE_RANGE) / 2)), (double) pos.getY() + random.nextDouble(state.getValue(PARTICLE_RANGE) - (state.getValue(PARTICLE_RANGE) / 2)), (double) pos.getZ() + random.nextDouble(state.getValue(PARTICLE_RANGE) - (state.getValue(PARTICLE_RANGE) / 2)), 0.0D, 0.0D, 0.0D);
			}
		}
	}


	@Override
	public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> list, TooltipFlag pFlag) {
		//TODO
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
		builder.add(PARTICLE_TYPE);
		builder.add(PARTICLE_STRENGTH);
		builder.add(PARTICLE_RANGE);
	}


	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(POWERED, false).setValue(PARTICLE_TYPE, 1).setValue(PARTICLE_STRENGTH, 1).setValue(PARTICLE_RANGE, 1);
	}


}