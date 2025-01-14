package br.com.edielsonassis.course.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import br.com.edielsonassis.course.models.enums.CourseLevel;
import br.com.edielsonassis.course.models.enums.CourseStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "courseId")
@Getter
@Setter
@Entity
@Table(name = "courses")
public class CourseModel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "course_id")
    private UUID courseId;

    @Column(nullable = false, length = 150)
	private String name;
	
	@Column(nullable = false, length = 255)
	private String description;
	
	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(nullable = false, name = "creation_date")
	private LocalDateTime creationDate;
	
	@Column(nullable = false, name = "last_update_date")
	private LocalDateTime lastUpdateDate;
	
	@Column(nullable = false, name = "course_status")
	@Enumerated(EnumType.STRING)
	private CourseStatus courseStatus;
	
	@Column(nullable = false, name = "course_level")
	@Enumerated(EnumType.STRING)
	private CourseLevel courseLevel;
	
	@Column(nullable = false, name = "user_instructor")
	private UUID userInstructor;
}