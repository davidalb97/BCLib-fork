package ru.bclib.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.Nullable;
import ru.bclib.client.models.BasePatterns;
import ru.bclib.client.models.ModelsHelper;
import ru.bclib.client.models.PatternsHelper;
import ru.bclib.client.render.BCLRenderLayer;
import ru.bclib.interfaces.BlockModelProvider;
import ru.bclib.interfaces.RenderLayerProvider;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

abstract class FeatureSaplingBlockCommon extends SaplingBlock implements RenderLayerProvider, BlockModelProvider {
	public FeatureSaplingBlockCommon() {
		super(
			null,
			FabricBlockSettings.of(Material.PLANT)
							   .breakByHand(true)
							   .collidable(false)
							   .instabreak()
							   .sound(SoundType.GRASS)
							   .randomTicks()
		);
	}
	
	public FeatureSaplingBlockCommon(int light) {
		super(
			null,
			FabricBlockSettings.of(Material.PLANT)
							   .breakByHand(true)
							   .collidable(false)
							   .luminance(light)
							   .instabreak()
							   .sound(SoundType.GRASS)
							   .randomTicks()
		);
	}
	
	@Deprecated
	/**
	 * Override {@link #getFeature(BlockState)} directly. Will be removed in 5.x
	 */
	protected Feature<?> getFeature() { return null; }
	
	protected Feature<?> getFeature(BlockState state){
		return getFeature();
	}
	
	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Collections.singletonList(new ItemStack(this));
	}
	
	@Override
	public BlockState updateShape(BlockState state, Direction facing, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		if (!canSurvive(state, world, pos)) return Blocks.AIR.defaultBlockState();
		else return state;
	}
	
	@Override
	public boolean isBonemealSuccess(Level world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(16) == 0;
	}
	
	@Override
	public void advanceTree(ServerLevel world, BlockPos pos, BlockState blockState, Random random) {
		FeaturePlaceContext context = new FeaturePlaceContext(
			world,
			world.getChunkSource().getGenerator(),
			random,
			pos,
			null
		);
		getFeature(blockState).place(context);
	}
	
	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		this.tick(state, world, pos, random);
	}
	
	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (isBonemealSuccess(world, random, pos, state)) {
			performBonemeal(world, random, pos, state);
		}
	}
	
	@Override
	public BCLRenderLayer getRenderLayer() {
		return BCLRenderLayer.CUTOUT;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public BlockModel getItemModel(ResourceLocation resourceLocation) {
		return ModelsHelper.createBlockItem(resourceLocation);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public @Nullable BlockModel getBlockModel(ResourceLocation resourceLocation, BlockState blockState) {
		Optional<String> pattern = PatternsHelper.createJson(BasePatterns.BLOCK_CROSS, resourceLocation);
		return ModelsHelper.fromPattern(pattern);
	}
}
