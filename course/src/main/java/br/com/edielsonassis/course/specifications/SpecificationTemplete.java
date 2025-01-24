package br.com.edielsonassis.course.specifications;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.course.models.CourseModel;
import br.com.edielsonassis.course.models.ModuleModel;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
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
}