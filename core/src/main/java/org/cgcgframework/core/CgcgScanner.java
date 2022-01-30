package org.cgcgframework.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhicong.lin
 */
public class CgcgScanner {
    public static final Set<Class<?>> CLASS_HASH_SET = new HashSet<>();
    public static final Set<String> PACKAGES = new HashSet<>();
    public Set<Class<?>> scan(String pkgPath) {
        final Set<Class<?>> classes = ClassScanner.getClasses(pkgPath);
        CLASS_HASH_SET.addAll(classes);
        return classes;
    }

    public static void addPackages(Collection<String> packages) {
        PACKAGES.addAll(packages);
    }
}
