package com.gempukku.libgdx.graph.shader.common.noise;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.common.noise.VoronoiBorder2DNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class VoronoiBorder2DShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public VoronoiBorder2DShaderNodeBuilder() {
        super(new VoronoiBorder2DNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs,
                                                                       CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        FieldOutput uvValue = inputs.get("uv");
        FieldOutput scaleValue = inputs.get("scale");
        FieldOutput progressValue = inputs.get("progress");

        String scale = (scaleValue != null) ? scaleValue.getRepresentation() : "1.0";
        String progress = (progressValue != null) ? progressValue.getRepresentation() : "0.0";

        loadFragmentIfNotDefined(commonShaderBuilder, configuration, "voronoiBorder2d");

        commonShaderBuilder.addMainLine("// Voronoi border 2D node");
        String name = "result_" + nodeId;
        String output;
        if (uvValue.getFieldType().getName().equals(ShaderFieldType.Vector2)) {
            output = "voronoiBorder2d(" + uvValue.getRepresentation() + " * " + scale + ", " + progress + ")";
        } else {
            output = "voronoiBorder2d(vec2(" + uvValue.getRepresentation() + ", 0.0) * " + scale + ", " + progress + ")";
        }

        commonShaderBuilder.addMainLine("float " + name + " = " + output + ";");

        return LibGDXCollections.mapWithOne("output", new DefaultFieldOutput(ShaderFieldType.Float, name));
    }
}
