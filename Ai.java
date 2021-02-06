import com.sun.imageio.plugins.common.I18NImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Stack;

public class Ai {

    Game game;  //holds current game state info
    String role;
    int n;
    BufferedWriter bw;
    BufferedReader br;
    ServerSocket serverSocket;
    Socket socket;

    final double INFINITE = 99999;

    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public Ai(int n, String role) throws Exception {
        this.n = n;
        this.role = role;

//        serverSocket = new ServerSocket(5555);
//        System.out.println("waiting");
//        socket =  serverSocket.accept();
//        System.out.println("connection accepted");
//
//        objectInputStream = new ObjectInputStream(socket.getInputStream());
//        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

        bw = new BufferedWriter(new FileWriter(new File("ai-move.txt")));
        bw.write("0\n");
        bw.close();

        game = new Game(n);
    }

    void readUserMove() throws Exception {

        //file has one string: oldx,oldy,newx,newy

//         String input = (String) objectInputStream.readObject();



        while(true) {
            br = new BufferedReader(new FileReader(new File("user-move.txt")));
            String state = br.readLine();
            if(state != null && state.equals("1")) {
                String input = br.readLine();
                String[] positionsString = input.split(",");

                int[] positions = new int[4];

                for(int i = 0; i < 4; i++) {
                    positions[i] = Integer.parseInt(positionsString[i]);
                }
                game.updateBoard(positions[0], positions[1], positions[2], positions[3]);

                bw = new BufferedWriter(new FileWriter(new File("user-move.txt")));
                bw.write("0\n");
                bw.close();
                break;

            }

            br.close();
        }

        br.close();




    }

    int[] getMove(int[][] newBoard) {
        //gets move given old board and newBoard
        ///returns [oldi, oldj, newi, newj]

        // need to consider eating of pieces here too

        int[] move = new int[4];

//        System.out.println("old board");
//        game.printBoard();
//
//
//        System.out.println("new board");
//
//        for(int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                System.out.print(newBoard[i][j] + ", ");
//            }
//            System.out.println();
//        }
//
//        System.out.println();



        //find value thats different
        int oldi, oldj, newi, newj;
        oldi = 0;
        oldj = 0;
        newi = 0;
        newj = 0;
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {

                int color;

                if(game.turn.equalsIgnoreCase("black")) {
                    color = 1;
                }
                else {
                    color = -1;
                }
                if(game.board[i][j] != newBoard[i][j]) {

//                    if(game.board[i][j] == 0 || newBoard[i][j] == 0) {
//                        //no eating happened,
//                        if(game.board[i][j] == 0 && newBoard[i][j] != 0) {
//                            newi = i;
//                            newj = j;
//                        }
//                        else {
//                            oldi = i;
//                            oldj = j;
//                        }
//                    }
//                    else {
//                        //eating happened
//
//                        if()
//
//                    }

                    if(game.board[i][j] == color && newBoard[i][j] != color) {
                        oldi = i;
                        oldj = j;
                    }
                    else {
                        newi = i;
                        newj = j;

                    }




                }
            }
        }


        //translate to xy format
        move[0] = oldj;
        move[1] = n - 1 - oldi;
        move[2] = newj;
        move[3] = n - 1 - newi;




        return move;
    }

    int minimax(Game node, double alpha, double beta) {
//        System.out.println(node.depth);

        //alpha is the worst possible score for max player (init = -inf)
        //alpha is the score that max player is guaranteed to get with calculations so far
        //beta is the same thing for the min player
        //beta is the worst possible score for min player (init = +inf)

        //returns the param node, with score set to minimax value of that node

        int bestMoveIndex = -1;

        if(node.depth > 3) {
            node.calculateScore();
            return -1;  //index here doesnt matter, since theres no next move from this node

        }

        List<Game> nextStates = node.getNextStates();



        if(node.turn.equalsIgnoreCase("white")) {
            //white is min player


            node.score = INFINITE;

        }
        else {

            node.score = -INFINITE;

        }



//        for(Game temp : nextStates) {
        for(int i = 0; i < nextStates.size(); i++) {
            Game temp = nextStates.get(i);
            temp.depth = node.depth + 1;
            minimax(temp, alpha, beta); //minimax sets score = minimax of the node that calls it

            if(node.turn.equalsIgnoreCase("white")) {
                //white is min player




                if(temp.score < node.score) {
                    bestMoveIndex = i;
                    node.score = temp.score;

                }




                beta = Math.min(beta, temp.score);
                if(beta <= alpha) {
                    break;
                }
            }

            else {

                if(temp.score > node.score) {
                    bestMoveIndex = i;
                    node.score = temp.score;

                }

                alpha = Math.max(alpha, temp.score);
                if(beta <= alpha) {
                    break;
                }

            }
        }

        return bestMoveIndex;

        //i have all minimax values of the children nodes nextStates here
        //select max or min value of minimax values depending on whose turn it is for this current node

//        int index = 0;
//        double value = nextStates.get(0).score;
//        for(int i = 0 ;i < nextStates.size(); i++) {
//            if(node.turn.equalsIgnoreCase("white")) {
//                //choose least score
//                if(nextStates.get(i).score < value) {
//                    index = i;
//                }
//            }
//            else if(node.turn.equalsIgnoreCase("black")) {
//                //choose max score
//                if(nextStates.get(i).score > value) {
//                    index = i;
//                }
//
//            }
//
//        }

//        return nextStates.get(index);



    }


    void makeMove() throws Exception {
        //get best move

        System.out.println(game.turn);

        int bestMoveIndex = minimax(game, -INFINITE, INFINITE);

        List<Game> nextStates = game.getNextStates();

        Game next = nextStates.get(bestMoveIndex);



        //getting first move for now
//        Game next = game.getNextStates().get(0);





        bw = new BufferedWriter(new FileWriter(new File("ai-move.txt")));

        int[] move = getMove(next.board);

        System.out.println("next");

//        for(int i = 0; i < 8; i++) {
//            for (int j = 0; j < 8; j++) {
//                System.out.print(next.board[i][j] + ", ");
//            }
//            System.out.println();
//        }


        System.out.println("making move from " + move[0] + ", " + move[1] + " to " + move[2] + ", " + move[3]);
        game.updateBoard(move[0], move[1], move[2], move[3]);

        bw.write("1\n");
        bw.write(move[0] + "," + move[1] + "," + move[2] + "," + move[3]);

        bw.close();

//        game.printBoard();



    }


    void play() throws Exception {

        if(role.equalsIgnoreCase("white")) {
            while(true) {
                readUserMove();
                System.out.println("current state");
                game.printBoard();

                makeMove();
                System.out.println("after the pro plays");
                game.printBoard();
                System.out.println(game.calculateScore());


            }

        }

        else {

            while(true) {

                makeMove();
                System.out.println("after the pro plays");
                game.printBoard();
                System.out.println(game.calculateScore());
                readUserMove();
                System.out.println("current state");
                game.printBoard();




            }

        }








    }



}
