package com.ebay.epd.sudoku.resource;

import com.ebay.epd.sudoku.game.Board;
import com.ebay.epd.sudoku.game.SudokuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Component
@Path("/board")
@Produces("application/json")
@Consumes("application/json")
public class BoardResource {

	@Autowired
	SudokuService service;

	@GET
	public Board getBoard() {
		return service.getNewBoard();
	}

	@GET
	@Path("/{id}")
	public Board getBoard(@PathParam("id") String id) {
		return service.getBoard(id);
	}

	@PUT
	@Path("/validate")
	public Response validateBoard(@Valid Board b) throws Exception {
		service.validateBoard(b);
		return Response.ok(b).build();
	}
}
