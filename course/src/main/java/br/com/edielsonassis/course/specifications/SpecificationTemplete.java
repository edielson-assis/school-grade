package br.com.edielsonassis.course.specifications;

import org.springframework.data.jpa.domain.Specification;

import br.com.edielsonassis.course.models.CourseModel;
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
}