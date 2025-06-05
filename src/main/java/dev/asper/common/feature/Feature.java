package dev.asper.common.feature;


import dev.asper.common.fsm.FeaturedState;
import dev.asper.common.fsm.Stateful;

import java.util.function.Function;

public class Feature<E, S extends Stateful<? extends FeaturedState<S>>> {

    private final Enum<?> name;

    private final FeatureType featureType;

    private final Function<S, Descriptor<E>> descriptorFunc;

    public Feature(Enum<?> name, FeatureType featureType, Function<S, Descriptor<E>> descriptorFunc) {
        this.name = name;
        this.featureType = featureType;
        this.descriptorFunc = descriptorFunc;
    }

    public Enum<?> name() {
        return name;
    }

    public Descriptor<E> descriptor(S stateful) {
        return descriptorFunc.apply(stateful);
    }

    public E value(S stateful) {
        return descriptorFunc.apply(stateful).value();
    }

    public FeatureType featureType() {
        return featureType;
    }

    @SuppressWarnings("unchecked")
    public Descriptor<E> from(Object obj) {
        return new Descriptor<>(name, (E) obj);
    }
}
