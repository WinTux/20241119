package com.wintux.j20241119.Topologias;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.apache.kafka.streams.kstream.Printed.toSysOut;

@Component
@Slf4j
public class MiPrimerStreamTopology {
    public static String MENSAJE = "mensaje";
    public static String OUTPUT_MENSAJE = "output-mensaje";
    @Autowired
    public void procesar(StreamsBuilder streamsBuilder){
        KStream<String,String> mensajeStream =
                streamsBuilder.stream(MENSAJE, Consumed.with(Serdes.String(),Serdes.String()));
        mensajeStream.print(Printed.<String,String>toSysOut().withLabel("original"));

        KStream<String,String> mensajeModificadoStream =
                mensajeStream.mapValues((llave,valor)-> valor.toUpperCase());

        mensajeModificadoStream.print(Printed.<String,String>toSysOut().withLabel("modificado"));

        mensajeModificadoStream.to(OUTPUT_MENSAJE, Produced.with(Serdes.String(),Serdes.String()));

    }
}
