package com.insa.lifraison.model;

/**
 * This class describes the possible states of the calculation of the path of a tour.
 */
public enum DeliveryState {
    NotCalculated,
    Ok,
    NotPossible,
    Late;

    /**
     * Description of the Delivery State
     * @return A string containing the description of the delivery state.
     */
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
