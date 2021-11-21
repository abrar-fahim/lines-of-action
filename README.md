# lines-of-action

A Project on designing game AI using adverserial search. 
Ths project consists of an AI that can play the board game Lines of Action. A basic UI for the game is included in the source files which allows for human vs human as well as human vs AI gameplay. 

The game code allows for either 6 by 6 board games or 8 by 8 board games by changing a variable inside M   ain.java

The color of the AI pieces can also be changed through the Main.java

The AI is coded to go upto depth 3. 



## AI Design

The AI uses a traditional minimax search algorithm with alpha beta pruning to speed up search. 

Some of the heursitics used were:
- A "score" matrix that prioritizes keeping as many pieces as close to the center of the board as possible. 
- Maximizing "Connectedness", which aims to maximize the number of connected pieces in the board. 
- Minimizing "Area", which determines the rectangular area containing all the pieces. 


## Setup

The Project uses python to code the "UI" consisting of the logic to display the board, allow a human to play the game, show available moves for a piece upon clicking a piece etc. Java is used to code the AI logic, which runs the minimax algorithm upto depth 3 using alpha beta pruning and the mentioned heurisitics to calculate the scores of each possible move and select the best one. 

So, both java and python runtimes are needed to run this project. 


The python and java runtimes intereact via files that the programs create by themselves when running. 
The file "user-move.txt" records the human's move. 
"ai-move.txt" records the AI's move. 
Both the java and python run times take turns in reading from and writing to these files to communicate to each other. 


### Project todos

- [ ] make depth a variable 





