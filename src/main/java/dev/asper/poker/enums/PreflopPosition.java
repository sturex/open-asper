package dev.asper.poker.enums;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Collectors;

/**
 * Values are stored in natural order and comparable by enum's ordinal
 */
public enum PreflopPosition {
    SB,
    BB,
    UTG1,
    UTG2 {
        @Override
        public PreflopPosition to6maxName() {
            return UTG1;
        }
    },
    UTG3 {
        @Override
        public PreflopPosition to6maxName() {
            return UTG1;
        }
    },
    MP1,
    MP2 {
        @Override
        public PreflopPosition to6maxName() {
            return MP1;
        }
    },
    MP3 {
        @Override
        public PreflopPosition to6maxName() {
            return MP1;
        }
    },
    CO,
    BTN;

    public static final EnumSet<PreflopPosition> utgPositions = Arrays.stream(new PreflopPosition[]{UTG1, UTG2, UTG3}).collect(Collectors.toCollection(() -> EnumSet.noneOf(PreflopPosition.class)));
    public static final EnumSet<PreflopPosition> mpPositions = Arrays.stream(new PreflopPosition[]{MP1, MP2, MP3}).collect(Collectors.toCollection(() -> EnumSet.noneOf(PreflopPosition.class)));
    public static final EnumSet<PreflopPosition> freePositions = Arrays.stream(new PreflopPosition[]{UTG1, UTG2, UTG3, MP1, MP2, MP3, CO, BTN}).collect(Collectors.toCollection(() -> EnumSet.noneOf(PreflopPosition.class)));

    public PreflopPosition to6maxName() {
        return this;
    }

    public boolean isAfter(PreflopPosition preflopPosition) {
        return this.ordinal() > preflopPosition.ordinal();
    }

    public boolean isFreePosition() {
        return freePositions.contains(this);
    }
    public boolean isBlindPosition() {
        return this == SB || this == BB;
    }

    public boolean isAfterOrEquals(PreflopPosition preflopPosition) {
        return this.ordinal() >= preflopPosition.ordinal();
    }

    public boolean isBefore(PreflopPosition preflopPosition) {
        return this.ordinal() < preflopPosition.ordinal();
    }

    public boolean isBeforeOrEquals(PreflopPosition preflopPosition) {
        return this.ordinal() <= preflopPosition.ordinal();
    }
}
