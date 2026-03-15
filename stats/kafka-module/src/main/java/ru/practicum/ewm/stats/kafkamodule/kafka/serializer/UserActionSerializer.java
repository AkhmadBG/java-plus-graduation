package ru.practicum.ewm.stats.kafkamodule.kafka.serializer;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.kafka.common.serialization.Serializer;
import ru.practicum.ewm.stats.avro.UserActionAvro;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.avro.specific.SpecificDatumWriter;

public class UserActionSerializer implements Serializer<UserActionAvro> {

    @Override
    public byte[] serialize(String topic, UserActionAvro data) {

        if (data == null) {
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            SpecificDatumWriter<UserActionAvro> writer =
                    new SpecificDatumWriter<>(UserActionAvro.class);

            BinaryEncoder encoder =
                    EncoderFactory.get().binaryEncoder(out, null);

            writer.write(data, encoder);

            encoder.flush();

            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Serialization error", e);
        }
    }

}