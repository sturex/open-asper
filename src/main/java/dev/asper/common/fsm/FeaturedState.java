package dev.asper.common.fsm;




import dev.asper.common.feature.Feature;

import java.util.List;

public interface FeaturedState<S extends Stateful<? extends FeaturedState<S>>> {

    List<Feature<?, S>> getServiceFeatures();
    List<Feature<?, S>> getFeatures();
    List<Feature<?, S>> getLabels();
}
