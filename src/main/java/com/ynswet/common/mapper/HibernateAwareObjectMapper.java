package com.ynswet.common.mapper;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature;

public class HibernateAwareObjectMapper
    extends ObjectMapper
{

    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     *
     * @since Ver 1.1
     */

    private static final long serialVersionUID = 1L;

    public HibernateAwareObjectMapper()
    {
        Hibernate5Module hm = new Hibernate5Module();

        hm.enable(Feature.FORCE_LAZY_LOADING );
        
        hm.disable(Feature.USE_TRANSIENT_ANNOTATION);

        registerModule( hm );

        setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        setSerializationInclusion(JsonInclude.Include.NON_NULL);

        configure( SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false );

        configure( SerializationFeature.WRITE_NULL_MAP_VALUES, false );

        configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false );

        configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );

        configure( DeserializationFeature.WRAP_EXCEPTIONS, false );
    }

}
