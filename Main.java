import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {

//            BufferedWriter bw = new BufferedWriter(new FileWriter("moves.json"));

//            game.printBoard();



//            System.out.println();
//
////            game.updateBoard(0, 6, 0, 7);
//            game.printBoard();
//            System.out.println("updated");
//
//            List<int[][]> list= game.getNextState(6, 7);
//            System.out.println("next stated");
//
//            for(int[][] t: list) {
//                for(int i = 0; i < 8; i++) {
//                    for (int j = 0; j < 8; j++) {
//                        System.out.print(t[i][j] + ", ");
//                    }
//                    System.out.println();
//                }
//
//                System.out.println();
//            }
//
//            System.out.println();

            Ai ai = new Ai(6, "black");

            ai.play();



//            game.printBoard();
//





        }

        catch(Exception e) {
            System.out.println(e);
        }




        System.out.println("Hello World!");
    }
}
