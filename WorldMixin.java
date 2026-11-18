package dev.bitpack.mixin;

import dev.bitpack.BitPack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(World.class)
public abstract class WorldMixin {
    /** Проверка допустимых горизонтальных координат (ваниль: ±30 000 000). */
    @ModifyConstant(
            method = "isValidHorizontally",
            constant = {@Constant(intValue = 30000000), @Constant(intValue = -30000000)},
            require = 0)
    private static int bitpack$limit(int original) {
        return original < 0 ? -BitPack.HORIZONTAL_RANGE : BitPack.HORIZONTAL_RANGE;
    }
}
