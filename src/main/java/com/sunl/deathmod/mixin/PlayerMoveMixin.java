package com.sunl.deathmod.mixin;

import com.sunl.deathmod.event.PlayerMoveEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author sunl
 * @date 2024/4/4
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerMoveMixin {
    @Shadow
    public abstract PlayerInventory getInventory();

    @Inject(at = @At("HEAD"), method = "tickMovement", cancellable = true)
    public void onTickMovement(CallbackInfo info) {
        PlayerEntity player = getInventory().player;
        ActionResult result = PlayerMoveEvent.EVENT.invoker().interact(player);
        if (result == ActionResult.FAIL) {
            info.cancel();
        }
    }
}
