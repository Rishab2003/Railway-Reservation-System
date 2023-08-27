package com.railwayreservation.railwayreservation.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.railwayreservation.railwayreservation.entities.Train;
import com.railwayreservation.railwayreservation.entities.User;

import jakarta.transaction.Transactional;

public interface TrainRepository extends JpaRepository<Train,Long>{
    
    @Transactional
    @Query(value = "update train set user_id=null where train_id=?1 ",nativeQuery = true)
    public void makeNull(@Param("train_id") Long train_id);

    public Optional<Train> findByUser(User user);

    public List<Train> findAllByUser(User user);
}
