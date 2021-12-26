package com.hadoopweb.utils;

import com.hadoopweb.PackageInfo;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
        Object obj = clazz.newInstance();
        PackageInfo pki = new PackageInfo();
        pki.jarPath = jarPath;
        pki.MapperClass = (Class<? extends Mapper>) clazz.getField("MapperClass").get(obj);
        pki.ReducerClass = (Class<? extends Reducer>) clazz.getField("ReducerClass").get(obj);
        pki.MapOutputKeyClass = (Class<?>) clazz.getField("MapOutputKeyClass").get(obj);
        pki.MapOutputValueClass = (Class<?>) clazz.getField("MapOutputValueClass").get(obj);
        pki.OutputKeyClass = (Class<?>) clazz.getField("OutputKeyClass").get(obj);
        pki.OutputValueClass = (Class<?>) clazz.getField("OutputValueClass").get(obj);
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


    public static Set<Class> loadClassFromJar(String basePackage, JarFile jar, URLClassLoader loader) throws Exception {
        Set<Class> classes = new HashSet<>();
        String pkgPath = basePackage.replace(".", "/");
        Enumeration<JarEntry> entries = jar.entries();
        Class<?> clazz;
        System.out.println("----- Class -----");
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
        Class<?> clazz2 = loader.loadClass("com.hadoopweb.utils.PackageInfo");
        Method[] methods = clazz2.getDeclaredMethods();
        Field[] fields = clazz2.getDeclaredFields();
        Object obj = clazz2.newInstance();
        System.out.println("----- Methods -----");
        for(Method method:methods) System.out.println(method);
        System.out.println("----- Fields -----");
        for(Field field:fields) System.out.println(field.get(obj));
        return classes;
    }
}

