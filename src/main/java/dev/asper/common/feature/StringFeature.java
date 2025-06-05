package dev.asper.common.feature;


import dev.asper.common.fsm.FeaturedState;
import dev.asper.common.fsm.Stateful;

import java.util.function.Function;

public class StringFeature<S extends Stateful<? extends FeaturedState<S>>> extends Feature<String, S> {
    public StringFeature(Enum<?> name, Function<S, String> func) {
        super(name, FeatureType.CATEGORICAL, s -> new Descriptor<>(name, func.apply(s)));
    }
}
