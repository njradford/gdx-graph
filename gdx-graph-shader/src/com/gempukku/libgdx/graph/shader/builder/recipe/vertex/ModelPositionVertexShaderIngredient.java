package com.gempukku.libgdx.graph.shader.builder.recipe.vertex;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.ModelsUniformSetters;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.recipe.GraphShaderRecipeIngredient;
import com.gempukku.libgdx.graph.shader.builder.recipe.source.FieldOutputSource;
import com.gempukku.libgdx.graph.shader.node.GraphShaderNodeBuilder;

public class ModelPositionVertexShaderIngredient implements GraphShaderRecipeIngredient {
    private final FieldOutputSource positionSource;

    public ModelPositionVertexShaderIngredient(FieldOutputSource positionSource) {
        this.positionSource = positionSource;
    }

    @Override
    public void processIngredient(
            boolean designTime, GraphWithProperties graph, GraphShader graphShader,
            VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder,
            GraphShaderOutputResolver outputResolver, FileHandleResolver assetResolver) {
        GraphShaderNodeBuilder.FieldOutput positionField = positionSource.resolveOutput(outputResolver);

        String positionType = positionSource.resolveNode(graph).getData().getString("positionType", "World space");
        if (positionType.equals("World space")) {
            vertexShaderBuilder.addMainLine("vec3 positionWorld = " + positionField.getRepresentation() + ";");
        } else if (positionType.equals("Object space")) {
            vertexShaderBuilder.addUniformVariable("u_worldTrans", "mat4", false, ModelsUniformSetters.worldTrans,
                    "Model to world transformation");
            vertexShaderBuilder.addMainLine("vec3 positionWorld = (u_worldTrans * vec4(" + positionField.getRepresentation() + ", 1.0)).xyz;");
        }

        vertexShaderBuilder.addVaryingVariable("v_position_world", "vec3");
        vertexShaderBuilder.addMainLine("v_position_world = positionWorld;");

        vertexShaderBuilder.addUniformVariable("u_projViewTrans", "mat4", true, UniformSetters.projViewTrans,
                "Project view transformation");
        vertexShaderBuilder.addMainLine("// End Graph Node");
        vertexShaderBuilder.addMainLine("gl_Position = u_projViewTrans * vec4(positionWorld, 1.0);");
    }
}