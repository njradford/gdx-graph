package com.gempukku.libgdx.graph.render.postprocess.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.Float;
import static com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType.RenderPipeline;

public class BloomPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public BloomPipelineNodeConfiguration() {
        super("Bloom", "Bloom post-processor", "Post-processing");
        addNodeInput(
                new DefaultGraphNodeInput("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new DefaultGraphNodeInput("bloomRadius", "Radius", Float));
        addNodeInput(
                new DefaultGraphNodeInput("minimalBrightness", "Min Brightness", Float));
        addNodeInput(
                new DefaultGraphNodeInput("bloomStrength", "Strength", Float));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, GraphNodeInputSide.Top, RenderPipeline));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", GraphNodeOutputSide.Bottom, RenderPipeline));
    }
}
