package br.com.edielsonassis.course.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import br.com.edielsonassis.course.services.UtilsService;

public class UtilsServiceImpl implements UtilsService {

    @Override
    public String createUrlGetAllUsersByCourse(UUID courseId, Pageable pageable) {		
		return "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
	}
}