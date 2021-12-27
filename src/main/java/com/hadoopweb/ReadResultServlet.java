package com.hadoopweb;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

@WebServlet(name = "ReadResultServlet", value = "/ReadResultServlet")
public class ReadResultServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        String jobID = request.getParameter("jobID");
        PrintWriter pw = response.getWriter();
        if(jobID == null) {
            pw.write("No job ID.");
            return;
        }
        PackageInfo pki = null;
        List<PackageInfo> pkis = (List<PackageInfo>) getServletContext().getAttribute("pkis");
        if(pkis == null) {
            pw.write("No job found.");
            return;
        }
        for(PackageInfo pkii:pkis) {
            if(pkii.job.getJobID().toString().equals(jobID)) {
                pki = pkii;
            }
        }
        if(pki == null) {
            pw.write("No job with id "+jobID+" found.");
            return;
        }
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", "hdfs://222.27.167.89:8020");

        FileSystem fs = null;
        try {
            fs = FileSystem.get(new URI("hdfs://222.27.167.89:8020"),conf, "guoguang");
        } catch (InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
        FSDataInputStream inStream = fs.open(new Path(pki.OutputPath+"/part-r-00000"));
        String line;
        while((line = inStream.readLine())!=null) {
            pw.println(line);
        }
        inStream.close();
        fs.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}


