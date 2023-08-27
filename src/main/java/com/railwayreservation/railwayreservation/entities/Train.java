package com.railwayreservation.railwayreservation.entities;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Train {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long train_id;


    private int train_no;

    private String train_name;

    private String start;

    private String Destination;


    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date date;

    private Integer ticket_price;

    private Integer passengers;
    
    private String seat_class;

    @ManyToOne
     @JoinColumn(
        name = "user_id"
    )
    private User user;

}
