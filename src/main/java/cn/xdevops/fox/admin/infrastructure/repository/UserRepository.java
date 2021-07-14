package cn.xdevops.fox.admin.infrastructure.repository;

import cn.xdevops.fox.admin.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByLoginName(String loginName);
}