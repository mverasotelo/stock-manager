package com.mvs.stockmanager.web.rest.errors;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class BadRequestAlertException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    private final String entityName;

    private final String errorKey;

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey) {
        this(ErrorConstants.DEFAULT_TYPE, defaultMessage, entityName, errorKey);
    }

    public BadRequestAlertException(String defaultMessage, String entityName, String errorKey, String... args) {
        super(ErrorConstants.DEFAULT_TYPE, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey, args));
        this.entityName = entityName;
    this.errorKey = errorKey;

    }

    public BadRequestAlertException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, Status.BAD_REQUEST, null, null, null, getAlertParameters(entityName, errorKey));
        this.entityName = entityName;
        this.errorKey = errorKey;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getErrorKey() {
        return errorKey;
    }

    private static Map<String, Object> getAlertParameters(String entityName, String errorKey,String... args) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("message", "error." + errorKey);
        parameters.put("params", entityName);
        Arrays.stream(args).forEach((element) -> {
            int index = Arrays.asList(args).indexOf(element);
            parameters.put(String.format("arg%d", index), element);
        });        
        return parameters;
    }
}
