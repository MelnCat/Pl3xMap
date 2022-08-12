package net.pl3x.map.api.heightmap;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import net.pl3x.map.api.coordinate.BlockCoordinate;
import net.pl3x.map.render.task.ScanData;

public abstract class Heightmap {
    public int[] x = new int[16];
    public int[] z = new int[16];

    public int getColor(BlockCoordinate coordinate, ScanData data, ScanData.Data scanData, boolean flat) {
        return getColor(data, scanData.get(coordinate.west()), scanData.get(coordinate.north()), flat);
    }

    public abstract int getColor(ScanData data1, ScanData data2, ScanData data3, boolean flat);

    public enum Type {
        OLD_SCHOOL(() -> new OldSchoolHeightmap()),
        MODERN(() -> new ModernHeightmap()),
        DYNMAP(() -> new DynmapHeightmap());

        private static final Map<String, Type> BY_NAME = new HashMap<>();

        private final Supplier<Heightmap> supplier;

        Type(Supplier<Heightmap> supplier) {
            this.supplier = supplier;
        }

        public Heightmap createHeightmap() {
            return this.supplier.get();
        }

        public static Type get(String name) {
            Type type = BY_NAME.get(name.toUpperCase(Locale.ROOT)
                    .replaceAll("\\s+", "_")
                    .replaceAll("\\W", ""));
            return type == null ? MODERN : type;
        }

        static {
            for (Type type : values()) {
                BY_NAME.put(type.name(), type);
            }
        }
    }
}