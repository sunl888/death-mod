package com.sunl.deathmod.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;

/**
 * 自定义一个玩家移动事件
 *
 * @see net.fabricmc.fabric.mixin.entity.event.ServerPlayerEntityMixin
 *
 * @author sunl
 * @date 2024/4/4
 */
public interface PlayerMoveEvent {
    Event<PlayerMoveEvent> EVENT = EventFactory.createArrayBacked(PlayerMoveEvent.class,
            (listeners) -> (player) -> {
                for (PlayerMoveEvent listener : listeners) {
                    ActionResult result = listener.interact(player);
                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }
                return ActionResult.PASS;
            });


    ActionResult interact(PlayerEntity player);
}
