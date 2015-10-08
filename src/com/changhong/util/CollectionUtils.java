package com.changhong.util;

import java.util.Collection;
import java.util.Map;

public class CollectionUtils {
    /**
     * Returns true if the string is null or 0-length.
     * 
     * @param str
     *            the string to be examined
     * @return true if str is null or zero length
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();
        return str.length() == 0 || str.equals("null");
    }

    /**
     * 获取集合大小
     * 
     * @param collection
     *            集合
     * @return
     */
    public static int size(Collection<?> collection) {
        return isEmpty(collection) ? 0 : collection.size();
    }

    /**
     * 检查集合元素是否存在
     * 
     * @param collection
     *            集合
     * @return
     */
    public static boolean isAvailable(Collection<?> collection, int index) {
        if (isEmpty(collection))
            return false;
        return index >= 0 && index < collection.size();

    }

    /**
     * 检查集合元素是否为空
     * 
     * @param collection
     *            集合
     * @return
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();

    }

    /**
     * 检查数组元素是否为空
     * 
     * @param array
     *            数组
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;

    }

    /**
     * 检查Map元素是否为空
     * 
     * @param map
     *            Map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();

    }
}
