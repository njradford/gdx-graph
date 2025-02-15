package com.gempukku.libgdx.graph.shader.config.common.math.arithmetic;

import com.gempukku.libgdx.graph.config.DefaultMenuNodeConfiguration;
import com.gempukku.libgdx.graph.config.VectorArithmeticOutputTypeFunction;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.data.GraphNodeInputSide;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeInput;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphNodeOutput;

public class MultiplyShaderNodeConfiguration extends DefaultMenuNodeConfiguration {
    public MultiplyShaderNodeConfiguration() {
        super("Multiply", "Multiply", "Math/Arithmetic");
        addNodeInput(
                new DefaultGraphNodeInput("inputs", "Inputs", true, GraphNodeInputSide.Left, true,
                        ShaderFieldType.Vector4, ShaderFieldType.Vector3, ShaderFieldType.Vector2, ShaderFieldType.Float));
        addNodeOutput(
                new DefaultGraphNodeOutput("output", "Result",
                        new VectorArithmeticOutputTypeFunction(ShaderFieldType.Float, "inputs"),
                        ShaderFieldType.Float, ShaderFieldType.Vector2, ShaderFieldType.Vector3, ShaderFieldType.Vector4));
    }
}
