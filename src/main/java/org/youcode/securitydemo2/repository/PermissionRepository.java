package org.youcode.securitydemo2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.youcode.securitydemo2.domain.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
