package br.com.edielsonassis.course.specifications;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.course.models.CourseModel;
import br.com.edielsonassis.course.models.LessonModel;
import br.com.edielsonassis.course.models.ModuleModel;
import br.com.edielsonassis.course.models.UserModel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.EqualIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;

public class SpecificationTemplete {
    
    @And({
		@Spec(path = "courseLevel", spec = EqualIgnoreCase.class),
		@Spec(path = "courseStatus", spec = EqualIgnoreCase.class),
		@Spec(path = "name", spec = Like.class)
	})
	public interface CourseSpecification extends Specification<CourseModel> {}

	@Spec(path = "title", spec = Like.class)
	public interface ModuleSpecification extends Specification<ModuleModel> {}

	public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
		return (root, query, cb) -> {
			query.distinct(true);
			Root<ModuleModel> module = root;
			Root<CourseModel> course = query.from(CourseModel.class);
			Expression<Collection<ModuleModel>> courseModules = course.get("modules");
			return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(module, courseModules));
		};
	}

	@Spec(path = "title", spec = Like.class)
	public interface LessonSpecification extends Specification<LessonModel> {}

	public static Specification<LessonModel> lessonModuleId(final UUID moduleId) {
		return (root, query, cb) -> {
			query.distinct(true);
			Root<LessonModel> lesson = root;
			Root<ModuleModel> module = query.from(ModuleModel.class);
			Expression<Collection<LessonModel>> moduleLessons = module.get("lessons");
			return cb.and(cb.equal(module.get("moduleId"), moduleId), cb.isMember(lesson, moduleLessons));
		};
	}

	@And({
		@Spec(path = "email", spec = Like.class),
		@Spec(path = "fullName", spec = Like.class),
		@Spec(path = "userStatus", spec = Equal.class),
		@Spec(path = "userType", spec = Equal.class)
	})
	public interface UserSpecification extends Specification<UserModel>{}

	public static Specification<UserModel> userCourseId(final UUID courseId) {
		return (root, query, cb) -> {
			query.distinct(true);
			Root<UserModel> user = root;
			Root<CourseModel> course = query.from(CourseModel.class);
			Expression<Collection<UserModel>> coursesUsers = course.get("users");
			return cb.and(cb.equal(course.get("courseId"), courseId), cb.isMember(user, coursesUsers));
		};
	}
	
	public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, cb) -> {
            query.distinct(true);
            Root<CourseModel> course = root;
            Root<UserModel> user = query.from(UserModel.class);
            Expression<Collection<CourseModel>> usersCourses = user.get("courses");
            return cb.and(cb.equal(user.get("userId"), userId), cb.isMember(course, usersCourses));
        };
    }
}