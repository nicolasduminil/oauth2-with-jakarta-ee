package fr.simplex_software.security.oauth2;

import jakarta.servlet.*;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;

import java.io.*;

@WebServlet("/unsecured")
public class UnsecuredPage extends HttpServlet
{
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.getWriter().print("This is an unsecured web page");
  }
}

