package servlets;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by Александр on 13.12.2015.
 */
//TODO: Добавить проставку ACCESS_TOKEN в или в какой-нить статический класс

public class InstagramOAuth2Servlet extends HttpServlet {
    private static final String CLIENT_ID = "ef924dd68e224ef6862fe7d188f5759a";
    private static final String REDIRECT_URI = "http://localhost:8080/auth";
    private static final String CLIENT_SECRET = "37a0bdda7f8c4270afebecf7a6fa3ff7";
    private static final String INSTAGRAM_AUTH_URL = "https://api.instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=code";
    private static final String INSTAGRAM_ACCESS_TOKEN_URL = "https://api.instagram.com/oauth/access_token/";
    private static String CODE = "";
    private static String ACCESS_TOKEN = "";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        Map<String, String[]> paramMap = req.getParameterMap();

        if(paramMap.isEmpty()){
            resp.sendRedirect(String.format(INSTAGRAM_AUTH_URL, CLIENT_ID, REDIRECT_URI));
            return;
        }
        else if(paramMap.containsKey("code")){
            CODE = paramMap.get("code")[0];
            ACCESS_TOKEN = getAccessToken();
            resp.sendRedirect("/?token=" + ACCESS_TOKEN);
        }



        System.out.println(ACCESS_TOKEN);
//        out.println("<h2>" + paramMap.toString() + "!</h2>");
//        out.println("<h2>CODE " + CODE + "</h2>");
//        out.println("<h2>ACCESS_TOKEN" + ACCESS_TOKEN + "!</h2>");
//        System.out.println("LOLD");

    }

    private String getAccessToken() throws ServletException, IOException {
        HttpClient httpclient = new HttpClient();
        BufferedReader bufferedreader = null;
        String result = null;

        PostMethod postmethod = new PostMethod(INSTAGRAM_ACCESS_TOKEN_URL);
        postmethod.addParameter("client_id",CLIENT_ID);
        postmethod.addParameter("client_secret", CLIENT_SECRET);
        postmethod.addParameter("grant_type", "authorization_code");
        postmethod.addParameter("redirect_uri", REDIRECT_URI);
        postmethod.addParameter("code", CODE);


        int rCode = 0;
        try {
            rCode = httpclient.executeMethod(postmethod);

            StringBuffer sb = new StringBuffer();

            if(rCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                throw new ServletException("The Post postmethod is not implemented by this URI");
            } else {
                bufferedreader = new BufferedReader(new InputStreamReader(postmethod.getResponseBodyAsStream()));
                String readLine;
                while (((readLine = bufferedreader.readLine()) != null))
                    sb.append(readLine);
                JSONObject json = new JSONObject(sb.toString());

                System.out.println(json);
                System.out.println("lold");

                result =  json.getString("access_token");
            }

        } catch (IOException e) {
            throw e;
        }
        finally {
            postmethod.releaseConnection();
            bufferedreader.close();
        }


        return result;
    }
}
