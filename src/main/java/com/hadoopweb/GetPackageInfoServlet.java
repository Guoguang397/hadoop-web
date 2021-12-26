package com.hadoopweb;

import com.hadoopweb.utils.JarLoader;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;

@WebServlet(name = "GetPackageInfoServlet", value = "/GetPackageInfoServlet")
public class GetPackageInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        PrintWriter pw = response.getWriter();
        String jarName = request.getParameter("jarName");
        //String inputPath = request.getParameter("inPath");
        if(jarName == null) {
            pw.println("1\nNo jarPath input.");
            return;
        }
        jarName = "WEB-INF/uploads/maven.jar";
        String realPath = this.getServletContext().getRealPath(jarName);
        System.out.println(realPath);
        try {
            PackageInfo pki = JarLoader.loadJar(realPath);
            System.out.println(pki);
            System.out.println(pki.MapperClass);
            System.out.println(pki.ReducerClass);
            System.out.println(pki.MapOutputKeyClass);
            System.out.println(pki.MapOutputValueClass);
            System.out.println(pki.OutputKeyClass);
            System.out.println(pki.OutputValueClass);
            pw.println("1");
            pw.println("JarPath: " + realPath);
            pw.println("MapperClass: " + pki.MapperClass);
            pw.println("ReducerClass: " + pki.ReducerClass);
            pw.println("MapOutputKeyClass: " + pki.MapOutputKeyClass);
            pw.println("MapOutputValueClass: " + pki.MapOutputValueClass);
            pw.println("OutputKeyClass: " + pki.OutputKeyClass);
            pw.println("OutputValueClass: " + pki.OutputValueClass);
        } catch (Exception e) {
            pw.println("1\nError while loading pkgs.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        doGet(request, response);
//        String jarPath = request.getParameter("jarPath");
//        String inputPath = request.getParameter("inPath");
//        jarPath = "WEB-INF/uploads/maven.jar";
//        String realPath = this.getServletContext().getRealPath(jarPath);
//        System.out.println(realPath);
//        try {
//            PackageInfo pki = JarLoader.loadAll(realPath);
//            System.out.println(pki);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
