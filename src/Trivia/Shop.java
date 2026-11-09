package Trivia;
import java.util.EnumMap;

public class Shop
{

    public Shop()
    {
        prices = new EnumMap<>(PowerUp.class);
        stock = new EnumMap<> (PowerUp.class);
    }
    //~ Fields ................................................................

    private EnumMap<PowerUp, Integer> prices;
    private EnumMap<PowerUp, Integer> stock;
    
    //~ Constructors ..........................................................

    //~Public  Methods ........................................................

}
