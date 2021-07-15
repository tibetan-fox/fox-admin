package cn.xdevops.fox.admin.common.utils.async;

import cn.xdevops.fox.admin.application.service.LoginInfoService;
import cn.xdevops.fox.admin.application.service.OperLogService;
import cn.xdevops.fox.admin.common.constants.Constants;
import cn.xdevops.fox.admin.domain.authentication.constants.AuthenticationConstants;
import cn.xdevops.fox.admin.infrastructure.entity.SysLogininfor;
import cn.xdevops.fox.admin.infrastructure.entity.SysOperLog;
import cn.xdevops.fox.admin.common.utils.ServletUtils;
import cn.xdevops.fox.admin.common.utils.ip.AddressUtils;
import cn.xdevops.fox.admin.common.utils.ip.IpUtils;
import cn.xdevops.fox.admin.common.utils.log.LogUtils;
import cn.xdevops.fox.admin.common.utils.spring.SpringUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public class AsyncFactory
{
    private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status 状态
     * @param message 消息
     * @param args 列表
     * @return 任务task
     */
    public static TimerTask recordLogininfor(final String username, final String status, final String message,
                                             final Object... args)
    {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        return new TimerTask()
        {
            @Override
            public void run()
            {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                sys_user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                SysLogininfor loginInfo = new SysLogininfor();
                loginInfo.setUserName(username);
                loginInfo.setIpaddr(ip);
                loginInfo.setLoginLocation(address);
                loginInfo.setBrowser(browser);
                loginInfo.setOs(os);
                loginInfo.setMsg(message);
                // 日志状态
                if (AuthenticationConstants.LOGIN_SUCCESS.equals(status) || AuthenticationConstants.LOGOUT.equals(status))
                {
                    loginInfo.setStatus(Constants.SUCCESS);
                }
                else if (AuthenticationConstants.LOGIN_FAIL.equals(status))
                {
                    loginInfo.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(LoginInfoService.class).insertLogininfor(loginInfo);
            }
        };
    }

    /**
     * 操作日志记录
     *
     * @param operLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOper(final SysOperLog operLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                // 远程查询操作地点
                operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
                SpringUtils.getBean(OperLogService.class).insertOperlog(operLog);
            }
        };
    }
}
