package dev.asper.app.graphql;

import dev.asper.advice.ActionTweak;
import dev.asper.advice.ActionTweakStrategy;

public record ActionTweakInput(ActionTweakStrategy actionTweakStrategy,
                               ActionVectorInput actionVectorInput) {
    public ActionTweak toActionTweak(){
        return new ActionTweak(actionTweakStrategy, actionVectorInput.toActionVector());
    }
}
