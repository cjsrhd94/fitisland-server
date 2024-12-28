package happyperson.fitisland.domain.exercise.controller;

import happyperson.fitisland.domain.exercise.dto.request.ExerciseGuideCreateRequest;
import happyperson.fitisland.domain.exercise.dto.response.exerciseguide.ExerciseGuideCreateResponse;
import happyperson.fitisland.domain.exercise.dto.response.exerciseguide.ExerciseGuideDetailResponse;
import happyperson.fitisland.domain.exercise.service.ExerciseGuideService;
import happyperson.fitisland.domain.oauthjwt.dto.CustomOAuth2User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exercise-guides")
public class ExerciseGuideController {

    private final ExerciseGuideService exerciseGuideService;

    /**
     * 가이드 상세 보기(완료)
     * @param @PathVariable Long guideId
     * @return ExerciseGuideDetailResponse
     */
    @GetMapping("/{guideId}")
    public ExerciseGuideDetailResponse getExerciseGuide(
        @PathVariable("guideId") Long guideId,
        @RequestParam("userId") Long userId
    ) {

        return exerciseGuideService.findExerciseGuideDetail(guideId, userId);
    }

    /**
     * 생성(유저아이디 수정하기)
     */
    //수정

    /**
     * 삭제(유저아이디수정하기)
     */

    /**
     * 운동 가이드 목록 보기
     * 중요! 부위 별로 보이는 기능 구현 어떻게할지 생각
     */

}
