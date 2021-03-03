package me.ingyun.todolist.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();

        for(FieldError fieldError : errors.getFieldErrors()){
            try{
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("field", fieldError.getField());
                jsonGenerator.writeStringField("objectName", fieldError.getObjectName());
                jsonGenerator.writeStringField("code", fieldError.getCode());
                jsonGenerator.writeStringField("defaultMessage", fieldError.getDefaultMessage());

                Object rejectedValue = fieldError.getRejectedValue();
                jsonGenerator.writeStringField("rejectedValue", rejectedValue == null ? "null" : rejectedValue.toString());
                jsonGenerator.writeEndObject();
            } catch (IOException e1){
                e1.printStackTrace();
            }
        }

        for(ObjectError e : errors.getGlobalErrors()){
            try{
                jsonGenerator.writeStartObject();;
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                jsonGenerator.writeEndObject();
            }catch(IOException e1){
                e1.printStackTrace();
            }
        }

        jsonGenerator.writeEndArray();
    }
}
