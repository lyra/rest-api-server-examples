package com.lyra.sdk.server.resource;

import com.lyra.rest.client.Client;
import com.lyra.rest.client.ClientException;
import com.lyra.rest.client.ClientResource;
import com.lyra.sdk.server.util.ServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Rest controller that allows to invoke the payment platform in order to create a payment or token instance
 * and return all the details in JSON format.
 * <p>
 * The information retrieved should be immediately provided to mobile SDK.
 *
 * @author Lyra Network
 */
@RestController
public class CreateResource {
    private final Logger logger = LoggerFactory.getLogger(CreateResource.class);
    private final ServerConfiguration configuration;

    @Autowired
    public CreateResource(ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Uses client API in order to create a payment and returns the formToken associated with payment session
     * that should be provided to client mobile SDK
     *
     * @param payload the payment session input data
     * @return ResponseEntity<String> JSON string containing the formToken
     */
    @RequestMapping(path = "/createPayment", method = POST)
    public ResponseEntity<String> createPayment(@RequestBody Map<String, Object> payload) {
        logger.info("createPayment called");
        logger.info("Request data: {}", Arrays.toString(payload.entrySet().toArray()));

        if (!validatePayloadData(payload)) {
            return new ResponseEntity<>("Server cannot process this request because it is invalid", HttpStatus.BAD_REQUEST);
        }

        //Handle errors here

        return executeOperation(ClientResource.CREATE_PAYMENT.toString(), payload);
    }

    /**
     * Uses client API in order to create a payment by token and returns the formToken associated with payment session
     * that should be provided to client mobile SDK
     *
     * @param payload the payment session input data
     * @return ResponseEntity<String> JSON string containing the formToken
     */
    @RequestMapping(path = "/createToken", method = POST)
    public ResponseEntity<String> createRegister(@RequestBody Map<String, Object> payload) {
        logger.info("createToken called");
        logger.info("Request data: {}", Arrays.toString(payload.entrySet().toArray()));

        if (!validatePayloadData(payload)) {
            return new ResponseEntity<>("Server cannot process this request because it is invalid", HttpStatus.BAD_REQUEST);
        }

        //Handle errors here

        return executeOperation(ClientResource.CREATE_TOKEN.toString(), payload);
    }

    /*
     * Method that uses the client library in order to call the requested operation of the payment Rest API
     */
    private ResponseEntity<String> executeOperation(String operation, Map<String, Object>  payload) {
        String responseMessage;
        try {
            responseMessage = Client.post(operation, payload, configuration.getConfiguration());
        } catch (ClientException lce) {
            logger.error("Error when calling " + operation, lce);
            //Check exception and sent code and message if possible
            if (lce.getResponseCode() > 0) {
                return new ResponseEntity<>(lce.getResponseMessage(), HttpStatus.valueOf(lce.getResponseCode()));
            } else {
                return new ResponseEntity<>(lce.getResponseMessage() + "-" + lce.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        logger.info("Response OK: " + responseMessage);
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    /*
     * Checks sent data in order to detect incoherence with real payment data: amount, currency
     *
     * FIXME: this method should be implemented in order to avoid attacks that modify the payload content
     */
    private boolean validatePayloadData(Map<String, Object> payload) {
        //TODO: implement method
        return true;
    }
}