package tech.lapsa.javax.jms;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import tech.lapsa.java.commons.function.MyOptionals;

public class MyConstraintViolationExcetpion extends ConstraintViolationException {

    private static final long serialVersionUID = 1L;

    MyConstraintViolationExcetpion(Set<? extends ConstraintViolation<?>> constraintViolations) {
	super(MyOptionals.of(constraintViolations) //
		.map(x -> x.stream() //
			.map(y -> String.format("%1$s '%2$s.%3$s' was %4$s",
				y.getMessage(), // 1
				y.getRootBeanClass().getName(), // 2
				y.getPropertyPath(), // 3
				y.getInvalidValue() // 4
			)).collect(Collectors.joining("; "))) //
		.orElse("None violations"), //
		constraintViolations);
    }

}
