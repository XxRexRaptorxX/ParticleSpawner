package xxrexraptorxx.particle_spawner.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import xxrexraptorxx.particle_spawner.main.ParticleSpawner;
import xxrexraptorxx.particle_spawner.registry.ModBlocks;
import xxrexraptorxx.particle_spawner.utils.Config;
import xxrexraptorxx.particle_spawner.utils.ParticleHelper;

import javax.annotation.Nullable;
import java.util.Random;


public class ParticleBlock extends Block implements SimpleWaterloggedBlock {

	protected static final VoxelShape OFF_SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);
	protected static final VoxelShape ON_SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final IntegerProperty PARTICLE_TYPE = IntegerProperty.create("type", 1, 58);			//TODO wip
	public static final IntegerProperty PARTICLE_STRENGTH = IntegerProperty.create("strength", 1, 10);	//TODO wip
	public static final IntegerProperty PARTICLE_RANGE = IntegerProperty.create("range", 1, 10);			//TODO wip


	public ParticleBlock(Properties properties) {
		super(properties);

		this.registerDefaultState(this.defaultBlockState().setValue(POWERED, Boolean.valueOf(false)).setValue(PARTICLE_TYPE, 1).setValue(PARTICLE_STRENGTH, 1).setValue(PARTICLE_RANGE, 1).setValue(WATERLOGGED, Boolean.valueOf(false)));
	}

	/**
	 * Allows easy setting of new blockstates
	 */
	public static void refreshBlockStates(Level level, BlockPos pos, BlockState state, Integer typeChange, Integer strengthChange, Integer rangeChange) {
		if(state.getBlock() == ModBlocks.PARTICLE.get()) {
			FluidState fluidstate = level.getFluidState(pos);

			//calculate the new blockstate value
			int type = state.getValue(PARTICLE_TYPE) + typeChange;
			int strength = state.getValue(PARTICLE_STRENGTH) + strengthChange;
			int range = state.getValue(PARTICLE_RANGE) + rangeChange;

			//test if new value is higher than the max value, and reset it if to high
			if (type > Config.PARTICLE_SPAWNER_TYPE_MAX_VALUE) 			type = 1;
			if (strength > Config.PARTICLE_SPAWNER_STRENGTH_MAX_VALUE) 	strength = 1;
			if (range > Config.PARTICLE_SPAWNER_RANGE_MAX_VALUE) 			range = 1;

			//test if the new value is 0 (because of the subtract mode) and set it to the max value
			if (type == 0) 		type = Config.PARTICLE_SPAWNER_TYPE_MAX_VALUE;
			if (strength == 0)	 	strength = Config.PARTICLE_SPAWNER_STRENGTH_MAX_VALUE;
			if (range == 0) 		range = Config.PARTICLE_SPAWNER_RANGE_MAX_VALUE;

			//update the block with the new values
			level.setBlock(pos, ModBlocks.PARTICLE.get().defaultBlockState().setValue(ParticleBlock.POWERED, true).setValue(ParticleBlock.PARTICLE_TYPE, type).setValue(ParticleBlock.PARTICLE_STRENGTH, strength).setValue(ParticleBlock.PARTICLE_RANGE, range).setValue(ParticleBlock.WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER)), 11);
		}
	}


	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
		super.animateTick(state, level, pos, randomSource);
		Random random = new Random();

		if(state.getValue(POWERED)) {
			for (int i = 0; i < state.getValue(PARTICLE_STRENGTH); i++) {
				level.addParticle(ParticleHelper.getParticleById(state.getValue(PARTICLE_TYPE)), false, Config.ALWAYS_RENDER_PARTICLES.get(), (double) pos.getX() - (state.getValue(PARTICLE_RANGE) / 2) + random.nextDouble(state.getValue(PARTICLE_RANGE).doubleValue()), (double) pos.getY() - (state.getValue(PARTICLE_RANGE) / 2) + random.nextDouble(state.getValue(PARTICLE_RANGE)), (double) pos.getZ() - (state.getValue(PARTICLE_RANGE) / 2) + random.nextDouble(state.getValue(PARTICLE_RANGE)), 0.0D, 0.0D, 0.00);
			}
		}
	}


	//Shapes

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.getValue(POWERED)) {
			return Shapes.empty();
		} else {
			return OFF_SHAPE;
		}
	}


	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		if (state.getValue(POWERED)) {
			return ON_SHAPE;
		} else {
			return OFF_SHAPE;
		}
	}


	//Blockstates

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED, PARTICLE_TYPE, PARTICLE_STRENGTH, PARTICLE_RANGE, WATERLOGGED);
	}


	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());

		return this.defaultBlockState().setValue(POWERED, false).setValue(PARTICLE_TYPE, 1).setValue(PARTICLE_STRENGTH, 1).setValue(PARTICLE_RANGE, 1).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
	}


	public static Property getStateByName(String mode) {
		switch (mode) {
			case "strength":
				return PARTICLE_STRENGTH;

			case "range":
				return PARTICLE_RANGE;

			case "type":
				return PARTICLE_TYPE;

			case "powered":
				return POWERED;

			default:
				ParticleSpawner.LOGGER.error("Unknown BlockState");
				return WATERLOGGED;
		}
	}


	// Waterlogging

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess scheduledTickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
	if (state.getValue(WATERLOGGED)) {
			scheduledTickAccess.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, level, scheduledTickAccess, pos, direction, neighborPos, neighborState, random);
	}


	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}


}