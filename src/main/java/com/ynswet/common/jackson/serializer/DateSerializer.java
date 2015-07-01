package com.ynswet.common.jackson.serializer;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;


public class DateSerializer extends JsonSerializer<Calendar> {

    @Override
    public void serialize(Calendar date, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate= sdf.format(date.getTime());
        gen.writeString(formattedDate);
    }

}

