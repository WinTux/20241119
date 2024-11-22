package com.wintux.j20241119.Topologias;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wintux.principal.Models.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class MiSegundoStreamTopology {
    public static String MENSAJE = "jsonTpc";
    public static String OUTPUT_MENSAJE="output-mensaje-json";
    @Autowired
    public void procesar(StreamsBuilder streamsBuilder){
        /*
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL, 
                JsonTypeInfo.As.PROPERTY
        );
        objectMapper.deactivateDefaultTyping();
        JsonSerde<Usuario> usuarioSerde = new JsonSerde<>(Usuario.class, objectMapper);
        usuarioSerde.configure(Map.of(JsonDeserializer.TRUSTED_PACKAGES, "com.wintux.principal.Models"),false);
        */
        KStream<String, Usuario> mensajeStream =
                streamsBuilder.stream(MENSAJE, Consumed.with(Serdes.String(),new JsonSerde<>(Usuario.class)));
        // Mostrando el mensaje original
        mensajeStream.print(Printed.<String,Usuario>toSysOut().withLabel("original"));
        KStream<String,Usuario> mensajeModificadoStream =
                mensajeStream.mapValues(
                        (llave,valor)-> new Usuario(
                                valor.getId(),
                                valor.getNombre().toUpperCase(),
                                valor.getApellido().toUpperCase())
                );
        // Mostrando el mensaje modificado
        mensajeModificadoStream.print(Printed.<String,Usuario>toSysOut().withLabel("modificado"))
                ;
        mensajeModificadoStream.to(OUTPUT_MENSAJE, Produced.with(Serdes.String(),new JsonSerde<>(Usuario.class)));
    }
}
