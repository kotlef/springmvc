package com.ynswet.common.jackson.deserializer;

import java.io.IOException;
import java.math.BigDecimal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import static java.math.BigDecimal.ROUND_HALF_UP;

public class MoneyDeserializer
    extends JsonDeserializer<BigDecimal>
{

    @Override
    public BigDecimal deserialize( JsonParser parser, DeserializationContext context )
        throws IOException, JsonProcessingException
    {

        return new BigDecimal( parser.getText() ).setScale( 2, ROUND_HALF_UP );

    }
}
