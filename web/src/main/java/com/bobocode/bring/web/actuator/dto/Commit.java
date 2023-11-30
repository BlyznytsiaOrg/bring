package com.bobocode.bring.web.actuator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents information about a Git commit.
 * Includes the commit ID, commit message, and commit timestamp.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Setter
@Getter
@ToString
@Builder
public class Commit {
    // Commit ID
    private String id;

    // Commit message
    private String message;

    // Commit timestamp
    private String time;
}
