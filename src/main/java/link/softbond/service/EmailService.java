package link.softbond.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String email;

    /**
     * Envio de correo electronico al email proporcionado
     * @param emailTo Correo electronico receptor.
     * @param mensaje Mensaje que se envia adjunto al correo electronico.
     * @throws RuntimeException si ocurre un error al enviar el correo
     */
    public void sendListEmail(String emailTo, String mensaje) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(email);
            helper.setTo(emailTo);
            helper.setSubject("Registro Laboratorio SQL");
            helper.setText(mensaje, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
