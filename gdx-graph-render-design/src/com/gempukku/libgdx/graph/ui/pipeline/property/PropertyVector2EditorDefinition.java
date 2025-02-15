package com.gempukku.libgdx.graph.ui.pipeline.property;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.ui.graph.property.DefaultPropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditor;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyEditorDefinition;
import com.gempukku.libgdx.graph.ui.graph.property.PropertyGraphEditorCustomization;
import com.gempukku.libgdx.ui.graph.editor.part.Vector2EditorPart;

public class PropertyVector2EditorDefinition implements PropertyEditorDefinition {
    @Override
    public String getType() {
        return "Vector2";
    }

    @Override
    public String getDefaultName() {
        return "New Vector2";
    }

    @Override
    public Iterable<? extends PropertyGraphEditorCustomization> getCustomizations() {
        return null;
    }

    @Override
    public PropertyEditor createPropertyEditor(String name, JsonValue jsonObject) {
        DefaultPropertyEditor result = new DefaultPropertyEditor(name, getType());
        result.addPropertyEditorPart(new Vector2EditorPart("Vector2", "x", "y", 0, 0, null, null,
                "gdx-graph-property-label", "gdx-graph-property"));
        result.initialize(jsonObject);
        return result;
    }
}
