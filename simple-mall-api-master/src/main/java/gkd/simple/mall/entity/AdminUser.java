
package gkd.simple.mall.entity;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdminUser implements Serializable {
    private Long adminUserId;
    private String loginName;
    private String nickName;
    private String passwordMd5;
    private Byte lockedFlag;

}