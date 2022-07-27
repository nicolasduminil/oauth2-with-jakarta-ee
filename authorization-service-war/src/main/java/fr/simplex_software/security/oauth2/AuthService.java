package fr.simplex_software.security.oauth2;

import jakarta.json.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.*;

import org.apache.commons.lang3.*;

@WebServlet("/auth")
public class AuthService extends HttpServlet
{
  private static final String SECRET = "secret123";
  private static final String CLIENT_ID = "nicolas";
  private static final String GRANT_TYPE = "authorization_code";
  private static String authorizationCode = RandomStringUtils.randomAlphabetic(12);

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    StringBuilder redirectUrl = new StringBuilder(request.getParameter("redirect_uri"));
    redirectUrl.append("?&state=").append(request.getParameter("state"));
    redirectUrl.append("&code=").append(authorizationCode);
    if (!"code".equals(request.getParameter("response_type")))
      response.sendError(401);
    if (!CLIENT_ID.equals(request.getParameter("client_id")))
      response.sendError(401);
    response.sendRedirect(redirectUrl.toString());
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    boolean grant = GRANT_TYPE.equals(request.getParameter("grant_type"));
    boolean code = authorizationCode.equals(request.getParameter("code"));
    boolean client = CLIENT_ID.equals(request.getParameter("client_id"));
    boolean secret = SECRET.equals(request.getParameter("client_secret"));
    JsonObjectBuilder jsonresponse = Json.createObjectBuilder();
    if (grant && code && client && secret)
    {
      jsonresponse.add("access_token", RandomStringUtils.randomAlphabetic(12));
      jsonresponse.add("state", request.getParameter("state"));
      jsonresponse.add("token_type", "bearer");
      String built = jsonresponse.build().toString();
      response.getWriter().write(built);
      response.setHeader("Cache-Control", "no-store");
      response.setHeader("Pragma", "no-cache");
    }
    else
    {
      jsonresponse.add("error", "somethingwentwrong");
      String errors = Boolean.toString(grant) + Boolean.toString(code) + Boolean.toString(client) + Boolean.toString(secret);
      jsonresponse.add("error_desc", errors);
      String built = jsonresponse.build().toString();
      response.getWriter().write(built);
      response.sendError(401);
    }
  }
}
