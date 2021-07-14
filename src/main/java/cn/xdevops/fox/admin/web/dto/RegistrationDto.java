package cn.xdevops.fox.admin.web.dto;

import cn.xdevops.fox.admin.constraint.FieldMatch;
import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@FieldMatch.List({
        @FieldMatch(
                first = "password",
                second = "confirmPassword",
                message = "The password and confirm password should be same")
})
public class RegistrationDto {
    @NotBlank(message = "Login name is mandatory")
    private String loginName;
    @NotBlank(message = "User name is mandatory")
    private String userName;
    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;
    private String phone;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotBlank(message = "Confirm password is mandatory")
    private String confirmPassword;
    @AssertTrue
    private Boolean terms;

}
