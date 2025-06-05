package dev.asper.spark;

import dev.asper.app.entity.Model;
import org.apache.spark.ml.PipelineModel;

public record PipelineInfo(Model model, PipelineModel pipelineModel) {
}
