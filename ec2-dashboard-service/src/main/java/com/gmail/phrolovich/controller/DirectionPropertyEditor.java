package com.gmail.phrolovich.controller;

import com.gmail.phrolovich.api.dto.Direction;

import java.beans.PropertyEditorSupport;

public class DirectionPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) {
        setValue(Direction.valueOf(text.toUpperCase()));
    }

}
