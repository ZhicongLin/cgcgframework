package org.cgcgframework.web.parameter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zhicong.lin
 */
public class FileTempContext {
    private static final Map<String, Set<String>> FILE_PATHS = new HashMap<>();
    private static final String FILE_TEMP = "_cg_mvc_file_temp";

    /**
     * @param filePath 文件路径
     * @return void
     * @author : zhicong.lin
     * @date : 2022/1/30 15:43
     */
    public static void put(String filePath) {
        final String key = Thread.currentThread().getId() + FILE_TEMP;
        Set<String> value = get();
        if (value == null) {
            value = new HashSet<>();
        }
        value.add(filePath);
        FILE_PATHS.put(key, value);
    }

    public static Set<String> get() {
        return FILE_PATHS.get(Thread.currentThread().getId() + FILE_TEMP);
    }

    public static void clear() {
        FILE_PATHS.remove(Thread.currentThread().getId() + FILE_TEMP);
    }
}
