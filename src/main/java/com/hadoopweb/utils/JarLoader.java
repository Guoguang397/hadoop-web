package com.hadoopweb.utils;

import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader {
    public static PackageInfo loadJar(String jarPath) throws Exception {
        URL[] urls = new URL[] { new URL("jar:file:"+jarPath+"!/")};
        URLClassLoader loader = new URLClassLoader(urls ,Thread.currentThread().getContextClassLoader());
        Class<?> clazz = loader.loadClass("com.hadoopweb.utils.PackageInfo");
        PackageInfo pki = (PackageInfo) clazz.newInstance();
        return pki;
    }

    public static PackageInfo loadAll(String jarPath) throws Exception {
        URL[] urls = new URL[] { new URL("jar:file:"+jarPath+"!/")};
        URLClassLoader loader = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        System.out.println(urls[0]);
        JarFile jarFile = ((JarURLConnection) urls[0].openConnection()).getJarFile();
        loadClassFromJar("com.hadoopweb.utils",jarFile, loader);
        return null;
    }


    public static Set<Class> loadClassFromJar(String basePackage, JarFile jar, URLClassLoader loader) throws ClassNotFoundException {
        Set<Class> classes = new HashSet<>();
        String pkgPath = basePackage.replace(".", "/");
        Enumeration<JarEntry> entries = jar.entries();
        Class<?> clazz;
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.charAt(0) == '/') {
                entryName = entryName.substring(1);
            }
            if (jarEntry.isDirectory() || !entryName.startsWith(pkgPath) || !entryName.endsWith(".class")) {
                continue;
            }
            String className = entryName.substring(0, entryName.length() - 6);
            System.out.println(className.replace("/", "."));
            clazz = loader.loadClass(className.replace("/", "."));
            if (clazz != null && !clazz.isInterface()) {
                classes.add(clazz);
            }
        }
        return classes;
    }
}

