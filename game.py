"""
Starting Template

Once you have learned how to use classes, you can begin your program with this
template.

If Python and Arcade are installed, this example can be run from the command line with:
python -m arcade.examples.starting_template
"""
import arcade
import json
import socket

from collections import deque

SCREEN_WIDTH = 800
SCREEN_HEIGHT = 600
SCREEN_TITLE = "Starting Template"

N = 6

class Board:
    def __init__(self, size):

        self.size = size

        self.pieces = None

    




class Piece:


    def __init__(self, x, y, color):
        # x and y are board positions, from 0 to 7 inclusive for a 8x8 board
        self.x = x
        self.y = y
        self.color = color
        self.radius = 30
        self.island = 100
        

    def draw(self):
        arcade.draw_circle_filled(self.x * SCREEN_WIDTH / N + SCREEN_WIDTH / (N * 2), self.y * SCREEN_HEIGHT / N + SCREEN_HEIGHT / (N * 2), self.radius, self.color)

    







class MyGame(arcade.Window):
    """
    Main application class.

    NOTE: Go ahead and delete the methods you don't need.
    If you do need a method, delete the 'pass' and replace it
    with your own code. Don't leave 'pass' in this program.
    """


    

    


    def __init__(self, width, height, title):
        super().__init__(width, height, title)

        arcade.set_background_color(arcade.color.AMAZON)

        self.SELECTED = "SELECTED"
        self.NOT_SELECTED = "NOT_SELECTED"
        self.BLACK = "BLACK"
        self.WHITE = "WHITE"

        self.GAME_OVER = "GAME_OVER"

        self.turn = None

        self.whites = []

        self.blacks = []

        self.pieces = []

        self.moves = []

        self.state = None

        self.selectedPiece = None

        self.user_color = None

        self.ai_color = None

        
        

        

        # If you have sprite lists, you should create them here,
        # and set them to None

    def setup(self):
        """ Set up the game variables. Call to re-start the game. """
        # Create your sprites and sprite lists here

        self.state = self.NOT_SELECTED
        self.selectedPiece = None
        self.turn = self.BLACK

        self.user_color = self.WHITE
        self.ai_color = self.BLACK

        for x in range (1,N - 1):
            for y in [0,N - 1]:
                piece = Piece(x,y,arcade.color.BLACK)
                self.blacks.append(piece)
                self.pieces.append(piece)

        for y in range (1,N - 1):
            for x in [0, N - 1]:
                piece = Piece(x,y,arcade.color.WHITE)
                self.whites.append(piece)
                self.pieces.append(piece)

        f = open("offline2/user-move.txt", "w")
        f.write("0\n")

        f.close()


    def on_draw(self):
        """
        Render the screen.
        """

        # This command should happen before we start drawing. It will clear
        # the screen to the background color, and erase what we drew last frame.
        arcade.start_render()

        # 0,0 is bottom left

        # lines

        for x in range(0, N):
            arcade.draw_line(x * SCREEN_WIDTH / N, SCREEN_HEIGHT, x * SCREEN_WIDTH / N , 0, arcade.color.BLACK)
            arcade.draw_line(SCREEN_WIDTH, x * SCREEN_HEIGHT / N, 0, x * SCREEN_HEIGHT / N, arcade.color.BLACK)


        
        # Call draw() on all your sprite lists below

        # pieces



        for piece in self.pieces:
            piece.draw()

        for move in self.moves:
            move.draw()

    def getAIInput(self):

        while True:

            f = open("offline2/ai-move.txt", "r")
            state = f.readline()
            if state == "1\n":
                print("making move")
                move = f.readline().split(",")

                destPiece = self.getPiece(int(move[2]), int(move[3]))
                

                if destPiece is not None and destPiece.color == arcade.color.BLACK and self.ai_color == self.WHITE:
                    #black piece got eaten
                    self.pieces.remove(destPiece)
                    
                    self.blacks.remove(destPiece)

                elif  destPiece is not None and destPiece.color == arcade.color.WHITE and self.ai_color == self.BLACK:
                    self.pieces.remove(destPiece)
                    
                    self.whites.remove(destPiece)





            
                self.movePiece(int(move[0]), int(move[1]), int(move[2]), int(move[3]))
                [blackWin, whiteWin] = self.isWinningState()
                if blackWin and whiteWin:
                    print("draw")

                elif blackWin:
                    print("black wins")
                    self.state = self.GAME_OVER


                elif whiteWin:
                    print("white wins")
                    self.state = self.GAME_OVER

                if self.ai_color == self.BLACK:

                    self.turn = self.WHITE
                else:
                    self.turn = self.BLACK

                w = open("offline2/ai-move.txt", "w")
                w.write("0\n")
                w.close()
                break


            f.close()

        
        f.close()
            

            



        # print(f.read())
        


        # HOST = '127.0.0.1'
        # PORT = 5555
        # with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        #     s.connect((HOST, PORT))
        #     s.sendall(b'Hello, world')
        #     data = s.recv(1024)

        # print('Received', repr(data))



    def getPieceMouse(self, x, y):
        #x and y are mouse coordinates, return the piece on the place that the mouse clicks, or null.

        #first determine which block is clicked 


        cell_x = (x - SCREEN_WIDTH / (N * 2)) * N / SCREEN_WIDTH



        cell_y = (y - SCREEN_HEIGHT / (N * 2)) * N / SCREEN_HEIGHT

        #now find piece in this cell


 
        for piece in self.pieces:
            if piece.x >= cell_x - 0.5 and piece.x <= cell_x + 0.5 and piece.y >= cell_y - 0.5 and piece.y <= cell_y + 0.5:
                return piece


    def getPiece(self, x, y):

        for piece in self.pieces:
            if piece.x == x and piece.y == y:
                return piece


    def getMoves(self, piece):

        #returns array of cells that the piece can move to

        #find no of pieces in 2 diagonals and 2 axes
        num_x = 0
        num_y = 0
        num_right = 1   # /
        num_left = 0   # \

        if piece is None:
            return

        for other in self.pieces:
            if other.x == piece.x:
                num_y += 1
            if other.y == piece.y:
                num_x += 1
            if abs(other.x - piece.x) == abs(other.y - piece.y):
                if (other.x - piece.x) * (other.y - piece.y) > 0:
                    num_right += 1

                else:
                    num_left += 1

       

        right = [piece.x + num_x, piece.y]
        left = [piece.x - num_x, piece.y]
        up = [piece.x, piece.y  + num_y]
        down = [piece.x, piece.y - num_y]
        top_right = [piece.x + num_right, piece.y + num_right]
        down_left = [piece.x - num_right, piece.y - num_right]
        top_left = [piece.x - num_left, piece.y + num_left]
        down_right = [piece.x + num_left, piece.y - num_left]

        moves = [up, down, left, right, top_right, top_left, down_right, down_left]

        for other in self.pieces:


            if other.color != piece.color:
                if other.y == piece.y and other.x < right[0] and other.x > piece.x:
                    #piece cant move right
                    if right in moves:
                        moves.remove(right)

                if other.y == piece.y and other.x > left[0] and other.x < piece.x:
                    #piece cant move left
                    if left in moves:
                        moves.remove(left)
                
                if other.x == piece.x and other.y < up[1] and other.y > piece.y:
                    #piece cant move 1
                    if up in moves:
                        moves.remove(up)

                if other.x == piece.x and other.y > down[1] and other.y < piece.y:
                    #piece cant move down
                    if down in moves:
                        moves.remove(down)

                if abs(other.x - piece.x) == abs(other.y - piece.y):
                    if (other.x - piece.x) * (other.y - piece.y) > 0:
                        if other.x < top_right[0] and other.x > piece.x:
                            if top_right in moves:
                                moves.remove(top_right)
                        elif other.x > down_left[0] and other.x < piece.x:
                            if down_left in moves:
                                moves.remove(down_left)

                    else:
                        if other.x > top_left[0] and other.x < piece.x:
                            if top_left in moves:
                                moves.remove(top_left)

                        elif other.x < down_right[0] and other.x > piece.x:
                            if down_right in moves:
                                moves.remove(down_right)
            else:
                #same color
                temp = [other.x, other.y]
                if temp in moves:
                    moves.remove(temp)
       
                
                    


        return moves


    def drawMove(self, x, y):
        move = Piece(x, y, arcade.color.RED)

        self.moves.append(move)   


    def makeMove(self, piece, x, y):

        cell_x = round((x - SCREEN_WIDTH / (N * 2)) * N / SCREEN_WIDTH)

        cell_y = round((y - SCREEN_HEIGHT / (N * 2)) * N / SCREEN_HEIGHT)

        print(self.state)


        moves = self.getMoves(piece)

        move = [cell_x, cell_y]

        other_piece = self.getPiece(cell_x, cell_y)

        


        if piece.color == arcade.color.BLACK and self.turn != self.BLACK or piece.color == arcade.color.WHITE and self.turn != self.WHITE:
            print("its not your turn")
            self.moves.clear()
            return
            


        # if(other_piece != None):
        #     self.pieces.remove(other_piece)
        #     if other_piece.color == arcade.color.BLACK:
        #         self.blacks.remove(other_piece)

        #     else:
        #         self.whites.remove(other_piece)

        # piece.x = cell_x
        # piece.y = cell_y
        # if self.turn == self.BLACK:
        #     self.turn = self.WHITE
        # else:
        #     self.turn = self.BLACK

        # self.moves.clear()
        
        

        if move in moves:
            if(other_piece != None):
                self.pieces.remove(other_piece)
                if other_piece.color == arcade.color.BLACK:
                    self.blacks.remove(other_piece)

                else:
                    self.whites.remove(other_piece)


            f = open("offline2/user-move.txt", "w")
            f.write("1\n")
            f.write(str(piece.x) + "," + str(piece.y) + "," + str(cell_x) + "," + str(cell_y))
            f.close()
            piece.x = cell_x
            piece.y = cell_y
            
            if self.turn == self.BLACK:
                self.turn = self.WHITE
            else:
                self.turn = self.BLACK

            self.moves.clear()

        else:
            print("invalid move")
            self.moves.clear()

        
    def isWinningState(self):

        blackWin = False
        whiteWin = False
        black = arcade.color.BLACK
        white = arcade.color.WHITE

        self.blacks[0].island = 0
        self.whites[0].island = 0

        blackIslands = 0
        whiteIslands = 0



        



        for color in [self.blacks, self.whites]:
            q = deque()

            visited = []
            q.append(color[0])
            

            while q:
                piece = q.popleft()
                
                top_right = self.getPiece(piece.x + 1, piece.y + 1)
                top_left = self.getPiece(piece.x - 1, piece.y + 1)
                bottom_right = self.getPiece(piece.x + 1, piece.y - 1)
                bottom_left = self.getPiece(piece.x - 1, piece.y - 1)
                top = self.getPiece(piece.x, piece.y + 1)
                bottom = self.getPiece(piece.x, piece.y - 1)
                left = self.getPiece(piece.x - 1, piece.y)
                right = self.getPiece(piece.x + 1, piece.y)

                neighbors = [top_right, top_left, top, bottom, bottom_right, bottom_left, right, left]


                #see if 12 all pieces are connected
                #do bfs from one piece to see if all are connected

          

                for neighbor in neighbors:
                    if neighbor is not None and neighbor not in visited and neighbor not in q and neighbor.color == piece.color:
                        q.append(neighbor)
                        # print(len(q))
                        

                

                visited.append(piece)
            

            # print("BLACKS: " + str(len(self.blacks)))
            # print("WHITES: " + str(len(self.whites)))
            # print("visited: " + str(len(visited)))

            # for piece in visited:
            #     print(str(piece.x) + ", " + str(piece.y))
            if visited[0].color == black and len(visited) == len(self.blacks):
                blackWin = True

            elif visited[0].color == white and len(visited) == len(self.whites):
                whiteWin = True      


        return [blackWin, whiteWin]

                
    def movePiece(self, oldx, oldy, newx, newy):
        
        for piece in self.pieces:
            if piece.x == oldx and piece.y == oldy:
                
               
                piece.x = newx
                piece.y = newy
       

    def on_update(self, delta_time):
        """
        All the logic to move, and the game logic goes here.
        Normally, you'll call update() on the sprite lists that
        need it.
        """

        if self.turn == self.ai_color:
            self.getAIInput()

        pass

    def on_key_press(self, key, key_modifiers):
        """
        Called whenever a key on the keyboard is pressed.

        For a full list of keys, see:
        http://arcade.academy/arcade.key.html
        """
        pass

    def on_key_release(self, key, key_modifiers):
        """
        Called whenever the user lets off a previously pressed key.
        """
        pass

    def on_mouse_motion(self, x, y, delta_x, delta_y):
        """
        Called whenever the mouse moves.
        """
        pass

    def on_mouse_press(self, x, y, button, key_modifiers):
        """
        Called when the user presses a mouse button.
        """

        print(self.state)
        if self.state == self.NOT_SELECTED:
            piece = self.getPieceMouse(x, y)

            
            

            if(piece == None):
                print("none")

            else:
                self.selectedPiece = piece

                print(str(piece.x) + ", " + str(piece.y))

                self.moves.clear()

                moves = self.getMoves(piece)

                if moves is not None:
                    for move in moves:
                    
                        self.drawMove(move[0], move[1])

                self.state = self.SELECTED

        elif self.state == self.SELECTED:
            #make move
            

            self.makeMove(self.selectedPiece, x, y)

        

            self.state = self.NOT_SELECTED

            [blackWin, whiteWin] = self.isWinningState()
            if blackWin and whiteWin:
                if self.turn == self.WHITE:
                    print("white wins")
                else:
                    print("black wins")

               

            elif blackWin:
                print("black wins")
                self.state = self.GAME_OVER


            elif whiteWin:
                print("white wins")
                self.state = self.GAME_OVER

        elif self.state == self.GAME_OVER:
            print("GAME OVER")

    def on_mouse_release(self, x, y, button, key_modifiers):
        """
        Called when a user releases a mouse button.
        """
        pass


def main():
    """ Main method """
    game = MyGame(SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_TITLE)
    game.setup()
    arcade.run()


if __name__ == "__main__":
    main()