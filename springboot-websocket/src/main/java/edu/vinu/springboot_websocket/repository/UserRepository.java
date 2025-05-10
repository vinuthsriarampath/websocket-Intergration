package edu.vinu.springboot_websocket.repository;

import edu.vinu.springboot_websocket.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends JpaRepository<UserEntity,Long> {
    UserEntity findByUsername(String userName);

    boolean existsByUsername(String username);
}
