package dev.asper.common.feature;


import dev.asper.common.fsm.FeaturedState;
import dev.asper.common.fsm.Stateful;

import java.util.function.Function;

public class QuantitativeFeature<S extends Stateful<? extends FeaturedState<S>>> extends Feature<Number, S> {
    public QuantitativeFeature(Enum<?> name, Function<S, Number> func) {
        super(name, FeatureType.QUANTITATIVE, s -> new Descriptor<>(name, func.apply(s)));
    }
}
