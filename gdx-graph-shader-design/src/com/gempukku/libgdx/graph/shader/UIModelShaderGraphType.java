package com.gempukku.libgdx.graph.shader;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class UIModelShaderGraphType extends ModelShaderGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;
    private Drawable icon;

    public UIModelShaderGraphType(Drawable icon) {
        this.icon = icon;
        configurations = new UIGraphConfiguration[]{
                new UIModelShaderConfiguration()};
    }

    @Override
    public String getFileExtension() {
        return "mds";
    }

    @Override
    public String getPresentableName() {
        return "Model shader";
    }

    @Override
    public Drawable getIcon() {
        return icon;
    }

    @Override
    public Iterable<? extends GraphTemplate> getGraphTemplates() {
        return ModelsTemplateRegistry.getTemplates();
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}
