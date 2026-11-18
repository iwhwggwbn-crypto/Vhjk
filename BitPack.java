package dev.bitpack;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitPack implements ModInitializer {
    public static final String MOD_ID = "bitpack";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /** 24 бита на ось: допустимые координаты -8 388 608 .. 8 388 607. */
    public static final int HORIZONTAL_RANGE = 8_388_608;

    /** Диаметр границы мира, чтобы координаты не «заворачивались». */
    public static final double MAX_BORDER_SIZE = 16_777_200.0;

    @Override
    public void onInitialize() {
        BlockPos probe = new BlockPos(8_388_607, 32_000, -8_388_608);
        boolean ok = BlockPos.fromLong(probe.asLong()).equals(probe);
        if (ok) {
            LOGGER.info("BitPack: упаковка BlockPos 24/24/16 работает. SIZE_BITS_Y={}, высота мира {}..{}",
                    BlockPos.SIZE_BITS_Y, DimensionType.MIN_HEIGHT, DimensionType.MAX_COLUMN_HEIGHT);
        } else {
            LOGGER.error("BitPack: BlockPos всё ещё упакован по-ванильному, миксин на BlockPos не сработал!");
        }

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            for (ServerWorld world : server.getWorlds()) {
                WorldBorder border = world.getWorldBorder();
                if (border.getSize() > MAX_BORDER_SIZE) {
                    border.setSize(MAX_BORDER_SIZE);
                    LOGGER.info("BitPack: граница мира {} уменьшена до {}", world.getRegistryKey().getValue(), MAX_BORDER_SIZE);
                }
            }
        });
    }
}
