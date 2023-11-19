package com.bobocode.bring.web.servlet.error;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private Integer code;
    private String status;
    private String message;
    private String stackTrace;
}
