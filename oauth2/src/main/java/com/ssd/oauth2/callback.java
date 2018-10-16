package com.ssd.oauth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author bhhilk
 */
@WebServlet(name = "callback", urlPatterns = {"/callback"})
public class callback extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String authorizationCode = request.getParameter("code");
        if (authorizationCode != null && authorizationCode.length() > 0) {

            String accessToken = getToken(authorizationCode);
            HttpSession session = request.getSession();
            session.setAttribute("accessToken", accessToken);
            response.sendRedirect("addfile.jsp");

        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            try {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Error</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Error</h1>");
                out.println("</body>");
                out.println("</html>");
            } finally {
                out.close();
            }
        }
    }

    private String getToken(String authorizationCode) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://www.googleapis.com/oauth2/v4/token");

        // Request parameters and other properties.
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("code", authorizationCode));
        params.add(new BasicNameValuePair("redirect_uri", "http://localhost:8080/oauth2/callback"));
        params.add(new BasicNameValuePair("client_id", "13840819056-agafbse9s20gjf7h4urv49q1lbhqh7pr.apps.googleusercontent.com"));
        params.add(new BasicNameValuePair("client_secret", "43VyeOTrpZYWw_gkYm-cq07w"));
        httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        //Execute and get the response.
        HttpResponse response;
        response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        String tokenOutput = "";
        String accessToken = null;
        if (entity != null) {
            try (InputStream instream = entity.getContent()) {
                // do something useful
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream, StandardCharsets.UTF_8));
                StringBuilder builder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                tokenOutput = builder.toString();
                JSONParser parser;
                parser = new JSONParser();
                try {
                    Object obj = parser.parse(tokenOutput);
                    JSONObject jsonobj = (JSONObject) obj;
                    accessToken = jsonobj.get("access_token").toString();

                } catch (ParseException e) {
                    System.out.println("ERROR: " + e.getMessage());
                }
            }
        }
        return accessToken;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
