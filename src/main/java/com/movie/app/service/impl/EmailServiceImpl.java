package com.movie.app.service.impl;

import com.movie.app.dto.EmailDTO;
import com.movie.app.exception.EmailException;
import com.movie.app.response.MailJetResponse;
import com.movie.app.service.EmailService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${mailjet.api.url}")
    private String mailjetApiUrl;

    @Value("${mailjet.api.key}")
    private String apiKey;

    @Value("${mailjet.api.secret}")
    private String apiSecret;

    @Override
    public MailJetResponse sendEmail(String email, String link) throws EmailException {
        try {
            if (email == null || link == null) {
                throw new EmailException("Email details are missing");
            }

            String subject = "Please Confirm Your Email Address";

            return sendEmailToMailjet(email, subject , link);

        } catch (Exception e) {
            throw new EmailException("Failed to send email: " + e.getMessage(), e);
        }
    }

    private MailJetResponse sendEmailToMailjet(String recipient, String subject, String content) {
        HttpURLConnection connection = null;
        MailJetResponse response = null;

        try {
            URL url = new URL(mailjetApiUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            String auth = apiKey + ":" + apiSecret;
            connection.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()));
            connection.setRequestProperty("Content-Type", "application/json");

            // Create the JSON request body
            JSONObject message = new JSONObject();
            message.put("From", new JSONObject().put("Email", "ezemosesjohn21997@gmail.com").put("Name", "Eze"));
            message.put("To", new JSONArray().put(new JSONObject().put("Email", recipient)));
            message.put("Subject", subject);
            message.put("HTMLPart", content);

            JSONObject requestBody = new JSONObject();
            requestBody.put("Messages", new JSONArray().put(message));

            // Send the request
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
            }

            // Read the response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                response = new MailJetResponse("Success", responseCode);
            } else {
                response = new MailJetResponse("Failure", responseCode);
            }

        } catch (Exception e) {
            logger.error("Error while sending email via Mailjet", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return response;
    }

    @Override
    public String buildEmail(String name, String link) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n")
                .append("<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n")
                .append("<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n")
                .append("<tbody><tr>\n")
                .append("<td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n")
                .append("<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n")
                .append("<tbody><tr>\n")
                .append("<td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n")
                .append("<table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n")
                .append("<tbody><tr>\n")
                .append("<td style=\"padding-left:10px\"></td>\n")
                .append("<td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n")
                .append("<span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n")
                .append("</td>\n")
                .append("</tr>\n")
                .append("</tbody></table>\n")
                .append("</td>\n")
                .append("</tr>\n")
                .append("</tbody></table>\n")
                .append("</td>\n")
                .append("</tr>\n")
                .append("</tbody></table>\n")
                .append("<table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n")
                .append("<tbody><tr>\n")
                .append("<td width=\"10\" height=\"10\" valign=\"middle\"></td>\n")
                .append("<td>\n")
                .append("<table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n")
                .append("<tbody><tr>\n")
                .append("<td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n")
                .append("</tr>\n")
                .append("</tbody></table>\n")
                .append("</td>\n")
                .append("<td width=\"10\" valign=\"middle\" height=\"10\"></td>\n")
                .append("</tr>\n")
                .append("</tbody></table>\n")
                .append("<table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n")
                .append("<tbody><tr>\n")
                .append("<td height=\"30\"><br></td>\n")
                .append("</tr>\n")
                .append("<tr>\n")
                .append("<td width=\"10\" valign=\"middle\"><br></td>\n")
                .append("<td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n")
                .append("<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi ")
                .append(name)
                .append(",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account:</p>\n")
                .append("<blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\">")
                .append("<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"")
                .append(link)
                .append("\" style=\"color:#1D70B8;text-decoration:none;\">Activate Now</a> </p>")
                .append("</blockquote>\n")
                .append("<p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Link will expire in 15 minutes.</p>")
                .append("<p>See you soon</p>\n")
                .append("</td>\n")
                .append("<td width=\"10\" valign=\"middle\"><br></td>\n")
                .append("</tr>\n")
                .append("<tr>\n")
                .append("<td height=\"30\"><br></td>\n")
                .append("</tr>\n")
                .append("</tbody></table>\n")
                .append("<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>");

        return emailContent.toString();
    }

}