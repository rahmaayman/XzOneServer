/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoeserverapp;

/**
 *
 * @author khattab
 */
public class Match {
    
    private PlayersHandeler player1;
    private PlayersHandeler player2;
    private GameBoard gameBoard;
    
    public Match(PlayersHandeler player1, PlayersHandeler player2){
        
        this.player1 = player1;
        this.player2 = player2;
        
        this.gameBoard = new GameBoard();
    }

    public PlayersHandeler getPlayer1() {
        return player1;
    }

    public PlayersHandeler getPlayer2() {
        return player2;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setPlayer1(PlayersHandeler player1) {
        this.player1 = player1;
    }

    public void setPlayer2(PlayersHandeler player2) {
        this.player2 = player2;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }
    
    private class GameBoard{
        
        private int [][] board;
        
        public GameBoard(){
            
            board = new int[3][3];
            initBoard();
            
        }
        
        public void initBoard(){
            
            for(int i = 0;i < 3;i++)
                for(int j = 0; j < 3;j++)
                    board[i][j] = 0;
        }
        
        
        public boolean checkState(){
            return true;
            
        }
        
        public void updateBoard(int position, int playPiece){
            
            Cell cell = convert1DPositionTo2DIndexes(position);
            board[cell.getRow()][cell.getCol()] = playPiece;
            
        }
        
        private Cell convert1DPositionTo2DIndexes(int position){
            int row = 0;
            int col = 0;
            
            row = position/3;
            col = position%3;
            
            return new Cell(row, col);
        }
        
        
    }
    
    private class Cell{
        private int row;
        private int col;

        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
        
        
    }
    
    
    
}
