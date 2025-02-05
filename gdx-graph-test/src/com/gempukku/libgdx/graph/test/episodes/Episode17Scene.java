package com.gempukku.libgdx.graph.test.episodes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gempukku.libgdx.graph.pipeline.PipelineLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.pipeline.RenderOutputs;
import com.gempukku.libgdx.graph.pipeline.impl.SimplePipelineRendererConfiguration;
import com.gempukku.libgdx.graph.pipeline.time.TimeKeeper;
import com.gempukku.libgdx.graph.render.ui.UIRendererConfiguration;
import com.gempukku.libgdx.graph.shader.ModelContainer;
import com.gempukku.libgdx.graph.shader.RenderableModel;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;
import com.gempukku.libgdx.graph.shader.lighting3d.Directional3DLight;
import com.gempukku.libgdx.graph.shader.lighting3d.LightingRendererConfiguration;
import com.gempukku.libgdx.graph.test.LibgdxGraphTestScene;
import com.gempukku.libgdx.graph.util.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.SimpleLightingRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleShaderRendererConfiguration;
import com.gempukku.libgdx.graph.util.SimpleUIRendererConfiguration;
import com.gempukku.libgdx.graph.util.lighting.Lighting3DEnvironment;
import com.gempukku.libgdx.graph.util.model.MaterialModelInstanceModelAdapter;

public class Episode17Scene implements LibgdxGraphTestScene {
    private PipelineRenderer pipelineRenderer;
    private Camera camera;
    private Stage stage;
    private Skin skin;
    private Lighting3DEnvironment lights;
    private final float cameraSpeed = -0.8f;
    private float cameraAngle = 0f;
    private final float cameraDistance = 4f;
    private AnimationController animationController;

    private Model model;
    private final TimeKeeper timeKeeper = new DefaultTimeKeeper();
    private MaterialModelInstanceModelAdapter mainRobotAdapter;
    private SimplePipelineRendererConfiguration configuration;
    private SimpleShaderRendererConfiguration shaderConfiguration;

    @Override
    public String getName() {
        return "YouTube Episode 17";
    }

    @Override
    public void initializeScene() {
        lights = createLights();
        stage = createStage();

        camera = createCamera();

        pipelineRenderer = loadPipelineRenderer();
        createModels(shaderConfiguration);

        Gdx.input.setInputProcessor(stage);
    }

    private Lighting3DEnvironment createLights() {
        float ambientBrightness = 0.3f;
        float directionalBrightness = 0.8f;
        Lighting3DEnvironment lights = new Lighting3DEnvironment();
        lights.setAmbientColor(new Color(ambientBrightness, ambientBrightness, ambientBrightness, 1f));
        DirectionalLight directionalLight = new DirectionalLight();
        directionalLight.setColor(directionalBrightness, directionalBrightness, directionalBrightness, 1f);
        directionalLight.setDirection(-1f, -0.3f, 0);
        lights.addDirectionalLight(new Directional3DLight(directionalLight));
        return lights;
    }

    private Camera createCamera() {
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.near = 0.5f;
        camera.far = 100f;
        return camera;
    }

    private void createModels(ModelContainer<RenderableModel> modelContainer) {
        JsonReader jsonReader = new JsonReader();
        G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);
        model = modelLoader.loadModel(Gdx.files.classpath("model/gold-robot/gold-robot.g3dj"));

        ModelInstance mainRobotInstance = new ModelInstance(model);
        final float scale = 0.008f;
        mainRobotInstance.transform.idt().scale(scale, scale, scale);
        animationController = new AnimationController(mainRobotInstance);
        animationController.animate("Root|jog", -1, null, 0f);

        mainRobotAdapter = new MaterialModelInstanceModelAdapter(mainRobotInstance, modelContainer);
        mainRobotAdapter.addTag("Default");

        ModelInstance secondRobotInstance = new ModelInstance(model);
        secondRobotInstance.transform.idt().translate(1.5f, 0, 0).scale(scale, scale, scale);

        MaterialModelInstanceModelAdapter secondRobotAdapter = new MaterialModelInstanceModelAdapter(secondRobotInstance, modelContainer);
        secondRobotAdapter.addTag("Default");
    }

    private Stage createStage() {
        skin = new Skin(Gdx.files.classpath("skin/default/uiskin.json"));

        final CheckBox selected = new CheckBox("Outline", skin);
        selected.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (selected.isChecked())
                            mainRobotAdapter.addTag("Outline");
                        else
                            mainRobotAdapter.removeTag("Outline");
                    }
                });

        final Slider outlineWidth = new Slider(0f, 0.1f, 0.001f, false, skin);
        outlineWidth.setValue(0.01f);
        outlineWidth.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        mainRobotAdapter.setProperty("Outline Width", outlineWidth.getValue());
                    }
                });

        Stage stage = new Stage(new ScreenViewport());

        Table tbl = new Table(skin);
        tbl.add(selected).left().colspan(2).row();
        tbl.add("Width");
        tbl.add(outlineWidth).row();

        tbl.setFillParent(true);
        tbl.align(Align.topLeft);

        stage.addActor(tbl);
        return stage;
    }

    @Override
    public void resizeScene(int width, int height) {
        stage.getViewport().update(width, height, true);
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void renderScene() {
        float delta = Gdx.graphics.getDeltaTime();
        timeKeeper.updateTime(delta);
        animationController.update(delta);

        cameraAngle += delta * cameraSpeed;

        camera.position.set(cameraDistance * MathUtils.sin(cameraAngle), 3f, cameraDistance * MathUtils.cos(cameraAngle));
        camera.up.set(0f, 1f, 0f);
        camera.lookAt(0, 0.8f, 0f);
        camera.update();

        stage.act(delta);

        pipelineRenderer.render(RenderOutputs.drawToScreen);
    }

    @Override
    public void disposeScene() {
        model.dispose();
        stage.dispose();
        skin.dispose();
        pipelineRenderer.dispose();
        configuration.dispose();
    }

    private PipelineRenderer loadPipelineRenderer() {
        configuration = new SimplePipelineRendererConfiguration(timeKeeper);
        configuration.getPipelinePropertyContainer().setValue("Camera", camera);

        SimpleUIRendererConfiguration uiConfiguration = new SimpleUIRendererConfiguration();
        uiConfiguration.setStage("", stage);
        configuration.setConfig(UIRendererConfiguration.class, uiConfiguration);

        SimpleLightingRendererConfiguration lightingConfiguration = new SimpleLightingRendererConfiguration();
        configuration.setConfig(LightingRendererConfiguration.class, lightingConfiguration);
        lightingConfiguration.setEnvironment("", lights);

        shaderConfiguration = new SimpleShaderRendererConfiguration(configuration.getPipelinePropertyContainer());
        configuration.setConfig(ShaderRendererConfiguration.class, shaderConfiguration);

        return PipelineLoader.loadPipelineRenderer(Gdx.files.local("examples-assets/episode17.json"), configuration);
    }
}