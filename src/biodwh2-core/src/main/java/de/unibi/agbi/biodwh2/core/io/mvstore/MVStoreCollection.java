package de.unibi.agbi.biodwh2.core.io.mvstore;

import org.h2.mvstore.MVMap;

import java.util.*;

public final class MVStoreCollection<T extends MVStoreModel> implements Iterable<T> {
    private static final String INDEX_KEYS = "index_keys";
    private static final String INDEX_ARRAY_FLAGS = "index_array_flags";

    private final MVStoreDB db;
    private final String name;
    private final MVMap<Long, T> map;
    private final MVMap<String, Object> metaMap;
    private final Map<String, MVStoreIndex> indices;
    private boolean isDirty;

    MVStoreCollection(final MVStoreDB db, final String name) {
        this.db = db;
        this.name = name;
        map = db.openMap(name);
        metaMap = db.openMap(name + "!meta");
        indices = new HashMap<>();
        String[] indexKeys = (String[]) metaMap.get(INDEX_KEYS);
        boolean[] indexArrayFlags = (boolean[]) metaMap.get(INDEX_ARRAY_FLAGS);
        if (indexKeys != null) {
            for (int i = 0; i < indexKeys.length; i++)
                getIndex(indexKeys[i], indexArrayFlags[i], true);
        } else {
            metaMap.put(INDEX_KEYS, new String[0]);
            metaMap.put(INDEX_ARRAY_FLAGS, new boolean[0]);
        }
        isDirty = false;
    }

    public MVStoreIndex getIndex(final String key) {
        return getIndex(key, false, false);
    }

    public MVStoreIndex getIndex(final String key, final boolean arrayIndex) {
        return getIndex(key, arrayIndex, false);
    }

    private MVStoreIndex getIndex(final String key, final boolean arrayIndex, final boolean reopen) {
        MVStoreIndex index = indices.get(key);
        if (index == null) {
            index = new MVStoreIndex(db, name + "$" + key, key, arrayIndex);
            indices.put(key, index);
            if (!reopen) {
                String[] indexKeys = (String[]) metaMap.get(INDEX_KEYS);
                String[] newIndexKeys = indexKeys != null ? Arrays.copyOf(indexKeys, indexKeys.length + 1) :
                                        new String[1];
                newIndexKeys[newIndexKeys.length - 1] = index.getKey();
                boolean[] indexArrayFlags = (boolean[]) metaMap.get(INDEX_ARRAY_FLAGS);
                boolean[] newIndexArrayFlags = indexArrayFlags != null ? Arrays.copyOf(indexArrayFlags,
                                                                                       indexArrayFlags.length + 1) :
                                               new boolean[1];
                newIndexArrayFlags[newIndexArrayFlags.length - 1] = arrayIndex;
                metaMap.put(INDEX_KEYS, newIndexKeys);
                metaMap.put(INDEX_ARRAY_FLAGS, newIndexArrayFlags);
                if (isDirty)
                    for (final T obj : map.values())
                        index.update(obj);
            }
        }
        return index;
    }

    public void put(final T obj) {
        isDirty = true;
        map.put(obj.getIdValue(), obj);
        for (final MVStoreIndex index : indices.values())
            index.update(obj);
    }

    public T get(final MVStoreId id) {
        return map.getOrDefault(id.getIdValue(), null);
    }

    public T get(final long id) {
        return map.getOrDefault(id, null);
    }

    public String getName() {
        return name;
    }

    public Iterable<T> find(final String propertyKey, final Comparable<?> propertyValue) {
        return find(new String[]{propertyKey}, new Comparable<?>[]{propertyValue});
    }

    public Iterable<T> find(final String propertyKey1, final Comparable<?> propertyValue1, final String propertyKey2,
                            final Comparable<?> propertyValue2) {
        return find(new String[]{propertyKey1, propertyKey2}, new Comparable<?>[]{propertyValue1, propertyValue2});
    }

    public Iterable<T> find(final String propertyKey1, final Comparable<?> propertyValue1, final String propertyKey2,
                            final Comparable<?> propertyValue2, final String propertyKey3,
                            final Comparable<?> propertyValue3) {
        return find(new String[]{propertyKey1, propertyKey2, propertyKey3},
                    new Comparable<?>[]{propertyValue1, propertyValue2, propertyValue3});
    }

    public Iterable<T> find(final String propertyKey1, final Comparable<?> propertyValue1, final String propertyKey2,
                            final Comparable<?> propertyValue2, final String propertyKey3,
                            final Comparable<?> propertyValue3, final String propertyKey4,
                            final Comparable<?> propertyValue4) {
        return find(new String[]{propertyKey1, propertyKey2, propertyKey3, propertyKey4},
                    new Comparable<?>[]{propertyValue1, propertyValue2, propertyValue3, propertyValue4});
    }

    public synchronized Iterable<T> find(final String[] propertyKeys, final Comparable<?>[] propertyValues) {
        Set<Long> ids = null;
        final boolean[] hasIndexFlags = new boolean[propertyKeys.length];
        for (int i = 0; i < propertyKeys.length; i++) {
            final MVStoreIndex index = indices.get(propertyKeys[i]);
            if (index != null) {
                hasIndexFlags[i] = true;
                if (ids == null)
                    ids = index.find(propertyValues[i]);
                else
                    ids.retainAll(index.find(propertyValues[i]));
            }
        }
        for (int i = 0; i < propertyKeys.length; i++) {
            if (!hasIndexFlags[i]) {
                final Comparable<?> searchValue = propertyValues[i];
                if (ids == null) {
                    ids = new HashSet<>();
                    for (final Long id : map.keySet()) {
                        final T obj = map.get(id);
                        final Comparable<?> value = obj.getProperty(propertyKeys[i]);
                        if (value != null && value.equals(searchValue))
                            ids.add(id);
                    }
                } else {
                    Set<Long> matchedIds = new HashSet<>();
                    for (final Long id : ids) {
                        final T obj = map.get(id);
                        final Object value = obj.get(propertyKeys[i]);
                        if (value instanceof Comparable<?>) {
                            if (value.equals(searchValue))
                                matchedIds.add(id);
                        } else if (value instanceof Comparable<?>[]) {
                            final Comparable<?>[] valueArray = (Comparable<?>[]) value;
                            for (Comparable<?> comparable : valueArray) {
                                if (comparable != null && comparable.equals(searchValue)) {
                                    matchedIds.add(id);
                                    break;
                                }
                            }
                        }
                    }
                    ids.retainAll(matchedIds);
                }
            }
        }
        final Set<Long> finalIds = ids != null ? ids : new HashSet<>();
        return () -> finalIds.stream().map(this::get).iterator();
    }

    @Override
    public Iterator<T> iterator() {
        return map.values().iterator();
    }

    public long size() {
        return map.sizeAsLong();
    }

    public MVStoreIndex[] getIndices() {
        return indices.values().toArray(new MVStoreIndex[0]);
    }

    public void remove(final T obj) {
        isDirty = true;
        map.remove(obj.getId().getIdValue());
        for (final MVStoreIndex index : indices.values())
            index.remove(obj);
    }
}