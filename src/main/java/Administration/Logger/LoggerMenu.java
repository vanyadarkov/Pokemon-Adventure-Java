package Administration.Logger;

/**
 * The class used to create a logger depending on the required option (stdout / file)
 */
public class LoggerMenu implements Logger{
    private Logger logger;

    /**
     * Creating a logger that will operate with the received string
     * @param loggerType 0 - stdout, 1 - fisier
     * @param inputFileNumber he input file from which it was read
     */
    public LoggerMenu(int loggerType, int inputFileNumber) {
        if(loggerType == 0){
            logger = new ConsoleLogger();
        }
        else if (loggerType == 1){
            logger = new FileLogger(inputFileNumber);
        }
    }

    /**
     * Display the toPrint string using the set logger
     */
    @Override
    public void print(String toPrint) {
        logger.print(toPrint);
    }
}
