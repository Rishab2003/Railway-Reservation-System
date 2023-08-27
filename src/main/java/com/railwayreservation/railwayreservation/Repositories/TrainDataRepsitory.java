package com.railwayreservation.railwayreservation.Repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.railwayreservation.railwayreservation.entities.TrainData;
import com.railwayreservation.railwayreservation.entities.User;


public interface TrainDataRepsitory extends JpaRepository<TrainData,Long> {
    
    @Query(value =  "SELECT * FROM Train_data t WHERE t.start=?1 and t.Destination=?2 and DATE(t.date)=?3",nativeQuery = true)
    public  List<TrainData> findTrainByFilter(@Param("start")  String start, @Param("dest") String dest, @Param("date") Date date);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Train_data SET SL=?1 WHERE train_data_id=?2",nativeQuery = true)
    public void reduceSL(@Param("seats") Integer seats, @Param("train_id") Long train_id);

    @Modifying
    @Transactional
    @Query(value = "update Train_data t set t.AC2=?1 where t.train_data_id=?2",nativeQuery = true)
    public void reduceAC2(@Param("seats") Integer seats, @Param("train_id") Long train_id);

    @Modifying
    @Transactional
    @Query(value = "update Train_data t set t.AC3=?1 where t.train_data_id=?2",nativeQuery = true)
    public void reduceAC3(@Param("seats") Integer seats, @Param("train_id") Long train_id);

    @Modifying
    @Transactional
    @Query(value = "update Train_data t set t.CC=?1 where t.train_data_id=?2",nativeQuery = true)
    public void reduceCC(@Param("seats") Integer seats, @Param("train_id") Long train_id);

    @Transactional
    @Modifying
    @Query(value = "update Train_data t set t.SL=?1 where t.train_name=?2",nativeQuery = true)
    public void addSL(@Param("seats") Integer seats, @Param("train_name") String train_name);

    @Transactional
    @Modifying
    @Query(value = "update Train_data t set t.AC2=?1 where t.train_name=?2",nativeQuery = true)
    public void addAC2(@Param("seats") Integer seats, @Param("train_name") String train_name);

    @Transactional
    @Modifying
    @Query(value = "update Train_data t set t.AC3=?1 where t.train_name=?2",nativeQuery = true)
    public void addAC3(@Param("seats") Integer seats, @Param("train_name") String train_name);

    @Transactional
    @Modifying
    @Query(value = "update Train_data t set t.CC=?1 where t.train_name=?2",nativeQuery = true)
    public void addCC(@Param("seats") Integer seats, @Param("train_name") String train_name);


    @Query(value = "select * from Train_data t where t.train_no=?1",nativeQuery = true)
    public TrainData findByTrain_No(@Param("train_no") Integer train_no);

    
    @Query(value = "select * from Train_data t where t.train_name=?1",nativeQuery = true)
    public Optional<TrainData> findByTrain_Name(String train_name);



    
        

}
