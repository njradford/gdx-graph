package com.gempukku.libgdx.graph.shader.provided;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.LibGDXCollections;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ModelsUniformSetters;
import com.gempukku.libgdx.graph.shader.builder.CommonShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.provided.ObjectToWorldShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.node.ConfigurationCommonShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class ObjectToWorldShaderNodeBuilder extends ConfigurationCommonShaderNodeBuilder {
    public ObjectToWorldShaderNodeBuilder() {
        super(new ObjectToWorldShaderNodeConfiguration());
    }

    @Override
    protected ObjectMap<String, ? extends FieldOutput> buildCommonNode(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, CommonShaderBuilder commonShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        commonShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, ModelsUniformSetters.worldTrans,
                "Model to world transformation");
        FieldOutput input = inputs.get("input");

        String resultName = "result_" + nodeId;
        commonShaderBuilder.addMainLine("// Object to World Node");
        commonShaderBuilder.addMainLine("vec3 " + resultName + " = (u_worldTrans * vec4(" + input.getRepresentation() + ", 1.0)).xyz;");

        return LibGDXCollections.mapWithOne("output", new DefaultFieldOutput(ShaderFieldType.Vector3, resultName));
    }
}
