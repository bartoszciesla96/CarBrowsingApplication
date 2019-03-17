package car.homework.service;

import car.homework.exceptions.MyException;
import car.homework.model.enums.Color;
import car.homework.service.enums.SortType;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Scanner;

public class UserDataService {
    private Scanner scanner = new Scanner(System.in);

    public String getString(String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    public int getInt(String message) {
        System.out.println(message);

        String text = scanner.nextLine();
        if (!text.matches("\\d+")) {
            throw new MyException("INT VALUE IS NOT CORRECT");
        }
        return Integer.parseInt(text);
    }

    public BigDecimal getBigDecimal(String message) {
        System.out.println(message);

        String text = scanner.nextLine();
        if(!text.matches("\\d+")) {
            throw new MyException("VALUE IS NOT A NUMBER");
        }
        DecimalFormat df = new DecimalFormat();
        df.setParseBigDecimal(true);
        try {
            return (BigDecimal) df.parse(text);
        }
        catch (Exception e) {
            throw new MyException("BIGDECIMAL IS NOT CORRECT");
        }
    }


    public SortType getSortType() {
        System.out.println("1 - price");
        System.out.println("2 - model");
        System.out.println("3 - color");
        System.out.println("4 - mileage");
        System.out.println("Enter sort type");
        String text = scanner.nextLine();

        if (!text.matches("\\d")) {
            throw new MyException("SORT TYPE IS NOT VALID");
        }

        int option = Integer.parseInt(text);
        if (option < 1 || option > 4) {
            throw new MyException("SORT TYPE IS NOT VALID");
        }

        return SortType.values()[option - 1];
    }

    public Color getColor() {
        System.out.println("1 - nice");
        System.out.println("2 - ugly");
        System.out.println("3 - ok");
        String text = scanner.nextLine();

        if (!text.matches("\\d")) {
            throw new MyException("COLOR VALUE IS NOT VALID");
        }

        int option = Integer.parseInt(text);
        if (option < 1 || option > 3) {
            throw new MyException("COLOR VALUE IS NOT VALID");
        }
        return Color.values()[option - 1];
    }

    public void close() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
    }
}
