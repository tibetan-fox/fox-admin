package cn.xdevops.fox.admin.web.controller;

import cn.xdevops.fox.admin.application.service.LoginService;
import cn.xdevops.fox.admin.application.service.MenuService;
import cn.xdevops.fox.admin.application.service.PermissionService;
import cn.xdevops.fox.admin.application.service.TokenService;
import cn.xdevops.fox.admin.domain.authentication.constants.AuthenticationConstants;
import cn.xdevops.fox.admin.domain.authentication.LoginUser;
import cn.xdevops.fox.admin.infrastructure.entity.SysMenu;
import cn.xdevops.fox.admin.infrastructure.entity.SysUser;
import cn.xdevops.fox.admin.common.utils.ServletUtils;
import cn.xdevops.fox.admin.web.dto.AjaxResult;
import cn.xdevops.fox.admin.web.dto.LoginBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class LoginController {

    private final LoginService loginService;

    private MenuService menuService;

    private PermissionService permissionService;

    private TokenService tokenService;

    public LoginController(LoginService loginService, MenuService menuService, PermissionService permissionService, TokenService tokenService) {
        this.loginService = loginService;
        this.menuService = menuService;
        this.permissionService = permissionService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody)
    {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(AuthenticationConstants.TOKEN, token);
        return ajax;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters()
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        // 用户信息
        SysUser user = loginUser.getUser();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(user.getUserId());
        return AjaxResult.success(menuService.buildMenus(menus));
    }

}
