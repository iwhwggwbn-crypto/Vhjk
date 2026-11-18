package dev.bitpack.mixin;

import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BlockPos.class)
public abstract class BlockPosMixin {
    /**
     * Ваниль: SIZE_BITS_X = 1 + floorLog2(smallestEncompassingPowerOfTwo(30_000_000)) = 26,
     * SIZE_BITS_Y = 64 - 26 - 26 = 12.
     * С 8_000_000 → 2^23 → X = Z = 24 бита, Y = 16 бит.
     */
    @ModifyConstant(method = "<clinit>", constant = @Constant(intValue = 30000000))
    private static int bitpack$horizontalRange(int original) {
        return 8_000_000;
    }
}
