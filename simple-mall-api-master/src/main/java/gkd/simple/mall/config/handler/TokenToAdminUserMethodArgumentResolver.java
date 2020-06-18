
package gkd.simple.mall.config.handler;

import gkd.simple.mall.common.Constants;
import gkd.simple.mall.common.SimpleMallException;
import gkd.simple.mall.common.ServiceResultEnum;
import gkd.simple.mall.config.annotation.TokenToAdminUser;
import gkd.simple.mall.dao.AdminUserMapper;
import gkd.simple.mall.dao.AdminUserTokenMapper;
import gkd.simple.mall.entity.AdminUser;
import gkd.simple.mall.entity.AdminUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenToAdminUserMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private AdminUserTokenMapper adminUserTokenMapper;

    public TokenToAdminUserMethodArgumentResolver() {
    }
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(TokenToAdminUser.class)) {
            return true;
        }
        return false;
    }
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        if (parameter.getParameterAnnotation(TokenToAdminUser.class) instanceof TokenToAdminUser) {
            AdminUser adminUser = null;
            String token = webRequest.getHeader("token");
            if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
                AdminUserToken adminUserToken = adminUserTokenMapper.selectByToken(token);
                if (adminUserToken == null || adminUserToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                    SimpleMallException.fail(ServiceResultEnum.TOKEN_EXPIRE_ERROR.getResult());
                }
                adminUser = adminUserMapper.selectByPrimaryKey(adminUserToken.getAdminUserId());
                if (adminUser == null) {
                    SimpleMallException.fail(ServiceResultEnum.USER_NULL_ERROR.getResult());
                }
                if (adminUser.getLockedFlag().intValue() == 1) {
                    SimpleMallException.fail(ServiceResultEnum.LOGIN_USER_LOCKED_ERROR.getResult());
                }
                return adminUser;
            } else {
                SimpleMallException.fail(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
        }
        return null;
    }

    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

}
