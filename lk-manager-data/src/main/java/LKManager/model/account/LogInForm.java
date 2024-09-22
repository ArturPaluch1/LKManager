package LKManager.model.account;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LogInForm {
    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;

   /* @NotEmpty(message = "email is required")
    @Email
    private String email;
*/
}