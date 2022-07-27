package fr.simplex_software.security.oauth2;
import fish.payara.security.annotations.*;
import jakarta.annotation.security.*;
import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet("/secured")
@OAuth2AuthenticationDefinition(
  authEndpoint = "http://localhost:8080/oauth/auth",
  tokenEndpoint = "http://localhost:8080/oauth/auth",
  clientId = "nicolas",
  clientSecret = "secret123",
  redirectURI = "http://localhost:8080/oauth/callback")
@DeclareRoles("all")
@ServletSecurity(@HttpConstraint(rolesAllowed = "all"))
public class SecuredPage extends HttpServlet
{
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IOException
  {
    response.getWriter().println("This is a secured web page");
  }
}
