package io.github.blyznytsiaorg.bring.web.servlet.error;

import lombok.*;

import java.time.LocalDateTime;

/**
 * The ErrorResponse class represents an object containing details about an error response.
 * It includes information such as timestamp, error code, status, error message, and stack trace.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    /**
     * The timestamp indicating when the error response was created.
     */
    private LocalDateTime timestamp;

    /**
     * The error code associated with the error response.
     */
    private Integer code;

    /**
     * The status message corresponding to the error code.
     */
    private String status;

    /**
     * The error message providing additional information about the error.
     */
    private String message;

    /**
     * The stack trace information indicating the sequence of method calls leading to the error.
     */
    private String stackTrace;
}
