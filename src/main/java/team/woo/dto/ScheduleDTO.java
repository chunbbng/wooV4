package team.woo.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
//    private Long id;
    private String name;
    private String startTime;
    private String deadLine;
    private int difficulty;
    private int urgency;
    private int importance;
    private int restTime;
    private int stress;
}
