package com.ynswet.common.jackson.deserializer;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;



public class DateDeserializer extends JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonParser parser, DeserializationContext context)
    throws IOException, JsonProcessingException {
        String dateFormat= "yyyy-MM-dd";
        SimpleDateFormat sdf= new SimpleDateFormat(dateFormat);
        try{
            String fieldData= parser.getText();
            return sdf.parse(fieldData);
        }catch (Exception e) {
            Calendar ca= Calendar.getInstance();
            ca.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
            return ca.getTime();
        }
    }
}
