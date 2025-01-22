package br.com.edielsonassis.course.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(of = "lessonId")
@Getter
@Setter
@Entity
@Table(name = "lessons")
public class LessonModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "lesson_id")
	private UUID lessonId;
	
	@Column(nullable = false, length = 150)
	private String title;
	
	@Column(nullable = false, length = 255)
	private String description;
	
	@Column(nullable = false, name = "video_url", length = 255)
	private String videoUrl;
	
	@Column(nullable = false, name = "creation_date")
	private LocalDateTime creationDate;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "module_id", nullable = false)
	private ModuleModel module;
}