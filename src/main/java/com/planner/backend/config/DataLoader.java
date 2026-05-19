package com.planner.backend.config;

import com.planner.backend.model.User;
import com.planner.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                    .username("planner1")
                    .password("pass")
                    .fullName("Ahmet Yılmaz (Şehir Plancısı)")
                    .role("ROLE_PLANNER")
                    .build());
            
            userRepository.save(User.builder()
                    .username("planner2")
                    .password("pass")
                    .fullName("Ayşe Kaya (Şehir Plancısı)")
                    .role("ROLE_PLANNER")
                    .build());

            userRepository.save(User.builder()
                    .username("entity1")
                    .password("pass")
                    .fullName("ABC İnşaat A.Ş.")
                    .role("ROLE_ENTITY")
                    .build());

            userRepository.save(User.builder()
                    .username("entity2")
                    .password("pass")
                    .fullName("Belediye İmar A.Ş.")
                    .role("ROLE_ENTITY")
                    .build());
        }
    }
}
