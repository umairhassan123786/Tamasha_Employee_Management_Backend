package com.example.demo.Service;

import com.example.demo.Models.MyUserDetails;
import com.example.demo.Models.User;
import com.example.demo.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByemail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new MyUserDetails(user.getEmail(), user.getPassword(), user.getRole());
    }
}
