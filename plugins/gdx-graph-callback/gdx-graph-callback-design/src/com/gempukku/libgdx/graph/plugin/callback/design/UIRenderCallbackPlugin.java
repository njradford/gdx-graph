package com.gempukku.libgdx.graph.plugin.callback.design;

import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.callback.RenderCallbackPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.callback.design.producer.RenderCallbackBoxProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.pipeline.UIRenderPipelineConfiguration;

public class UIRenderCallbackPlugin implements UIGdxGraphPlugin {
    public void initialize() {
        // Register node editors
        UIRenderPipelineConfiguration.register(new RenderCallbackBoxProducer());

        // Register runtime plugin
        RuntimePluginRegistry.register(RenderCallbackPluginRuntimeInitializer.class);
    }
}