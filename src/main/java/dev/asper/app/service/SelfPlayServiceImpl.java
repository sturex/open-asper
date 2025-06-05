package dev.asper.app.service;

import com.clickhouse.jdbc.ClickHouseDataSource;
import dev.asper.advice.Advice;
import dev.asper.advice.AdviceCorrector;
import dev.asper.advice.Decision;
import dev.asper.app.graphql.SelfPlayPropertiesInput;
import dev.asper.app.graphql.SolutionAliasInput;
import dev.asper.app.service.log.LogRecord;
import dev.asper.app.service.log.LogRecordCode;
import dev.asper.app.service.log.LogService;
import dev.asper.clickhouse.OpponentType;
import dev.asper.poker.ai.AdvisedGame;
import dev.asper.poker.card.CardPair;
import dev.asper.poker.card.Cards;
import dev.asper.poker.card.Deck;
import dev.asper.poker.engine.*;
import dev.asper.poker.enums.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class SelfPlayServiceImpl implements SelfPlayService {
    private static final int CLICKHOUSE_BATCH_SIZE = 100000;
    private static final String PREFLOP_POSITION_KEY = "PREFLOP_POSITION_KEY";
    private final LogService logService;
    private final JdbcTemplate clickhouseJdbcTemplate;
    private final Connection clickHouseConnection;
    private final AdviceService adviceService;
    private final PokerEngine pokerEngine = new PokerEngine();
    private final Random random = new Random();

    private record GameHand(PokerGame pokerGame, PokerHand pokerHand) {
    }

    @Autowired
    public SelfPlayServiceImpl(LogService logService,
                               AdviceService adviceService,
                               ClickHouseDataSource clickHouseDataSource) throws SQLException {
        this.logService = logService;
        this.adviceService = adviceService;
        clickHouseConnection = clickHouseDataSource.getConnection();
        this.clickhouseJdbcTemplate = new JdbcTemplate(clickHouseDataSource);
    }

    @Override
    public List<PokerGame> play(Set<SolutionAliasInput> solutionAliasInputs,
                                SelfPlayPropertiesInput selfPlayPropertiesInput) {
        Map<String, OpponentType> oppTypeByAlias = solutionAliasInputs.stream().collect(Collectors.toMap(SolutionAliasInput::alias, SolutionAliasInput::opponentType));
        Map<String, String> solutionNameAdviceByAlias = solutionAliasInputs.stream().collect(Collectors.toMap(SolutionAliasInput::alias, SolutionAliasInput::solutionNameAdvice));
        List<SolutionAliasInput> mandatoryPlayers = solutionAliasInputs.stream().filter(SolutionAliasInput::isMandatory).collect(Collectors.toList());
        List<SolutionAliasInput> restPlayers = solutionAliasInputs.stream().filter(solutionAliasInput -> !solutionAliasInput.isMandatory()).collect(Collectors.toList());
        Map<String, String> specialSpotCollectionNameByAlias = solutionAliasInputs.stream().collect(Collectors.toMap(SolutionAliasInput::alias, SolutionAliasInput::specialSpotTweakCollectionName));
        int handCount = selfPlayPropertiesInput.batchSize();
        List<BoardSize> boardSizes = selfPlayPropertiesInput.boardSizes();
        int bbAmount = selfPlayPropertiesInput.bbAmount();
        List<GameHand> gameHands = IntStream.range(0, handCount)
                .mapToObj(idx -> {
                    Deck deck = Deck.shuffled();
                    Collections.shuffle(mandatoryPlayers);
                    Collections.shuffle(restPlayers);
                    List<SolutionAliasInput> aliasInputs = Stream.of(mandatoryPlayers, restPlayers).flatMap(Collection::stream).distinct().collect(Collectors.toList());
                    BoardSize boardSize = boardSizes.get(random.nextInt(boardSizes.size()));
                    int tableSize = boardSize.size();
                    List<SolutionAliasInput> solutionAliasInput = aliasInputs.subList(0, tableSize);
                    Collections.shuffle(solutionAliasInput);
                    EnumMap<PreflopPosition, PlayerInitialState> shuffledInitialStatesMap = createShuffledInitialStatesMap(bbAmount,
                            boardSize,
                            deck,
                            solutionAliasInput,
                            selfPlayPropertiesInput.minInitialStack(),
                            selfPlayPropertiesInput.maxInitialStack());
                    EnumMap<PreflopPosition, PokerPlayer> playersByPosition = shuffledInitialStatesMap.entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey,
                                    e -> new PokerPlayer(e.getValue().pocketCards(), e.getValue().initialStack(), e.getKey(), e.getValue().playerName(), oppTypeByAlias.get(e.getValue().playerName())),
                                    (o, o2) -> o,
                                    () -> new EnumMap<>(PreflopPosition.class)));
                    PokerGame pokerGame = new PokerGame(playersByPosition,
                            UUID.randomUUID().toString(),
                            Timestamp.from(Instant.now()).toString(),
                            bbAmount / 2,
                            bbAmount,
                            CompetitionType.CASH, "");
                    ArrayList<Move> pfMoves = new ArrayList<>();
                    PokerHand pokerHand = new PokerHand(
                            UUID.randomUUID().toString(),
                            Timestamp.from(Instant.now()).toString(),
                            bbAmount / 2,
                            bbAmount,
                            shuffledInitialStatesMap,
                            pfMoves,
                            new ArrayList<>(),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            new Cards(deck.pick(), deck.pick(), deck.pick()),
                            deck.pick(),
                            deck.pick(),
                            new EnumMap<>(PreflopPosition.class),
                            CompetitionType.CASH,
                            ""
                    );
                    PokerSituation pokerSituation = switch (pokerGame.getBoardSize()) {
                        case HEADS_UP -> PokerSituation.PF_HU_SB_FIRST;
                        case SIZE_3 -> PokerSituation.PF_FIRST_BTN;
                        case SIZE_4 -> PokerSituation.PF_FIRST_CO;
                        case SIZE_5 -> PokerSituation.PF_FIRST_VS_4_CALLERS;
                        case SIZE_6 -> PokerSituation.PF_FIRST_VS_5_CALLERS;
                        case SIZE_7 -> PokerSituation.PF_FIRST_VS_6_CALLERS;
                        case SIZE_8 -> PokerSituation.PF_FIRST_VS_7_CALLERS;
                        case SIZE_9 -> PokerSituation.PF_FIRST_VS_8_CALLERS;
                        case SIZE_10 -> PokerSituation.PF_FIRST_VS_9_CALLERS;
                    };
                    pokerGame.setState(pokerSituation);
                    return new GameHand(pokerGame, pokerHand);
                }).toList();
        Map<String, GameHand> gamesById = gameHands.stream().collect(Collectors.toMap(g -> g.pokerGame.getHandId(), Function.identity()));
        List<GameHand> incompletePokerGames = gameHands;
        Instant started = Instant.now();
        while (!incompletePokerGames.isEmpty()) {
            logService.log(started, LogRecord.supply(LogRecordCode.SELFPLAY_CLOCK, Map.of("incompletePokerGamesSize", String.valueOf(incompletePokerGames.size()))));
            try {
                incompletePokerGames = singleDecision(solutionNameAdviceByAlias, gamesById, incompletePokerGames, specialSpotCollectionNameByAlias);
            } catch (Exception e) {
                incompletePokerGames.forEach(gameHand -> gameHand.pokerGame.setState(PokerSituation.GAME_BROKEN));
            }
        }
        List<PokerGame> pokerGames = gameHands.stream().map(gameHand -> gameHand.pokerGame)
                .filter(pokerGame -> pokerGame.getState() != PokerSituation.GAME_BROKEN)
                .filter(PokerGame::isValid)
                .collect(Collectors.toList());
        Collections.shuffle(pokerGames);
        return pokerGames;
    }

    private List<GameHand> singleDecision(Map<String, String> solutionNameAdviceByAlias,
                                          Map<String, GameHand> gamesById,
                                          List<GameHand> incompleteGames,
                                          Map<String, String> specialSpotCollectionNameByAlias) {
        Map<String, List<PokerGame>> incompleteGamesBySolutionAdvice = incompleteGames.stream().map(gwd -> gwd.pokerGame)
                .collect(Collectors.groupingBy(pokerGame -> solutionNameAdviceByAlias.get(pokerGame.getNextPlayerOrThrow().getPlayerName()), Collectors.toList()));
        List<AdvisedGame> advisedGames = incompleteGamesBySolutionAdvice.entrySet().parallelStream()
                .map(e -> {
                    List<PokerGame> pokerGames = e.getValue();
                    Map<PokerSituation, List<PokerGame>> pokerGamesBySituation = pokerGames.stream().collect(Collectors.groupingBy(PokerGame::getState));
                    List<PokerGame> decisionMakingGames = pokerGamesBySituation.values().stream()
                            .flatMap(Collection::stream)
                            .filter(pokerGame -> PokerSituation.allSituations.contains(pokerGame.getState()))
                            .toList();
                    Map<String, List<PokerGame>> pokerGamesBySpecialSpotTweak = decisionMakingGames.stream()
                            .collect(Collectors.groupingBy(pg -> specialSpotCollectionNameByAlias.get(pg.getNextPlayerOrThrow().getPlayerName())));
                    pokerGamesBySituation.getOrDefault(PokerSituation.PF_COMPLETED, Collections.emptyList()).parallelStream()
                            .forEach(pokerGame -> {
                                pokerEngine.apply(pokerGame, PokerGameEventType.FL_CARDS, 0, gamesById.get(pokerGame.getHandId()).pokerHand);
                            });
                    pokerGamesBySituation.getOrDefault(PokerSituation.FL_COMPLETED, Collections.emptyList()).parallelStream()
                            .forEach(pokerGame -> {
                                pokerEngine.apply(pokerGame, PokerGameEventType.TN_CARD, 0, gamesById.get(pokerGame.getHandId()).pokerHand);
                            });
                    pokerGamesBySituation.getOrDefault(PokerSituation.TN_COMPLETED, Collections.emptyList()).parallelStream()
                            .forEach(pokerGame -> {
                                pokerEngine.apply(pokerGame, PokerGameEventType.RV_CARD, 0, gamesById.get(pokerGame.getHandId()).pokerHand);
                            });
                    pokerGamesBySituation.getOrDefault(PokerSituation.RV_COMPLETED, Collections.emptyList()).parallelStream()
                            .forEach(pokerGame -> {
                                pokerGame.calculateAndDistributeCollectedAmounts();
                                pokerEngine.apply(pokerGame, PokerGameEventType.END, 0, gamesById.get(pokerGame.getHandId()).pokerHand);
                            });
                    String solutionName = e.getKey();
                    return pokerGamesBySpecialSpotTweak.entrySet().stream()
                            .map(de -> {
                                List<PokerGame> pgs = de.getValue();
                                String specialSpotCollectionName = de.getKey();
                                if (solutionName.equals("CS")) {
                                    return pokerGames.stream().map(pokerGame -> {
                                                Street street = pokerGame.getState().getStreet();
                                                int size = pokerGame.getPlayers().size();
                                                //TODO remove after pf counter strategy
                                                if (street != Street.PF && size == 2) {
                                                    try {
                                                        Advice advice = adviceService.adviseCS(pokerGame, CounterStrategyType.NO_ALLIN_REDUCING_RAISE_SIZE);
                                                        return List.of(new AdvisedGame(pokerGame, advice));
                                                    } catch (Exception ex) {
                                                        pokerGame.setState(PokerSituation.GAME_BROKEN);
                                                        return List.of(new AdvisedGame(pokerGame, Advice.fAdvice()));
                                                    }
                                                } else {
                                                    //TODO remove patch
                                                    return adviceService.advices(List.of(pokerGame), "CHART_SOLUTION_OVERALL_CASH_BS6", specialSpotCollectionName);
                                                }
                                            })
                                            .flatMap(Collection::stream)
                                            .filter(advisedGame -> advisedGame.pokerGame().getState() != PokerSituation.GAME_BROKEN)
                                            .collect(Collectors.toList());
                                } else {
                                    return adviceService.advices(pgs, solutionName, specialSpotCollectionName);
                                }
                            }).flatMap(Collection::stream).toList();
                })
                .flatMap(Collection::parallelStream)
                .toList();
        advisedGames.parallelStream()
                .forEach(advisedGame -> {
                    String handId = advisedGame.pokerGame().getHandId();
                    GameHand gameHand = gamesById.get(handId);
                    PokerGame pokerGame = gameHand.pokerGame;
                    PokerHand pokerHand = gameHand.pokerHand;
                    PreflopPosition preflopPosition = pokerGame.getNextPlayerOrThrow().getPreflopPosition();
                    Advice advice = AdviceCorrector.correct(advisedGame.advice(), advisedGame.pokerGame());
                    Move move = new Move(preflopPosition, new Decision(advice.action(), advice.amount()));
                    Street currentStreet = pokerGame.getState().getStreet();
                    switch (currentStreet) {
                        case PF -> {
                            List<Move> moves = pokerHand.pfMoves();
                            moves.add(move);
                            pokerEngine.apply(pokerGame, PokerGameEventType.PF_MOVE, moves.size() - 1, pokerHand);
                        }
                        case FL -> {
                            List<Move> moves = pokerHand.flMoves();
                            moves.add(move);
                            pokerEngine.apply(pokerGame, PokerGameEventType.FL_MOVE, moves.size() - 1, pokerHand);
                        }
                        case TN -> {
                            List<Move> moves = pokerHand.tnMoves();
                            moves.add(move);
                            pokerEngine.apply(pokerGame, PokerGameEventType.TN_MOVE, moves.size() - 1, pokerHand);
                        }
                        case RV -> {
                            List<Move> moves = pokerHand.rvMoves();
                            moves.add(move);
                            pokerEngine.apply(pokerGame, PokerGameEventType.RV_MOVE, moves.size() - 1, pokerHand);
                        }
                    }
                });
        return incompleteGames.parallelStream()
                .peek(gameHand -> {
                    PokerGame pokerGame = gameHand.pokerGame;
                    if (pokerGame.isCompleted()) {
                        PokerSituation pokerSituation = pokerGame.getState();
                        switch (pokerSituation) {
                            case PF_COMPLETED -> {
                                pokerEngine.apply(pokerGame, PokerGameEventType.FL_CARDS, 0, gameHand.pokerHand);
                                pokerEngine.apply(pokerGame, PokerGameEventType.TN_CARD, 0, gameHand.pokerHand);
                                pokerEngine.apply(pokerGame, PokerGameEventType.RV_CARD, 0, gameHand.pokerHand);
                            }
                            case FL_COMPLETED -> {
                                pokerEngine.apply(pokerGame, PokerGameEventType.TN_CARD, 0, gameHand.pokerHand);
                                pokerEngine.apply(pokerGame, PokerGameEventType.RV_CARD, 0, gameHand.pokerHand);
                            }
                            case TN_COMPLETED -> {
                                pokerEngine.apply(pokerGame, PokerGameEventType.RV_CARD, 0, gameHand.pokerHand);
                            }
                        }
                        pokerGame.calculateAndDistributeCollectedAmounts();
                        pokerEngine.apply(pokerGame, PokerGameEventType.END, 0, gameHand.pokerHand);
                    }
                })
                .filter(gameHand -> gameHand.pokerGame.getState() != PokerSituation.GAME_BROKEN)
                .filter(gameHand -> gameHand.pokerGame.getState() != PokerSituation.END)
                .toList();

    }

    private EnumMap<PreflopPosition, PlayerInitialState> createShuffledInitialStatesMap(double bbAmount, BoardSize boardSize, Deck deck, List<SolutionAliasInput> solutionAliasInputs, int minInitialStack, int maxInitialStack) {
        Map<PreflopPosition, SolutionAliasInput> solutionAliasByPosition = new HashMap<>();
        PreflopPosition[] preflopPositions = boardSize.preflopPositions();
        for (int i = 0; i < preflopPositions.length; i++) {
            solutionAliasByPosition.put(preflopPositions[i], solutionAliasInputs.get(i));
        }
        int step = (int) (bbAmount / 2);
        return solutionAliasByPosition.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> {
                            SolutionAliasInput solutionAliasInput = e.getValue();
                            return new PlayerInitialState(solutionAliasInput.alias(),
                                    CardPair.from(deck.pick().stdName() + deck.pick().stdName()),
                                    roundByStep(random.nextInt(minInitialStack, maxInitialStack), step), 0);
                        },
                        (o, o2) -> o,
                        () -> new EnumMap<>(PreflopPosition.class)));
    }

    public static int roundByStep(int input, int step) {
        return (int) (Math.round((double) (input / step)) * step);
    }


}
