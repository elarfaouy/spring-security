package org.youcode.securitydemo2.seeder;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.youcode.securitydemo2.domain.entity.Permission;
import org.youcode.securitydemo2.domain.entity.Role;
import org.youcode.securitydemo2.domain.entity.User;
import org.youcode.securitydemo2.repository.PermissionRepository;
import org.youcode.securitydemo2.repository.RoleRepository;
import org.youcode.securitydemo2.repository.UserRepository;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class InitialSeeder implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        Permission readPermissionAdmin = Permission.builder().name("Can Read Admin").build();
        Permission writePermissionAdmin = Permission.builder().name("Can Write Admin").build();
        Permission readPermissionUser = Permission.builder().name("Can Read User").build();
        Permission writePermissionUser = Permission.builder().name("Can Write User").build();

        permissionRepository.saveAll(List.of(
                readPermissionAdmin,
                writePermissionAdmin,
                readPermissionUser,
                writePermissionUser
        ));

        Role adminRole = new Role(1L, "ADMIN", List.of(
                readPermissionAdmin,
                writePermissionAdmin,
                readPermissionUser,
                writePermissionUser
        ));

        Role userRole = new Role(2L, "USER", List.of(
                readPermissionUser,
                writePermissionUser
        ));

        roleRepository.saveAll(List.of(adminRole, userRole));

        User adminUser = new User(1L, "admin", passwordEncoder.encode("password"), adminRole);
        User userUser = new User(2L, "user", passwordEncoder.encode("password"), userRole);

        userRepository.saveAll(List.of(adminUser, userUser));
    }
}
