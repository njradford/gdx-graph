package com.gempukku.libgdx.graph.util.storage;

public interface ObjectMeshStorage<T, U> {
    float[] getVertexArray();

    short[] getIndexArray();

    int getMaxVertexCount();

    U addObject(T object);

    boolean containsObject(U objectReference);

    U updateObject(T object, U objectReference);

    boolean removeObject(U objectReference);

    boolean canStore(T object);

    int getUsedIndexStart();

    int getUsedIndexCount();

    int getMinUpdatedVertexArrayIndex();

    int getMaxUpdatedVertexArrayIndex();

    int getMinUpdatedIndexArrayIndex();

    int getMaxUpdatedIndexArrayIndex();

    void resetUpdates();
}