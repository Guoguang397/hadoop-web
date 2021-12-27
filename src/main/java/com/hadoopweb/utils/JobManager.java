package com.hadoopweb.utils;

import com.hadoopweb.PackageInfo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class JobManager {
   List<Job> jobs = new LinkedList<Job>();

   public static Job submitJob(PackageInfo pki) throws Exception {
      // 0x01 Get configuration and Job instance
      Configuration conf = new Configuration();
      conf.set("fs.defaultFS", "hdfs://222.27.167.89:8020");
      conf.set("hadoop.job.user", "guoguang");
      conf.set("mapreduce.framework.name", "yarn");
//      conf.set("mapreduce.jobtracker.address", "192.168.48.10:9001");
      conf.set("yarn.resourcemanager.hostname", "222.27.167.83");
      conf.set("mapreduce.jobhistory.address", "222.27.167.89:10020");
      conf.set("mapreduce.jobhistory.webapp.address", "222.27.167.89:19888");

      Job job = Job.getInstance(conf);

      // 0x02 Set jar path
      job.setJar(pki.jarPath);

      // 0x03 Connect Mapper and Reducer
      job.setMapperClass(pki.MapperClass);
      job.setReducerClass(pki.ReducerClass);

      // 0x04 Set type of Mapper Output
      job.setMapOutputKeyClass(pki.MapOutputKeyClass);
      job.setMapOutputValueClass(pki.MapOutputValueClass);

      // 0x05 Set type of Final Output
      job.setOutputKeyClass(pki.OutputKeyClass);
      job.setOutputValueClass(pki.OutputValueClass);

      // 0x06 Specify Job input Path
      FileInputFormat.setInputPaths(job, new Path(pki.InputPath));

      // 0x07 Specify Job output Path
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
      String outputPath = "/output/"+sdf.format(new Date());
      FileOutputFormat.setOutputPath(job, new Path(outputPath));
      pki.OutputPath = outputPath;

      // 0x08 Submit Job
      job.submit();
      pki.job = job;
      return job;
   }
}
