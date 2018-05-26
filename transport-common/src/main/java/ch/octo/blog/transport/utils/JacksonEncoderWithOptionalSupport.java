package ch.octo.blog.transport.utils;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import feign.jackson.JacksonEncoder;

import java.util.Collections;

@SuppressWarnings("unused")
public class JacksonEncoderWithOptionalSupport extends JacksonEncoder {

    public JacksonEncoderWithOptionalSupport() {
        super(Collections.singletonList(new Jdk8Module()));
    }
}
