package ru.lanwen.junit.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.lanwen.junit.beans.ServerError;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        ServerError error = new ServerError()
                .withStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                .withMessage(exception.getMessage());

        if (exception instanceof WebApplicationException) {
            error.setStatus(((WebApplicationException) exception).getResponse().getStatus());
        }

        LOGGER.error("An exception occurs during processing request: ", exception);
        return Response.status(error.getStatus()).entity(error).build();
    }
}
