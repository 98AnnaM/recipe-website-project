package bg.example.recepeWebsite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Locale;

@Service
public class EmailService {

  private final TemplateEngine templateEngine;
  private final MessageSource messageSource;
  private final JavaMailSender javaMailSender;
  private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

  public EmailService(TemplateEngine templateEngine,
                      MessageSource messageSource,
                      JavaMailSender javaMailSender) {
    this.templateEngine = templateEngine;
    this.messageSource = messageSource;
    this.javaMailSender = javaMailSender;
  }

  public void sendRegistrationEmail(
    String userEmail,
    String userName,
    Locale preferredLocale
  ) {

    try {
      Socket socket = new Socket();
      socket.connect(new InetSocketAddress("localhost", 1025), 1000);
      socket.close();
    } catch (IOException e) {
      logger.error("There is no connection to the MailHog server.");
      return;
    }

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
      mimeMessageHelper.setFrom("bestCook@bestCook.com");
      mimeMessageHelper.setTo(userEmail);
      mimeMessageHelper.setSubject(getEmailSubject(preferredLocale));
      mimeMessageHelper.setText(generateMessageContent(preferredLocale, userName), true);

      javaMailSender.send(mimeMessageHelper.getMimeMessage());
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  private String getEmailSubject(Locale locale) {
    return messageSource.getMessage(
        "registration_subject",
        new Object[0],
        locale);
  }

  private String generateMessageContent(Locale locale,
                                        String userName) {
    Context ctx = new Context();
    ctx.setLocale(locale);
    ctx.setVariable("userName", userName);
    return templateEngine.process("email/registration", ctx);
  }

}
