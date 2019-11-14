package com.mycompany.segudoprevio.dto;

import com.mycompany.segudoprevio.dto.Liquidacion;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-11-12T11:47:47")
@StaticMetamodel(Empleado.class)
public class Empleado_ { 

    public static volatile ListAttribute<Empleado, Liquidacion> liquidacionList;
    public static volatile SingularAttribute<Empleado, String> codigo;
    public static volatile SingularAttribute<Empleado, Date> fechanacimiento;
    public static volatile SingularAttribute<Empleado, String> cedula;
    public static volatile SingularAttribute<Empleado, Date> fecharetiro;
    public static volatile SingularAttribute<Empleado, Date> fechaingreso;
    public static volatile SingularAttribute<Empleado, String> nombre;

}