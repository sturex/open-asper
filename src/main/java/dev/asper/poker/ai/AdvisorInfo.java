package dev.asper.poker.ai;


import dev.asper.advice.ActionTweak;
import dev.asper.advice.AmountTweak;
import dev.asper.app.graphql.SpotInfo;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public record AdvisorInfo(
        SpotInfo spotInfo,
        String actionAdvisorName,
        ActionAdvisorType actionAdvisorType,
        ActionTweak actionTweak,
        String amountAdvisorName,
        AmountAdvisorType amountAdvisorType,
        AmountTweak amountTweak
) implements Serializable {
}