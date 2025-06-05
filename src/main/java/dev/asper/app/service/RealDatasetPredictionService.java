package dev.asper.app.service;

import dev.asper.advice.*;
import dev.asper.app.component.PipelineModelRecoverException;
import dev.asper.app.graphql.CardCellActionVector;
import dev.asper.app.graphql.CardCellWeightedAmount;
import dev.asper.poker.ai.ActionAdvisorType;
import dev.asper.poker.ai.AmountAdvisorType;
import dev.asper.poker.engine.spot.Spot;

import java.util.List;

public interface RealDatasetPredictionService {
    List<CardCellActionVector> realDatasetPreflopActionPredictions(String schemaName, ActionAdvisorType actionAdvisorType, String actionAdvisorName, ActionTweak actionTweak, Spot spot, int limit) throws PipelineModelRecoverException;

    List<CardCellWeightedAmount> realDatasetPreflopAmountPredictions(String schemaName, AmountAdvisorType amountAdvisorType, String amountAdvisorName, AmountTweak amountTweak, Spot spot, double minWeightThreshold, int limit) throws PipelineModelRecoverException;

    ActionVector realDatasetPostflopActionPredictions(String schemaName, ActionAdvisorType actionAdvisorType, String actionAdvisorName, ActionTweak actionTweak, Spot spot, int limit) throws PipelineModelRecoverException;

    List<WeightedAmount> realDatasetPostflopAmountPredictions(String schemaName, String amountAdvisorName, Spot spot, double minWeightThreshold, int limit) throws PipelineModelRecoverException;
}
