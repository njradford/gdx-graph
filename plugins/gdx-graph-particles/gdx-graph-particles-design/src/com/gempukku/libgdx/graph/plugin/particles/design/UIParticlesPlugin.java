package com.gempukku.libgdx.graph.plugin.particles.design;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.common.Producer;
import com.gempukku.libgdx.graph.plugin.RuntimePluginRegistry;
import com.gempukku.libgdx.graph.plugin.particles.design.generator.PositionParticleGeneratorProducer;
import com.gempukku.libgdx.graph.shader.UIModelShaderConfiguration;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.particles.ParticleAttributeFunctions;
import com.gempukku.libgdx.graph.shader.particles.ParticlesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.shader.particles.config.ParticleLifePercentageShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.particles.config.ParticleLifetimeShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.particles.config.ParticleTimeToDeathShaderNodeConfiguration;
import com.gempukku.libgdx.graph.shader.preview.PreviewRenderableModelProducer;
import com.gempukku.libgdx.graph.ui.UIGdxGraphPlugin;
import com.gempukku.libgdx.graph.ui.graph.GdxGraphNodeEditorProducer;
import com.gempukku.libgdx.graph.util.particles.generator.LinePositionGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.PointPositionGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.SpherePositionGenerator;
import com.gempukku.libgdx.graph.util.particles.generator.SphereSurfacePositionGenerator;

public class UIParticlesPlugin implements UIGdxGraphPlugin {
    public void initialize(FileHandleResolver assetResolver) {
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ParticleLifetimeShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ParticleLifePercentageShaderNodeConfiguration()));
        UIModelShaderConfiguration.register(new GdxGraphNodeEditorProducer(new ParticleTimeToDeathShaderNodeConfiguration()));

        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Float, ParticleAttributeFunctions.ParticleBirth);
        UIModelShaderConfiguration.registerPropertyFunction(ShaderFieldType.Float, ParticleAttributeFunctions.ParticleDeath);

        UIModelShaderConfiguration.registerPreviewModel(
                "Sprite particles", new Producer<PreviewRenderableModelProducer>() {
                    @Override
                    public PreviewRenderableModelProducer create() {
                        return new ParticlePreviewRenderableModelProducer();
                    }
                });

        UIParticlesPluginConfiguration.registerParticleGeneratorProducer(
                "Point", new PositionParticleGeneratorProducer(new PointPositionGenerator()));
        UIParticlesPluginConfiguration.registerParticleGeneratorProducer(
                "Sphere", new PositionParticleGeneratorProducer(new SpherePositionGenerator(0.4f)));
        UIParticlesPluginConfiguration.registerParticleGeneratorProducer(
                "Sphere surface", new PositionParticleGeneratorProducer(new SphereSurfacePositionGenerator(0.4f)));

        UIParticlesPluginConfiguration.registerParticleGeneratorProducer(
                "Line", new PositionParticleGeneratorProducer(new LinePositionGenerator(
                        new Vector3(-0.4f, 0, 0), new Vector3(0.4f, 0, 0))));

        // Register runtime plugin
        RuntimePluginRegistry.register(ParticlesPluginRuntimeInitializer.class);
    }
}
