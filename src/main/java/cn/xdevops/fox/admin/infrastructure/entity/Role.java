package cn.xdevops.fox.admin.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色信息表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_sys_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色名称
     */
    @Column(name = "role_name", nullable = false)
    private String roleName;

    /**
     * 角色权限字符串
     */
    @Column(name = "role_key", nullable = false)
    private String roleKey;

    /**
     * 显示顺序
     */
    @Column(name = "role_sort", nullable = false)
    private Integer roleSort;

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    @Column(name = "data_scope")
    private String dataScope;

    /**
     * 角色状态（0正常 1停用）
     */
    @Column(name = "status")
    private String status;

    public Role(String roleName, String roleKey) {
        this.roleName = roleName;
        this.roleKey = roleKey;
    }
}
