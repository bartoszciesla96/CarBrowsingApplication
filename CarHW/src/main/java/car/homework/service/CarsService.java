package car.homework.service;

import car.homework.collectors.AverageCollector;
import car.homework.converters.CarsJsonConverter;
import car.homework.exceptions.MyException;
import car.homework.model.Car;
import car.homework.model.enums.Color;
import car.homework.service.enums.SortType;
import car.homework.validation.CarValidator;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarsService {

    private final Set<Car> cars;

    public CarsService(String jsonFilename) {
        cars = getCarsFromJsonFile(jsonFilename);
    }

    //method that loads cars from json file
    private Set<Car> getCarsFromJsonFile(String jsonFilename) {
        if(jsonFilename.isEmpty()) {
            throw new MyException("FILENAME IS NOT CORRECT");
        }

        CarValidator carValidator = new CarValidator();
        return new CarsJsonConverter(jsonFilename)
                .fromJson()
                .orElseThrow(() -> new MyException("CAR SERVICE CONVERSION IS NOT CORRECT"))
                .stream()
                .filter(car -> {
                    System.out.println("-------------------------------------------------------");
                    carValidator.validate(car).forEach((k, v) -> System.out.println(k + " " + v));
                    return !carValidator.hasErrors();
                })
                .collect(Collectors.toSet());
    }

    //method that allows to add a single car to the cars collection
    public void addCar(Car car) {
        if (car == null) {
            throw new MyException("CAR OBJECT IS NULL");
        }
        cars.add(car);
    }

    //method that reloads cars collection to the default value (from json file)
    public void resetCars(String jsonFilename) {
        if(jsonFilename.isEmpty()) {
            throw new MyException("FILENAME IS NOT CORRECT");
        }

        cars.clear();
        cars.addAll(getCarsFromJsonFile(jsonFilename));
    }

    @Override
    public String toString() {
        return cars.stream().map(Object::toString).collect(Collectors.joining("\n"));
    }

    //method that sorts cars by requested value
    public List<Car> sort(SortType sortType, boolean descending) {
        if (sortType == null) {
            throw new MyException("SORT TYPE IS NOT CORRECT");
        }

        Stream<Car> carsStream = null;

        switch (sortType) {
            case COLOR:
                carsStream = cars.stream().sorted(Comparator.comparing(Car::getColor));
                break;
            case MODEL:
                carsStream = cars.stream().sorted(Comparator.comparing(Car::getModel));
                break;
            case PRICE:
                carsStream = cars.stream().sorted(Comparator.comparing(Car::getPrice));
                break;
            case MILEAGE:
                carsStream = cars.stream().sorted(Comparator.comparing(Car::getMileage));
                break;
        }

        List<Car> sortedCars = carsStream.collect(Collectors.toList());

        if (descending) {
            Collections.reverse(sortedCars);
        }
        return sortedCars;
    }

    //method that finds cars with higher mileage than requested
    public List<Car> showCarsAboveMileage(int mileage) {

        if (mileage <= 0) {
            throw new MyException("MILEAGE VALUE IS NOT CORRECT");
        }

        return cars
                .stream()
                .filter(c -> c.getMileage() > mileage)
                .collect(Collectors.toList());
    }

    //method that shows how many cars have certain color
    public Map<Color, Long> countedByColor() {
        return cars
                .stream()
                .collect(Collectors.groupingBy(Car::getColor, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, LinkedHashMap::new)
                );

    }


    //method that shows most expensive cars of certain model
    public Map<String, Car> carModelsWithMostExpensiveCar() {
        return cars
                .stream()
                .collect(Collectors.groupingBy(
                            Car::getModel,
                            Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Car::getPrice)), carOptional -> carOptional.orElseThrow(() -> new MyException("CAR IS NOT CORRECT")))
                        )
                );

    }


    //method that shows car statistics
    public void carStatistics() {
        System.out.println("PRICE STATISTICS");
        System.out.println("AVERAGE PRICE");
        System.out.println(cars.stream().map(Car::getPrice).collect(new AverageCollector()));
        System.out.println("MAX PRICE");
        System.out.println(cars.stream().max(Comparator.comparing(Car::getPrice)).orElseThrow(() -> new MyException("MAX PRICE IS NOT CORRECT")));
        System.out.println("MIN PRICE");
        System.out.println(cars.stream().min(Comparator.comparing(Car::getPrice)).orElseThrow(() -> new MyException("MIN PRICE IS NOT CORRECT")));

        System.out.println("--------------------------");

        System.out.println("MILEAGE STATISTICS");
        IntSummaryStatistics iss = cars.stream().collect(Collectors.summarizingInt(Car::getMileage));
        System.out.println("AVERAGE MILEAGE");
        System.out.println(iss.getAverage());
        System.out.println("MAX MILEAGE");
        System.out.println(iss.getMax());
        System.out.println("MIN MILEAGE");
        System.out.println(iss.getMin());
    }

    //method that shows most expensive car(s)
    public List<Car> mostExpensiveCar() {
        return cars
                .stream()
                .max(Comparator.comparing(Car::getPrice))
                .map(car -> cars.stream().filter(c -> c.getPrice().equals(car.getPrice())))
                .orElseThrow(() -> new MyException("COULD NOT FIND MOST EXPENSIVE CARS"))
                .collect(Collectors.toList());
   }

    //method that sorts components of cars
    public Set<Car> sortComponents() {
        return cars
                .stream()
                .peek(c -> c.setComponents(
                        c.getComponents()
                                .stream()
                                .sorted()
                                .collect(Collectors.toCollection(LinkedHashSet::new))
                        )
                )
                .collect(Collectors.toSet());
    }

   //method that shows how many cars have certain component
    public Map<String, Set<Car>> carContainsComponent() {
        return cars
                .stream()
                .flatMap(car -> car.getComponents().stream())
                .distinct()
                .collect(Collectors.toMap(
                        component -> component,
                        component -> cars.stream().filter(car -> car.getComponents().contains(component)).collect(Collectors.toSet())
                ))
                .entrySet()
                .stream()
                .sorted(Comparator.comparing(e -> e.getValue().size(), Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> v1,
                        LinkedHashMap::new
                ));
    }

    //method that shows cars in price range
    public List<Car> showCarsInPriceRange(BigDecimal a, BigDecimal b) {
        if (b.compareTo(a) < 0) {
            throw new MyException("GIVEN PRICE RANGE IS NOT CORRECT");
        }
        return cars.stream()
                .filter(c -> c.getPrice().compareTo(a) >= 0 && c.getPrice().compareTo(b) <= 0)
                .sorted(Comparator.comparing(Car::getModel))
                .collect(Collectors.toList());
    }
}
