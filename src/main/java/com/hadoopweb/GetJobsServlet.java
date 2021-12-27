package com.hadoopweb;

import org.apache.hadoop.mapreduce.Job;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "GetJobsServlet", value = "/GetJobsServlet")
public class GetJobsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        List<PackageInfo> pkis = (List<PackageInfo>) this.getServletContext().getAttribute("pkis");
        if(pkis != null) {
            for (PackageInfo info:pkis) {
                try {
                    Job job = info.job;
                    pw.println(String.format("%s|%s|%s|%d|%d|%f|%f|%f|%f|%b|%s",
                            job.getJobID().toString(),job.getJobName(),job.getJobState().toString(),job.getStartTime(),job.getFinishTime(),
                            job.setupProgress(),job.mapProgress(),job.reduceProgress(),job.cleanupProgress(),job.isComplete(),job.getTrackingURL()));
                } catch (InterruptedException e) {
                    pw.println("Error while getting job info.");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
