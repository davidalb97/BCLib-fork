package ru.bclib.mixin.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.bclib.api.TagAPI;

@Mixin(CraftingMenu.class)
public abstract class CraftingMenuMixin {
	@Final
	@Shadow
	private ContainerLevelAccess access;
	
	@Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
	private void bclib_stillValid(Player player, CallbackInfoReturnable<Boolean> info) {
		if (access.evaluate((world, pos) -> {
			BlockState state = world.getBlockState(pos);
			return state.getBlock() instanceof CraftingTableBlock || state.is(TagAPI.BLOCK_WORKBENCHES);
		}, true)) {
			info.setReturnValue(true);
		}
	}
}
