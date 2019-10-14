package the.flash.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务端Session对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    /**
     * 用户唯一性标识
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    @Override
    public String toString() {
        return userId + ":" + userName;
    }

}
