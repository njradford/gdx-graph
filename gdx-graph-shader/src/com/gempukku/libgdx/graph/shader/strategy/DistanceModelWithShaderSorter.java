package com.gempukku.libgdx.graph.shader.strategy;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.graph.shader.ShaderRendererConfiguration;

import java.util.Comparator;

public class DistanceModelWithShaderSorter implements Comparator<ModelWithShader> {
    public enum Order {
        Front_To_Back, Back_To_Front;

        public int result(float dst) {
            if (this == Front_To_Back)
                return dst > 0 ? 1 : (dst < 0 ? -1 : 0);
            else
                return dst < 0 ? 1 : (dst > 0 ? -1 : 0);
        }
    }

    private ShaderRendererConfiguration configuration;
    private Vector3 cameraPosition;
    private final Order order;

    public DistanceModelWithShaderSorter(Order order) {
        this.order = order;
    }

    public void sort(ShaderRendererConfiguration configuration, Vector3 cameraPosition, Array<ModelWithShader> renderables) {
        this.configuration = configuration;
        this.cameraPosition = cameraPosition;
        renderables.sort(this);
    }

    @Override
    public int compare(ModelWithShader o1, ModelWithShader o2) {
        Vector3 position1 = configuration.getPosition(o1.getModel(), o1.getShader());
        Vector3 position2 = configuration.getPosition(o2.getModel(), o2.getShader());
        final float dst = (int) (1000f * cameraPosition.dst2(position1)) - (int) (1000f * cameraPosition.dst2(position2));
        return order.result(dst);
    }
}
