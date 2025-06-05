package dev.asper.common.feature;




import dev.asper.common.fsm.FeaturedState;
import dev.asper.common.fsm.Stateful;

import java.util.function.Function;

public class IntFeature<S extends Stateful<? extends FeaturedState<S>>> extends Feature<Integer, S> {
    public IntFeature(Enum<?> name, Function<S, Integer> func) {
        super(name, FeatureType.QUANTITATIVE, s -> new Descriptor<>(name, func.apply(s)));
    }
}