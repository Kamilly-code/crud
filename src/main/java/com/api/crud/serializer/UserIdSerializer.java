package com.api.crud.serializer;

import com.api.crud.models.UserModel;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class UserIdSerializer extends JsonSerializer<UserModel> {
    @Override
    public void serialize(UserModel value, JsonGenerator gen, SerializerProvider serializers)
            throws IOException {
        gen.writeString(value.getId());
    }
}
