package com.gempukku.libgdx.graph.pipeline.config.math.common;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.MathCommonOutputTypeFunction;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class StepPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public StepPipelineNodeConfiguration() {
        super("Step", "Step", "Math/Common");
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("edge", "Edge", true, PipelineFieldType.Color, PipelineFieldType.Vector3, PipelineFieldType.Vector2, PipelineFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new MathCommonOutputTypeFunction(PipelineFieldType.Float, new String[]{"input"}, new String[]{"edge"}),
                        PipelineFieldType.Float, PipelineFieldType.Vector2, PipelineFieldType.Vector3, PipelineFieldType.Color));
    }
}
