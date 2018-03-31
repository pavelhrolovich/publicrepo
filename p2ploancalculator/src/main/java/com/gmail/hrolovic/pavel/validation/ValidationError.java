package com.gmail.hrolovic.pavel.validation;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode @Getter @Builder
public class ValidationError {
    private String argumentName;
    private String message;
}
