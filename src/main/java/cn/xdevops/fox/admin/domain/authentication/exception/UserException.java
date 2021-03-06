package cn.xdevops.fox.admin.domain.authentication.exception;

import cn.xdevops.fox.admin.common.exception.BaseException;

/**
 * 用户信息异常类
 *
 * @author ruoyi
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
