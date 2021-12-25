package com.hadoopweb;

import com.hadoopweb.utils.JarLoader;
import com.hadoopweb.utils.PackageInfo;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;

@WebServlet(name = "GetPackageInfoServlet", value = "/GetPackageInfoServlet")
public class GetPackageInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        String jarPath = request.getParameter("jarPath");
        String inputPath = request.getParameter("inPath");
        jarPath = "WEB-INF/uploads/maven.jar";
        String realPath = this.getServletContext().getRealPath(jarPath);
        System.out.println(realPath);
        try {
            PackageInfo pki = JarLoader.loadAll(realPath);
            System.out.println(pki);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        doGet(request, response);
    }
}
