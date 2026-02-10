package com.lovable.lovable_clone.error;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class BadRequestException extends RuntimeException{

    public BadRequestException(String message){
        super(message);
    }
}
