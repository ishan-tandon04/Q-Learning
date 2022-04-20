/**
Ishan Tandon; 4/18/22,
This class is the Q learning of the lab
 **/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
public class QLearning { //import files

    private Maze m; //instance variables

    private final double x = 0.1;
    private final double y = 0.9;

    private final int mazeWid = 5;
    private final int mazeHei = 5;
    private final int count = mazeHei * mazeWid;

    private final int rew = 100;
    private final int pen = -10;

    private double[][] Q;
    private char[][] Maze;
    private int[][] R;



    public static void main(String args[]) {
        QLearning ql = new QLearning();

        ql.init();
        ql.calculateQ();
        ql.printQ();
        ql.PrintPolicy();

    }

//initiating the code
    public void init() {
        m = new Maze();
        m.buildMaze();

        File file = new File("src/Maze.txt");

        R = new int[count][count];
        Q = new double[count][count];
        Maze = new char[mazeHei][mazeWid];


        try (FileInputStream fis = new FileInputStream(file)) {


            int i = 0;
            int j = 0;
            int content;

            while ((content = fis.read()) != -1) {   // Reads maze from text file
                char c = (char) content;

                if (c != '2' && c != 'B' && c != 'V') {
                    continue;
                }

                Maze[i][j] = c;
                j++;
                if (j == mazeWid) {
                    j = 0;
                    i++;
                }
            }

            for (int k = 0; k < count; k++) { //for loop to run through maze

                i = k / mazeWid;
                j = k - i * mazeWid;

                for (int s = 0; s < count; s++) {
                    R[k][s] = -1;
                }
                if (Maze[i][j] != 'B') {


                    int goLeft = j - 1;
                    if (goLeft >= 0) { //iterating through all possibilities for if loops
                        int target = i * mazeWid + goLeft;
                        if (Maze[i][goLeft] == '2') {
                            R[k][target] = 0;
                        } else if (Maze[i][goLeft] == 'B') {
                            R[k][target] = rew;
                        } else {
                            R[k][target] = pen;
                        }
                    }


                    int goRight = j + 1;
                    if (goRight < mazeWid) {
                        int target = i * mazeWid + goRight;
                        if (Maze[i][goRight] == '2') {
                            R[k][target] = 0;
                        } else if (Maze[i][goRight] == 'B') {
                            R[k][target] = rew;
                        } else {
                            R[k][target] = pen;
                        }
                    }


                    int goUp = i - 1;
                    if (goUp >= 0) {
                        int target = goUp * mazeWid + j;
                        if (Maze[goUp][j] == '2') {
                            R[k][target] = 0;
                        } else if (Maze[goUp][j] == 'B') {
                            R[k][target] = rew;
                        } else {
                            R[k][target] = pen;
                        }
                    }


                    int goDown = i + 1;
                    if (goDown < mazeHei) {
                        int target = goDown * mazeWid + j;
                        if (Maze[goDown][j] == '2') {
                            R[k][target] = 0;
                        } else if (Maze[goDown][j] == 'B') {
                            R[k][target] = rew;
                        } else {
                            R[k][target] = pen;
                        }
                    }
                }
            }
            initQ();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    void initQ()
    {
        for (int i = 0; i < count; i++){
            for(int j = 0; j < count; j++){
                Q[i][j] = (double)R[i][j];
            }
        }
    }



    void calculateQ() { //calculate method
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            int crtState = rand.nextInt(count);

            while (!FinalState(crtState)) {
                int[] actionsFromCurrentState = possibleActionsFromState(crtState);

                int index = rand.nextInt(actionsFromCurrentState.length);
                int nextState = actionsFromCurrentState[index];

                double q = Q[crtState][nextState];
                double maxQ = maxQ(nextState);
                int r = R[crtState][nextState];

                double value = q + x * (r + y * maxQ - q);
                Q[crtState][nextState] = value;

                crtState = nextState;
            }
        }
    }


    int[] possibleActionsFromState(int state) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if (R[state][i] != -1) {
                result.add(i);
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    boolean FinalState(int state) {
        int i = state / mazeWid;
        int j = state - i * mazeWid;

        return Maze[i][j] == 'B';
    }


    double maxQ(int nextState) {
        int[] actionsFromState = possibleActionsFromState(nextState);
        double maxValue = -10;
        for (int nextAction : actionsFromState) {
            double value = Q[nextState][nextAction];

            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }

    //printing policy
    void PrintPolicy() {
        System.out.println("\nPrint policy");
        for (int i = 0; i < count; i++) {
            System.out.println("From state " + i + " goto state " + getPolicyFromState(i));
        }
    }

    int getPolicyFromState(int state) { //takes in an int and gets policy from state
        int[] actionsFromState = possibleActionsFromState(state);

        double maxValue = Double.MIN_VALUE;
        int policyGotoState = state; //variables

        for (int nextState : actionsFromState) { 
            double value = Q[state][nextState];

            if (value > maxValue) {
                maxValue = value;
                policyGotoState = nextState;
            }
        }
        return policyGotoState;
    }

    void printQ() {
        String s = "Q matrix" + "\n";
        System.out.println("Q matrix");

        for (int i = 0; i < Q.length; i++) {
            System.out.print("From state " + i + ":  ");
            s += "From state" + i + ": ";

            for (int j = 0; j < Q[i].length; j++) { //nested for loop to run through maze
                System.out.printf("%6.2f ", (Q[i][j]));
                s += String.format("%6.2f ", (Q[i][j]));
            }
            s += "\n";
            System.out.println();
        }
        try {
            FileWriter myWriter = new FileWriter("src/QMatrixSolution.txt");//grabs from txt file
            myWriter.write(s);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Error"); //print error if error is caught
            e.printStackTrace();
        }
    }

}
