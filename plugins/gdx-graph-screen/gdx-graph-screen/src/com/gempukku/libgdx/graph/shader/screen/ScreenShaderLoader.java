package com.gempukku.libgdx.graph.shader.screen;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;
import com.gempukku.libgdx.graph.GraphType;
import com.gempukku.libgdx.graph.GraphTypeRegistry;
import com.gempukku.libgdx.graph.data.GraphWithProperties;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.builder.GraphShaderBuilder;

public class ScreenShaderLoader {
    public static GraphShader loadShader(FileHandle graphFile, String tag) {
        return loadShader(graphFile, tag, new InternalFileHandleResolver());
    }

    public static GraphShader loadShader(FileHandle graphFile, String tag, FileHandleResolver assetResolver) {
        GraphType graphType = GraphTypeRegistry.findGraphType(ScreenShaderGraphType.TYPE);

        GraphWithProperties graph = GraphLoader.loadGraph(graphType.getType(), new JsonReader().parse(graphFile));

        if (graphType.getGraphValidator().validateGraph(graph, graphType.getStartNodeIdForValidation()).hasErrors())
            throw new GdxRuntimeException("Unable to load graph - not valid, open it in graph designer and fix it");

        return GraphShaderBuilder.buildScreenShader(tag, assetResolver, graph, false);
    }
}