package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.entity.TypeEntity;
import bg.example.recepeWebsite.model.entity.enums.TypeNameEnum;
import bg.example.recepeWebsite.repository.TypeRepository;
import bg.example.recepeWebsite.web.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TypeService {

    private final TypeRepository typeRepository;

    public TypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    public TypeEntity findByTypeName(TypeNameEnum typeNameEnum) {
        return this.typeRepository.findByName(typeNameEnum)
                .orElseThrow(() -> new ObjectNotFoundException("Type with name " + typeNameEnum.name() + " not found!"));
    }

    public List<TypeNameEnum> getAllTypes() {
        return this.typeRepository.findAll().stream().map(TypeEntity::getName).collect(Collectors.toList());
    }
}
