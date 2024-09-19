package team.woo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team.woo.algorithm.TaskAllocation;
import team.woo.domain.Schedule;
import team.woo.domain.ScheduleRepository;
import team.woo.domain.ScheduleService;
import team.woo.member.Member;
import team.woo.session.SessionManager;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final SessionManager sessionManager;
    private final ScheduleRepository repository;

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);


    @GetMapping("/create")
    public String add() {
        return "create";
    }

    @PostMapping("/create")
    public String addSchedule(
            @RequestParam("name") String name,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startTime,
            @RequestParam("deadLine") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadLine,
            @RequestParam("difficulty") int difficulty,
            @RequestParam("urgency") int urgency,
            @RequestParam("importance") int importance,
            @RequestParam("restTime") String restTime,
            @RequestParam("stress") int stress,
            @RequestParam("preferenceTime") String preferenceTime,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        // "member" 키를 사용해 세션에서 로그인된 사용자 정보를 가져옴
        Member loginMember = (Member) sessionManager.getSession(request, "member");

        Schedule schedule;
        if (loginMember != null) {
            // 로그인된 사용자의 경우
            schedule = new Schedule(name, startTime, deadLine, difficulty, importance, urgency, restTime, preferenceTime, stress, loginMember.getId());
        } else {
            // 비로그인 사용자의 경우
            schedule = new Schedule(name, startTime, deadLine, difficulty, importance, urgency, restTime, preferenceTime, stress);
        }

        Schedule savedSchedule = scheduleService.saveSchedule(schedule, loginMember != null ? loginMember.getId() : null);

        redirectAttributes.addAttribute("id", savedSchedule.getId());

        // 세션에 스케줄 ID 저장
        HttpSession session = request.getSession();
        ((HttpSession) session).setAttribute("scheduleId", savedSchedule.getId());

        return "redirect:/result/{id}";  // result 페이지로 이동
    }

    @GetMapping("/result/{id}")
    public String showResult(@PathVariable Long id, Model model, HttpServletRequest request) {
        Schedule schedule = scheduleService.getSchedule(id);
        model.addAttribute("schedule", schedule);

        // "member" 키를 사용해 세션에서 로그인된 사용자 정보를 가져옴
        Member loginMember = (Member) sessionManager.getSession(request, "member");
        model.addAttribute("loginMember", loginMember);  // 로그인 사용자 정보를 추가하여 뷰에서 사용할 수 있도록 함

        return "result";
    }

    @GetMapping("/select/{id}")
    public String task(@PathVariable Long id, Model model, HttpServletRequest request) {
        // 두 번째 인자로 "member" 키를 전달하여 세션에서 Member 객체를 가져옵니다.
        Member loginMember = (Member) sessionManager.getSession(request, "member");

        if (loginMember != null) {
            logger.info("사용자 세션 확인: 사용자 ID = {}", loginMember.getId());
        } else {
            logger.info("비로그인 상태에서 접근: 세션이 없습니다.");
        }

        Schedule schedule = scheduleService.getSchedule(id);
        model.addAttribute("schedule", schedule);
        model.addAttribute("loginMember", loginMember);
        return "select";
    }

    @PostMapping("/select")
    public String selectOption(@RequestParam("option") String option,
                               @RequestParam("id") Long id,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {

        Schedule schedule = scheduleService.getSchedule(id);
        Member loginMember = (Member) sessionManager.getSession(request, "member");  // 두 번째 인자로 "member" 키를 전달

        schedule.setOption(option);

        int totalScore = TaskAllocation.calculateTotalScore(schedule.getDifficulty(), schedule.getUrgency(), schedule.getImportance(), schedule.getStress());

        LocalDate deadLine = schedule.getDeadLine();
        LocalDate startTime = schedule.getStartTime();
        int availableDays = (int) (deadLine.toEpochDay() - startTime.toEpochDay());
        int adjustDays = TaskAllocation.calculateAdjustDays(availableDays, option);
        double adjustTime = TaskAllocation.calculateAdjustTime(totalScore);

        schedule.setAdjustDays(adjustDays);
        schedule.setAdjustTime(adjustTime);

        if (loginMember != null && schedule.getMember() == null) {
            schedule.setMember(loginMember);
        }

        scheduleService.updateSchedule(schedule);

        redirectAttributes.addAttribute("id", id);
        return "redirect:/result_ts/{id}";
    }

    @GetMapping("/schedule/{scheduleId}")
    public String schedule(@PathVariable Long scheduleId, Model model){
        Schedule schedule = repository.findScheduleById(scheduleId);
        log.info("schedule={}",schedule.getName());
        model.addAttribute("schedule", schedule);
        return "scheduleInfo";
    }
}