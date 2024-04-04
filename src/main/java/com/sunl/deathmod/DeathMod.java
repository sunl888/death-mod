package com.sunl.deathmod;

import com.sunl.deathmod.callback.PlayerMoveCallback;
import com.sunl.deathmod.event.PlayerMoveEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 半格生存mod（脚下的entity只要是整格即表示挑战失败）
 *
 * @author sunl
 * @date 2024/4/5
 */
public class DeathMod implements ModInitializer {
    public static Map<String, BlockPos> playerCurrentPosMap = new ConcurrentHashMap<>(2);


    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            playerCurrentPosMap.clear();// 第一次进入世界的时候初始化
        });

        PlayerMoveEvent.EVENT.register(new PlayerMoveCallback(playerCurrentPosMap));
    }


}
