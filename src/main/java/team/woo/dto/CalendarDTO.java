package team.woo.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CalendarDTO {
    private List<String> selectedDates;
    private String scheduleName;
    private Long scheduleId;  // 스케줄 ID 추가
}