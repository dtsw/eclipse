// File Name SendHTMLEmail.java
package company.eventprocessor;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import javax.jms.Message;

public class SendHTMLEmail {

	static {
		System.setProperty("java.util.logging.config.file", "logging.properties");
	}
    private static final Logger LOGGER = Logger.getLogger(SendHTMLEmail.class.getName());

    public void send(String recipient, Message message) {

        Properties config = Configuration.getInstance().getProperties();

        // Recipient's email ID needs to be mentioned.
        String to = recipient;

        // Sender's email ID needs to be mentioned
        String from = config.getProperty("mail_sender");

        // Assuming you are sending email from localhost
        String host = config.getProperty("mail_host");

        String port = config.getProperty("mail_port");

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        StringBuilder sb = new StringBuilder();

        try {
            String[] propsToFetch = DataMapping.getAttributesForMail();
            HashMap<String, Object> props = MessagePropertiesHelper.getMessageProperties(message);
            for (int i = 0; i < propsToFetch.length; i++) {
                String bgColor = "rgb(222,222,222)";
                // zebra table
                if ((i % 2) == 0) {
                    bgColor = "rgb(255,255,255)";
                }
                sb.append(String.format("<tr><td class='title' style='padding: 5px; text-align: right; font-weight: bold; background-color: %s;'>%s</td>", bgColor, propsToFetch[i]));
                Object obj = props.get(propsToFetch[i]);
                if (obj == null) {
                    continue;
                }
                String val = obj.toString();
                if (propsToFetch[i].equalsIgnoreCase("date_reception")) {
                    val = epochToIso8601(Long.valueOf(val));
                }
                sb.append(String.format("<td style='padding: 5px; background-color: %s;'>%s</td></tr>\n", bgColor, val));
            }
        } catch (Exception e) {
            LOGGER.severe("Exception in SendHTMLEmail");
            e.printStackTrace();
        }

        // Create a default MimeMessage object.
        MimeMessage mimeMessage = new MimeMessage(session);
        MimeMultipart content = new MimeMultipart("related");

        try {

            // ContentID is used by both parts
            String cid = ContentIdGenerator.getContentId();
            String cid_header = ContentIdGenerator.getContentId();

            String title = "<tr><td colspan=\"2\"><img width='750' src=\"cid:" + cid_header + "\" /></td></tr>";
            String subTitle = "<tr><td colspan=\"2\"><h3 style='font-size: 22px; margin: 12px;'>Automated Notification</h3></td></tr>";
            String spacerRow = "<tr><td colspan=\"2\">&nbsp;</td></tr>";
            //String brandRow = "<tr><td colspan=\"2\"><div style='height: 5px; padding-top: 5px; padding-bottom: 5px; background-color: rgb(152,7,63);'></div></td></tr>";
            String imageRow = "<tr><td colspan=\"2\"><img width='750' src=\"cid:" + cid + "\" /></td></tr>";

            // The main (text) part
            MimeBodyPart mainPart = new MimeBodyPart();
            mainPart.setText("<!DOCTYPE HTML>\n<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" /></head>"
                    + "<body style='background-color: rgb(255,255,255); font-family: Arial, sans;'>"
                    + "<!--[if mso]>\n"
                    + "<style type=\"text/css\">\n"
                    + "body, table, td {font-family: Arial, Helvetica, sans-serif !important;}\n"
                    + "</style>\n"
                    + "<![endif]-->"
                    + "<table class=\"body_content\" width=\"750\">" + title + subTitle + spacerRow + sb.toString() + spacerRow + imageRow
                    + "</table>\n</body></html>", "UTF-8", "html");
            content.addBodyPart(mainPart);

            // The footer image
            MimeBodyPart imagePart = new MimeBodyPart();
            //imagePart.attachFile("resources/helsana_footer.png");
            URL footer = SendHTMLEmail.class.getClassLoader().getResource("footer.png");
            imagePart.setDataHandler(new DataHandler(footer));
            imagePart.setContentID("<" + cid + ">");   // to embed
            imagePart.setDisposition(MimeBodyPart.INLINE);  // to embed
            content.addBodyPart(imagePart);

            // The header image
            MimeBodyPart imagePart2 = new MimeBodyPart();
            //imagePart.attachFile("resources/helsana_footer.png");
            URL header = SendHTMLEmail.class.getClassLoader().getResource("header.png");
            imagePart2.setDataHandler(new DataHandler(header));
            imagePart2.setContentID("<" + cid_header + ">");   // to embed
            imagePart2.setDisposition(MimeBodyPart.INLINE);  // to embed
            content.addBodyPart(imagePart2);

            // setting defaults
            String severity = "n/a";
            String incident = "n/a";
            String cell = "n/a";
            String status = "n/a";
            try {
                severity = message.getStringProperty("severity");
                incident = message.getStringProperty("incident_id");
                cell = message.getStringProperty("bppm_cell");
                status = message.getStringProperty("status");
            } catch (Exception e) {
                LOGGER.severe("Exception in SendHTMLEmail while getting Properties from Message");
                e.printStackTrace();
            }

            mimeMessage.setContent(content);
            mimeMessage.setSubject("TrueSight Sev: " + severity + " Status: " + status + " Cell: " + cell + " Incident: " + incident);

            // Set From: header field of the header.
            mimeMessage.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));

            //mimeMessage.writeTo(new FileOutputStream(new File("c:/Users/Oliver/Desktop/mail.eml")));
            // Send message
            Transport.send(mimeMessage);
            LOGGER.info("EMAIL OK");

        } catch (Exception e) {
            LOGGER.severe("EMAIL ERROR");
            e.printStackTrace();
        }

    }

    private String epochToIso8601(long time) {
        String format = "E dd. MMM yyyy HH:mm:ss z";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date(time * 1000));
    }

    @SuppressWarnings("unused")
	private void saveMail(MimeMessage mimeMessage, MimeMultipart content) {

        try {
            // debug print email
            String subj = mimeMessage.getHeader("Subject", ",");
            //System.out.println(content.getBodyPart(0).getContent());
            String fileName = "/opt/test.html";
            Writer fileWriter = new FileWriter(fileName);
            fileWriter.write(subj);
            fileWriter.write((String) content.getBodyPart(0).getContent());
            fileWriter.close();
        } catch (Exception ex) {
            Logger.getLogger(SendHTMLEmail.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
