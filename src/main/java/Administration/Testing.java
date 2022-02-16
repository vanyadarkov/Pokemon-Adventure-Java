package Administration;

import Administration.Logger.LoggerMenu;
import Adventure.Arena;
import Adventure.Pokemons.PokemonUtils.Coach;

import java.util.List;
import java.util.Scanner;

public class Testing {
    public static void main(String[] args){

        InputHelper inputHelper = new InputHelper();

        /* Choose the input file number */
        int inputFileNumber = inputHelper.getAvailableFileNumber();

        Scanner sc = new Scanner(System.in);
        inputHelper.setFileNumber(inputFileNumber);

        /* Read from the file */
        List<Coach> coaches = inputHelper.getCoachesFromXml();
        /*
         * Returning an empty list means that the input file was invalid
         */
        if(coaches.isEmpty()) return;

        System.out.print("Do you want to display to stdout or to the file? (0 - stdout / 1 - file) ");
        int outputOption;

        do{
            outputOption = sc.nextInt();
            if(outputOption == 0 || outputOption == 1){
                break;
            }
            System.out.print("The selected option is invalid! Choose 0 / 1: ");
        } while (true);

        LoggerMenu loggerMenu = new LoggerMenu(outputOption, inputFileNumber);

        Arena arena = new Arena(coaches.get(0), coaches.get(1));
        try{
            arena.beginAdventure();
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

        /* Get arenaHistory */
        String arenaLogger = arena.getArenaHistory();
        /* Use the logger to write to the desired output stream */
        loggerMenu.print(arenaLogger);
        sc.close();
    }
}
