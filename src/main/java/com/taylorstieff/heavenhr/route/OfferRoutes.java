package com.taylorstieff.heavenhr.route;

import com.taylorstieff.heavenhr.controller.ApplicationController;
import com.taylorstieff.heavenhr.controller.OfferController;
import com.taylorstieff.heavenhr.exception.OfferNotFoundException;
import com.taylorstieff.heavenhr.model.Application;
import com.taylorstieff.heavenhr.model.Offer;
import com.taylorstieff.heavenhr.model.dto.ExceptionError;
import com.taylorstieff.heavenhr.model.dto.OfferDto;
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
public class OfferRoutes {
    @Value("${response.header.total_pages:X-Total-Pages}")
    private String headerKeyTotalPages;

    @Value("${response.header.total_items:X-Total-Items}")
    private String headerKeyTotalItems;

    @Autowired
    private OfferController offerController;

    @Autowired
    private ApplicationController applicationController;

    @ApiOperation(value = "Find offers", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Offer.class, responseContainer = "List"),
    })
    @GetMapping(value = "/offers")
    @ResponseBody
    ResponseEntity<List<Offer>> getOffers(@RequestParam(required = false) Integer page, HttpServletResponse response) {
        if (page == null) {
            page = 0;
        }

        Page<Offer> offers = offerController.getOffers(page);

        response.setHeader(headerKeyTotalPages, String.valueOf(offers.getTotalPages()));
        response.setHeader(headerKeyTotalItems, String.valueOf(offers.getTotalElements()));

        return ResponseEntity.ok(offers.getContent());
    }

    @ApiOperation(value = "Find an Offer", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Offer.class),
            @ApiResponse(code = 404, message = "Offer not found", response = ExceptionError.class)
    })
    @GetMapping("/offer/{id}")
    @ResponseBody
    ResponseEntity<Offer> get(@PathVariable long id) {
        Optional<Offer> offer = offerController.getOffer(id);

        if (offer.isPresent()) {
            return ResponseEntity.ok(offer.get());
        }

        return ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Find all applications for a specific offer", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Application.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "No offer found with that ID", response = ExceptionError.class),
    })
    @GetMapping("/offer/{id}/applications")
    @ResponseBody
    ResponseEntity<List<Application>> getApplicationsForOffer(@PathVariable long id,
                                                              @RequestParam(required = false) Integer page,
                                                              HttpServletResponse response) throws OfferNotFoundException {
        if (page == null) {
            page = 0;
        }

        Page<Application> applications = applicationController.getApplicationsForOffer(id, page);

        response.setHeader(headerKeyTotalPages, String.valueOf(applications.getTotalPages()));
        response.setHeader(headerKeyTotalItems, String.valueOf(applications.getTotalElements()));

        return ResponseEntity.ok(applications.getContent());
    }

    @ApiOperation(value = "Create an offer", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Offer.class),
            @ApiResponse(code = 500, message = "Internal server error while creating offer")
    })
    @PostMapping("/offer")
    @ResponseBody
    ResponseEntity<Offer> post(@RequestBody OfferDto offerDto) {
        try {
            Offer offer = offerController.saveOfferDto(offerDto);

            return ResponseEntity.created(new URI("/offer/" + offer.getId())).body(offer);
        } catch (URISyntaxException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
