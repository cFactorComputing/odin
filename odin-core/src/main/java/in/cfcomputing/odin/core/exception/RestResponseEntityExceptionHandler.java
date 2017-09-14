/*
 * Copyright (c) 2017 cFactor Computing Pvt. Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package in.cfcomputing.odin.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler({AbstractOdinServiceException.class})
    protected ResponseEntity<Object> handleSpecific(final AbstractOdinServiceException e, final WebRequest request) {
        final ErrorResponse error = new ErrorResponse()
                .withStatus(BAD_REQUEST.value())
                .withCode(e.getCode().name())
                .withParams(e.getParams());
        LOGGER.error(String.format("Exception Occurred : %s ", error.toString()), e);
        return handleExceptionInternal(e, error, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleDefault(final Exception e, final WebRequest request) {
        final ErrorResponse error = new ErrorResponse()
                .withStatus(INTERNAL_SERVER_ERROR.value())
                .withCode(INTERNAL_SERVER_ERROR.getReasonPhrase());
        LOGGER.error(String.format("Unexpected Error Occurred :  %s ", error.toString()), e);
        return handleExceptionInternal(e, error, new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(final Exception e, final WebRequest request) {
        final ErrorResponse error = new ErrorResponse()
                .withStatus(FORBIDDEN.value())
                .withCode(FORBIDDEN.getReasonPhrase());
        LOGGER.error(String.format("Access Denied :  %s ", error.toString()), e);
        return handleExceptionInternal(e, error, new HttpHeaders(), FORBIDDEN, request);
    }
}
