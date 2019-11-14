<%-- 
    Document   : principal
    Created on : 10/11/2019, 05:42:29 PM
    Author     : Daniel
--%>
<%@page import="com.mycompany.segudoprevio.dto.Empleado"%>
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
            <div class="content-body">

                <div class="component-section no-code">

                    <div class="card card-body">

                        <span>Buscar Empleado:</span>
                        <form action="Buscar.do">
                            <div class="header-search">
                                <i data-feather="search"></i>

                                <input type="search" name="codigo"  maxlength="5" class="form-control" placeholder="Codigo del Empleado" required>
                                <button type="submit" class="btn btn-primary btn-block">Buscar</button>

                            </div>
                        </form>
                        <br><br>
                        <% String prueba=(String)request.getAttribute("estado");
                        System.err.println(""+prueba);
                            Empleado e = (Empleado) request.getAttribute("empleado");

                        if (e==null) {%>
                        

                        <%} else {%>


                        <div class="table-responsive">
                            <table class="table mg-b-0">
                                <thead>
                                    <tr>
                                        <th>Codigo</th>
                                        <th>Cedula</th>
                                        <th>Nombres</th>
                                        <th>Fecha Nacimiento</th>
                                        <th>Fecha Ingreso</th>
                                        <th>Fecha Retiro</th>
                                       
                                    </tr>
                                </thead>
                                <tbody>


                                    <tr>
                                        <td><%=e.getCodigo()%></td>
                                        <td><%=e.getCedula()%></td>
                                        <td><%=e.getNombre()%></td>
                                        <td><%=e.getFechanacimiento()%></td>
                                        <td><%=e.getFechaingreso()%></td>
                                        <td><%=e.getFecharetiro()%></td>
                                        
                            
                                </tr>

                                </tbody>
                            </table>
                        </div>
                        <%} if(prueba=="noencontrado"){%>
                         <div class="alert alert-danger" role="alert">
              Empleado No encontrado!
            </div>
                       <%}if(prueba=="eliminado"){%>
                         <div class="alert alert-danger" role="alert">
              Empleado Eliminado Correctamente!
            </div>
                       <%}%>

                        <!-- table-responsive -->
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
