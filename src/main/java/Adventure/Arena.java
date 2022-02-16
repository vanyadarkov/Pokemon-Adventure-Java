package Adventure;

import Adventure.Pokemons.PokemonUtils.Coach;
import Adventure.Pokemons.PokemonUtils.PokemonFactory;
import Adventure.Pokemons.PokemonUtils.PokemonForBattle;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The class of an arena where pokemon fights will take place. This is where the battles are created, sent to execution,
 * and random events are chosen for the battle
 */
public class Arena {
    private final Coach coach1;
    private final Coach coach2;
    private final StringBuilder arenaHistory;
    /* Number of battles between coaches that took place */
    private int coachBattles = 0;

    public Arena(Coach coach1, Coach coach2) {
        this.coach1 = coach1;
        this.coach2 = coach2;
        this.arenaHistory = new StringBuilder();
    }

    public enum OpponentType {
        Neutrel1, Neutrel2, Coach
    }

    /**
     * The method that deals with creating a battle between coaches. Receive their coaches and pokemon
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    private @NotNull Battle createBattle(
            Coach firstPlayer, PokemonForBattle firstPlayerPokemon,
            Coach secondPlayer, PokemonForBattle secondPlayerPokemon){
        return new Battle(firstPlayer, firstPlayerPokemon, secondPlayer, secondPlayerPokemon);
    }

    /**
     * The method of creating a battle between a coach and a neutral.
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    private @NotNull Battle createBattle(
            Coach firstPlayer, PokemonForBattle firstPlayerPokemon,
            PokemonForBattle secondPlayer){
        return new Battle(firstPlayer, firstPlayerPokemon, secondPlayer);
    }

    public void beginAdventure() throws InterruptedException {
        appendArenaEntering();
        /*
         * Creating neutrals for each coach, each coach will have one neutrel1 and neutrel2 to fight
         */
        PokemonForBattle neutrel1forCoach1 = new PokemonForBattle(PokemonFactory.getFactory().getPokemon("Neutrel1"));
        PokemonForBattle neutrel1forCoach2 = new PokemonForBattle(PokemonFactory.getFactory().getPokemon("Neutrel1"));
        PokemonForBattle neutrel2forCoach1 = new PokemonForBattle(PokemonFactory.getFactory().getPokemon("Neutrel2"));
        PokemonForBattle neutrel2forCoach2 = new PokemonForBattle(PokemonFactory.getFactory().getPokemon("Neutrel2"));
        /* List for saving each battle */
        List<Battle> battles = new ArrayList<>();
        Battle battle;
        /* As long as there were no 3 battles between coaches */
        while(coachBattles != 3){
            /* Choosing a random opponent */
            OpponentType opponent = OpponentType.values()[new Random().nextInt(3)];
            if(opponent.equals(OpponentType.Coach)) {
                /* The order of the battles is displayed */
                arenaHistory.append("\n").append(coach1.getName()).append("'s ");
                arenaHistory.append(coach1.getPokemons().get(coachBattles).getName()).append(" vs ");
                arenaHistory.append(coach2.getName()).append("'s ");
                arenaHistory.append(coach2.getPokemons().get(coachBattles).getName());

                /* creating a battle between coaches */
                battle = createBattle(coach1, coach1.getPokemons().get(coachBattles),
                        coach2, coach2.getPokemons().get(coachBattles));
                /* Adding the battle to the list */
                battles.add(battle);
                /* executing battle */
                battle.run();
                coachBattles++;
            }
            else if(opponent.equals(OpponentType.Neutrel1)){
                ExecutorService executorService = Executors.newCachedThreadPool();

                /* The order of the battles is displayed */
                arenaHistory.append("\n").append(coach1.getName()).append("'s ");
                arenaHistory.append(coach1.getPokemons().get(coachBattles).getName()).append(" vs ");
                arenaHistory.append("Neutrel1 and ");
                arenaHistory.append(coach2.getName()).append("'s ");
                arenaHistory.append(coach2.getPokemons().get(coachBattles).getName()).append(" vs ");
                arenaHistory.append("Neutrel1");
                /* Performs coach battles with neutrals */
                executeBattlesWithNeutrel(neutrel1forCoach1, neutrel1forCoach2, battles, executorService);
            }
            else {
                ExecutorService executorService = Executors.newCachedThreadPool();

                /* The order of the battles is displayed */
                arenaHistory.append("\n").append(coach1.getName()).append("'s ");
                arenaHistory.append(coach1.getPokemons().get(coachBattles).getName()).append(" vs ");
                arenaHistory.append("Neutrel2 and ");
                arenaHistory.append(coach2.getName()).append("'s ");
                arenaHistory.append(coach2.getPokemons().get(coachBattles).getName()).append(" vs ");
                arenaHistory.append("Neutrel2");

                /* Performs coach battles with neutrals */
                executeBattlesWithNeutrel(neutrel2forCoach1, neutrel2forCoach2, battles, executorService);
            }
        }

        /* Display last battle */
        arenaHistory.append("\n")
                    .append(coach1.getName()).append("'s best pokemon")
                    .append(" vs ")
                    .append(coach2.getName()).append("'s best pokemon");

        appendBattlesHistory(battles);
        appendCoachesBestPokemons();

        arenaHistory.append("\n====================================")
                    .append(" FINAL BATTLE ")
                    .append("====================================\n");

        /* Create a final battle */
        Battle finalBattle = createBattle(coach1, coach1.getBestPokemon(), coach2, coach2.getBestPokemon());
        finalBattle.run();
        arenaHistory.append(finalBattle.getBattleHistory().toString())
                    .append("\n===========================================")
                    .append("===========================================\n");

        /* Display adventure winner */
        if(finalBattle.isDraw()){
            arenaHistory.append("It's draw in this adventure!");
        }
        else if(finalBattle.isPokemon1Wins()){
            arenaHistory.append(coach1.getName()).append(" won this adventure!");
        }
        else {
            arenaHistory.append(coach2.getName()).append(" won this adventure!");
        }
    }

    /**
     * Append the history of the battles to the history of the arena
     */
    private void appendBattlesHistory(@NotNull List<Battle> battles) {
        int i = 1;
        for(Battle b : battles){
            arenaHistory.append("\n====================================== Battle ")
                        .append(i)
                        .append(" ======================================\n")
                        .append(b.getBattleHistory()
                        .toString());
            i++;
        }
    }

    /**
     *  Append to the ArenaHistory of the best pokemon for each coach
     */
    private void appendCoachesBestPokemons() {
        arenaHistory.append("\n===========================================")
                    .append("===========================================\n");
        arenaHistory.append("The best pokemon of the coaches\n");

        appendCoachBestPokemon(coach1);

        arenaHistory.append("\n");

        appendCoachBestPokemon(coach2);
    }

    /**
     * Create and execute battles between coaches and neutrals
     * @param neutrelforCoach1 neutrel for coach1
     * @param neutrelforCoach2 neutrel for coach2
     * @param battles the list of battles to which will be added the battles created here
     * @param executorService the execution service that will run the battles
     */
    private void executeBattlesWithNeutrel(PokemonForBattle neutrelforCoach1, PokemonForBattle neutrelforCoach2, @NotNull List<Battle> battles, @NotNull ExecutorService executorService) {
        Battle battle;
        battle = createBattle(coach1, coach1.getPokemons().get(coachBattles),
                neutrelforCoach1);
        battles.add(battle);

        /* Execute the battle of coach 1 with neutral */
        executorService.execute(battle);

        battle = createBattle(coach2, coach2.getPokemons().get(coachBattles),
                neutrelforCoach2);
        battles.add(battle);

        /* Execute the battle of coach 1 with neutral */
        executorService.execute(battle);

        /* Execute the execution service and wait for it to end */
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Append to the arenaHistory the opening message of the arena
     */
    private void appendArenaEntering() {
        arenaHistory.append("-------------------------------");
        arenaHistory.append(" Arena Adventure Begins ");
        arenaHistory.append("-------------------------------\n");
        appendCoachWithPokemons(coach1);
        appendCoachWithPokemons(coach2);
        arenaHistory.append("------------------------------");
        arenaHistory.append(" The order of the battles ");
        arenaHistory.append("------------------------------");
    }

    /**
     * Displays the best pokemon of the trainer and his score
     * @param coach the coach for whom I was displaying the score
     */
    private void appendCoachBestPokemon(@NotNull Coach coach) {
        arenaHistory.append(coach.getBestPokemon().getName()).append(" (").append(coach.getName()).append(") ");
        arenaHistory.append("Score [").append(coach.getBestPokemon().getScore()).append("]");
    }

    /**
     * Displaying his trainer and pokemon
     * @param coach the coach I was showing for
     */
    private void appendCoachWithPokemons(@NotNull Coach coach) {
        arenaHistory.append(coach.getName()).append(" with pokemons: ")
                    .append(coach.getPokemons().get(0).getName()).append(", ")
                    .append(coach.getPokemons().get(1).getName()).append(", ")
                    .append(coach.getPokemons().get(2).getName()).append("\n");
    }

    /**
     * getting arenaHistory
     * @return The string that contains the history of the arena
     */
    public String getArenaHistory(){
        return this.arenaHistory.toString();
    }

}
