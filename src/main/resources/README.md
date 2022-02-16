# Pokemon Adventure in Java

## Description
In this Pokemon universe there are pokemon trainers about whom we know the name, age and pokemon that each of them owns.
There are, of course, real pokemonies. They have a **name** (which actually represents the type of pokemon, eg Pikachu), **HP** (in English "hit points", which represents how many life points a pokemon has when participating in a battle), **Attack** (the power of an attack) Special Attack, Defense, Special Defense (for Special Attack). Each pokemon will also have **two special abilities** that produce a hit and / or immobilize the stun pokemon and / or dodge the enemy's next move and a cooldown time. More details will be presented for each pokemon and in the following sections.
**Note**: A pokemon will have only one type of attack - either a normal attack or a special attack.
This universe is a little different from the usual Pokemon and offers the possibility for a pokemon to "carry" with it in a battle up to 3 objects that improve its qualities. Coaches have access to a lot of items and can give them to pokemon before a battle. There is no limit to a single item that can only be used by a coach's pokemon. That is, if we have the object "shield" and a trainer with 3 pokemon, all pokemon can wear the shield.
The only limitation is that a pokemon cannot carry the same item multiple times in the same battle. I mean, he can't have a shield, a shield, a shield.
## Adventure
Our adventure takes place in an arena. Two coaches will enter the arena each time, each of them having the opportunity to choose which pokemon from their list to enter the arena and which objects to offer to him.
In this arena 3 events can happen decided in a random way by the arena

- Each coach and his pokemon will fight separately with a neutral pokemon type 1 (Neutrel1).
- Each coach and his pokemon will fight separately with a neutral pokemon type 2 (Neutrel2).
- Coaches and their pokemons will duel with each other. (This will be the last fight in the arena and the coach who wins this fight will be declared the winner of the adventure).
## Battle
A battle will unfold as follows:
- At every moment each battle pokemon will execute a command indicated by the trainer.
- The trainer is the one who controls the pokemon and gives orders to it. Orders can be:
  - Normal attack - then the pokemon will attack the opposing pokemon with the type of attack it possesses (normal / special).
  - Ability 1
  - Ability 2
- If an ability is executed it will return 4 pieces of information
  - The damage done
  - If it's stunning
  - If they dodge this attack
  - Cooldown, how long does it take for the ability to be available again
- When a pokemon is attacked it will update its status (HP and if it is blocked, ie if it has been stunned) according to the enemy's movement and movement.
  - If he has executed a dodge-containing ability, then his condition remains unchanged no matter what the other pokemon did. Basically, in this case it doesn't matter what the opposing pokemon did and the details below can be ignored.
  - If the enemy has carried out a normal attack then he will lose HP equal to the difference between the type of enemy attack and his defense on that type of attack. Example: if the enemy pokemon has a normal attack 50 and the current pokemon has a defense 20, he will lose 30 life; If the enemy pokemon has a special attack 25 and the current pokemon has a special defense 5, he will lose 20 life.
  - If the enemy has executed an ability then the damage caused by the ability will be deducted from life points regardless of the current pokemon defense. If the enemy's ability is a stun-containing ability then the pokemon will not be able to execute any moves at the next time.
- The battle will end when one or both pokemon run out of life. This can happen when pokemon fight with neutral pokemon or when coaches fight with each other. The arena will have to return the name of the winning coach or "Draw" in case of a tie.
- After a fight (event) is over, the Pokemon return to their original HP status. Also, the pokemon of a coach who has won a battle (regardless of the type of battle (vs a neutral pokemon or vs a pokemon of another trainer)) will receive 1 extra point for all its features: hp, attack (normal or special), defense and especially defense.
  **Note**: The battle between the two coaches is done using **threads**.
## Pokemons
| Name |HP  |Normal Attack  | Special Attack | Def |Special Def |Ability 1 |Ability 2 |
|--|--|--|--|--|--|--|--|
|Neutrel1  |10  |3| N/A| 1| 1 | N/A | N/A|
|Neutrel2|20  |4|N/A| 1| 1| N/A| N/A|
|Pikachu| 20| N/A| 4| 2|3|Dmg: 6, Stun: No, Dodge: No, Cd:4| Dmg: 4, Stun: Yes, Dodge: Yes, Cd: 5
|Bulbasaur|42|N/A|5|3|1|Dmg: 6, Stun: No, Dodge: No, Cd: 4| Dmg: 5, Stun: No, Dodge: No, Cd: 3
|Charmander|50|4|N/A|3|2|Dmg: 4, Stun: Yes, Dodge: No, Cd: 4| Dmg: 7, Stun: No, Dodge: No, Cd: 6|
|Squirtle|60|N/A|3|5|5|Dmg: 4, Stun: No, Dodge: No, Cd: 3| Dmg: 2, Stun: Yes, Dodge: No, Cd: 2|
|Snorlax|62|3|N/A|6|4|Dmg: 4, Stun: Yes, Dodge: No, Cd: 5| Dmg: 0, Stun: No, Dodge: Yes, Cd: 5|
|Vulpix|36|5|N/A|2|4|Dmg: 8, Stun: Yes, Dodge: No, Cd: 6| Dmg: 2, Stun: No, Dodge: Yes, Cd: 7|
|Eevee|39|N/A|4|3|3|Dmg: 5, Stun: No, Dodge: No, Cd: 3| Dmg: 3, Stun: Yes, Dodge: No, Cd: 3|
|Jigglypuff|34|4|N/A|2|3|Dmg: 4, Stun: Yes, Dodge: No, Cd: 4|Dmg: 3, Stun: Yes, Dodge: No, Cd: 3|
|Meowth|41|3|N/A|4|2|Dmg: 5, Stun: No, Dodge: Yes, Cd: 4|Dmg: 1, Stun: No, Dodge: Yes, Cd: 3|
|Psyduck|43|3|N/A|3|3|Dmg: 2, Stun: No, Dodge: No, Cd: 4|Dmg: 2, Stun: Yes, Dodge: No, Cd: 5|
## Items
|Name|HP|Attack|Special Attack|Defense|Special Defense|
|--|--|--|--|--|--|
|Shield|-|-|-|+2|+2|
|Vest|+10|-|-|-|-|
|Sword|-|+3|-|-|-|
|Magic wand|-|-|+3|-|-|
|Vitamins|+2|+2|+2|-|-|
|Christmas tree|-|+3|-|+1|-|
|Cape|-|-|-|-|+3

## Implementation
Pokemon are represented at the beginning of the program as peaceful pokemon. After reading from the file, pokemon for battle is created for each coach. Items are added to each pokemon but are used when the pokemon first appears in an arena battle, after which it is reported that it has already used its items. Each pokemon is created with a PokemonFactory which is Singleton, and for Iteme we used an ItemBuilder. After the initialization of the coaches, the adventure on the arena begins. The logic on the arena is as follows, each coach has a neutral to fight with. As long as there are not 3 fights between coaches, one random opponent will be chosen. If neutrel has been chosen, 2 battles are created (coach 1 vs neutrel and coach 2 vs neutrel) which will run simultaneously, for the fight between the coaches only one fight will take place.
Next, in the actual battle, details about the battle, pokemon, and whether or not he used his items are displayed. After that, as long as the two pokemon are alive, they will attack each other. The attack for each pokemon is chosen by the trainer as randomly, and if it is neutral, its only possibility will be a simple attack (not an ability). The pokemon attack will take place at the same time because it will run with a service executor. At the end of the fight, details about the winner are displayed, his characteristics are incremented and his score is calculated. Finally, the state of the pokemon is reset to its original state.
When there are 3 battles between the coaches, the final battle between the best pokemon is created and played. Neutral battles can fall before and after any battle between coaches. I did this so that every pokemon of the coach could receive and fight with the neutrals to receive buffs.
## Used design patterns

- **Singleton** - used to always have a single instance of PokemonFactory and ItemBuilder, because I don't need multiple instances that would create a desired object for me - Factory / Builder will be one that will be valid everywhere in the project.
- **Factory** - used to create pokemon, and the reason I used factory is to hide the logic from
  back used to create Pokemon objects. Also, factory was useful because there are many classes whose fields for each object will always be the same, and instantiating an object when needed would be inefficient, causing duplicate code and non-readable format.
- **Builder** - used to create pokemon objects. I used it because of the efficiency of creating objects that may have some fields blank. With the help of the builder, each object is created step-by-step and only with the required fields, so that in the end the build is called and the required object is returned. The builder allowed me to produce objects with different representations using the same building code. When not all fields are required, I set only the required ones, and the rest of the Builder used them with 0.
- **Command** - used to know where the output will be displayed - console or file. At the beginning of the program execution this will be asked, and depending on which option was chosen, the print command of the specific logger will be executed. LoggerMenu is the main logger, which will have the logger responsible for printing. The Logger interface tells us which method to overwrite the class that will implement it, and it is implemented by FileLogger, ConsoleLogger, LoggerMenu.
