package com.gempukku.libgdx.graph.pipeline.config.math.geometric;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.SameTypeOutputTypeFunction;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class NormalizePipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public NormalizePipelineNodeConfiguration() {
        super("Normalize", "Normalize", "Math/Geometric");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new SameTypeOutputTypeFunction("input"),
                        PipelineFieldType.Float, PipelineFieldType.Vector2, PipelineFieldType.Vector3, PipelineFieldType.Color));
    }
}
