package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public interface ShaderPropertySource {
    String getAttributeName();

    String getAttributeName(int index);

    String getVariableName();

    String getVariableName(int index);

    String getUniformName();

    int getPropertyIndex();

    String getPropertyName();

    ShaderFieldType getShaderFieldType();

    PropertyLocation getPropertyLocation();

    String getAttributeFunction();

    Object getValueToUse(Object givenValue);

    boolean isDefiningAttribute(String attributeName);
}
