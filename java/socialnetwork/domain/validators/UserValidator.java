package socialnetwork.domain.validators;

import socialnetwork.domain.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserValidator implements Validator<User> {


    @Override
    public void validate(User entity) throws ValidationException {
        String error = "";
        String regexEmail = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regexEmail);
        Matcher matcher = pattern.matcher(entity.getEmail());
        if(!matcher.matches())
            error+="Invalid email\n";
        if(!entity.getName().matches("(?i)(^[a-z])((?![ .,'-]$)[a-z .,'-]){0,24}$"))
            error+="Invalid name\n";
        if(error!="")
            throw new ValidationException(error);
    }
}
