package bg.example.recepeWebsite.service;

import bg.example.recepeWebsite.model.dto.UserRegisterDto;
import bg.example.recepeWebsite.model.entity.UserEntity;
import bg.example.recepeWebsite.model.entity.enums.LevelEnum;
import bg.example.recepeWebsite.model.entity.enums.RoleNameEnum;
import bg.example.recepeWebsite.model.view.UserView;
import bg.example.recepeWebsite.repository.RoleRepository;
import bg.example.recepeWebsite.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserDetailsService userDetailsService;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userDetailsService = userDetailsService;
    }

    private void login(UserEntity userEntity){
        UserDetails userDetails = userDetailsService
                .loadUserByUsername(userEntity.getUsername());

        Authentication auth = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities());

        SecurityContextHolder
                .getContext()
                .setAuthentication(auth);

    }

    public void register(UserRegisterDto userModel) {
        UserEntity newUser = modelMapper.map(userModel, UserEntity.class);
        newUser.setPassword(passwordEncoder.encode(userModel.getPassword()));

        newUser.setRoles(roleRepository.findAll()
                .stream()
                .filter(r -> r.getRole() == (RoleNameEnum.USER))
                .collect(Collectors.toSet()));

        userRepository.save(newUser);
        //login(newUser);
    }

    public UserView findById(Long id) {
        return this.userRepository.findById(id)
                .map(userEntity -> modelMapper.map(userEntity, UserView.class))
                .orElse(null);
    }





}
