package com.taylorstieff.heavenhr.route;

import com.taylorstieff.heavenhr.controller.ApplicationController;
import com.taylorstieff.heavenhr.exception.ApplicationNotFoundException;
import com.taylorstieff.heavenhr.exception.InvalidApplicationStatusException;
import com.taylorstieff.heavenhr.exception.OfferNotFoundException;
import com.taylorstieff.heavenhr.model.Application;
import com.taylorstieff.heavenhr.model.ApplicationStatus;
import com.taylorstieff.heavenhr.model.Offer;
import com.taylorstieff.heavenhr.model.dto.ApplicationDto;
import com.taylorstieff.heavenhr.model.dto.ExceptionError;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * The OfferRoutes class handles the routes for the {@link Offer} object
 */
@RestController
public class ApplicationRoutes {

    @Value("${response.header.total_pages:X-Total-Pages}")
    private String headerKeyTotalPages;

    @Value("${response.header.total_items:X-Total-Items}")
    private String headerKeyTotalItems;

    @Autowired
    private ApplicationController applicationController;

    @ApiOperation(value = "Find Applications", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Application.class, responseContainer = "List")
    })
    @GetMapping(value = "/applications")
    @ResponseBody
    ResponseEntity<List<Application>> getApplications(@RequestParam(required = false) Integer page, HttpServletResponse response) {
        if (page == null) {
            page = 0;
        }

        Page<Application> applications = applicationController.getApplications(page);

        response.setHeader(headerKeyTotalPages, String.valueOf(applications.getTotalPages()));
        response.setHeader(headerKeyTotalItems, String.valueOf(applications.getTotalElements()));

        return ResponseEntity.ok(applications.getContent());
    }

    @ApiOperation(value = "Find an Application", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Application.class),
            @ApiResponse(code = 404, message = "Application not found", response = ExceptionError.class)
    })
    @GetMapping("/application/{id}")
    @ResponseBody
    ResponseEntity<Application> get(@PathVariable long id) {
        Optional<Application> application = applicationController.getApplication(id);

        if (application.isPresent()) {
            return ResponseEntity.ok(application.get());
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Update an application's status", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Application.class),
            @ApiResponse(code = 404, message = "Application not found", response = ExceptionError.class)
    })
    @PutMapping("/application/{id}/{applicationStatus}")
    @ResponseBody
    ResponseEntity<Application> putStatus(@PathVariable long id, @PathVariable String applicationStatus) throws ApplicationNotFoundException, InvalidApplicationStatusException {
        ApplicationStatus applicationStatusEnum;
        try {
            applicationStatusEnum = ApplicationStatus.valueOf(applicationStatus);
        } catch (IllegalArgumentException e) {
            throw new InvalidApplicationStatusException(applicationStatus);
        }

        Application application = applicationController.updateApplicationStatus(id, applicationStatusEnum);

        return ResponseEntity.ok(application);
    }

    @ApiOperation(value = "Create an application", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Application.class),
            @ApiResponse(code = 500, message = "Internal server error while creating application")
    })
    @PostMapping("/application")
    @ResponseBody
    ResponseEntity<Application> post(@RequestBody ApplicationDto applicationDto) throws OfferNotFoundException {
        try {
            Application application = applicationController.saveApplicationDto(applicationDto);

            return ResponseEntity.created(new URI("/application/" + application.getId())).body(application);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
