package team.woo.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import team.woo.member.Member;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate startTime;
    private LocalDate deadLine;
    private int difficulty;
    private int importance;
    private int urgency;
    private String restTime;
    private int stress;
    private String preferenceTime;
    private String option;

    @ElementCollection
    private List<String> selectedDates;

    private int adjustDays = 0; // 기본 값 설정
    private double adjustTime = 0.0; // 기본 값 설정

    // Member와의 연관관계 대신 단순히 memberId를 저장
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "existing_foreign_key_column_name")
    @JsonIgnore
    @ToString.Exclude
    private Member member;

    public Schedule() {
        // 기본 생성자
    }

    // 로그인한 사용자의 Schedule 생성 시 사용
    public Schedule(String name, LocalDate startTime, LocalDate deadLine, int difficulty,
                    int importance, int urgency, String restTime, String preferenceTime, int stress, Long memberId) {
        this.name = name;
        this.startTime = startTime;
        this.deadLine = deadLine;
        this.difficulty = difficulty;
        this.importance = importance;
        this.urgency = urgency;
        this.restTime = restTime;
        this.preferenceTime = preferenceTime;
        this.stress = stress;
        this.memberId = memberId;
        this.adjustDays = 0;
        this.adjustTime = 0.0;
    }

    // 비로그인 사용자의 Schedule 생성 시 사용
    public Schedule(String name, LocalDate startTime, LocalDate deadLine, int difficulty,
                    int importance, int urgency, String restTime, String preferenceTime, int stress) {
        this.name = name;
        this.startTime = startTime;
        this.deadLine = deadLine;
        this.difficulty = difficulty;
        this.importance = importance;
        this.urgency = urgency;
        this.restTime = restTime;
        this.preferenceTime = preferenceTime;
        this.stress = stress;
        this.memberId = null;
        this.adjustDays = 0;
        this.adjustTime = 0.0;
    }

}