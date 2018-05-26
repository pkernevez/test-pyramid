package ch.octo.blog.transport.utils;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import feign.jackson.JacksonDecoder;

import java.util.Collections;

@SuppressWarnings("unused")
public class JacksonDecoderWithOptionalSupport extends JacksonDecoder {

    public JacksonDecoderWithOptionalSupport() {
        super(Collections.singletonList(new Jdk8Module()));
    }
}
