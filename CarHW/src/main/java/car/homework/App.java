package car.homework;

import car.homework.service.MenuService;

public class App
{
    public static void main( String[] args )
    {
        final String jsonFilename = "cars.json";
        new MenuService(jsonFilename).mainMenu();
    }
}
