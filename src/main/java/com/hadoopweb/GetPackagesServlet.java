package com.hadoopweb;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "GetPackagesServlet", value = "/GetPackagesServlet")
public class GetPackagesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter pw = response.getWriter();
        String realPath = this.getServletContext().getRealPath("/");
        File dir = new File(realPath + "WEB-INF/uploads/");
        printFiles(pw, dir);

    }

    private void printFiles(PrintWriter out, File dir) {
        File[] files = dir.listFiles();
        if(files != null) {
            for(File file:files) {
                if(file.isFile() && file.getName().endsWith(".jar")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    out.println(file.getName()+"|"+file.length()+"|"+sdf.format(new Date(file.lastModified())));
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("Method not allowed.");
    }
}
