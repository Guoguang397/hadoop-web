package com.hadoopweb.utils;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class PackageInfo {
    public Class<? extends Mapper> MapperClass;
    public Class<? extends Reducer> ReducerClass;
    public Class<?> MapOutputKeyClass;
    public Class<?> MapOutputValueClass;
    public Class<?> OutputKeyClass;
    public Class<?> OutputValueClass;
    public String InputPath;
}
