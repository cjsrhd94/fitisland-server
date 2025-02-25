package happyperson.fitisland.domain.exercise.exception;

import happyperson.fitisland.global.exception.CustomException;
import org.springframework.http.HttpStatus;

public class ExerciseGuideNotFoundException extends CustomException {

    private static final String MESSAGE = "운동가이드를 찾을 수 없습니다";
    public ExerciseGuideNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
