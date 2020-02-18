package org.cgcgframework.web.parameter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FileTempContext {
    private static final Map<String, Set<String>> filePaths = new HashMap<>();
    private static final String FILE_TEMP = "_cg_mvc_file_temp";

    public static void put(String filePath) {
        final String key = Thread.currentThread().getId() + FILE_TEMP;
        Set<String> value = get();
        if (value == null) {
            value =  new HashSet<>();
        }
        value.add(filePath);
        filePaths.put(key, value);
    }

    public static Set<String> get() {
        return filePaths.get(Thread.currentThread().getId() + FILE_TEMP);
    }

    public static void clear() {
        filePaths.remove(Thread.currentThread().getId() + FILE_TEMP);
    }
}
