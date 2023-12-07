package io.github.blyznytsiaorg.bring.web.actuator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Represents information about Git.
 * Includes the branch information and details about the latest commit.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
@Setter
@Getter
@ToString
@Builder
public class GitInfo {
    // Git branch information
    private String branch;

    // Details about the latest commit
    private Commit commit;
}
