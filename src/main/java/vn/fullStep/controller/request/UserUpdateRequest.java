package vn.fullStep.controller.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class UserUpdateRequest implements java.io.Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private Date birthday;
    private String username;
    private String email;
    private String phone;
    private String password;
}
