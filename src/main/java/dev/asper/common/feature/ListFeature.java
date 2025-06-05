package dev.asper.common.feature;


import dev.asper.common.fsm.FeaturedState;
import dev.asper.common.fsm.Stateful;

import java.util.List;
import java.util.function.Function;

public class ListFeature<S extends Stateful<? extends FeaturedState<S>>> extends Feature<List<?>, S> {
    public ListFeature(Enum<?> name, Function<S, List<?>> func) {
        super(name, FeatureType.CATEGORICAL, s -> new Descriptor<>(name, func.apply(s)));
    }
}
