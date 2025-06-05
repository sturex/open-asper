package dev.asper.app.graphql;

import dev.asper.advice.ActionTweak;
import dev.asper.poker.ai.ActionAdvisorType;

public record ActionAdvisorInput(String name,
                                 ActionAdvisorType actionAdvisorType,
                                 ActionTweakInput actionTweakInput) {
}
