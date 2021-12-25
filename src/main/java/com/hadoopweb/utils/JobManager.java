package com.hadoopweb.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class JobManager {
   List<Job> jobs = new LinkedList<Job>();

   public Job submitJob(String jarPath, PackageInfo pki) throws Exception {
      // 0x01 Get configuration and Job instance
      Configuration conf = new Configuration();
      Job job = Job.getInstance(conf);

      // 0x02 Set jar path
      job.setJar(jarPath);

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
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
      FileOutputFormat.setOutputPath(job, new Path("/output/"+sdf.format(new Date())));

      // 0x08 Submit Job
      job.submit();
      return job;
   }
}
