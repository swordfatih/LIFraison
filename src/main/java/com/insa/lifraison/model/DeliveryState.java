package com.insa.lifraison.model;

public enum DeliveryState {
    NotCalculated,
    Ok,
    NotPossible,
    Late;

    @Override
    public String toString() {
        return switch (this) {
            case NotCalculated -> "Not calculated";
            case Ok -> "Ok";
            case NotPossible -> "Not possible";
            case Late -> "Late";
            default -> "Default";
        };
    }
}
