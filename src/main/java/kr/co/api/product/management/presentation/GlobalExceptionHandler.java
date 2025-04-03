package kr.co.api.product.management.presentation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import kr.co.api.product.management.domain.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolatedException(ConstraintViolationException ex){
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        //List<String> errors = constraintViolations.stream().map(
        //        constraintViolation -> extractField(constraintViolation.getPropertyPath()) + ", " + constraintViolation.getMessage()).toList();

        List<String> errors = new ArrayList<>();
        for(ConstraintViolation<?> constraintViolation : constraintViolations ) {
            errors.add(extractField(constraintViolation.getPropertyPath()) + ", " + constraintViolation.getMessage());
        }
        // 불변 리스트로 변환 (Stream의 toList()와 동일한 효과)
        errors = List.copyOf(errors);

        ErrorMessage errorMessage = new ErrorMessage(errors);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        //List<String> errors = fieldErrors.stream().map(
        //        fieldError -> fieldError.getField() + ", " + fieldError.getDefaultMessage()).toList();

        List<String> errors = new ArrayList<>();
        for(FieldError fieldError : fieldErrors ) {
            errors.add(fieldError.getField() + ", " + fieldError.getDefaultMessage());
        }
        // 불변 리스트로 변환 (Stream의 toList()와 동일한 효과)
        errors = List.copyOf(errors);

        ErrorMessage errorMessage = new ErrorMessage(errors);
        return new ResponseEntity(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleEntityNotFoundExceptionException(
            EntityNotFoundException ex
    ) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());

        ErrorMessage errorMessage = new ErrorMessage(errors);
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    private String extractField(Path path){
        String[] splittedArray = path.toString().split("[.]");
        int lastIndex = splittedArray.length - 1;
        return splittedArray[lastIndex];
    }



}
