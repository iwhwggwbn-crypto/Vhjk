package dev.bitpack;

/** Передача полного (не обрезанного до byte) Y секции между точками внедрения в ChunkSerializer. */
public final class SectionYHolder {
    public static final ThreadLocal<Integer> WRITE = new ThreadLocal<>();
    public static final ThreadLocal<Integer> READ = new ThreadLocal<>();

    private SectionYHolder() {}
}
