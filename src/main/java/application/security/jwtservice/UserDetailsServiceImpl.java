package application.security.jwtservice;

import application.model.common.UserField;
import application.model.entity.User;
import application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) {
        List<User> userList = userRepository.findByFieldWithFixedValue(UserField.EMAIL.getName(), email);

        if (userList.isEmpty()) {
            throw new UsernameNotFoundException("Email not found "+email);
        }

        return UserDetailsImpl.build(userList.get(0));

    }
}
