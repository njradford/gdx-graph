package com.gempukku.libgdx.graph.shader.lighting3d.producer;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.common.SimpleNumberFormatter;
import com.gempukku.libgdx.graph.pipeline.PipelineRendererConfiguration;
import com.gempukku.libgdx.graph.shader.GraphShader;
import com.gempukku.libgdx.graph.shader.UniformSetters;
import com.gempukku.libgdx.graph.shader.builder.FragmentShaderBuilder;
import com.gempukku.libgdx.graph.shader.builder.GLSLFragmentReader;
import com.gempukku.libgdx.graph.shader.builder.VertexShaderBuilder;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldType;
import com.gempukku.libgdx.graph.shader.field.ShaderFieldTypeRegistry;
import com.gempukku.libgdx.graph.shader.lighting3d.LightingRendererConfiguration;
import com.gempukku.libgdx.graph.shader.node.ConfigurationShaderNodeBuilder;
import com.gempukku.libgdx.graph.shader.node.DefaultFieldOutput;

public class ShadowBlinnPhongLightingShaderNodeBuilder extends ConfigurationShaderNodeBuilder {
    public ShadowBlinnPhongLightingShaderNodeBuilder() {
        super(new ShadowBlinnPhongLightingShaderNodeConfiguration());
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildVertexNodeSingleInputs(boolean designTime, String nodeId, JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, GraphShader graphShader, PipelineRendererConfiguration configuration) {
        throw new UnsupportedOperationException("At the moment light calculation is not available in vertex shader");
    }

    @Override
    public ObjectMap<String, ? extends FieldOutput> buildFragmentNodeSingleInputs(boolean designTime, String nodeId, final JsonValue data, ObjectMap<String, FieldOutput> inputs, ObjectSet<String> producedOutputs, VertexShaderBuilder vertexShaderBuilder, FragmentShaderBuilder fragmentShaderBuilder, final GraphShader graphShader, final PipelineRendererConfiguration configuration) {
        final LightingRendererConfiguration lightingConfiguration = configuration.getConfig(LightingRendererConfiguration.class);
        if (lightingConfiguration == null)
            throw new GdxRuntimeException("A configuration with class LightingRendererConfiguration needs to be define for pipeline");

        final String environmentId = data.getString("id", "");

        int maxNumberOfDirectionalLights = lightingConfiguration.getMaxNumberOfDirectionalLights(environmentId, graphShader);
        int maxNumberOfPointLights = lightingConfiguration.getMaxNumberOfPointLights(environmentId, graphShader);
        int maxNumberOfSpotlights = lightingConfiguration.getMaxNumberOfSpotlights(environmentId, graphShader);
        float shadowAcneValue = lightingConfiguration.getShadowAcneValue(environmentId, graphShader);
        int shadowSoftness = lightingConfiguration.getShadowSoftness(environmentId, graphShader);

        fragmentShaderBuilder.addStructure("Lighting",
                "  vec3 diffuse;\n" +
                        "  vec3 specular;\n");

        Lighting3DUtils.configureShadowInformation(fragmentShaderBuilder, nodeId, environmentId, maxNumberOfDirectionalLights, configuration.getPipelineHelper().getWhitePixel().textureRegion, lightingConfiguration);

        Lighting3DUtils.configureAmbientLighting(fragmentShaderBuilder, nodeId, environmentId, lightingConfiguration);

        loadFragmentIfNotDefined(fragmentShaderBuilder, configuration, "unpackVec3ToFloat");

        String isLightedFunctionName = "isLighted_" + nodeId;
        String probeShadowMapFunctionName = "probeShadowMap_" + nodeId;
        fragmentShaderBuilder.addFunction(probeShadowMapFunctionName, createProbeShadowMapFunction(nodeId, maxNumberOfDirectionalLights));

        ObjectMap<String, String> variables = new ObjectMap<>();
        variables.put("NUM_SPOT_LIGHTS", String.valueOf(maxNumberOfSpotlights));
        variables.put("NUM_POINT_LIGHTS", String.valueOf(maxNumberOfPointLights));
        variables.put("NUM_DIRECTIONAL_LIGHTS", String.valueOf(maxNumberOfDirectionalLights));
        variables.put("SHADOW_ACNE_VALUE", SimpleNumberFormatter.format(shadowAcneValue));
        variables.put("SHADOW_SOFTNESS", String.valueOf(shadowSoftness));
        variables.put("SHADOW_PROBE_COUNT", SimpleNumberFormatter.format((shadowSoftness + 1) * (shadowSoftness + 1)));
        variables.put("NODE_ID", nodeId);

        fragmentShaderBuilder.addFunction(isLightedFunctionName, GLSLFragmentReader.getFragment(configuration.getAssetResolver(), "isLighted", variables));

        if (maxNumberOfDirectionalLights > 0)
            passDirectionalLights(environmentId, fragmentShaderBuilder, nodeId, variables, configuration.getAssetResolver(), maxNumberOfDirectionalLights, lightingConfiguration);
        if (maxNumberOfPointLights > 0)
            passPointLights(environmentId, fragmentShaderBuilder, nodeId, variables, configuration.getAssetResolver(), maxNumberOfPointLights, lightingConfiguration);
        if (maxNumberOfSpotlights > 0)
            passSpotLights(environmentId, fragmentShaderBuilder, nodeId, variables, configuration.getAssetResolver(), maxNumberOfSpotlights, lightingConfiguration);

        FieldOutput positionValue = inputs.get("position");
        FieldOutput normalValue = inputs.get("normal");
        FieldOutput albedoValue = inputs.get("albedo");
        FieldOutput emissionValue = inputs.get("emission");
        FieldOutput specularValue = inputs.get("specular");
        FieldOutput ambientOcclusionValue = inputs.get("ambientOcclusion");
        FieldOutput shininessValue = inputs.get("shininess");

        String position = positionValue.getRepresentation();
        String normal = normalValue.getRepresentation();
        String albedo = albedoValue != null ? albedoValue.getRepresentation() : "vec3(0.0)";
        String emission = emissionValue != null ? emissionValue.getRepresentation() : "vec3(0.0)";
        String specular = specularValue != null ? specularValue.getRepresentation() : "vec3(1.0)";
        String ambientOcclusion = ambientOcclusionValue != null ? ambientOcclusionValue.getRepresentation() : "1.0";
        String shininess = shininessValue != null ? shininessValue.getRepresentation() : "32.0";

        fragmentShaderBuilder.addMainLine("// Blinn-Phong Lighting node");
        String calculateLightingFunctionName = "calculateBlinnPhongLightingFunction_" + nodeId;
        fragmentShaderBuilder.addFunction(calculateLightingFunctionName, createCalculateLightingFunction(nodeId, maxNumberOfDirectionalLights, maxNumberOfPointLights, maxNumberOfSpotlights));
        String lightingVariable = "lighting_" + nodeId;
        fragmentShaderBuilder.addMainLine("Lighting " + lightingVariable + " = " + calculateLightingFunctionName + "(" + position + ", " + normal + ", " + shininess + ");");

        ShaderFieldType resultType = ShaderFieldTypeRegistry.findShaderFieldType(ShaderFieldType.Vector3);
        ObjectMap<String, DefaultFieldOutput> result = new ObjectMap<>();
        if (producedOutputs.contains("output")) {
            String name = "color_" + nodeId;
            fragmentShaderBuilder.addMainLine(resultType.getShaderType() + " " + name + " = " + emission + ".rgb + " + ambientOcclusion + " * u_ambientLight_" + nodeId + " * " + albedo + ".rgb;");
            fragmentShaderBuilder.addMainLine(name + " += " + lightingVariable + ".diffuse * " + albedo + ".rgb + " + lightingVariable + ".specular * " + specular + ".rgb;");
            result.put("output", new DefaultFieldOutput(resultType.getName(), name));
        }
        if (producedOutputs.contains("diffuse")) {
            result.put("diffuse", new DefaultFieldOutput(resultType.getName(), lightingVariable + ".diffuse"));
        }
        if (producedOutputs.contains("specularOut")) {
            result.put("specularOut", new DefaultFieldOutput(resultType.getName(), lightingVariable + ".specular"));
        }

        return result;
    }

    private String createProbeShadowMapFunction(String nodeId, int maxNumberOfDirectionalLights) {
        StringBuilder sb = new StringBuilder();
        sb.append("vec4 probeShadowMap_" + nodeId + "(int lightIndex, vec2 coord) {\n");
        for (int i = 0; i < maxNumberOfDirectionalLights; i++) {
            sb.append("  if (lightIndex == " + i + ")\n");
            sb.append("    return texture2D(u_shadowMap_" + nodeId + "_" + i + ", coord);\n");
        }
        sb.append("  return vec4(1.0);\n");
        sb.append("}\n");
        return sb.toString();
    }

    private String createCalculateLightingFunction(String nodeId, int maxNumberOfDirectionalLights, int maxNumberOfPointLights, int maxNumberOfSpotlights) {
        StringBuilder sb = new StringBuilder();

        sb.append("Lighting calculateBlinnPhongLightingFunction_" + nodeId + "(vec3 position, vec3 normal, float shininess) {\n");
        sb.append("  vec3 normalizedNormal = normalize(normal);\n");
        sb.append("  Lighting lighting = Lighting(vec3(0.0), vec3(0.0));\n");
        if (maxNumberOfDirectionalLights > 0)
            sb.append("  lighting = getDirectionalBlinnPhongLightContribution_" + nodeId + "(position, normalizedNormal, shininess, lighting);\n");
        if (maxNumberOfPointLights > 0)
            sb.append("  lighting = getPointBlinnPhongLightContribution_" + nodeId + "(position, normalizedNormal, shininess, lighting);\n");
        if (maxNumberOfSpotlights > 0)
            sb.append("  lighting = getSpotBlinnPhongLightContribution_" + nodeId + "(position, normalizedNormal, shininess, lighting);\n");
        sb.append("  return lighting;\n");
        sb.append("}\n");

        return sb.toString();
    }

    private void passSpotLights(final String environmentId, FragmentShaderBuilder fragmentShaderBuilder, String nodeId, final ObjectMap<String, String> variables, FileHandleResolver assetResolver, int maxNumberOfSpotlights, final LightingRendererConfiguration lightingRendererConfiguration) {
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition,
                "Camera position");

        Lighting3DUtils.configureSpotLighting(fragmentShaderBuilder, nodeId, environmentId, maxNumberOfSpotlights, lightingRendererConfiguration);

        fragmentShaderBuilder.addFunction("getSpotBlinnPhongLightContribution_" + nodeId,
                GLSLFragmentReader.getFragment(assetResolver, "blinn-phong/spotLightContribution", variables));
    }

    private void passPointLights(final String environmentId, FragmentShaderBuilder fragmentShaderBuilder, String nodeId, final ObjectMap<String, String> variables, FileHandleResolver assetResolver, int maxNumberOfPointLights, final LightingRendererConfiguration lightingRendererConfiguration) {
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition,
                "Camera position");

        Lighting3DUtils.configurePointLighting(fragmentShaderBuilder, nodeId, environmentId, maxNumberOfPointLights, lightingRendererConfiguration);

        fragmentShaderBuilder.addFunction("getPointBlinnPhongLightContribution_" + nodeId,
                GLSLFragmentReader.getFragment(assetResolver, "blinn-phong/pointLightContribution", variables));
    }

    private void passDirectionalLights(final String environmentId, FragmentShaderBuilder fragmentShaderBuilder, String nodeId, final ObjectMap<String, String> variables, FileHandleResolver assetResolver, int maxNumberOfDirectionalLights, final LightingRendererConfiguration lightingRendererConfiguration) {
        fragmentShaderBuilder.addUniformVariable("u_cameraPosition", "vec3", true, UniformSetters.cameraPosition,
                "Camera position");

        Lighting3DUtils.configureDirectionalLighting(fragmentShaderBuilder, nodeId, environmentId, maxNumberOfDirectionalLights, lightingRendererConfiguration);

        fragmentShaderBuilder.addFunction("getDirectionalBlinnPhongLightContribution_" + nodeId,
                GLSLFragmentReader.getFragment(assetResolver, "blinn-phong/shadowDirectionalLightContribution", variables));
    }
}
