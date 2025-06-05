package dev.asper.common.feature;




import dev.asper.common.fsm.FeaturedState;
import dev.asper.common.fsm.Stateful;

import java.util.function.Function;

public class DoubleFeature<S extends Stateful<? extends FeaturedState<S>>> extends Feature<Double, S> {
    public DoubleFeature(Enum<?> name, Function<S, Double> func) {
        super(name, FeatureType.QUANTITATIVE, s -> new Descriptor<>(name, func.apply(s)));
    }
}