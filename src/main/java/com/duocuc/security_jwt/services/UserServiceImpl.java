package com.duocuc.security_jwt.services;

import com.duocuc.security_jwt.models.users.Role;
import com.duocuc.security_jwt.models.users.User;
import com.duocuc.security_jwt.repository.RoleRepository;
import com.duocuc.security_jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Agregamos el repositorio de role para poder consultar por el rol de usuario
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        // Realizamos la busqueda del role usuario si es que se encuentra presenta
        // Como aclaración todos los roles que sean guardados tienen que ir con el formato ROLE_NOMBRE_DEL_ROLE
        // por ejemplo ROLE_USER o ROLE_ADMIN
        Role findRoleUser = this.roleRepository.findByName("ROLE_USER").orElseThrow(
                () -> new RuntimeException("El rol ROLE_USER no existe")
        );
        // Se genera un nuevo arraylist que permite
        List<Role> roles = new ArrayList<>();
        // Se agrega el rol al usuario
        roles.add(findRoleUser);

        // Se debe comprobar si el usuario que se esta enviando tiene el rol del admin
        if(user.isAdmin()){
            Role findRoleAdmin = this.roleRepository.findByName("ROLE_ADMIN").orElseThrow(
                    () -> new RuntimeException("El rol ROLE_ADMIN no existe")
            );
            // En caso que lo encuentre el role de admin se agrega al usuario
            roles.add(findRoleAdmin);
        }

        // Seteamos la lista de roles al usuario
        user.setRoles(roles);

        // Ahora se genera la encriptación de la contraseña
        // En este momento la contraseña viene desencriptada por ende la tenemos que encriptar, recordar que la
        // encriptación es de una sola ida, no podremos desencriptar la contraseña.
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
}
