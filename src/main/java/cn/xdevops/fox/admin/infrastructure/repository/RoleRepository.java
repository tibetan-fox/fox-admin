package cn.xdevops.fox.admin.infrastructure.repository;

import cn.xdevops.fox.admin.infrastructure.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<Role, Long> {

}