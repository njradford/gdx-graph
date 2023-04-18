package com.gempukku.libgdx.graph.plugin.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.shader.GraphShaderBuilder;
import com.gempukku.libgdx.graph.shader.config.GraphConfiguration;

public class ScreenShaderLoader {
    public static ScreenGraphShader loadShader(JsonValue jsonGraph, String tag, Texture defaultTexture,
                                               final GraphConfiguration... graphConfiguration) {
        GraphType graphType = GraphTypeRegistry.findGraphType("Screen_Shader");

        GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), jsonGraph);

        if (graphType.getGraphValidator().validateGraph(graph).hasErrors())
            throw new GdxRuntimeException("Unable to load graph - not valid, open it in graph designer and fix it");

        return GraphShaderBuilder.buildScreenShader(tag, defaultTexture, graph, false);
    }
}