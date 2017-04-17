package utils.validation;

import entities.user.User;
import service.LibraryService;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidationUtil {
    public static void validateRegistrationData(User user, HttpServletRequest req,
                                                HttpServletResponse resp, LibraryService service)
            throws DataValidationException, InternalDataValidationException, SQLException {

        String password = user.getPassword();
        String confirmPassword = req.getParameter("confirmPassword");
        String username = user.getUsername();
        String email = user.getEmail();

        if (confirmPassword == null
                || !confirmPassword.equals(password)) {
            throw new DataValidationException("Confirmation password must match password");
        }
        if (service.findUserByEmail(email) != null) {
            throw new DataValidationException("The email is already taken by another user");
        }
        if (service.findUserByUsername(username) != null) {
            throw new DataValidationException("The username is already taken by another user");
        }
    }

    // TODO: 2016-02-29 review account email validation
    public static boolean isValidEmail(String email) {
        boolean flag = true;

        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            flag = false;
        }

        return flag;
    }

    public static boolean xssInjectionCheck(String stringToCheck) {
        Pattern pattern = Pattern.compile("[<>]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(stringToCheck);
        return matcher.find();
    }
}
