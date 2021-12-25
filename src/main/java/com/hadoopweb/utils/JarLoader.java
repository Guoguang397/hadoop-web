package com.hadoopweb.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader {
    public static PackageInfo loadJar(String jarPath) throws Exception {
        URL[] urls = new URL[] { new URL("file://"+jarPath)};
        URLClassLoader loader = new URLClassLoader(urls);
        Class<?> clazz = loader.loadClass("edu/nefu/hadoop/PackageInfo");
        PackageInfo pki = (PackageInfo) clazz.getConstructor().newInstance();
        return pki;
    }

    public static PackageInfo loadAll(String jarPath) throws Exception {
        URL[] urls = new URL[] { new URL("file://"+jarPath)};
        URLClassLoader loader = new URLClassLoader(urls);
        System.out.println(urls[0]);
        JarFile jarFile = ((JarURLConnection) urls[0].openConnection()).getJarFile();
        loadClassFromJar("edu.nefu.hadoop",jarFile);
        return null;
    }


    public static Set<Class> loadClassFromJar(String basePackage, JarFile jar) {

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
//            clazz = loadClass(className.replace("/", "."));
            System.out.println(className.replace("/", "."));
//            if (clazz != null && !clazz.isInterface() && superClass.isAssignableFrom(clazz)) {
//                classes.add(clazz);
//            }
        }
        return classes;
    }
}

