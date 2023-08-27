package com.railwayreservation.railwayreservation.entities;

import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank()
    @Size(min = 3, max = 30, message = "Username nust be between 3 and 12!!")
    private String name;

    @Column(unique = true, nullable = false)
    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Invalid email")
    private String email;

    @NotBlank(message = "Please enter password")
    private String password;

    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL,
        mappedBy = "user"
        
        
    )
    private List<Train> train;

    
    @Range(min=1,max=100)
    private Integer age;
    
    @NotBlank(message = "Please enter mobile.No")
    private String mobileno;
    private String address;



    private String Role;

    public User(String name,String email,String password){
        this.name=name;
        this.email=email;
        this.password=password;
    }
}
