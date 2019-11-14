<%-- 
    Document   : principal
    Created on : 10/11/2019, 05:42:29 PM
    Author     : Daniel
--%>
<%@page import="com.mycompany.segudoprevio.dto.Estado"%>
<%@page import="com.mycompany.segudoprevio.dao.EstadoJpaController"%>
<%@page import="com.mycompany.segudoprevio.dto.Genero"%>
<%@page import="com.mycompany.segudoprevio.dao.GeneroJpaController"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="com.mycompany.segudoprevio.controller.Conexion"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>

        <!-- Required meta tags -->
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <!-- Meta -->
        <meta name="description" content="Responsive Bootstrap 4 Dashboard and Admin Template">
        <meta name="author" content="ThemePixels">

        <!-- Favicon -->
        <link rel="shortcut icon" type="image/x-icon" href="Librerias/assets/img/favicon.png">
        <link href="https://www.jqueryscript.net/css/jquerysctipttop.css" rel="stylesheet" type="text/css">
        <title>Principal</title>

        <!-- vendor css -->
        <link href="Librerias/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
        <link href="Librerias/ionicons/css/ionicons.min.css" rel="stylesheet">
        <link href="Librerias/jqvmap/jqvmap.min.css" rel="stylesheet">

        <!-- template css -->
        <link rel="stylesheet" href="Librerias/assets/css/cassie.css">
    </head>
    <body>
        <div   id="menu" >


        </div>
        <div class="content">
            <div class="header">
                <div class="header-left">
                    <a href="" class="burger-menu"><i data-feather="menu"></i></a>


                </div><!-- header-left -->
                <div class="header-right" id="user">

                </div><!-- header -->
            </div>
            <div class="content-header">
                <div>
                    <nav aria-label="breadcrumb">
                        <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="#">Dashboard</a></li>
                            <li class="breadcrumb-item" aria-current="page"><a href="#">Pagina en Blanco</a></li>

                        </ol>
                    </nav>
                    <h4 class="content-title content-title-xs"></h4>
                </div>

            </div><!-- content-header -->
            <%
                //CONECTANOD A LA BASE DE DATOS:
                Conexion cn = Conexion.getConexion();
                Connection con = Conexion.getConexion2();
                PreparedStatement ps;
                //Emnpezamos Listando los Datos de la Tabla Usuario
                Statement smt;
                ResultSet rs;
                smt = con.createStatement();
                rs = smt.executeQuery("select * from heroe");
                //Creamo la Tabla:     
            %>


            <table class="table table-striped jambo_table bulk_action"  id="tablaDatos">
                <thead>
                    <tr>

                        <th class="column-title text-center">id</th>
                        <th class="column-title text-center">Nombre</th>
                        <th class="column-title text-center">Heroe</th>
                        <th class="column-title text-center">Fecha Nacimiento</th>
                        <th class="column-title text-center">Fecha Aparicion</th>
                        <th class="column-title text-center">Genero</th>
                        <th class="column-title text-center">Estado</th>
                        <th class="column-title text-center">Arma</th>
                        <th class="column-title text-center">Descripcion</th>


                    </tr>
                </thead>
                <tbody id="tbodys">
                    <%
                        while (rs.next()) {

                    %>
                    <tr>


                        <td class="text-center"><%= rs.getString("id")%></td>
                        <td class="text-center"><%= rs.getString("nombre")%></td>
                        <td class="text-center"><%= rs.getString("heroe")%></td>
                        <td class="text-center"><%= rs.getString("fechanacimiento")%></td>
                        <td class="text-center"><%= rs.getString("fechaaparicion")%></td>
                        <td class="text-center"><%GeneroJpaController tp = new GeneroJpaController(cn.getBd());
                            Genero g = tp.findGenero(rs.getString("genero"));

                            %>
                            <%=g.getDescripcion()%>
                        </td>
                        <td class="text-center ">
                            <%if (rs.getString("estado") != null) {
                                          EstadoJpaController ter = new EstadoJpaController(cn.getBd());
                                          Estado est= ter.findEstado(rs.getString("estado")); %>

                            <%=est.getDescripcion()%> 

                            <%} else {
                            %>

                            <%= rs.getString("codtercero")%>
                            <%}%>
                        </td>
<td class="text-center"><%= rs.getString("arma")%></td>
<td class="text-center"><%= rs.getString("descripcion")%></td>

                    </tr>
                    <%}%>
            </table> 



        </div>
    </div>
</div><!-- content-body -->
<!-- content -->
</div>
<script src="Librerias/jquery/jquery.min.js"></script>
<script src="Librerias/bootstrap/js/bootstrap.bundle.min.js"></script>
<script src="Librerias/feather-icons/feather.min.js"></script>
<script src="Librerias/perfect-scrollbar/perfect-scrollbar.min.js"></script>
<script src="Librerias/js-cookie/js.cookie.js"></script>
<script src="Librerias/chart.js/Chart.bundle.min.js"></script>
<script src="Librerias/jquery.flot/jquery.flot.js"></script>
<script src="Librerias/jquery.flot/jquery.flot.stack.js"></script>
<script src="Librerias/jquery.flot/jquery.flot.resize.js"></script>
<script src="Librerias/jquery.flot/jquery.flot.threshold.js"></script>
<script src="Librerias/jqvmap/jquery.vmap.min.js"></script>
<script src="Librerias/jqvmap/maps/jquery.vmap.world.js"></script>

<script src="Librerias/assets/js/cassie.js"></script>
<script src="Librerias/assets/js/flot.sampledata.js"></script>
<script src="Librerias/assets/js/vmap.sampledata.js"></script>
<script src="Librerias/assets/js/dashboard-one.js"></script>
</body>
</html>
