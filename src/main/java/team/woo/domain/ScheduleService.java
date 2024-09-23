package team.woo.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.woo.dto.CalendarDTO;
import team.woo.member.Member;
import team.woo.member.MemberRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Schedule saveSchedule(Schedule schedule, Long memberId) {
        if (memberId != null) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member Id: " + memberId));
            schedule.setMember(member);
        }
        return scheduleRepository.save(schedule);
    }

    // 매개변수로 직접 데이터를 받아 Schedule 객체를 생성하고 저장하는 메서드
    @Transactional
    public Schedule saveSchedule(String name, LocalDate startTime, LocalDate deadline, int difficulty, int urgency, int importance, String restTime, int stress, String preferenceTime, Long memberId) {
        Schedule schedule = new Schedule(name, startTime, deadline, difficulty, importance, urgency, restTime, preferenceTime, stress);
        return saveSchedule(schedule, memberId);
    }

    @Transactional
    public Schedule updateSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Transactional(readOnly = true)
    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id: " + id));
    }

    @Transactional(readOnly = true)
    public List<Schedule> getSchedulesByMemberId(Long memberId) {
        return scheduleRepository.findByMemberId(memberId);
    }

    /**
     * CalendarDTO로부터 선택된 날짜 및 스케줄 정보를 저장하는 메서드 (스케줄 ID 사용)
     */
    @Transactional
    public Schedule saveScheduleFromCalendarDTO(CalendarDTO calendarDTO, Long scheduleId, Long memberId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule ID: " + scheduleId));

        // 선택된 날짜를 Schedule의 selectedDates로 설정
        schedule.setSelectedDates(calendarDTO.getSelectedDates());

        // 필요에 따라 Member 정보도 업데이트
        if (memberId != null) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid member Id: " + memberId));
            schedule.setMember(member);
        }

        return scheduleRepository.save(schedule);  // 스케줄 저장
    }


    // 기존의 스케줄 정보 가져오는 메서드
    @Transactional(readOnly = true)
    public Schedule getScheduleById(Long scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found with ID: " + scheduleId));
    }


}
