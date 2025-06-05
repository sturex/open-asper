package dev.asper.poker.engine.spot;

import dev.asper.clickhouse.OpponentType;
import dev.asper.poker.engine.*;
import dev.asper.poker.enums.Street;
import dev.asper.poker.holding.ComboExType;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public enum Spots {
    ;

    public static final List<Spot> spots = stream().toList();
    public static final List<? extends Spot> pfSpots = Arrays.stream(PreflopSpot.values()).toList();
    public static final List<? extends Spot> flSpots = Arrays.stream(FlopSpot.values()).toList();
    public static final List<? extends Spot> tnSpots = Arrays.stream(TurnSpot.values()).toList();
    public static final List<? extends Spot> rvSpots = Arrays.stream(RiverSpot.values()).toList();

    public static Stream<Spot> stream() {
        return Stream.of(Arrays.stream(PreflopSpot.values()),
                        Arrays.stream(FlopSpot.values()),
                        Arrays.stream(TurnSpot.values()),
                        Arrays.stream(RiverSpot.values()))
                .flatMap(Function.identity());
    }

    public static String strFrom(PokerGame pokerGame) {
        PokerSituation pokerSituation = pokerGame.getState();
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        Street street = pokerSituation.getStreet();
        if (street == Street.PF) {
            if (PokerSituation.mainVpipSituations.contains(pokerSituation)) {
                return PreflopSpot.preflopSpotStr(pokerSituation, EffStackType.computePrecise((double) hero.getStackStub() / (double) pokerGame.getBbAmount()));
            } else {
                return PreflopSpot.preflopSpotStr(pokerSituation);
            }
        } else {
            return postflopSpotStr(pokerSituation, hero.getHolding(street).comboStatus().comboExType());
        }
    }

    // TODO refactor. This is ugly
    public static Spot fallbackSpot(Spot spot) {
        String spotName = spot.name();
        Street street = Street.valueOf(spotName.substring(0, 2));
        return switch (street) {
            case FL, TN, RV -> spot;
            case PF -> {
                PokerSituation fallback = spot.pokerSituation().fallback();
                if (PokerSituation.pfFirstSituations.contains(fallback) || PokerSituation.vsOpenRaiserSituations.contains(fallback)) {
                    yield PreflopSpot.preflopSpot(fallback, EffStackType.BB_40_PLUS);
                } else if (PokerSituation.mainVpipSituations.contains(fallback)) {
                    yield PreflopSpot.preflopSpot(fallback, EffStackType.BB_40_PLUS);
                } else {
                    yield PreflopSpot.preflopSpot(fallback);
                }
            }
        };
    }

    public static Spot from(PokerGame pokerGame) {
        PokerSituation pokerSituation = pokerGame.getState();
        Street street = pokerSituation.getStreet();
        PokerPlayer hero = pokerGame.getNextPlayerOrThrow();
        return switch (street) {
            case PF -> {
                if (PokerSituation.mainVpipSituations.contains(pokerSituation)) {
                    EffStackType effStackType = EffStackType.computePrecise((double) hero.getStackStub() / (double) pokerGame.getBbAmount());
                    yield PreflopSpot.preflopSpot(pokerSituation, effStackType);
                } else {
                    yield PreflopSpot.preflopSpot(pokerSituation);
                }
            }
            case FL -> FlopSpot.valueOf(postflopSpotStr(pokerSituation, hero.getHolding(street).comboStatus().comboExType()));
            case TN -> TurnSpot.valueOf(postflopSpotStr(pokerSituation, hero.getHolding(street).comboStatus().comboExType()));
            case RV -> RiverSpot.valueOf(postflopSpotStr(pokerSituation, hero.getHolding(street).comboStatus().comboExType()));
        };
    }

    public static String postflopSpotStr(PokerSituation pokerSituation, ComboExType comboExType) {
        return pokerSituation + "_" + comboExType;
    }

    private static Spot postflopSpot(PokerSituation pokerSituation, ComboExType comboExType) {
        Street street = pokerSituation.getStreet();
        return switch (street) {
            case FL -> FlopSpot.valueOf(postflopSpotStr(pokerSituation, comboExType));
            case TN -> TurnSpot.valueOf(postflopSpotStr(pokerSituation, comboExType));
            case RV -> RiverSpot.valueOf(postflopSpotStr(pokerSituation, comboExType));
            default -> throw new IllegalStateException("Unexpected value: " + street);
        };
    }

    public static Spot postflopSpot(String value) {
        Street street = Street.valueOf(value.substring(0, 2));
        return switch (street) {
            case FL -> FlopSpot.valueOf(value);
            case TN -> TurnSpot.valueOf(value);
            case RV -> RiverSpot.valueOf(value);
            default -> throw new IllegalStateException("Unexpected value: " + street);
        };
    }

    public static Spot fromStr(String value) {
        Street street = Street.valueOf(value.substring(0, 2));
        return switch (street) {
            case PF -> PreflopSpot.valueOf(value);
            case FL -> FlopSpot.valueOf(value);
            case TN -> TurnSpot.valueOf(value);
            case RV -> RiverSpot.valueOf(value);
        };
    }

    public static List<? extends Spot> spots(Street street) {
        return switch (street) {
            case PF -> pfSpots;
            case FL -> flSpots;
            case TN -> tnSpots;
            case RV -> rvSpots;
        };
    }
}
