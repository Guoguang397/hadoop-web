package com.hadoopweb;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class PackageInfo {
    public Class<? extends Mapper> MapperClass;
    public Class<? extends Reducer> ReducerClass;
    public Class<?> MapOutputKeyClass;
    public Class<?> MapOutputValueClass;
    public Class<?> OutputKeyClass;
    public Class<?> OutputValueClass;

    public String jarPath;
    public String InputPath;
    public String OutputPath;
    public Job job = null;
}
