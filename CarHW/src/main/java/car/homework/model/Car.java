package car.homework.model;


import car.homework.exceptions.MyException;
import car.homework.model.enums.Color;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    private String model;
    private int mileage;
    private BigDecimal price;
    private Color color;
    private Set<String> components;

/* LEGACY VALIDATOR USING BUILDER
    private Car(CarBuilder carBuilder) {
        model = carBuilder.model;
        mileage = carBuilder.mileage;
        price = carBuilder.price;
        color = carBuilder.color;
        components = carBuilder.components;
    }

    public static CarBuilder builder() {
        return new CarBuilder();
    }

    public static class CarBuilder {
        private String model;
        private int mileage;
        private BigDecimal price;
        private Color color;
        private Set<String> components;

        public CarBuilder model(String model) {

            if (model == null || !model.matches("[A-Z\\s]+")) {
                throw new MyException("MODEL VALUE IS NOT CORRECT");
            }

            this.model = model;
            return this;
        }

        public CarBuilder mileage(int mileage) {
            if (mileage < 0) {
                throw new MyException("MILEAGE VALUE IS NOT CORRECT");
            }
            this.mileage = mileage;
            return this;
        }

        public CarBuilder price(BigDecimal price) {
            if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
                throw new MyException("PRICE VALUE IS NOT CORRECT");
            }
            this.price = price;
            return this;
        }

        public CarBuilder color(Color color) {
            this.color = color;
            return this;
        }

        public CarBuilder components(Set<String> components) {
            Predicate<String> carFilter = Pattern.compile("[A-Z\\s]+").asPredicate();
            components = components
                    .stream()
                    .filter(carFilter)
                    .collect(Collectors.toSet());

            this.components = components;
            return this;
        }

        public Car build() {
            return new Car(this);
        }*/
}



