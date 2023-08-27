package com.railwayreservation.railwayreservation.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.railwayreservation.railwayreservation.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("select u from User u where u.email = :email")
    public Optional<User> findUserByEmail(@Param("email") String  email);
    
}
