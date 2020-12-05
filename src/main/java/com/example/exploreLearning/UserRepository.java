package com.example.exploreLearning;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByFirstNameAndLastName(String firstName, String lastName);

    List<UserEntity> findAllByOrderByLastName();
}
