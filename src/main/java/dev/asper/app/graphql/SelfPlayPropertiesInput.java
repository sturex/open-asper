package dev.asper.app.graphql;

import dev.asper.poker.enums.BoardSize;

import java.util.List;

public record SelfPlayPropertiesInput(List<BoardSize> boardSizes,
                                      int bbAmount,
                                      int maxInitialStack,
                                      int minInitialStack,
                                      int batchSize,
                                      int batchCount) {
}
