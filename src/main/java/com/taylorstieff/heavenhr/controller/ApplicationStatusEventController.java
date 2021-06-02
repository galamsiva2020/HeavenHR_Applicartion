package com.taylorstieff.heavenhr.controller;

import com.taylorstieff.heavenhr.model.Application;
import com.taylorstieff.heavenhr.model.ApplicationStatus;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

/**
 * The ApplicationStatusEventController handles status change events for {@link com.taylorstieff.heavenhr.model.Application} objects
 */
@Controller
public class ApplicationStatusEventController {

    private static final Logger log = Logger.getLogger(ApplicationStatusEventController.class);

    /**
     * Handle status changes for {@link Application} objects
     * @param application - Application where the status was updated
     * @param newApplicationStatus - New status requested
     * @param previousApplicationStatus - Previous status of application
     */
    public void handleApplicationStatusChange(Application application, ApplicationStatus newApplicationStatus, ApplicationStatus previousApplicationStatus) {
        log.info(String.format("Application status updated for application ID %s from %s to %s",
                application.getId(), previousApplicationStatus.toString(), newApplicationStatus.toString()));

        switch (newApplicationStatus) {
            case HIRED:
                hiredEvent(application, newApplicationStatus, previousApplicationStatus);
                break;
            case APPLIED:
                appliedEvent(application, newApplicationStatus, previousApplicationStatus);
                break;
            case INVITED:
                invitedEvent(application, newApplicationStatus, previousApplicationStatus);
                break;
            case REJECTED:
                rejectedEvent(application, newApplicationStatus, previousApplicationStatus);
                break;
        }
    }

    /**
     * Handler for an {@link Application} updated to {@link ApplicationStatus#REJECTED}
     * @param application - Application that was updated
     * @param newApplicationStatus - New status requested
     * @param previousApplicationStatus - Previous status of application
     */
    private void rejectedEvent(Application application, ApplicationStatus newApplicationStatus, ApplicationStatus previousApplicationStatus) {
        log.info(String.format("Handling REJECTED status change for application ID %d", application.getId()));

        // TODO: Unimplemented stub
    }

    /**
     * Handler for an {@link Application} updated to {@link ApplicationStatus#INVITED}
     * @param application - Application that was updated
     * @param newApplicationStatus - New status requested
     * @param previousApplicationStatus - Previous status of application
     */
    private void invitedEvent(Application application, ApplicationStatus newApplicationStatus, ApplicationStatus previousApplicationStatus) {
        log.info(String.format("Handling INVITED status change for application ID %d", application.getId()));

        // TODO: Unimplemented stub
    }

    /**
     * Handler for an {@link Application} updated to {@link ApplicationStatus#APPLIED}
     * @param application - Application that was updated
     * @param newApplicationStatus - New status requested
     * @param previousApplicationStatus - Previous status of application
     */
    private void appliedEvent(Application application, ApplicationStatus newApplicationStatus, ApplicationStatus previousApplicationStatus) {
        log.info(String.format("Handling APPLIED status change for application ID %d", application.getId()));

        // TODO: Unimplemented stub
    }

    /**
     * Handler for an {@link Application} updated to {@link ApplicationStatus#HIRED}
     * @param application - Application that was updated
     * @param newApplicationStatus - New status requested
     * @param previousApplicationStatus - Previous status of application
     */
    private void hiredEvent(Application application, ApplicationStatus newApplicationStatus, ApplicationStatus previousApplicationStatus) {
        log.info(String.format("Handling HIRED status change for application ID %d", application.getId()));

        // TODO: Unimplemented stub
    }
}
