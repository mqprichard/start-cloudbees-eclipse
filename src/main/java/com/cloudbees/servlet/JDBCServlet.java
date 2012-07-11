package com.cloudbees.servlet;

import com.cloudbees.jdbc.DAO;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 *
 * @author harpreet
 */
public class JDBCServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ServletConfig config;
	
	public void init(ServletConfig config) throws ServletException {
	    this.config = config;
	    super.init(config);
	}	

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            prefixHTML(out);
            
            ServletContext application = config.getServletContext();
            String environment = 
              application.getInitParameter("application.environment"); 
            out.println("Environment is ["+ environment + "] <br/><br/>");
            
            DAO dao = new DAO();
            dao.connect();
            ResultSet rst = dao.getAll();
            if (rst != null) {
                while (rst.next()) {
                    int id = rst.getInt(1);
                    String country = rst.getString(2);
                    String capital = rst.getString(3);
                    out.println(country + ", " + capital + "<br/>");
                }
            } else {
                out.println("failed to get data from database <br/>");
            }
            dao.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postfixHTML(out);
            out.close();
        }
    }

    void prefixHTML(PrintWriter out) {
        out.println("<html><head><title>CloudBees Tutorial</title></head><body>");
    }

    void postfixHTML(PrintWriter out) {
        out.println("</body></html>");
    }

    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "JDBCServlet for CloudBees Tutorial";
    }
}
