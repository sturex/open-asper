package dev.asper.common.feature;



import dev.asper.common.fsm.FeaturedState;
import dev.asper.common.fsm.Stateful;

import java.util.EnumSet;
import java.util.Objects;
import java.util.function.Function;

public class CategoricalFeature<E extends Enum<E>, S extends Stateful<? extends FeaturedState<S>>> extends Feature<E, S> {

    private final EnumSet<E> values;

    public CategoricalFeature(Enum<?> name, Function<S, E> func, EnumSet<E> values) {
        super(name, FeatureType.CATEGORICAL, s -> new Descriptor<>(name, func.apply(s), Objects::toString));
        this.values = values;
    }

    public EnumSet<E> values() {
        return values;
    }
}
