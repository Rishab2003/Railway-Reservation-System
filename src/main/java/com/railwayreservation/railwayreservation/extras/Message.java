package com.railwayreservation.railwayreservation.extras;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Message {

    private String content;
    private String type;
    
}
