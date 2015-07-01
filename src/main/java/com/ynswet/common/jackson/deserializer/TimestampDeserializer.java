package com.ynswet.common.jackson.deserializer;


import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;



public class TimestampDeserializer extends JsonDeserializer<Date> {

    @Override
    public Timestamp deserialize(JsonParser parser, DeserializationContext context)
    throws IOException, JsonProcessingException {

        String dateFormat= "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf= new SimpleDateFormat(dateFormat);

        try{
            String fieldData= parser.getText();
            Date parsedDate = sdf.parse(fieldData);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        }catch (Exception e) {
            Calendar ca= Calendar.getInstance();
            ca.set(1970, Calendar.JANUARY, 1, 0, 0, 0);
            return new Timestamp(ca.getTime().getTime());
        }
    }
}
