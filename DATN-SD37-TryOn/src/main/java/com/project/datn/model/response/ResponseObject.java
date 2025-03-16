package com.project.datn.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseObject<T> extends ResponseEntity<ResponseObject.Payload<T>> {

    public ResponseObject(HttpStatus status, String message, T data) {
        super(new Payload<>(status.value(), message, data), status);
    }

    @Builder
    public static class Payload<T> {
        public int code;
        public String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public T data;

        // Constructor
        public Payload(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }
}