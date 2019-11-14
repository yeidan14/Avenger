package com.mycompany.segudoprevio.dto;

import com.mycompany.segudoprevio.dto.Liquidacion;
import com.mycompany.segudoprevio.dto.Tercero;
import com.mycompany.segudoprevio.dto.Tipoconcepto;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.1.v20150605-rNA", date="2019-11-12T11:47:47")
@StaticMetamodel(Concepto.class)
public class Concepto_ { 

    public static volatile SingularAttribute<Concepto, String> descripcion;
    public static volatile SingularAttribute<Concepto, Tercero> codtercero;
    public static volatile ListAttribute<Concepto, Liquidacion> liquidacionList;
    public static volatile SingularAttribute<Concepto, Tipoconcepto> tipo;
    public static volatile SingularAttribute<Concepto, String> codconcepto;

}