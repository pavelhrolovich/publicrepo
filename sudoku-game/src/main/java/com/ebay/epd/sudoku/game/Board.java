package com.ebay.epd.sudoku.game;

import javax.validation.constraints.NotNull;

public class Board {

    @NotNull
    private String id;

    @NotNull
    private Integer[][] fields;


    public String dealsLink;
    private BoardState state = BoardState.VALID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer[][] getFields() {
        return fields;
    }

    public void setFields(Integer[][] fields) {
        this.fields = fields;
    }

    public void setState(BoardState state) {
        this.state = state;
    }

    public BoardState getState() {
        return state;
    }

    public String getDealsLink() {
        return dealsLink;
    }

    public void setDealsLink(String dealsLink) {
        this.dealsLink = dealsLink;
    }
}
