/**
Ishan Tandon; 4/18/22,
This class is the Maze section for the Lab
 **/
import java.io.FileWriter; //import
import java.io.IOException;
import java.util.Random;

public class Maze {
        private Random random; //instance variable
        int width;
        int height;

        public Maze(){ //default constructor
            random = new Random();
            width = 5;
            height = 5;
        }


        public void buildMaze(){ //builds maze inside
            int destin = random.nextInt(25);
            int spacesAdded = 0;

            String string = new String();
            int clearOrBlocked;

            for(int i = 0; i< 25; i++){ //for loop to go through all spaces
                if(spacesAdded % 5 == 0 && spacesAdded != 0){
                    string += "\n";
                }

                clearOrBlocked = random.nextInt(3);
                if(i == destin){
                    string+= "B";
                    spacesAdded += 1;
                }else if(clearOrBlocked == 0 || clearOrBlocked == 1){
                    string += "2";
                    spacesAdded += 1;
                }else{
                    string += "V";
                    spacesAdded += 1;
                }
            }

            try { //shows the maze and printed out
                FileWriter myWriter = new FileWriter("src/Maze.txt");
                myWriter.write(string);
                myWriter.close();
            } catch (IOException e) {
                System.out.println("Not working.");
                e.printStackTrace();
            }
        }

}
