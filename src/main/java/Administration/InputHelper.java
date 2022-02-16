package Administration;

import Adventure.Pokemons.PokemonUtils.*;
import org.xml.sax.SAXException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * The class that will be useful in reading the input file and choosing a valid input file.
 * The read will check at each reading step whether the input file is valid.
 */
public class InputHelper {
    private int fileNumber;

    /**
     * Sets the number of the input file to be read from
     * @param fileNumber input file number
     */
    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    /**
     * Read from the file "input_$(file_number).xml" data about coaches
     * @return coach list read from file / emptyList in case of invalid input file
     */
    public List<Coach> getCoachesFromXml(){
        List<Coach> coachesList = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try{
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File("src/main/resources/inputs/input_" + this.fileNumber + ".xml"));

            doc.getDocumentElement().normalize();

            NodeList coaches = doc.getElementsByTagName("coach");
            /*
             * Check if there are 2 coaches in the input file
             */
            if(coaches.getLength() != 2) {
                System.out.println("Invalid number of coaches!");
                return Collections.emptyList();
            }
            for(int i = 0; i < coaches.getLength(); i++){
                Coach coachObject = null;
                Node coach = coaches.item(i);
                if (coach.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) coach;
                    String name = element.getElementsByTagName("name").item(0).getTextContent();
                    int age = Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent());
                    coachObject = new Coach(name, age);

                    NodeList pokemons = element.getElementsByTagName("pokemon");
                    /*
                     * Check if the trainer has exactly 2 pokemon
                     */
                    if(pokemons.getLength() != 3){
                        System.out.println("Invalid number of pokemons!");
                        return Collections.emptyList();
                    }
                    for(int j = 0; j < pokemons.getLength(); j++){
                        Node pokemon = pokemons.item(j);
                        if(pokemon.getNodeType() == Node.ELEMENT_NODE){
                            Element pokemonElement = (Element) pokemon;
                            String pokemonType = pokemonElement.getElementsByTagName("name").item(0).getTextContent();
                            Pokemon pokemonObject = PokemonFactory.getFactory().getPokemon(pokemonType);
                            PokemonForBattle pokemonForBattle = new PokemonForBattle(pokemonObject);
                            /*
                             * Check if the trainer does not already have such a pokemon
                             */
                            if(coachObject.getPokemons().contains(pokemonForBattle)){
                                System.out.println("Pokemon duplicate for coach");
                                return Collections.emptyList();
                            }
                            coachObject.addPokemon(pokemonForBattle);

                            NodeList items = pokemonElement.getElementsByTagName("item");
                            /*
                             * Check the number of objects the pokemon can have.
                             * If more than 3 objects were requested -> invalid input
                             */
                            if(items.getLength() > 3){
                                System.out.println("Too many items for pokemon");
                                return Collections.emptyList();
                            }
                            for(int k = 0; k < items.getLength(); k++){
                                Node item = items.item(k);
                                if(item.getNodeType() == Node.ELEMENT_NODE){
                                    Element itemElement = (Element) item;
                                    String itemName = itemElement.getTextContent();
                                    Item itemObject = ItemBuilder.getBuilder().buildItem(itemName);
                                    if(itemObject != null) coachObject.addItemForPokemon(pokemonForBattle, itemObject);
                                }
                            }
                        }
                    }
                }
                coachesList.add(coachObject);
            }

        }
        catch (ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }
        return coachesList;
    }

    /**
     * List the available input files and ask us to choose one.
     * @return the number of the selected input file
     */
    public int getAvailableFileNumber(){

        System.out.println("Available input files:");

        String[] files;
        File f = new File("src/main/resources/inputs");

        files = f.list();
        assert files != null;
        for(String fileName : files){
            System.out.println(fileName);
        }

        System.out.print("Choose the number of the input file you want: ");
        Scanner sc = new Scanner(System.in);
        int inputFileNumber;

        do{
            inputFileNumber = sc.nextInt();
            if(new File("src/main/resources/inputs/input_" + inputFileNumber + ".xml").exists()){
                break;
            }
            System.out.print("The file with the required number does not exist! Choose from the list: ");
        } while (true);
        return inputFileNumber;
    }

}
