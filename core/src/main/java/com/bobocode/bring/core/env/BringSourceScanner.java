package com.bobocode.bring.core.env;

import java.io.File;
import java.util.List;

/**
 * Interface for scanning Bring sources to retrieve a list of files based on a specified type.
 *
 *  @author Blyzhnytsia Team
 *  @since 1.0
 */
public interface BringSourceScanner {

    /**
     * Scans the Bring sources for files of a specified type.
     *
     * @param type The type of files to scan for.
     * @return A list of File objects representing the files found of the specified type.
     */
    List<File> scan(String type);
}
