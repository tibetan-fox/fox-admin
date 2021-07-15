package cn.xdevops.fox.admin.domain.authentication.handler;

import cn.xdevops.fox.admin.application.service.TokenService;
import cn.xdevops.fox.admin.domain.authentication.constants.AuthenticationConstants;
import cn.xdevops.fox.admin.domain.authentication.LoginUser;
import cn.xdevops.fox.admin.common.utils.ServletUtils;
import cn.xdevops.fox.admin.common.utils.StringUtils;
import cn.xdevops.fox.admin.common.utils.async.AsyncFactory;
import cn.xdevops.fox.admin.common.utils.async.AsyncManager;
import cn.xdevops.fox.admin.web.dto.AjaxResult;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Configuration
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler
{
    @Autowired
    private TokenService tokenService;

    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, AuthenticationConstants.LOGOUT, "退出成功"));
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.error(HttpStatus.OK.value(), "退出成功")));
    }
}
