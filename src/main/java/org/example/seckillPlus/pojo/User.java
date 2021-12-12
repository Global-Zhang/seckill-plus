package org.example.seckillPlus.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.seckillPlus.validator.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

        private String username;
        private Long id;
        @NotNull
        @IsMobile
        private Long mobile;
        @NotNull
        @Length(min = 32)
        private String password;
        private String salt;
        private int loginCount;
        private Date RegisterDate;
        private Date lastLoginDate;

}
