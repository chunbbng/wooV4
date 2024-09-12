package team.woo.controller;

import jakarta.servlet.http.HttpServletRequest;  // HttpServletRequest import
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // Model import
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team.woo.domain.Schedule;
import team.woo.domain.ScheduleRepository;
import team.woo.domain.ScheduleService;
import team.woo.member.Member;
import team.woo.member.MemberRepository;
import team.woo.session.SessionManager;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final ScheduleService scheduleService;
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);
    private final SessionManager sessionManager;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("pageTitle", "메인 페이지 입니다.");
        return "index";
    }

    @GetMapping("/cabinet")
    public String cabinet(Model model,HttpServletRequest request) {

        Member loginMember = (Member) sessionManager.getSession(request, "member");

        if (loginMember == null) {
            log.info("로그인 정보가 없습니다.");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }
        String loginId = loginMember.getLoginId();
        log.info("loginMember={}", loginId);
        return "redirect:/cabinet/" + loginId;
    }

    @GetMapping("/cabinet/{loginID}")
    public String cabinet(@PathVariable String loginID,
                          HttpServletRequest request,
                          Model model) {

        Member loginMember = (Member) sessionManager.getSession(request, "member");

        if (loginMember == null) {
            log.info("로그인 정보가 없습니다.");
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        Member member = memberRepository.findByLoginId(loginID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid login Id:" + loginID));

        List<Schedule> schedules = scheduleRepository.findByMemberId(loginMember.getId());

        // 모델에 추가합니다
        model.addAttribute("member", member);
        model.addAttribute("schedules", schedules);
        String loginId = member.getLoginId();
        model.addAttribute("loginId", loginId);
        log.info("loginMember={}", loginId);

        return "cabinet";
    }

    @PostMapping("/cabinet")
    public String processSelectedSchedules(@RequestBody SelectionRequest request,
                                           RedirectAttributes redirectAttributes) {
        try {
            List<Long> selectedIds = request.getSelectedSchedules();

            for (Long selectedId : selectedIds) {
                log.info("selectedId={}", selectedId);
            }
            redirectAttributes.addAttribute("selectedIds", selectedIds);

            // 성공 시 리다이렉트
            return "redirect:/charts/example";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/errorPage"; // 에러 시 리다이렉트
        }
    }

    @Getter
    public static class SelectionRequest {
        private List<Long> selectedSchedules;

        public void setSelectedSchedules(List<Long> selectedSchedules) {
            this.selectedSchedules = selectedSchedules;
        }
    }

    @PostMapping("/cabinet/delete")
    public String deleteSelectedSchedules(@RequestBody SelectionRequest request, RedirectAttributes redirectAttributes) {
        try {
            List<Long> selectedIds = request.getSelectedSchedules();

            // 선택된 스케줄을 삭제합니다.
            for (Long selectedId : selectedIds) {
                scheduleRepository.deleteById(selectedId);
            }

            // 삭제 후 다시 Cabinet 페이지로 리다이렉트
            return "redirect:/cabinet";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/errorPage"; // 에러 시 리다이렉트
        }
    }

    @GetMapping("/check")
    public String check(Model model) {
        model.addAttribute("pageTitle", "Check 페이지 입니다.");
        return "check";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "About 페이지 입니다.");
        return "about";
    }

    @GetMapping("/result_ts/{id}")
    public String resultTs(@PathVariable Long id, Model model, HttpServletRequest request) {
        Schedule schedule = scheduleService.getSchedule(id);
        model.addAttribute("schedule", schedule);
        model.addAttribute("adjustDays", schedule.getAdjustDays());
        model.addAttribute("adjustTime", schedule.getAdjustTime());

        // 세션에서 "member" 키로 사용자 정보를 가져옴
        Member loginMember = (Member) sessionManager.getSession(request, "member");
        if (loginMember == null) {
            logger.warn("세션에 LOGIN_MEMBER 정보가 없음.");
            return "redirect:/login";
        }

        model.addAttribute("loginMember", loginMember);
        logger.info("세션 확인: 사용자 ID = {}", loginMember.getId());

        return "result_ts";
    }

    @GetMapping("/calendarCheck/{loginId}/{id}")
    public String calendarCheck(@PathVariable String loginId, @PathVariable Long id, Model model) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid schedule Id: " + id));

        model.addAttribute("adjustDays", schedule.getAdjustDays());
        model.addAttribute("adjustTime", schedule.getAdjustTime());
        model.addAttribute("startTime", schedule.getStartTime());
        model.addAttribute("deadLine", schedule.getDeadLine());
        model.addAttribute("id", id);
        model.addAttribute("scheduleName", schedule.getName());

        return "calendarCheck";
    }
}