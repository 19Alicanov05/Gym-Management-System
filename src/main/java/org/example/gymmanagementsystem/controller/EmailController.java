package org.example.gymmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.example.gymmanagementsystem.service.EmailService;

@RestController
@RequestMapping("/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @Operation(summary = "Send Email (With Attachment)", description = "This endpoint allows sending an email with PDF or other file attachments.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email sent successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping(value = "/attachment", consumes = "multipart/form-data")
    public String sendEmailWithAttachment(
            @RequestParam("to") String toEmail,
            @RequestParam("subject") String subject,
            @RequestParam("body") String body,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        emailService.sendEmailWithAttachment(toEmail, subject, body, file);
        return "Email sent successfully with attachment!";
    }
}
