package cn.xdevops.fox.admin.web.controller;

import cn.xdevops.fox.admin.application.service.OnlineUserService;
import cn.xdevops.fox.admin.common.annotation.Log;
import cn.xdevops.fox.admin.common.enums.BusinessType;
import cn.xdevops.fox.admin.common.utils.StringUtils;
import cn.xdevops.fox.admin.common.utils.redis.RedisCache;
import cn.xdevops.fox.admin.domain.authentication.LoginUser;
import cn.xdevops.fox.admin.domain.cache.CacheConstants;
import cn.xdevops.fox.admin.domain.page.TableDataInfo;
import cn.xdevops.fox.admin.domain.session.OnlineUser;
import cn.xdevops.fox.admin.web.dto.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 在线用户监控
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/monitor/online")
public class OnlineUserController extends BaseController
{
    @Autowired
    private OnlineUserService onlineUserService;

    @Autowired
    private RedisCache redisCache;

    @PreAuthorize("@ss.hasPermi('monitor:online:list')")
    @GetMapping("/list")
    public TableDataInfo list(String ipaddr, String userName)
    {
        Collection<String> keys = redisCache.keys(CacheConstants.LOGIN_TOKEN_KEY + "*");
        List<OnlineUser> userOnlineList = new ArrayList<OnlineUser>();
        for (String key : keys)
        {
            LoginUser user = redisCache.getCacheObject(key);
            if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName))
            {
                if (StringUtils.equals(ipaddr, user.getIpaddr()) && StringUtils.equals(userName, user.getUsername()))
                {
                    userOnlineList.add(onlineUserService.selectOnlineByInfo(ipaddr, userName, user));
                }
            }
            else if (StringUtils.isNotEmpty(ipaddr))
            {
                if (StringUtils.equals(ipaddr, user.getIpaddr()))
                {
                    userOnlineList.add(onlineUserService.selectOnlineByIpaddr(ipaddr, user));
                }
            }
            else if (StringUtils.isNotEmpty(userName) && StringUtils.isNotNull(user.getUser()))
            {
                if (StringUtils.equals(userName, user.getUsername()))
                {
                    userOnlineList.add(onlineUserService.selectOnlineByUserName(userName, user));
                }
            }
            else
            {
                userOnlineList.add(onlineUserService.loginUserToUserOnline(user));
            }
        }
        Collections.reverse(userOnlineList);
        userOnlineList.removeAll(Collections.singleton(null));
        return getDataTable(userOnlineList);
    }

    /**
     * 强退用户
     */
    @PreAuthorize("@ss.hasPermi('monitor:online:forceLogout')")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public AjaxResult forceLogout(@PathVariable String tokenId)
    {
        redisCache.deleteObject(CacheConstants.LOGIN_TOKEN_KEY + tokenId);
        return AjaxResult.success();
    }
}
