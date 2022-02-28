# \<Reversi\>

### Game logic:

- 2 players locally, 2 or 3 over the network
- First player is random
- Preview of possible moves
- If there are no moves available, the opponent plays for him (the player must explicitily press a button to skip the turn)
- Game finishes when all the cells are occupied or when there are no more possible moves
- 8x8 table for 2 players, 10x10 for 3
- Firebase to keep a top score with the 5 best results for each player

- Special moves (only one of each per player):
  - Bomb piece: explodes all the pieces in a radius of 1 cell
  - Piece sacrifice: sacrifice 2 pieces for 1 of the opponent
