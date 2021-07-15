package cn.xdevops.fox.admin.application.service;

import cn.xdevops.fox.admin.infrastructure.entity.SysOperLog;
import cn.xdevops.fox.admin.infrastructure.mapper.SysOperLogMapper;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 操作日志 服务层处理
 *
 * @author ruoyi
 */
@Service
public class OperLogService
{

    private final SysOperLogMapper operLogMapper;

    public OperLogService(SysOperLogMapper operLogMapper) {
        this.operLogMapper = operLogMapper;
    }

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }
}