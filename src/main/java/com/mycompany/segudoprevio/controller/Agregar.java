/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.segudoprevio.controller;

import com.mycompany.segudoprevio.dao.EmpleadoJpaController;

import com.mycompany.segudoprevio.dto.Empleado;

import java.io.IOException;

import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Daniel
 */
public class Agregar extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cod = request.getParameter("codigo");
        String cedula = request.getParameter("cedula");
        String nombres = request.getParameter("nombre");
        String fnacimiento = request.getParameter("fnacimiento");
        Date fn = Date.valueOf(fnacimiento);
        String fingreso = request.getParameter("fingreso");
        Date fi = Date.valueOf(fingreso);
        String fretiro = request.getParameter("fretiro");
        Date fr = Date.valueOf(fretiro);

        Empleado e = new Empleado();
        e.setCedula(cedula);
        e.setCodigo(cod);
        e.setNombre(nombres);
        e.setFechanacimiento(fn);
        e.setFechaingreso(fr);
        e.setFecharetiro(fr);
        Conexion con = Conexion.getConexion();
        EmpleadoJpaController per = new EmpleadoJpaController(con.getBd());

        try {
            if (per.findEmpleado(cod) == null) {
                per.create(e);
                String registrado = "registrado";
                request.setAttribute("registrado", registrado);
                request.getRequestDispatcher("Agregar.jsp").forward(request, response);

            } else {

                String registrado = "yaexiste";
                request.setAttribute("registrado", registrado);
                request.getRequestDispatcher("Agregar.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            Logger.getLogger(Agregar.class.getName()).log(Level.SEVERE, null, ex);
            response.sendRedirect("erroruser.jsp");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
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
     *
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
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
