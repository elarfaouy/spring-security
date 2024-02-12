package org.youcode.securitydemo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.youcode.securitydemo2.domain.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
