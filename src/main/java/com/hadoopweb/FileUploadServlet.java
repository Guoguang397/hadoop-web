package com.hadoopweb;

import javax.servlet.http.*;
import javax.servlet.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerException;

@WebServlet(name = "FileUploadServlet", value = "/FileUploadServlet")
public class FileUploadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        PrintWriter out = response.getWriter();
        out.println("Method not allowed.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws  IOException {
        PrintWriter out = response.getWriter();

        //定义上传文件的最大字节
        int MAX_SIZE = 1024 * 1024 * 50;
        //创建根路径的保存变量
        String rootPath;
        //声明文件读入类
        DataInputStream in;
        FileOutputStream fileOut;
        //取得互联网程序的绝对地址
        String realPath = request.getSession().getServletContext().getRealPath("/");
        //创建文件的保存目录
        rootPath = realPath + "/WEB-INF/uploads/";
        //取得客户端上传的数据类型
        String contentType = request.getContentType();
        try {
            if (contentType.contains("multipart/form-data")) {
                //读入上传数据
                in = new DataInputStream(request.getInputStream());
                int formDataLength = request.getContentLength();
                if (formDataLength > MAX_SIZE) {
                    out.println("Max size: " + MAX_SIZE + " Bytes");
                    return;
                }
                //保存上传文件的数据
                byte[] dataBytes = new byte[formDataLength];
                int byteRead;
                int totalBytesRead = 0;
                //上传的数据保存在byte数组里面
                while (totalBytesRead < formDataLength) {
                    byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
                    totalBytesRead += byteRead;
                }

                //根据byte数组创建字符串
                String file = new String(dataBytes, StandardCharsets.UTF_8);
                //取得上传数据的文件名
                String saveFile = file.substring(file.indexOf("filename=\"") + 10);
                saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
                saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1, saveFile.indexOf("\""));

                if(!saveFile.endsWith("jar")) {
                    out.println("Wrong ext.");
                    return;
                }
                file = new String(dataBytes, StandardCharsets.US_ASCII);

                //取得数据的分隔字符串
                String boundary = contentType.substring(contentType.lastIndexOf("=") + 1);
                //创建保存路径的文件名
                String fileName = rootPath + saveFile;
                int pos;
                pos = file.indexOf("filename=\"");
                pos = file.indexOf("\n", pos) + 1;
                pos = file.indexOf("\n", pos) + 1;
                pos = file.indexOf("\n", pos) + 1;
                int boundaryLocation = file.indexOf(boundary, pos) - 4;

                //取得文件数据的开始的位置
                int startPos = ((file.substring(0, pos)).getBytes()).length;
                //检查上传文件的目录是否存在
                File fileDir = new File(rootPath);
                if (!fileDir.exists()) {
                    if(!fileDir.mkdirs()) {
                        out.println("Cannot mkdir!");
                        return;
                    }
                }
                //创建文件的输出类
                fileOut = new FileOutputStream(fileName);

                //保存文件的数据
                fileOut.write(dataBytes, startPos, (boundaryLocation - startPos));
                fileOut.close();
                out.print("File uploaded.");
            } else {
                String content = request.getContentType();
                out.print("ContentType: " + content + " != multipart/form-data");
            }
        } catch (Exception ex) {
            out.println("Exception: " + ex.getMessage());
            throw new ServerException(ex.getMessage());
        }
    }
}
