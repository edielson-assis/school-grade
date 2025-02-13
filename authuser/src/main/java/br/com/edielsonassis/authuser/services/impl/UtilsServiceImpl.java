package br.com.edielsonassis.authuser.services.impl;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.edielsonassis.authuser.services.UtilsService;

@Service
public class UtilsServiceImpl implements UtilsService {

	public String createUrlGetAllCoursesByUser(UUID userId, Pageable pageable) {		
		return "?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&sort=" + pageable.getSort().toString().replaceAll(": ", ",");
	}
}