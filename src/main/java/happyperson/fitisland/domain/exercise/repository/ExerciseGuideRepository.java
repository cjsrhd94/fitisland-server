package happyperson.fitisland.domain.exercise.repository;

import happyperson.fitisland.domain.exercise.entity.exerciseguide.ExerciseGuide;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseGuideRepository extends JpaRepository<ExerciseGuide,Long> {
    @Query("select distinct eg from ExerciseGuide eg left join fetch eg.likes l left join fetch l.user where eg.id = :id")
    Optional<ExerciseGuide> findByIdWithLikesAndUser(@Param("id") Long id);

    // ExerciseGuide 목록 조회 (Like 정보 포함, 페이징 제외)
    @Query("select distinct eg from ExerciseGuide eg left join fetch eg.likes")
    List<ExerciseGuide> findAllWithLikes();
}
