package com.example.backend.Model.Poll;
import lombok.Data;
import java.time.Instant;

import com.example.backend.Model.User;
@Data
public class Vote {
    Instant publishedAt = Instant.now();
    Integer userId;
    Integer pollId;
    Integer optionId; 
    
}
