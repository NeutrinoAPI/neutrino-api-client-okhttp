package com.neutrinoapi.examples;

import com.neutrinoapi.client.APIResponse;
import com.neutrinoapi.client.NeutrinoAPIClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class BINListDownload {

    public static void main(String[] args) {

        Path tmpFile;
        try {
            tmpFile = Files.createTempFile("bin-list-download-", ".csv");
        } catch (IOException ex) {
            System.err.printf("Failed to create temp file: %s%n", ex);
            return;
        }

        NeutrinoAPIClient neutrinoAPI = new NeutrinoAPIClient("<your-user-id>", "<your-api-key>");
        Map<String, String> params = new HashMap<>();

        // Include ISO 3-letter country codes and ISO 3-letter currency codes in the data. These will be
        // added to columns 10 and 11 respectively
        params.put("include-iso3", "false");

        // Include 8-digit and higher BIN codes. This option includes all 6-digit BINs and all 8-digit and
        // higher BINs (including some 9, 10 and 11 digit BINs where available)
        params.put("include-8digit", "false");

        // Include all BINs and all available fields in the CSV file (overrides any values set for
        // 'include-iso3' or 'include-8digit')
        params.put("include-all", "false");

        // Set this option to 'gzip' to have the output file compressed using gzip
        params.put("output-encoding", "");

        APIResponse response = neutrinoAPI.binListDownload(params, tmpFile);
        if (response.getFile().isPresent()) {
            Path outputFile = response.getFile().get();
            // API request successful, print out the file path
            System.out.printf("API Response OK, output saved to: %s, content type: %s%n", outputFile, response.getContentType());
        } else {
            // API request failed, you should handle this gracefully!
            System.err.printf("API Error: %s, Error Code: %d, HTTP Status Code: %d%n", response.getErrorMessage(), response.getErrorCode(), response.getHttpStatusCode());
            response.getErrorCause().ifPresent(cause -> System.err.printf("Error Caused By: %s%n", cause));
        }
    }
}
