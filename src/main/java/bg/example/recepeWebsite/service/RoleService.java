package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.RoleEntity;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository repository) {
        this.roleRepository = repository;
    }

    public void initRoles() {
        if (roleRepository.count() != 0){
            return;
        }

        Arrays.stream(RoleNameEnum.values())
                .forEach(roleNameEnum -> {
                    RoleEntity role = new RoleEntity();
                    role.setRole(roleNameEnum);
                    roleRepository.save(role);
                });

    }
}
