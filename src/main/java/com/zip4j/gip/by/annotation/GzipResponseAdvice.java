package com.zip4j.gip.by.annotation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

@ControllerAdvice
public class GzipResponseAdvice implements ResponseBodyAdvice<Object> {
    private static final Logger log = LoggerFactory.getLogger(GzipResponseAdvice.class);

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // Only intercept if @ZipResponse is present
        return returnType.getMethodAnnotation(GzipResponse.class) != null;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            if (body instanceof String){
                byte[] uncompressedBytes =body.toString().getBytes(StandardCharsets.UTF_8);

                try (GZIPOutputStream gzipOut = new GZIPOutputStream(byteStream)) {
                    gzipOut.write(uncompressedBytes);
                }
            } else {
                byte[] uncompressedBytes = (new ObjectMapper().writeValueAsString(body)).getBytes(StandardCharsets.UTF_8);

                try (GZIPOutputStream gzipOut = new GZIPOutputStream(byteStream)) {
                    gzipOut.write(uncompressedBytes);
                }
            }


            response.getHeaders().add(HttpHeaders.CONTENT_ENCODING, "gzip");
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            // Write compressed data directly
            response.getBody().write(byteStream.toByteArray());

            // Returning null stops Spring from doing further conversion
            return null;

        } catch (Exception e) {
            throw new RuntimeException("Failed to compress response", e);
        }
    }
}