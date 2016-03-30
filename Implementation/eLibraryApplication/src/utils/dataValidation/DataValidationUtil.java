package utils.dataValidation;

import data.AccountDB;
import models.Account;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Leonti on 2016-03-28.
 */
public class DataValidationUtil {
    public static void validateRegistrationData(Account account, HttpServletRequest req,
                                                HttpServletResponse resp)
            throws DataValidationException, InternalDataValidationException, SQLException {

        String password = account.getPassword();
        String confirmPassword = req.getParameter("confirmPassword");
        String username = account.getUsername();
        String email = account.getEmail();

        if (confirmPassword == null
                || !confirmPassword.equals(password)) {
            throw new DataValidationException("Confirmation password must match password");
        }
        if (AccountDB.emailExists(email)) {
            throw new DataValidationException("The email is already taken by another user");
        }
        if (AccountDB.usernameExists(username)) {
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
