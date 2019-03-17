package car.homework.validation;

import car.homework.model.Car;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CarValidator {

    private Map<String, String> errors = new HashMap<>();

    public Map<String, String> validate(Car car) {

        if (car == null) {
            errors.put("car", "car is null");
        }

        if (!isModelValid(car)) {
            errors.put("model", "model is not valid");
        }

        if (!areComponentsValid(car)) {
            errors.put("components", "components are not valid");
        }

        if(!isPriceValid(car)) {
            errors.put("price", "price is not valid");
        }

        if(!isMileageValid(car)) {
            errors.put("mileage", "mileage is not valid");
        }

        return errors;

    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    private boolean isModelValid(Car car) {
        return car.getModel() != null && car.getModel().matches("[A-Z\\s]+");
    }

    private boolean areComponentsValid(Car car) {
        return car.getComponents() != null && car.getComponents().stream().allMatch(comp -> comp.matches("[A-Z\\s]+"));
    }

    private boolean isPriceValid(Car car) {
        return car.getPrice() != null && car.getPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isMileageValid(Car car) {
        return car.getMileage() > 0;
    }

}
