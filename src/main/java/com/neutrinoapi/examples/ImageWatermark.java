package com.neutrinoapi.examples;

import com.neutrinoapi.client.APIResponse;
import com.neutrinoapi.client.NeutrinoAPIClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ImageWatermark {

    public static void main(String[] args) {

        Path tmpFile;
        try {
            tmpFile = Files.createTempFile("image-watermark-", ".png");
        } catch (IOException ex) {
            System.err.printf("Failed to create temp file: %s%n", ex);
            return;
        }

        NeutrinoAPIClient neutrinoAPI = new NeutrinoAPIClient("<your-user-id>", "<your-api-key>");
        Map<String, String> params = new HashMap<>();

        // The resize mode to use, we support 3 main resizing modes:
        // • scale Resize to within the width and height specified while preserving aspect ratio. In this
        //   mode the width or height will be automatically adjusted to fit the aspect ratio
        // • pad Resize to exactly the width and height specified while preserving aspect ratio and pad
        //   any space left over. Any padded space will be filled in with the 'bg-color' value
        // • crop Resize to exactly the width and height specified while preserving aspect ratio and crop
        //   any space which fall outside the area. The cropping window is centered on the original image
        params.put("resize-mode", "scale");

        // The output image format, can be either png or jpg
        params.put("format", "png");

        // If set resize the resulting image to this width (in px)
        params.put("width", "");

        // The URL or Base64 encoded Data URL for the source image. You can also upload an image file
        // directly using multipart/form-data
        params.put("image-url", "https://www.neutrinoapi.com/img/LOGO.png");

        // The position of the watermark image, possible values are: center, top-left, top-center,
        // top-right, bottom-left, bottom-center, bottom-right
        params.put("position", "center");

        // The URL or Base64 encoded Data URL for the watermark image. You can also upload an image file
        // directly using multipart/form-data
        params.put("watermark-url", "https://www.neutrinoapi.com/img/icons/security.png");

        // The opacity of the watermark (0 to 100)
        params.put("opacity", "50");

        // The image background color in hexadecimal notation (e.g. #0000ff). For PNG output the special
        // value of 'transparent' can also be used. For JPG output the default is black (#000000)
        params.put("bg-color", "transparent");

        // If set resize the resulting image to this height (in px)
        params.put("height", "");

        APIResponse response = neutrinoAPI.imageWatermark(params, tmpFile);
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
