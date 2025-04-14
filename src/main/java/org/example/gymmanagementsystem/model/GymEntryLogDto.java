package org.example.gymmanagementsystem.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GymEntryLogDto {
    private Integer id;
    private LocalDateTime entryTime;
    private String customerName;
    private String customerSurname;


}
