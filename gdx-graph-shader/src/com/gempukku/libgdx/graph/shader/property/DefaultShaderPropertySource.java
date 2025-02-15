package com.gempukku.libgdx.graph.shader.property;

import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class DefaultShaderPropertySource implements ShaderPropertySource {
    private final int propertyIndex;
    private final String propertyName;
    private final ShaderFieldType shaderFieldType;
    private final PropertyLocation location;
    private final String attributeFunction;
    private final Object defaultValue;

    public DefaultShaderPropertySource(int propertyIndex, String propertyName, ShaderFieldType shaderFieldType,
                                       PropertyLocation location, String attributeFunction, Object defaultValue) {
        this.propertyIndex = propertyIndex;
        this.propertyName = propertyName;
        this.shaderFieldType = shaderFieldType;
        this.location = location;
        this.attributeFunction = attributeFunction;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getAttributeName() {
        if (location == PropertyLocation.Attribute) {
            return "a_property_" + propertyIndex;
        }
        return null;
    }

    @Override
    public String getAttributeName(int index) {
        if (location == PropertyLocation.Attribute) {
            return "a_property_" + propertyIndex + "_" + index;
        }
        return null;
    }

    @Override
    public String getVariableName() {
        return "v_property_" + propertyIndex;
    }

    @Override
    public String getVariableName(int index) {
        return "v_property_" + propertyIndex + "_" + index;
    }

    @Override
    public String getUniformName() {
        return "u_property_" + propertyIndex;
    }

    @Override
    public int getPropertyIndex() {
        return propertyIndex;
    }

    @Override
    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public ShaderFieldType getShaderFieldType() {
        return shaderFieldType;
    }

    @Override
    public PropertyLocation getPropertyLocation() {
        return location;
    }

    @Override
    public String getAttributeFunction() {
        return attributeFunction;
    }

    @Override
    public Object getValueToUse(Object givenValue) {
        if (!shaderFieldType.accepts(givenValue))
            return defaultValue;
        else
            return shaderFieldType.convert(givenValue);
    }

    @Override
    public boolean isDefiningAttribute(String attributeName) {
        String thisName = getAttributeName();
        return attributeName.equals(thisName) || attributeName.startsWith(thisName + "_");
    }
}
