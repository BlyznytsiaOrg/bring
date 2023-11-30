package com.bobocode.bring.web.actuator.service;

import com.bobocode.bring.web.actuator.dto.GitInfo;
import com.bobocode.bring.web.actuator.dto.PropertiesData;

/**
 * Service interface for providing actuator information.
 * Defines methods for preparing Git information and system properties data.
 *
 * @author Blyzhnytsia Team
 * @since 1.0
 */
public interface ActuatorService {

    /**
     * Prepares Git information including branch details and the latest commit information.
     *
     * @return Git information.
     */
    GitInfo getGitInfo();

    /**
     * Prepares system properties data.
     *
     * @return System properties data.
     */
    PropertiesData preparePropertyData();

}
