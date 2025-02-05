package com.gempukku.libgdx.graph.plugin.lighting3d.design;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.depth.DepthShaderGraphType;
import com.gempukku.libgdx.graph.ui.graph.GraphTemplate;
import com.gempukku.libgdx.graph.ui.graph.UIGraphConfiguration;
import com.gempukku.libgdx.graph.ui.graph.UIGraphType;

public class UIDepthShaderGraphType extends DepthShaderGraphType implements UIGraphType {
    private UIGraphConfiguration[] configurations;

    public UIDepthShaderGraphType() {
        configurations = new UIGraphConfiguration[]{
                new UIModelShaderConfiguration(),
                new UIShadowShaderConfiguration()};
    }

    @Override
    public String getFileExtension() {
        return "sds";
    }

    @Override
    public String getPresentableName() {
        return "Shadow shader";
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

    @Override
    public Iterable<? extends GraphTemplate> getGraphTemplates() {
        return ShadowTemplateRegistry.getTemplates();
    }

    @Override
    public UIGraphConfiguration[] getUIConfigurations() {
        return configurations;
    }
}
