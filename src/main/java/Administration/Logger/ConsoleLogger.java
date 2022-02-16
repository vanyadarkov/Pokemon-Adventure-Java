package Administration.Logger;

/**
 * Console display logger
 */
public class ConsoleLogger implements Logger{

    /**
     * Writing the string to the console
     */
    @Override
    public void print(String toPrint) {
        System.out.println(toPrint);
    }
}
