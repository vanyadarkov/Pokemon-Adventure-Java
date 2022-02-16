package Administration.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Logger folosit pentru scrirea in fisier
 */
public class FileLogger implements Logger{
    int fileNumber;

    /**
     * Sets the fileNumber field that will signify the number of the output file
     * necessary for creation
     * @param fileNumber number of file
     */
    public FileLogger(int fileNumber) {
        this.fileNumber = fileNumber;
    }

    /**
     * writing the string toPrint to the file.
     */
    @Override
    public void print(String toPrint) {
        String path = "src/main/resources/outputs";
        File outputDirectory = new File(path);
        /*
         * Check for src/main/resources/outputs directory. If it doesn't exist, create it.
         */
        if(!outputDirectory.exists()) outputDirectory.mkdir();
        /*
         * Initializing the file to be named "output_$(fileNumber).out"
         */
        File outputFile = new File(path + "/output_" + this.fileNumber + ".out");
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(toPrint);
            writer.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
