package com.mycompany.segudoprevio.dto;

import com.mycompany.segudoprevio.dto.Concepto;
import com.mycompany.segudoprevio.dto.Empleado;
import com.mycompany.segudoprevio.dto.Proceso;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-11-12T11:47:47")
@StaticMetamodel(Liquidacion.class)
public class Liquidacion_ { 

    public static volatile SingularAttribute<Liquidacion, Date> fecha;
    public static volatile SingularAttribute<Liquidacion, Empleado> codempleado;
    public static volatile SingularAttribute<Liquidacion, Proceso> numproceso;
    public static volatile SingularAttribute<Liquidacion, Long> valor;
    public static volatile SingularAttribute<Liquidacion, Integer> id;
    public static volatile SingularAttribute<Liquidacion, Concepto> codconcepto;
    public static volatile SingularAttribute<Liquidacion, Integer> codmodelo;

}