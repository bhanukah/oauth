package com.ssd.oauth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author bhhilk
 */
@MultipartConfig
@WebServlet(name = "uploadservlet", urlPatterns = {"/uploadservlet"})
public class UploadServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UploadServerlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UploadServerlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accessToken = "";
        HttpSession session = request.getSession();
        if (session.getAttribute("accessToken") != null) {
            accessToken = session.getAttribute("accessToken").toString();
        }

        Collection<Part> parts = request.getParts();

        Part filePart = request.getPart("file");
        InputStream imageInputStream = filePart.getInputStream();

        int length = imageInputStream.available();

        byte[] buffer = new byte[imageInputStream.available()];

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://www.googleapis.com/upload/drive/v2/files");
  
        MultipartEntityBuilder builder;
        builder = MultipartEntityBuilder.create();
        String boundary = "Content-Type: image/jpeg";
        builder.setBoundary(boundary);
        builder.addTextBody("name", "uploadedByOAuth2.jpg");
        builder.addBinaryBody("JPEG_DATA", buffer);

        HttpEntity multipart = builder.build();
        httppost.setEntity(multipart);

        httppost.setHeader("uploadType", "multipart");
        httppost.setHeader("Content-Type", "image/jpeg");
        httppost.setHeader("Authorization", "Bearer " + accessToken);
        
        //Execute and get the response.
        HttpResponse response2;
        response2 = httpclient.execute(httppost);
        HttpEntity entity = response2.getEntity();
        String upoutput = "";
        if (entity
                != null) {
            try (InputStream instream = entity.getContent()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream, StandardCharsets.UTF_8));
                StringBuilder sbuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    sbuilder.append(line);
                }
                upoutput = sbuilder.toString();

                response.setContentType("text/html;charset=UTF-8");
                try (PrintWriter out = response.getWriter()) {
                    out.println("<!DOCTYPE html>");
                    out.println("<html>");
                    out.println("<head>");
                    out.println("<title>Servlet UploadServerlet</title>");
                    out.println("</head>");
                    out.println("<body>");
                    out.println("<h1>Servlet UploadServerlet at " + upoutput + "</h1>");
                    out.println("</body>");
                    out.println("</html>");
                }
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
