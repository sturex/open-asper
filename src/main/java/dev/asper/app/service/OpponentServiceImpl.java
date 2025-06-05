package dev.asper.app.service;

import dev.asper.app.component.PlayersRepository;
import dev.asper.clickhouse.OpponentType;
import dev.asper.poker.enums.BoardSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpponentServiceImpl implements OpponentService {

    private final PlayersRepository playersRepository;
    private final StatisticsService statisticsService;

    @Autowired
    public OpponentServiceImpl(PlayersRepository playersRepository,
                               StatisticsService statisticsService) {
        this.playersRepository = playersRepository;
        this.statisticsService = statisticsService;
    }

    @Override
    public OpponentType resolveOpponentType(String playerName, BoardSize boardSize, double vpip) {
        //TODO 6 max cash only for now
        if (vpip <= 0.20) {
            return OpponentType.CASH_REG_BS_6_VPIP_0_20;
        } else if (vpip <= 0.25) {
            return OpponentType.CASH_REG_BS_6_VPIP_20_25;
        } else if (vpip <= 0.30) {
            return OpponentType.CASH_REG_BS_6_VPIP_25_30;
        } else if (vpip <= 0.40) {
            return OpponentType.CASH_REG_BS_6_VPIP_30_40;
        } else {
            return OpponentType.CASH_REG_BS_6_VPIP_40PLUS;
        }
    }
}
