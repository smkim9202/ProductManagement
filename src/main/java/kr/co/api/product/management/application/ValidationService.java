package kr.co.api.product.management.application;

import jakarta.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class ValidationService {
    public <T> void checkValid(@Valid T validationTarget){
    //public <T> void checkValid(@Valid Product validationTarget){ //오직 Product만의 checkValid
        //do nothing
    }
}
