package com.gempukku.libgdx.graph.render.postprocess.producer;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.pipeline.field.PipelineFieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.graph.data.GraphNodeOutputSide;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class GaussianBlurPipelineNodeConfiguration extends DefaultMenuNodeConfiguration {
    public GaussianBlurPipelineNodeConfiguration() {
        super("GaussianBlur", "Gaussian blur", "Post-processing");
        addNodeInput(
                new DefaultGraphNodeInput("enabled", "Enabled", false, PipelineFieldType.Boolean));
        addNodeInput(
                new DefaultGraphNodeInput("blurRadius", "Radius", PipelineFieldType.Float));
        addNodeInput(
                new DefaultGraphNodeInput("input", "Input", true, GraphNodeInputSide.Top, PipelineFieldType.RenderPipeline));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Output", GraphNodeOutputSide.Bottom, PipelineFieldType.RenderPipeline));
    }
}
