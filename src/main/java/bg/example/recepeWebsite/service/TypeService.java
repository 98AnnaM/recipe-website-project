package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.repository.TypeRepository;
import org.springframework.stereotype.Service;

@Service
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }


    public TypeEntity findByTypeName(TypeNameEnum typeNameEnum) {
        return this.typeRepository.findByName(typeNameEnum).orElse(null);
    }
}
