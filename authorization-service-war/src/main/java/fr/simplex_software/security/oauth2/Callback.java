package fr.simplex_software.security.oauth2;

import fish.payara.security.oauth2.api.*;
import jakarta.inject.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet("/callback")
public class Callback extends HttpServlet
{
  @Inject
  OAuth2AccessToken token;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String accessToken = token.getAccessToken();
    response.getWriter().println(accessToken);
  }
}

