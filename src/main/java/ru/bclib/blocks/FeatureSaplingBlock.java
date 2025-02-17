package ru.bclib.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public abstract class FeatureSaplingBlock extends FeatureSaplingBlockCommon {
	private static final VoxelShape SHAPE = Block.box(4, 0, 4, 12, 14, 12);
	
	public FeatureSaplingBlock() {
		super();
	}
	
	public FeatureSaplingBlock(int light) {
		super(light);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
		return SHAPE;
	}
}
