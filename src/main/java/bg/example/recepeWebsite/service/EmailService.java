package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.email.AbstractEmailContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final TemplateEngine templateEngine;
    private final MessageSource messageSource;
    private final JavaMailSender javaMailSender;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public EmailService(TemplateEngine templateEngine, MessageSource messageSource, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(AbstractEmailContext context) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(context.getFrom());
            mimeMessageHelper.setTo(context.getTo());
            mimeMessageHelper.setSubject(generateEmailSubject(context));
            mimeMessageHelper.setText(generateMessageContent(context), true);

            javaMailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateEmailSubject(AbstractEmailContext context) {
        return messageSource.getMessage(
                context.getSubject(),
                new Object[0],
                context.getLocale());
    }

    private String generateMessageContent(AbstractEmailContext context) {
        return templateEngine.process(context.getTemplateLocation(), context.getContext());
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bestcook@example.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);

        logger.info("Message was sent");
    }

}
