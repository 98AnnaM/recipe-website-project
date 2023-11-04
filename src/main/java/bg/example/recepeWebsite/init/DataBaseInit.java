package bg.example.recepeWebsite.init;

import bg.example.recepeWebsite.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataBaseInit implements CommandLineRunner {

    private final RoleService roleService;

    public DataBaseInit(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {

        this.roleService.initRoles();

    }
}
