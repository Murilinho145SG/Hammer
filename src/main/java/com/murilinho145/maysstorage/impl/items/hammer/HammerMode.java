package com.murilinho145.maysstorage.impl.items.hammer;

public enum HammerMode {
    FACING(1),
    ANGULAR(2);

    private HammerMode(int level) {}

    public static boolean isHammerMode(final HammerMode mode) {
        return mode == FACING || mode == ANGULAR;
    }

    public static HammerMode getMode(int level) {
        if (level == 1) {
            return FACING;
        } else {
            return ANGULAR;
        }
    }
}
