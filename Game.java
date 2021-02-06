import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {

    int[][] board;
    int n;
    double score;  //keeps track of score of current board
    String turn;
    int depth;

    final double INFINITE = 99999;

    static String BLACK = "BLACK";
    static String WHITE = "WHITE";

    int[][] pieceSquareTable8 = {
            {-80, -25, -20, -20, -20, -20, -25, -80 },
            {-25,  10,  10,  10,  10,  10,  10,  -25},
            {-20,  10,  25,  25,  25,  25,  10,  -20},
            {-20,  10,  25,  25,  25,  25,  10,  -20},
            {-20,  10,  25,  50,  50,  25,  10,  -20},
            {-20,  10,  25,  25,  25,  25,  10,  -20},
            {-25,  10,  10,  10,  10,  10,  10,  -25},
            {-80, -25, -20, -20, -20, -20, -25, -80}};

    int[][] pieceSquareTable6 = {
            {-80, -20, -20, -20, -20, -80},
            {-20,  25,  25,  25,  25, -20},
            {-20,  25,  25,  25,  25, -20},
            {-20,  25,  50,  50,  25, -20},
            {-20,  25,  25,  25,  25, -20},
            {-80, -20, -20, -20, -20, -80},
    };



    public Game(int n) {
        this.n = n;
        board = new int[n][n];
        turn = "black";

        //1 is black, -1 is white

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {

                if(i == 0 || i == n - 1) {
                    if(j > 0 && j < n - 1) {
                        board[i][j] = 1;
                    }
                    else board[i][j] = 0;
                }

                else if(j == 0 || j == n - 1) {
                    if(i > 0 && i < n - 1) {
                        board[i][j] = -1;
                    }
                    else board[i][j] = 0;
                }

            }
        }


    }

    void setBoard(int [][] temp) {

        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = temp[i][j];

            }
        }

    }

    //all heuristics give high value when good for black, and low when good for white

    int connectedness(int i, int j) {
        //returns high +ve if black and connected, high -ve if white and connected
//        System.out.println("calculating");

        int out = 0;

        int piece = board[i][j];

        for(int p = i - 1; p <= i + 1; p++) {
            for(int q = j - 1; q <= j + 1; q++) {
                if(p == i && q == j) {
                    continue;
                }

                if(p >= 0 && p < n && q >= 0 && q < n) {
                    if(board[p][q] == piece) {
                        out++;
                    }
                }
            }
        }

//        System.out.println("calculated");

        return piece * out;  //makes out +ve for black, -ve for white
    }

    double averageConnectness() {

        int count = 0;

        double sum = 0 ;


        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(board[i][j] == 0) {
                    continue;
                }

                sum += connectedness(i, j);
                count++;

            }
        }

        return sum / count;

    }

    int pieceSquareScore() {

        int totalScore = 0;

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {

                if(n == 8) {
                    totalScore += board[i][j] * pieceSquareTable8[i][j];
                }
                else {
                    totalScore += board[i][j] * pieceSquareTable6[i][j];
                }


            }
        }

        return totalScore;
    }


    int area() {





        Pair topLeftBlack = new Pair(n, n);
        Pair topLeftWhite = new Pair(n, n);
        Pair bottomRightBlack = new Pair(-1, -1);
        Pair bottomRightWhite = new Pair(-1, -1);


        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {


                if(board[i][j] == 1) {
                    if(i < topLeftBlack.x) {
                        topLeftBlack.x = i;

                    }
                    if(j < topLeftBlack.y) {
                        topLeftBlack.y = j;

                    }
                    if(i > bottomRightBlack.x ) {
                        bottomRightBlack.x = i;


                    }
                    if(j > bottomRightBlack.y) {
                        bottomRightBlack.y = j;

                    }


                }
                else if(board[i][j] == -1) {

                    if(i < topLeftWhite.x) {
                        topLeftWhite.x = i;

                    }
                    if(j < topLeftWhite.y) {
                        topLeftWhite.y = j;

                    }
                    if(i > bottomRightWhite.x ) {
                        bottomRightWhite.x = i;


                    }
                    if(j > bottomRightWhite.y) {
                        bottomRightWhite.y = j;

                    }


                }



            }


        }




        int blackArea = (bottomRightBlack.x - topLeftBlack.x) * (bottomRightBlack.y - topLeftBlack.y);


        int whiteArea = (bottomRightWhite.x - topLeftWhite.x) * (bottomRightWhite.y - topLeftWhite.y);

//        System.out.println("black: " + blackArea);
//        System.out.println("white: " + whiteArea);

        return Math.abs(whiteArea) - Math.abs(blackArea);   //opposite because smaller area values are better







    }

    double calculateScore() {


        //white has negative scores, black has positive scores

        //average connectedness

        if(isWinningState() != 0) {
//            System.out.println("winning state found");
            return isWinningState() * INFINITE;
        }


        this.score = area() + pieceSquareScore() + averageConnectness();



        return this.score;

    }



    void printBoard() {
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(board[i][j] + ", ");
            }
            System.out.println();
        }
    }

    List<Game>getNextStates() {

        List<Game> out = new ArrayList<Game>();

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {

                List<int[][]> list;

                if(board[i][j] == 0) {
                    continue;
                }

//                System.out.println(turn);


                if(board[i][j] == 1 && turn.equalsIgnoreCase("black") || board[i][j] == -1 && turn.equalsIgnoreCase("white")) {
                    list = getNextState(i,j);
                    for(int [][] board: list) {
                        Game temp = new Game(n);
                        temp.setBoard(board);
                        temp.calculateScore();
                        if(turn.equalsIgnoreCase("black")) {
                            temp.turn = "white";
                        }
                        else {
                            temp.turn = "black";
                        }
                        out.add(temp);
                    }

                }



            }
        }

        return out;

    }

    List<int[][]> getNextState(int p, int q) {
        //returns next states for a given piece at board[p][q]
        //upper left of board is 0,0



        int x = 0;
        int y = 0;
        int numRight = 1;  //  /
        int numLeft = 0;   //  \


        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                if(board[i][j] == 0) {
                    continue;
                }

                if(i == p) {
                    x++;

                }
                if(j == q) {
                    y++;
                }

//                System.out.println("j: " + j + ", q: " + q + ", p: " + p + ", i: " + i);
                if(Math.abs(p - i) == Math.abs(q - j)) {


                    if((j - q) * (p - i) > 0) {
                        numRight++;
                    }
                    else {

                        numLeft++;
                    }

                }

            }
        }


//
//        System.out.println("x = " + x);
//        System.out.println("y = " + y);
//        System.out.println("right = " + numRight);
//        System.out.println("left = " + numLeft);


        //piece.x = q, piece.y = p (inverse)

        List<Pair> moves = new ArrayList<Pair>();



        Pair right = new Pair(p, q + x);
        Pair left = new Pair(p, q - x);
        Pair up = new Pair(p - y, q);
        Pair down = new Pair(p + y, q);
        Pair topRight = new Pair(p - numRight, q + numRight);
        Pair downLeft = new Pair(p + numRight, q - numRight);
        Pair topLeft = new Pair(p - numLeft, q - numLeft);
        Pair downRight = new Pair(p + numLeft, q + numLeft);

        moves.add(right);
        moves.add(left);
        moves.add(up);
        moves.add(down);
        moves.add(topRight);
        moves.add(downLeft);
        moves.add(topLeft);
        moves.add(downRight);


        for(int i = 0; i < moves.size(); i++) {
            if(moves.get(i).x == p && moves.get(i).y == q) {
                moves.remove(moves.get(i));
            }
        }




        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if(board[i][j] == 0) {
                    continue;
                }


                if(board[i][j] != board[p][q] ) {
                    //colors different
                    if(i == p && j < (j + x) && j > q) {
                        //piece cant move right
                        moves.remove(right);

                    }


                    if(i == p && j > (j - x) && j < q) {

                        moves.remove(left);
                    }

                    if(j == q && i > (p - i) && i < p) {


                        moves.remove(up);
                    }

                    if(j == q && i < (p + i) && p < i) {


                        moves.remove(down);
                    }

                    if(Math.abs(p - i) == Math.abs(q - j)) {

                        if((j - q) * (p - i) > 0) {

                            if(j < q + numRight && j > q) {

                                moves.remove(topRight);
                            }

                            else if(j > q - numRight && j < q) {

                                moves.remove(downLeft);
                            }
                        }

                        else {
                            if(j > q - numLeft && j < q) {

                                moves.remove(topLeft);
                            }
                            else if(j < q + numLeft && j > q) {


                                moves.remove(downRight);

                            }
                        }

                    }

                }

                else {
                    //same color


                    moves.remove(new Pair(i, j));



                }

            }
        }


        List<int[][]> states = new ArrayList<int[][]>();




        for(Pair move: moves) {

//            System.out.println(move.x + ", " + move.y);


            if(move.x >= 0 && move.x < n && move.y >= 0 && move.y < n) {
                int[][] temp = copyBoard();

                temp[p][q] = 0;
                temp[move.x][move.y] = board[p][q];

                states.add(temp);

            }


        }



        return states;




    }

    int[][] copyBoard() {

        int[][] temp = new int[n][n];
        for(int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                temp[i][j] = board[i][j];

            }
        }

        return temp;

    }

    void updateBoard(int oldx, int oldy, int newx, int newy) {
        //here (x,y) == (0,0) points to bottom left of board, so need to translate to i and j format first



        int oldj = oldx;
        int newj = newx;

        int oldi = n - 1 - oldy;
        int newi = n - 1- newy;

        int temp = board[oldi][oldj];
        board[oldi][oldj] = 0;
        board[newi][newj] = temp;

        //updateBoard called means turns get reversed
        if(turn.equalsIgnoreCase("white")) {
            turn = "black";
        }
        else {
            turn = "white";
        }

    }

    int isWinningState() {
        //returns 0 if no, 1 if black wins, -1 if white wins

        List<Pair> blacks = new ArrayList<Pair>();
        List<Pair> whites = new ArrayList<Pair>();


        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {

                if(board[i][j] == 1) {
                    blacks.add(new Pair(i,j));

                }
                else if(board[i][j] == -1) {
                    whites.add(new Pair(i,j));


                }

            }
        }
        List<List<Pair>> colors = new ArrayList<List<Pair>>();

        colors.add(blacks);
        colors.add(whites);

        for(List<Pair> color : colors) {

            List<Pair> visited = new ArrayList<Pair>();
            Queue<Pair> q = new LinkedList<Pair>();

            q.add(blacks.get(0));

            while(!q.isEmpty()) {
                Pair piece = q.remove();

                for(int i = piece.x - 1; i <= piece.x + 1; i++) {
                    for(int j = piece.y - 1; j <= piece.y + 1; j++) {
                        if(i == piece.x && j == piece.y) {
                            continue;
                        }

                        if(i >= 0 && i < n && j >= 0 && j < n) {
                            if(board[i][j] == board[piece.x][piece.y] && !visited.contains(new Pair(i, j))) {
                                q.add(new Pair(i, j));
                            }
                        }
                    }
                }

                visited.add(piece);


            }

            if(visited.size() == color.size()) {
                //turn gets updated in getStates and updateBoard

                 return board[color.get(0).x][color.get(0).y];
                 //1 if black wins, -1 if white wins


            }

        }




        return 0;
    }




}
