package com.hadoopweb;

import com.hadoopweb.utils.JarLoader;
import com.hadoopweb.utils.JobManager;
import org.apache.hadoop.mapreduce.Job;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

@WebServlet(name = "ExecuteJobServlet", value = "/ExecuteJobServlet")
public class ExecuteJobServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        pw.println("0\nMethod not allowed.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        String jarName = request.getParameter("jarName");
        String inputPath = request.getParameter("inPath");
        if(jarName == null || inputPath == null) {
            pw.println("1\nNo jarPath or inputPath.");
            return;
        }
        jarName = "WEB-INF/uploads/maven.jar";
        String realPath = this.getServletContext().getRealPath(jarName);
        System.out.println(realPath);
        try {
            PackageInfo pki = JarLoader.loadJar(realPath);
            pki.InputPath = inputPath;
            Job job = JobManager.submitJob(pki);
            pw.println(job.getJobID());
            List<Job> jobs = (List<Job>) this.getServletContext().getAttribute("jobs");
            if(jobs == null) jobs = new LinkedList<>();
            jobs.add(job);
            this.getServletContext().setAttribute("jobs", jobs);
        } catch (Exception e) {
            pw.println("1\nError while loading pkgs or submitting job.");
            e.printStackTrace();
        }
    }
}
