package br.com.edielsonassis.course.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.edielsonassis.course.models.LessonModel;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    @Query(value = "select * from lessons where module_id = :moduleId", nativeQuery = true)
	List<LessonModel> findAllLessonsIntoModule(@Param("moduleId") UUID moduleId);
}