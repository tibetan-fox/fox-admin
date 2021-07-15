package cn.xdevops.fox.admin.application.service;

import cn.xdevops.fox.admin.domain.authentication.*;
import cn.xdevops.fox.admin.domain.authentication.constants.AuthenticationConstants;
import cn.xdevops.fox.admin.domain.authentication.exception.CaptchaException;
import cn.xdevops.fox.admin.domain.authentication.exception.CaptchaExpireException;
import cn.xdevops.fox.admin.domain.authentication.exception.UserPasswordNotMatchException;
import cn.xdevops.fox.admin.domain.cache.CacheConstants;
import cn.xdevops.fox.admin.common.exception.CustomException;
import cn.xdevops.fox.admin.infrastructure.entity.SysUser;
import cn.xdevops.fox.admin.common.utils.MessageUtils;
import cn.xdevops.fox.admin.common.utils.ServletUtils;
import cn.xdevops.fox.admin.common.utils.async.AsyncFactory;
import cn.xdevops.fox.admin.common.utils.async.AsyncManager;
import cn.xdevops.fox.admin.common.utils.date.DateUtils;
import cn.xdevops.fox.admin.common.utils.ip.IpUtils;
import cn.xdevops.fox.admin.common.utils.redis.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserService userService;

    @Autowired
    private SystemConfigService systemConfigService;

    @Autowired
    private TokenService tokenService;



    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid)
    {
        boolean captchaOnOff = systemConfigService.selectCaptchaOnOff();
        // 验证码开关
        if (captchaOnOff)
        {
            validateCapcha(username, code, uuid);
        }
        // 用户验证
        Authentication authentication = null;
        try
        {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (Exception e)
        {
            if (e instanceof BadCredentialsException)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, AuthenticationConstants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            }
            else
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, AuthenticationConstants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, AuthenticationConstants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        recordLoginInfo(loginUser.getUser());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    private void validateCapcha(String username, String code, String uuid)
    {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, AuthenticationConstants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, AuthenticationConstants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     */
    private void recordLoginInfo(SysUser user)
    {
        user.setLoginIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
        user.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(user);
    }
}
