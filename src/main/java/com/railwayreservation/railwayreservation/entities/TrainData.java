package com.railwayreservation.railwayreservation.entities;

import java.util.Date;
import java.sql.Timestamp;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long train_data_id;

    @Column(unique = true)
    private String train_name;

    @Column(unique = true)
    private Integer train_no;

    private String start;

    private String Destination;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date;

    
    private Integer price_SL;

    private Integer price_AC2;

    private Integer price_AC3;

    private Integer price_CC;
  
    private Integer SL;

    private Integer AC2;
    private Integer AC3;

    private Integer CC;

    
}
