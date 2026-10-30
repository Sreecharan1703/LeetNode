package com.example.demo.repository;

import com.example.demo.entity.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepo extends JpaRepository<Question, Integer> {

    List<Question> findByRatingBetween(int minRating, int maxRating, Pageable pageable);

    @Query("SELECT e.rating FROM Question e WHERE e.id = :id")
    Optional<Integer> getQuestionRatingById(@Param("id") int id);

    @Query("SELECT e.titleSlug FROM Question e WHERE e.id = :id")
    Optional<String> getTitleSlugById(@Param("id") int id);
}
