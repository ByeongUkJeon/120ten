package models.vaildators;

import java.util.ArrayList;
import java.util.List;

import models.User;

public class UserValidate {
    
    public static List<String> validate(User u, String confirmPassword) {
        List<String> errors = new ArrayList<String>();
        
        String account_error = validateAccount(u.getAccount());
        
        if(!account_error.equals("")) {
            errors.add(account_error);
        }

        String password_error = validatePassword(u.getPassword(), confirmPassword);
        if(!password_error.equals("")) {
            errors.add(password_error);
        }

        String username_error = validateUsername(u.getUsername());
        if(!username_error.equals("")) {
            errors.add(username_error);
        }

        return errors;
    }
    
    private static String validateAccount(String account) {
        if(account == null || account.equals("")) {
            return "アカウントを入力してください。";
        }

        return "";
    }

    // メッセージの必須入力チェック
    private static String validatePassword(String password, String confirmPassword) {
        if(password == null || password.equals("")) {
            return "暗証番号を入力してください。";
        }
        if (!password.equals(confirmPassword)) {
            return "暗証番号と確認用暗証番号が違います。";

        }
        return "";
    }
    // メッセージの必須入力チェック
    private static String validateUsername(String username) {
        if(username == null || username.equals("")) {
            return "ニックネームを入力してください。";
        }

        return "";
    }
    
}
