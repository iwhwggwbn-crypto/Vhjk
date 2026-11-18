package dev.bitpack.mixin;

import dev.bitpack.SectionYHolder;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.ChunkSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Ваниль пишет номер секции чанка как byte ("Y"), поэтому секций максимум ±127
 * (блоки -2032..2031). Здесь "Y" пишется как int, а читается любым числовым типом,
 * так что старые миры читаются без проблем.
 */
@Mixin(ChunkSerializer.class)
public abstract class ChunkSerializerMixin {

    // ---- запись ----

    /** Запоминаем полный Y секции в момент, когда цикл вызывает ChunkSectionPos.from(chunkPos, i). */
    @ModifyArg(
            method = "serialize",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/util/math/ChunkSectionPos;from(Lnet/minecraft/util/math/ChunkPos;I)Lnet/minecraft/util/math/ChunkSectionPos;"),
            index = 1)
    private static int bitpack$rememberWriteY(int sectionY) {
        SectionYHolder.WRITE.set(sectionY);
        return sectionY;
    }

    /** putByte("Y", (byte) i) → putInt("Y", i). */
    @Redirect(
            method = "serialize",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putByte(Ljava/lang/String;B)V"))
    private static void bitpack$writeY(NbtCompound nbt, String key, byte value) {
        Integer y = SectionYHolder.WRITE.get();
        if ("Y".equals(key) && y != null) {
            nbt.putInt(key, y);
        } else {
            nbt.putByte(key, value);
        }
    }

    // ---- чтение ----

    /** getByte("Y") обрезает значение, поэтому читаем полный int и кладём в ThreadLocal. */
    @Redirect(
            method = "deserialize",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;getByte(Ljava/lang/String;)B"))
    private static byte bitpack$readY(NbtCompound nbt, String key) {
        if ("Y".equals(key)) {
            SectionYHolder.READ.set(nbt.getInt("Y"));
        }
        return nbt.getByte(key);
    }

    /** После присвоения локальной переменной (номер секции) подменяем её полным значением. */
    @ModifyVariable(method = "deserialize", at = @At("STORE"), ordinal = 1)
    private static int bitpack$fixReadY(int original) {
        Integer y = SectionYHolder.READ.get();
        if (y == null) {
            return original;
        }
        SectionYHolder.READ.remove();
        return y;
    }
}
