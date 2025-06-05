package dev.asper.poker.enums;

import dev.asper.app.graphql.GamePart;

public enum Street {
    PF(PokerGameEventType.PF_MOVE, GamePart.PREFLOP), FL(PokerGameEventType.FL_MOVE, GamePart.PREFLOP), TN(PokerGameEventType.TN_MOVE, GamePart.PREFLOP), RV(PokerGameEventType.RV_MOVE, GamePart.PREFLOP);

    private final PokerGameEventType pokerGameEventType;

    private final GamePart gamePart;

    Street(PokerGameEventType pokerGameEventType, GamePart gamePart) {
        this.pokerGameEventType = pokerGameEventType;
        this.gamePart = gamePart;
    }

    public static Street fromBoardCardsSize(int size) {
        return switch (size) {
            case 0 -> PF;
            case 3 -> FL;
            case 4 -> TN;
            case 5 -> RV;
            default -> throw new IllegalStateException("Unexpected value: " + size);
        };
    }

    public PokerGameEventType getPokerGameEventType() {
        return pokerGameEventType;
    }

    public GamePart getGamePart() {
        return gamePart;
    }

}
