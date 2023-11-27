package bg.example.recepeWebsite.model.entity;

import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private RoleNameEnum role;

    public RoleEntity() {
    }

    public RoleNameEnum getRole() {
        return role;
    }

    public RoleEntity setRole(RoleNameEnum role) {
        this.role = role;
        return this;
    }
}
