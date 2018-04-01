package com.ebay.epd.sudoku.game;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BoardLogic {

    public BoardState isValid(Board b) throws SudokuValidationException {

        List<InvalidFieldError> errors = new LinkedList<InvalidFieldError>();

        validateDigits(b, errors);
        validateRows(b, errors);
        validateColumns(b, errors);
        validateCells(b, errors);

        if (errors.size() > 0) {
            throw new SudokuValidationException(errors);
        }

        BoardState state = getBoardState(b);
        return state;
    }

    private BoardState getBoardState(Board b) {
        for (int i = 0; i < b.getFields().length; i++) {
            for (int j = 0; j < b.getFields().length; j++) {
                if (b.getFields()[i][j] == null) {
                    return BoardState.VALID;
                }
                ;
            }
        }
        return BoardState.COMPLETED;
    }

    private void validateDigits(Board b, List<InvalidFieldError> errors)
     throws SudokuValidationException {
        int length = b.getFields().length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                Integer digit = b.getFields()[i][j];
                if (digit != null) {
                    if (digit < 1 || digit > 9)
                        errors.add(new InvalidFieldError(i, j));
                }
            }
        }
    }

    private boolean validateColumns(Board b, List<InvalidFieldError> errors) {
        int length = b.getFields().length;

        for (int i = 0; i < length; i++) {
            Integer[] column = new Integer[length];
            for (int j = 0; j < length; j++) {
                column[j] = b.getFields()[j][i];
            }
            Collection<Integer> invalidIds = validatePartial(column);
            for (Integer invalidId : invalidIds) {
                errors.add(new InvalidFieldError(invalidId, i));
            }
        }
        return true;
    }

    private boolean validateCells(Board b, List<InvalidFieldError> errors) {
        int cellCount = 3;
        for (int i = 0; i < cellCount; i++) {
            for (int j = 0; j < cellCount; j++) {
                if (!validateCell(i, j, b, errors)) {
                    return false;
                }
            }

        }
        return true;
    }

    private boolean validateCell(int cellCoordX, int cellCoordY, Board b,
                                 List<InvalidFieldError> errors) {
        int length = 3;
        Integer[] digits = new Integer[b.getFields().length];
        int curDigit = 0;
        int cellX = cellCoordX * 3;
        int cellY = cellCoordY * 3;
        for (int i = cellX; i < length; i++) {
            for (int j = cellY; j < length; j++) {
                digits[curDigit++] = b.getFields()[i][j];
            }
        }
        Collection<Integer> invalidIds = validatePartial(digits);
        for (Integer invalidId : invalidIds) {
            int invalidIdRow = invalidId / 3;
            int invalidIdCol = invalidId % 3;
            errors.add(new InvalidFieldError(cellX + invalidIdRow, cellY
                                                                    + invalidIdCol));
        }
        return true;
    }

    private void validateRows(Board b, List<InvalidFieldError> errors) {
        for (int i = 0; i < b.getFields().length; i++) {
            Collection<Integer> invalidIds = validatePartial(b.getFields()[i]);
            for (Integer invalidId : invalidIds) {
                errors.add(new InvalidFieldError(i, invalidId));
            }
        }
    }

    private Collection<Integer> validatePartial(Integer[] integers) {
        Set<Integer> invalidIds = new HashSet();
        for (int i = 0; i < integers.length - 1; i++) {
            for (int j = i + 1; j < integers.length; j++) {
                if (Objects.equals(integers[i], integers[j])) {
                    if (integers[i] != null && integers[j] != null) {
                        invalidIds.add(i);
                        invalidIds.add(j);
                    }
                }
            }
        }

        return invalidIds;
    }

    private Integer[][] generateBoardFields() {
        Integer[][] fields = new Integer[9][];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = new Integer[9];
        }

        fields[2][0] = 9;
        fields[3][0] = 2;
        fields[4][0] = 3;


        fields[1][1] = 3;
        fields[5][1] = 4;
        fields[6][1] = 5;
        fields[7][1] = 1;

        fields[1][2] = 2;
        fields[8][2] = 9;

        fields[1][3] = 9;
        fields[8][3] = 5;


        fields[0][4] = 8;
        fields[8][4] = 6;

        fields[0][5] = 5;
        fields[7][5] = 9;

        fields[0][6] = 7;
        fields[7][6] = 3;


        fields[1][7] = 4;
        fields[2][7] = 8;
        fields[3][7] = 7;
        fields[7][7] = 5;


        fields[4][8] = 6;
        fields[5][8] = 9;
        fields[6][8] = 2;

        return fields;
    }


    public Board generateBoard() {
        Board b = new Board();
        b.setId(UUID.randomUUID().toString());
        Integer[][] fields = generateBoardFields();
        b.setFields(fields);
        return b;
    }

}
