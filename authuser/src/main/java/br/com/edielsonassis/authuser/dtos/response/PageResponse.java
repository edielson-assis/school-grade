package br.com.edielsonassis.authuser.dtos.response;

import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageResponse<T> extends PageImpl<T> {

	private static final long serialVersionUID = 1L;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public PageResponse(
			@JsonProperty("content") List<T> content,
			@JsonProperty("pageable") JsonNode pageable,
			@JsonProperty("last") boolean last,
			@JsonProperty("totalElements") Long totalElements,
			@JsonProperty("totalPages") int totalPages,
			@JsonProperty("size") int size,
			@JsonProperty("number") int number,
			@JsonProperty("sort") JsonNode sort,
			@JsonProperty("first") boolean first,
			@JsonProperty("numberOfElements") int numberOfElements,
			@JsonProperty("empty") boolean empty
	) {
		super(content, PageRequest.of(number, size), totalElements);
	}
	
	public PageResponse(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public PageResponse(List<T> content) {
		super(content);
	}
}