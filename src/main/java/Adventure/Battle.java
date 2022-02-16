package Adventure;

import Adventure.Pokemons.PokemonUtils.Coach;
import Adventure.Pokemons.PokemonUtils.PokemonForBattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The class in which the battle between the pokemon will take place
 * Contains coaches and pokemon fighting
 */
public class Battle implements Runnable{

    private final Coach coach1;
    private final PokemonForBattle pokemon1;
    private final Coach coach2;
    private final PokemonForBattle pokemon2;

    /* flag that says if in the final battle there was a draw or a pokemon 1 win */
    private boolean isDraw = false;
    private boolean pokemon1Wins = false;

    /* StringBuilder in which the history of the battle will be stored */
    private final StringBuilder battleHistory;

    /**
     * Constructor who creates a battle between coaches
     * @param coach1 coach 1
     * @param pokemon1 pokemon of coach 1
     * @param coach2 coach 2
     * @param pokemon2 pokemon of coach 2
     */
    public Battle(Coach coach1, PokemonForBattle pokemon1, Coach coach2, PokemonForBattle pokemon2) {
        this.coach1 = coach1;
        this.pokemon1 = pokemon1;
        this.coach2 = coach2;
        this.pokemon2 = pokemon2;
        this.battleHistory = new StringBuilder();
    }

    /**
     * Constructor which creates a battle between coach and neutral
     * @param coach1 coach 1
     * @param pokemon1 pokemon of coach1
     * @param pokemon2 the neutral with which he struggles
     */
    public Battle(Coach coach1, PokemonForBattle pokemon1, PokemonForBattle pokemon2) {
        this.coach1 = coach1;
        this.pokemon1 = pokemon1;
        /* Because it's a neutral fight, coach2 will be null */
        this.coach2 = null;
        this.pokemon2 = pokemon2;
        this.battleHistory = new StringBuilder();
    }

    @Override
    public void run() {
        appendVersus();

        appendElementsForPokemons();

        appendPokemonsStats();

        int rounds = 0;
        do{
            rounds++;
            appendRounds(rounds);
            appendPokemonsHP();
            appendAbilitiesCdForPokemons();

            List<Object> pokemon1Moves = coach1.getPossibleAttacks(pokemon1);
            List<Object> pokemon2Moves;
            /* If coach 2 is null, it means that he is neutral and has only one possibility of attack */
            if(coach2 != null){
                pokemon2Moves = coach2.getPossibleAttacks(pokemon2);
            }
            else {
                pokemon2Moves = new ArrayList<>();
                pokemon2Moves.add(pokemon2.getAttack());
            }
            /* Randomly choose an attack ability for each pokemon */
            Object pokemon1Attack = pokemon1Moves.get(new Random().nextInt(pokemon1Moves.size()));
            Object pokemon2Attack = pokemon2Moves.get(new Random().nextInt(pokemon2Moves.size()));

            /* Create a service executor */
            ExecutorService executorService = Executors.newCachedThreadPool();

            /* Log for the first pokemon attack */
            StringBuilder pokemon1Log = new StringBuilder();
            /* Execute attack */
            executorService.execute(() -> pokemon1.attackPokemon(pokemon2, pokemon1Attack, pokemon2Attack, pokemon1Log));

            StringBuilder pokemon2Log = new StringBuilder();

            executorService.execute(() -> pokemon2.attackPokemon(pokemon1, pokemon2Attack, pokemon1Attack, pokemon2Log));

            /* Terminate executor service and wait in case a thread has not finished */
            executorService.shutdown();
            try {
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* Append to the battle history log from each attack */
            battleHistory.append("\n1) ").append(pokemon1Log).append("\n2) ").append(pokemon2Log);

            /* Check if any pokemon will have to be stunned in the next round and set it as stunned */
            if(pokemon1.getWillBeStunned()){
                pokemon1.setWillBeStunned(false);
                pokemon1.setStunned(true);
            }
            if(pokemon2.getWillBeStunned()){
                pokemon2.setWillBeStunned(false);
                pokemon2.setStunned(true);
            }

            /* Decreases the cooldown of each pokemon's abilities */
            pokemon1.refreshAbilities();
            pokemon2.refreshAbilities();
        } while(!pokemon1.isDead() && !pokemon2.isDead());

        appendBattleResult();

        calculateScoreBestPokemons();

        /* Reset the status of each pokemon because the battle is over */
        pokemon1.resetState();
        pokemon2.resetState();
    }

    /**
     * Calculate the pokemon score in case of a battle between coaches
     */
    public void calculateScoreBestPokemons(){
        if(coach2 != null){
            pokemon1.calculateScore();
            battleHistory.append("\n");
            battleHistory.append(pokemon1.getName()).append(" (").append(coach1.getName()).append(") Score = [");
            battleHistory.append(pokemon1.getScore()).append("]");
            coach1.setBestPokemon(pokemon1);
            pokemon2.calculateScore();
            battleHistory.append("\n");
            battleHistory.append(pokemon2.getName()).append(" (").append(coach2.getName()).append(") Score = [");
            battleHistory.append(pokemon2.getScore()).append("]");
            coach2.setBestPokemon(pokemon2);
        }
    }

    /**
     * Displays the battle result, and if any pokemon won, apply the battle bonus to him
     */
    private void appendBattleResult(){
        if(pokemon1.isDead() && pokemon2.isDead()){
            battleHistory.append("\n").append("\t+++ Draw +++");
            this.setDraw(true);
            return;
        }
        /* Pokemon 2 has won */
        if(pokemon1.isDead()) {
            battleHistory.append("\n\t+++ ").append(pokemon2.getName());
            /* If he is a coach, he displays the name of the coach */
            if(coach2 != null) battleHistory.append(" (").append(coach2.getName()).append(")");
            battleHistory.append(" Won the Battle +++\n");
            /* Aplica bonusul pentru castig */
            pokemon2.applyWinBonus();
            /*
             * Because the bonus is applied first and then the score is calculated, I decrease the score by 3 so as not
             * to get 3 extra score points (for Attack, Def, Special Def). When the score is calculated then it will be
             * calculated as if ignoring the win bonus.
             */
            pokemon2.setScore(pokemon2.getScore() - 3);
            battleHistory.append("\t+++ ");
            if(coach2 != null) battleHistory.append(coach2.getName()).append("'s ");
            battleHistory.append(pokemon2.getName()).append(" got +1 bonus to all stats +++");
        }
        /* Pokemon 1 has won */
        if(pokemon2.isDead()) {
            this.setPokemon1Wins(true);
            battleHistory.append("\n\t+++ ").append(pokemon1.getName());
            battleHistory.append(" (").append(coach1.getName()).append(")");
            battleHistory.append(" Won the Battle +++\n");
            pokemon1.applyWinBonus();
            pokemon2.setScore(pokemon2.getScore() - 3);
            battleHistory.append("\t+++ ").append(coach1.getName()).append("'s ").append(pokemon1.getName()).append(" got +1 bonus to all stats +++");
        }
    }

    /**
     * Append to battleHistory early round
     * @param rounds round number
     */
    private void appendRounds(int rounds) {
        battleHistory.append("\n^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ ROUND ")
                    .append(rounds)
                    .append(" ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^")
                    .append("\n");
    }

    /**
     * Append to battleHistory skill cooldowns for each pokemon
     */
    private void appendAbilitiesCdForPokemons(){
        battleHistory.append("\n");
        pokemon1.getAbilitiesCd(battleHistory);
        battleHistory.append(" || ");
        pokemon2.getAbilitiesCd(battleHistory);
    }

    /**
     * Append to battleHistory the characteristics of pokemon
     */
    private void appendPokemonsStats(){
        battleHistory.append("\n*").append(pokemon1.getName()).append(" ");
        pokemon1.getStats(battleHistory);
        battleHistory.append(")\n");
        battleHistory.append("*").append(pokemon2.getName()).append(" ");
        pokemon2.getStats(battleHistory);
        battleHistory.append(")");
    }

    /**
     * Append to the logger pokemon health status
     */
    private void appendPokemonsHP(){
        battleHistory.append(pokemon1.getName()).append(" (").append(coach1.getName()).append(")");
        battleHistory.append(" HP[").append(pokemon1.getCurrentHP()).append("] VS ");
        battleHistory.append(pokemon2.getName());
        if(coach2 != null) battleHistory.append(" (").append(coach2.getName()).append(")");
        battleHistory.append(" HP[").append(pokemon2.getCurrentHP()).append("]");
    }

    /**
     * Append to the logger who is fighting
     */
    private void appendVersus(){
        battleHistory.append(pokemon1.getName()).append(" (").append(coach1.getName()).append(") VS ");
        battleHistory.append(pokemon2.getName());
        if(coach2 != null) battleHistory.append(" (").append(coach2.getName()).append(")");
    }

    /**
     * Append to the logger the characteristics of the items and to what characteristics they increase if the items have
     * not yet been used
     */
    private void appendElementsForPokemons(){
        String s;
        /* Check if the items have not been used yet and if the pokemon generally has items */
        if(!pokemon1.usedItems() && !pokemon1.getItems().isEmpty()) {
            /* Use items */
            s = pokemon1.useItems();
            this.battleHistory.append("\n").append(coach1.getName()).append("'s ").append(pokemon1.getName()).append("\n").append(s);
        }
        if(!pokemon2.usedItems() && !pokemon2.getItems().isEmpty()){
            /* Use use items */
            s = pokemon2.useItems();

            /* Check if there is a fight between the coach to be able to apply the items */
            if(coach2 != null)
                this.battleHistory.append("\n").append(coach2.getName()).append("'s ").append(pokemon2.getName()).append("\n").append(s);
        }
    }

    /*
     * Getters and setters
     */
    public boolean isDraw() {
        return isDraw;
    }

    public boolean isPokemon1Wins() {
        return pokemon1Wins;
    }

    public void setDraw(boolean draw) {
        isDraw = draw;
    }

    public void setPokemon1Wins(boolean pokemon1Wins) {
        this.pokemon1Wins = pokemon1Wins;
    }

    public StringBuilder getBattleHistory() {
        return battleHistory;
    }
}
