package dev.asper.poker.enums;

import java.util.EnumSet;

public enum HeroActionType {
    R, C, CC, CR, RC, RR, OTHER;

    private final static EnumSet<HeroActionType> raises = EnumSet.of(R, CR, RR);

    public boolean isRaiser() {
        return raises.contains(this);
    }
}
