package bg.example.recepeWebsite.model.entity;


import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {

    private RoleNameEnum role;

    public RoleEntity() {
    }

    @Enumerated(EnumType.STRING)
    public RoleNameEnum getRole() {
        return role;
    }

    public RoleEntity setRole(RoleNameEnum role) {
        this.role = role;
        return this;
    }
}
