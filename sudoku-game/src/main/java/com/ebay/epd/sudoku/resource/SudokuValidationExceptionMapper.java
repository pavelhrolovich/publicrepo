package com.ebay.epd.sudoku.resource;

import java.util.HashMap;
import java.util.Map;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.springframework.stereotype.Component;

import com.ebay.epd.sudoku.game.SudokuValidationException;

@Component
public class SudokuValidationExceptionMapper implements ExceptionMapper<SudokuValidationException> {

	@Override
	public Response toResponse(SudokuValidationException exception) {
		Map<String, Object> result = new HashMap<>();
		result.put("errors", exception.getErrors());
		return Response.status(Status.BAD_REQUEST).entity(result).build();
	}
}
