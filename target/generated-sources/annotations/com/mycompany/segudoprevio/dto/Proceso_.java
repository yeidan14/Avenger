package com.mycompany.segudoprevio.dto;

import com.mycompany.segudoprevio.dto.Liquidacion;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-11-12T11:47:47")
@StaticMetamodel(Proceso.class)
public class Proceso_ { 

    public static volatile SingularAttribute<Proceso, String> descripcion;
    public static volatile SingularAttribute<Proceso, Date> fechainicio;
    public static volatile ListAttribute<Proceso, Liquidacion> liquidacionList;
    public static volatile SingularAttribute<Proceso, Date> fechafin;
    public static volatile SingularAttribute<Proceso, Integer> id;

}