package com.lyra.sdk.server.resource;

import com.lyra.rest.client.Client;
import com.lyra.rest.client.ClientException;
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
 * Rest controller that allows check the integrity of the server answer
 *
 * @author Lyra Network
 */
@RestController
public class VerifyResultResource {
    private final Logger logger = LoggerFactory.getLogger(VerifyResultResource.class);

    private final ServerConfiguration configuration;

    @Autowired
    public VerifyResultResource(ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    @RequestMapping(path = "/verifyResult", method = POST)
    public ResponseEntity<String> verifyResult(@RequestBody Map<String, Object> payload) {
        boolean isValid;
        logger.info("verifyResult called");
        logger.info("Request data: " + Arrays.toString(payload.entrySet().toArray()));

        try {
            isValid = Client.verifyAnswer(payload, configuration.getConfiguration());
        } catch (ClientException lce) {
            logger.error("Error when calling verifyResult", lce);
            return new ResponseEntity<>(lce.getResponseMessage() + "-" + lce.getCause(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("Answer integrity is valid?: " + isValid);
        if (isValid) {
        	return new ResponseEntity<>("{\"isAnswerIntegrityValid\" : \"true\"}", HttpStatus.OK);
        } else {
        	return new ResponseEntity<>("{\"isAnswerIntegrityValid\" : \"false\"}", HttpStatus.BAD_REQUEST);
        }
    }
}