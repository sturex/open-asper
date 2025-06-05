package dev.asper.poker.engine;

import java.util.EnumSet;

public enum EffStackType {
    BB_0_2,
    BB_2_4,
    BB_4_6,
    BB_6_8,
    BB_8_10,
    BB_10_12,
    BB_12_14,
    BB_14_16,
    BB_16_18,
    BB_18_20,
    BB_20_40,
    BB_40_PLUS,
    SHORT,
    MEDIUM,
    DEEP;

    public static final EnumSet<EffStackType> pfShortStacks = EnumSet.of(
            BB_0_2,
            BB_2_4,
            BB_4_6,
            BB_6_8,
            BB_8_10,
            BB_10_12,
            BB_12_14,
            BB_14_16,
            BB_16_18,
            BB_18_20,
            BB_20_40
    );

    public static EffStackType computeRough(double effStackBb) {
        if (effStackBb < 20) {
            return SHORT;
        } else if (effStackBb < 40) {
            return MEDIUM;
        } else {
            return DEEP;
        }
    }

    public static EffStackType computePrecise(double effStackBb) {
        if (effStackBb < 2) return BB_0_2;
        if (effStackBb < 4) return BB_2_4;
        if (effStackBb < 6) return BB_4_6;
        if (effStackBb < 8) return BB_6_8;
        if (effStackBb < 10) return BB_8_10;
        if (effStackBb < 12) return BB_10_12;
        if (effStackBb < 14) return BB_12_14;
        if (effStackBb < 16) return BB_14_16;
        if (effStackBb < 18) return BB_16_18;
        if (effStackBb < 20) return BB_18_20;
        if (effStackBb < 40) return BB_20_40;
        return BB_40_PLUS;
    }

}
