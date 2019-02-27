package car.homework.service;

import car.homework.exceptions.MyException;
import car.homework.model.Car;
import car.homework.model.enums.Color;
import car.homework.validation.CarValidator;

import java.math.BigDecimal;
import java.util.Set;

public class MenuService {

    private final String jsonFilename;
    private final UserDataService userDataService;
    private final CarsService carsService;
    private final CarValidator carValidator;

    public MenuService(String jsonFilename) {
        this.jsonFilename = jsonFilename;
        userDataService = new UserDataService();
        carsService = new CarsService(jsonFilename);
        carValidator = new CarValidator();
    }

    public void mainMenu() {
        do {
            try {
                int option = printMenu();
                switch (option) {
                    case 1:
                        option1();
                        break;
                    case 2:
                        option2();
                        break;
                    case 3:
                        option3();
                        break;
                    case 4:
                        option4();
                        break;
                    case 5:
                        option5();
                        break;
                    case 6:
                        option6();
                        break;
                    case 7:
                        option7();
                        break;
                    case 8:
                        option8();
                        break;
                    case 9:
                        option9();
                        break;
                    case 10:
                        option10();
                        break;
                    case 11:
                        option11();
                        break;
                    case 12:
                        option12();
                        break;
                    case 13:
                        userDataService.close();
                        return;
                        default:
                            throw new MyException("MENU OPTION IS NOT CORRECT");
                }
            } catch (MyException e) {
                System.err.println(e.getExceptionMessage());
            }
        } while (true);
    }

    private int printMenu() {
        System.out.println("1. Add new car");
        System.out.println("2. Reset cars to default from jsonfile");
        System.out.println("3. Print all cars");
        System.out.println("4. Sort cars by given value");
        System.out.println("5. Show cars about given mileage");
        System.out.println("6. Count cars by given color");
        System.out.println("7. Show most expensive car models");
        System.out.println("8. Show car statistics");
        System.out.println("9. Show most expensive cars");
        System.out.println("10. Sort car components");
        System.out.println("11. Count cars which contain component");
        System.out.println("12. Show cars within given price range");
        System.out.println("13. Finish");
        return userDataService.getInt("Choose option");
    }

    private void option1() {
        System.out.println("Adding a new car");
        String model = userDataService.getString("New car model = ");
        model = model.toUpperCase();
        int mileage = userDataService.getInt("Car mileage = ");
        BigDecimal price = userDataService.getBigDecimal("Car price = ");
        System.out.println("Color: 0 = NICE, 1 = UGLY, 2 = OK");
        int color = userDataService.getInt("Car color  = ");
        if (color != 0 || color != 1 || color != 2) {
            throw new MyException("COLOR VALUE IS NOT CORRECT");
            Set<String> components = null;

            boolean cont = true;
            String component = null;
            String yn = "y";
            while (cont) {
                component = userDataService.getString("Car component = ");
                components.add(component);
                yn = userDataService.getString("Do you want to add another component y/n ");
                if (yn.equals("y")) {
                    cont = true;
                } else {
                    cont = false;
                }
            }
            Car car = Car.builder().model(model).mileage(mileage).price(price).color(color).components(components).build();
            carValidator.validate(car);
            carsService.addCar(car);
        }
    }

    private void option2() {
        System.out.println("Resetting cars collection");
        carsService.resetCars(jsonFilename);
    }

    private void option3() {
        System.out.println("Cars: ");
        carsService.toString();
    }
    private void option4() {
        System.out.println("Sort cars by field");
        int sortType = userDataService.getInt("Choose field (0 - model/1 - mileage/2 - price/3 - color/4 - components) = ");
        String desc = userDataService.getString("Descending y/n");
        boolean descending = true;
        if (desc.equals("y")) { descending = true; }
        else { descending = false; }
        carsService.sort(sortType, descending);
    }
    private void option5() {
        System.out.println("Showing cars above mileage");
        int mileage = userDataService.getInt("Choose mileage = ");
        carsService.showCarsAboveMileage(mileage);
    }

    private void option6() {
        System.out.println("Counting cars by color");
        carsService.countedByColor();
    }
    private void option7() {
        System.out.println("Most expensive car model: ");
        carsService.carModelsWithMostExpensiveCar();
    }
    private void option8() {
        System.out.println("Showing car statistics: ");
        carsService.carStatistics();
    }

    private void option9() {
        System.out.println("Showing most expensive cars: ");
        carsService.mostExpensiveCar();
    }

    private void option10() {
        System.out.println("Sorting car components: ");
        carsService.sortComponents();
    }

    private void option11() {
        System.out.println("Counting cars with component: ");
        carsService.carContainsComponent();
    }

    private void option12() {
        System.out.println("Showing cars in price range: ");
        BigDecimal a = userDataService.getBigDecimal("Price from: ");
        BigDecimal b = userDataService.getBigDecimal("to: ");
        carsService.showCarsInPriceRange(a, b);
    }
}
