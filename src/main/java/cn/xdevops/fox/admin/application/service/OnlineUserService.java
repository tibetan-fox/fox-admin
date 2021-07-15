package cn.xdevops.fox.admin.application.service;

import cn.xdevops.fox.admin.common.utils.StringUtils;
import cn.xdevops.fox.admin.domain.authentication.LoginUser;
import cn.xdevops.fox.admin.domain.session.OnlineUser;
import org.springframework.stereotype.Service;

/**
 * 在线用户 服务层处理
 *
 * @author ruoyi
 */
@Service
public class OnlineUserService
{
    /**
     * 通过登录地址查询信息
     *
     * @param ipaddr 登录地址
     * @param user 用户信息
     * @return 在线用户信息
     */
    public OnlineUser selectOnlineByIpaddr(String ipaddr, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过用户名称查询信息
     *
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    public OnlineUser selectOnlineByUserName(String userName, LoginUser user)
    {
        if (StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 通过登录地址/用户名称查询信息
     *
     * @param ipaddr 登录地址
     * @param userName 用户名称
     * @param user 用户信息
     * @return 在线用户信息
     */
    public OnlineUser selectOnlineByInfo(String ipaddr, String userName, LoginUser user)
    {
        if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername()))
        {
            return loginUserToUserOnline(user);
        }
        return null;
    }

    /**
     * 设置在线用户信息
     *
     * @param user 用户信息
     * @return 在线用户
     */
    public OnlineUser loginUserToUserOnline(LoginUser user)
    {
        if (StringUtils.isNull(user) || StringUtils.isNull(user.getUser()))
        {
            return null;
        }
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setTokenId(user.getToken());
        onlineUser.setUserName(user.getUsername());
        onlineUser.setIpaddr(user.getIpaddr());
        onlineUser.setLoginLocation(user.getLoginLocation());
        onlineUser.setBrowser(user.getBrowser());
        onlineUser.setOs(user.getOs());
        onlineUser.setLoginTime(user.getLoginTime());
        if (StringUtils.isNotNull(user.getUser().getDept()))
        {
            onlineUser.setDeptName(user.getUser().getDept().getDeptName());
        }
        return onlineUser;
    }
}