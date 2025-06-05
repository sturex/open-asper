package dev.asper.poker.enums;

public enum BoardSize {

    HEADS_UP(2, new PreflopPosition[]{PreflopPosition.BB, PreflopPosition.SB}, 0.55),
    SIZE_3(3, new PreflopPosition[]{PreflopPosition.SB, PreflopPosition.BB, PreflopPosition.BTN}, 0.36),
    SIZE_4(4, new PreflopPosition[]{PreflopPosition.SB, PreflopPosition.BB, PreflopPosition.CO, PreflopPosition.BTN}, 0.36),
    SIZE_5(5, new PreflopPosition[]{PreflopPosition.SB, PreflopPosition.BB, PreflopPosition.UTG1, PreflopPosition.CO, PreflopPosition.BTN}, 0.36),
    SIZE_6(6, new PreflopPosition[]{PreflopPosition.SB, PreflopPosition.BB, PreflopPosition.UTG1, PreflopPosition.MP1, PreflopPosition.CO, PreflopPosition.BTN}, 0.36),
    SIZE_7(7, new PreflopPosition[]{PreflopPosition.SB, PreflopPosition.BB, PreflopPosition.UTG1, PreflopPosition.MP1, PreflopPosition.MP2, PreflopPosition.CO, PreflopPosition.BTN}, 0.28),
    SIZE_8(8, new PreflopPosition[]{PreflopPosition.SB, PreflopPosition.BB, PreflopPosition.UTG1, PreflopPosition.UTG2, PreflopPosition.MP1, PreflopPosition.MP2, PreflopPosition.CO, PreflopPosition.BTN}, 0.28),
    SIZE_9(9, new PreflopPosition[]{PreflopPosition.SB, PreflopPosition.BB, PreflopPosition.UTG1, PreflopPosition.UTG2, PreflopPosition.MP1, PreflopPosition.MP2, PreflopPosition.MP3, PreflopPosition.CO, PreflopPosition.BTN}, 0.28),
    SIZE_10(10, new PreflopPosition[]{PreflopPosition.SB, PreflopPosition.BB, PreflopPosition.UTG1, PreflopPosition.UTG2, PreflopPosition.UTG3, PreflopPosition.MP1, PreflopPosition.MP2, PreflopPosition.MP3, PreflopPosition.CO, PreflopPosition.BTN}, 0.28);

    private final PreflopPosition[] preflopPositions;
    private final int size;
    private final double stdVpip;

    BoardSize(int size, PreflopPosition[] preflopPositions, double stdVpip) {
        this.size = size;
        this.preflopPositions = preflopPositions;
        this.stdVpip = stdVpip;
    }

    public PreflopPosition[] preflopPositions() {
        return preflopPositions;
    }

    public int size() {
        return size;
    }

    public static BoardSize of(int size) {
        return switch (size) {
            case 2 -> HEADS_UP;
            case 3 -> SIZE_3;
            case 4 -> SIZE_4;
            case 5 -> SIZE_5;
            case 6 -> SIZE_6;
            case 7 -> SIZE_7;
            case 8 -> SIZE_8;
            case 9 -> SIZE_9;
            case 10 -> SIZE_10;
            default -> throw new IllegalStateException("Unexpected value: " + size);
        };
    }

    public double stdVpip() {
        return stdVpip;
    }
}
