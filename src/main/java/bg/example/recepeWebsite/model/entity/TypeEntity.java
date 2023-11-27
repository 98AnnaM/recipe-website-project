package bg.example.recepeWebsite.model.entity;

import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "types")
public class TypeEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private TypeNameEnum name;

    public TypeNameEnum getName() {
        return name;
    }

    public TypeEntity setName(TypeNameEnum name) {
        this.name = name;
        return this;
    }
}
