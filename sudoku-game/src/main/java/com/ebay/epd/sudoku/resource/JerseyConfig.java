package com.ebay.epd.sudoku.resource;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		register(BoardResource.class);
		register(SudokuValidationExceptionMapper.class);
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
	}
}
