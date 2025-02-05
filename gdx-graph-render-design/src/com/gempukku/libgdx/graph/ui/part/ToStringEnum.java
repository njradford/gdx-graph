package com.gempukku.libgdx.graph.ui.part;

import com.gempukku.libgdx.common.Function;

public class ToStringEnum<T extends Enum<T>> implements Function<T, String> {
    @Override
    public String evaluate(T t) {
        return t.toString();
    }
}
