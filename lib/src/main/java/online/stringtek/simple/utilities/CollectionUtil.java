package online.stringtek.simple.utilities;

import com.google.common.collect.ImmutableList;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class CollectionUtil {

    public static <K, V, T> Map<K, V> list2Map(List<T> list, Function<T, K> keySupplier,
        Function<T, V> valueSupplier, Supplier<Map<K, V>> mapSupplier) {
        Map<K, V> map = mapSupplier.get();
        list.forEach((item) -> {
            K key = keySupplier.apply(item);
            V val = valueSupplier.apply(item);
            map.put(key, val);
        });
        return map;
    }

    public static <K, V, T> Map<K, V> list2Map(List<T> list, Function<T, K> keySupplier,
        Function<T, V> valueSupplier) {
        return list2Map(list, keySupplier, valueSupplier, HashMap::new);
    }

    public static <K, T> Map<K, T> list2Map(List<T> list, Function<T, K> keySupplier) {
        return list2Map(list, keySupplier, (obj) -> obj);
    }

}
