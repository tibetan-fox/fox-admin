package cn.xdevops.fox.admin.application.service;


import cn.xdevops.fox.admin.infrastructure.entity.SysLogininfor;
import cn.xdevops.fox.admin.infrastructure.mapper.SysLogininforMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class LoginInfoService
{

    private final SysLogininforMapper loginInfoMapper;

    public LoginInfoService(SysLogininforMapper loginInfoMapper) {
        this.loginInfoMapper = loginInfoMapper;
    }

    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    public void insertLogininfor(SysLogininfor loginInfo)
    {
        loginInfoMapper.insertLogininfor(loginInfo);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param loginInfo 访问日志对象
     * @return 登录记录集合
     */
    public List<SysLogininfor> selectLogininforList(SysLogininfor loginInfo)
    {
        return loginInfoMapper.selectLogininforList(loginInfo);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return
     */
    public int deleteLogininforByIds(Long[] infoIds)
    {
        return loginInfoMapper.deleteLogininforByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    public void cleanLogininfor()
    {
        loginInfoMapper.cleanLogininfor();
    }
}
