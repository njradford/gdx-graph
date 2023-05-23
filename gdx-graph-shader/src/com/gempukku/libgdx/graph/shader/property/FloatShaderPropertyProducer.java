package com.gempukku.libgdx.graph.shader.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.shader.field.FloatShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;

public class FloatShaderPropertyProducer implements GraphShaderPropertyProducer {
    private final ShaderFieldType type = new FloatShaderFieldType();

    @Override
    public ShaderFieldType getType() {
        return type;
    }

    @Override
    public ShaderPropertySource createProperty(int index, String name, JsonValue data, PropertyLocation location, String attributeFunction, boolean designTime) {
        return new DefaultShaderPropertySource(index, name, type, location, attributeFunction, type.convertFromJson(data));
    }
}