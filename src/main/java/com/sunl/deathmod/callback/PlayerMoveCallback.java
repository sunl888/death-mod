package com.sunl.deathmod.callback;

import com.sunl.deathmod.event.PlayerMoveEvent;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.Map;

/**
 * @author sunl
 * @date 2024/4/5
 */
@Slf4j
public class PlayerMoveCallback implements PlayerMoveEvent {
    public Map<String, BlockPos> playerCurrentPosMap;

    public PlayerMoveCallback(Map<String, BlockPos> playerCurrentPosMap) {
        this.playerCurrentPosMap = playerCurrentPosMap;
    }

    @Override
    public ActionResult interact(PlayerEntity player) {
        String playerId = player.getUuidAsString();
        World world = player.getWorld();
        if (world instanceof ServerWorld) {
            BlockPos belowPos = player.getBlockPos();
            if (world.getBlockState(belowPos).getBlock().equals(Blocks.AIR)) {
                belowPos = player.getBlockPos().down();// 如果是空气则向下获取一个
            }

            BlockPos preBelowPos = playerCurrentPosMap.get(playerId);
            if (preBelowPos == null) {
                world.setBlockState(belowPos, Blocks.OAK_SLAB.getDefaultState());
                ItemStack sapling = new ItemStack(Items.OAK_SAPLING, 1);
                ItemStack dirt = new ItemStack(Items.DIRT, 1);

                player.giveItemStack(sapling);
                player.giveItemStack(dirt);

                player.sendMessage(new TranslatableText("player.init.give.gift"), false);
            }
            if (belowPos.equals(preBelowPos)) {
                // 没有移动
                return ActionResult.SUCCESS;
            }
            BlockState blockState = world.getBlockState(belowPos);
            log.info("block key: [{}] name: [{}]", blockState.getBlock().getTranslationKey(), blockState.getBlock().getName().getString());
            VoxelShape shape = blockState.getCollisionShape(world, belowPos);
            double l = calculateVolume(shape);
            if (l >= 1) {
                log.error("这是整方块");
                player.kill();
            } else {
                log.error("这是半方块");
            }
            playerCurrentPosMap.put(playerId, belowPos);
        }
        return ActionResult.SUCCESS;
    }

    static double calculateVolume(VoxelShape shape) {
        double volume = 0;

        // 获取形状的立方体列表
        Iterable<Box> cuboids = shape.getBoundingBoxes();

        // 遍历每个立方体并计算体积
        for (Box cuboid : cuboids) {
            volume += (cuboid.getXLength() * cuboid.getYLength() * cuboid.getZLength());
        }

        return volume;
    }
}
