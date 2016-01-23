/*
 * Copyright (c) 2006 Sun Microsystems, Inc.  All rights reserved.  U.S.
 * Government Rights - Commercial software.  Government users are subject
 * to the Sun Microsystems, Inc. standard license agreement and
 * applicable provisions of the FAR and its supplements.  Use is subject
 * to license terms.
 *
 * This distribution may include materials developed by third parties.
 * Sun, Sun Microsystems, the Sun logo, Java and J2EE are trademarks
 * or registered trademarks of Sun Microsystems, Inc. in the U.S. and
 * other countries.
 *
 * Copyright (c) 2006 Sun Microsystems, Inc. Tous droits reserves.
 *
 * Droits du gouvernement americain, utilisateurs gouvernementaux - logiciel
 * commercial. Les utilisateurs gouvernementaux sont soumis au contrat de
 * licence standard de Sun Microsystems, Inc., ainsi qu'aux dispositions
 * en vigueur de la FAR (Federal Acquisition Regulations) et des
 * supplements a celles-ci.  Distribue par des licences qui en
 * restreignent l'utilisation.
 *
 * Cette distribution peut comprendre des composants developpes par des
 * tierces parties. Sun, Sun Microsystems, le logo Sun, Java et J2EE
 * sont des marques de fabrique ou des marques deposees de Sun
 * Microsystems, Inc. aux Etats-Unis et dans d'autres pays.
 */


package servlets;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.*;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;


/**
* This is a simple example of an HTTP Servlet.  It responds to the GET
        * method of the HTTP protocol.
        */
public class ResponseServlet extends HttpServlet {
    private static final String INSTAGRAM_GET_SELF_INFO = "https://api.instagram.com/v1/users/self/?access_token=%s";


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        Map<String, String[]> paramMap = request.getParameterMap();

        if(paramMap.containsKey("token")) {
            String access_token = paramMap.get("token")[0];
            String resp = getResponseInfo(access_token);
            out.println("<h2>Json response is:</h2> " + resp);
        }
        else{
            out.println("<h2>Please authtorise:</h2>" +
                        "<form method=\"get\" action=\"/auth\">");
            out.println("<input type=\"submit\" value=\"Submit\">");
        }

    }

    public String getResponseInfo(String access_token) throws ServletException, IOException {
        HttpClient httpclient = new HttpClient();
        BufferedReader bufferedreader = null;
        String result = null;

        GetMethod getmethod = new GetMethod(String.format(INSTAGRAM_GET_SELF_INFO, access_token));


        int rCode = 0;
        try {
            rCode = httpclient.executeMethod(getmethod);

            StringBuffer sb = new StringBuffer();

            if(rCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                throw new ServletException("The Post postmethod is not implemented by this URI");
            } else {
                bufferedreader = new BufferedReader(new InputStreamReader(getmethod.getResponseBodyAsStream()));
                String readLine;
                while (((readLine = bufferedreader.readLine()) != null))
                    sb.append(readLine);
                System.out.println(sb.toString());

                System.out.println(sb.toString());

                result =  sb.toString();
            }

        } catch (IOException e) {
            throw e;
        }
        finally {
            getmethod.releaseConnection();
            bufferedreader.close();
        }

        return result;
    }
}
