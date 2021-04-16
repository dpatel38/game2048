package com.codegym.games.game2048;

import com.codegym.engine.cell.*;
import java.util.*;

public class Game2048 extends Game {
    
    private static final int SIDE = 4;
     private int[][] gameField = new int[SIDE][SIDE]; 
     private int score = 0;
     private boolean isGameStopped = false;
     
    @Override
    public void initialize (){
        super.initialize();
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();
    }
    
    private void createGame (){
       gameField = new int[SIDE][SIDE];
       score = 0;
       setScore(score);
       createNewNumber();
       createNewNumber();
    }
    
    private void drawScene(){
        for(int x = 0; x < SIDE; x++){
            for(int y = 0; y < SIDE; y++){
                setCellColoredNumber(x, y, gameField[y][x]);
            }    
        }
    }
    
    private void createNewNumber(){
        if(getMaxTileValue() < 2048){
            int random = getRandomNumber(10);
            int positionX = getRandomNumber(SIDE);
            int positionY = getRandomNumber(SIDE);
            
            while(gameField[positionX][positionY] != 0){
                positionX = getRandomNumber(SIDE);
                positionY = getRandomNumber(SIDE);
            }
            gameField[positionX][positionY] = random == 9 ? 4 : 2;
        }else {
            win();
        }
       
       
    }
    
    private Color getColorByValue(int value){
        if(value==0){return Color.NONE;}
        else if(value==2){return Color.ORANGE;}
        else if(value==4){return Color.PURPLE;}
        else if(value==8){return Color.BLUE;}
        else if(value==16){return Color.GREEN;}
        else if(value==32){return Color.YELLOW;}
        else if(value==64){return Color.BROWN;}
        else if(value==128){return Color.GRAY;}
        else if(value==256){return Color.RED;}
        else if(value==512){return Color.PINK;}
        else if(value==1024){return Color.WHITE;}
        else {return Color.BLACK;}
    }
    
    private void setCellColoredNumber(int x, int y, int value){
        if(value > 0){
            setCellValueEx(x, y, getColorByValue(value), Integer.toString(value));
        }
        else{
            setCellValueEx(x, y, getColorByValue(value), "");
        }
    }
    
    
    
    private boolean compressRow(int[] row){
        boolean test = false;
        for (int i = 0; i<row.length;i++){
            if(row[i]==0){
                for (int j = i+1; j < row.length; j++) {
                    if (row[j]!=0){
                        int temp = row[i];
                        row[i] = row[j];
                        row[j] = temp;
                        test = true;
                        break;
                    }
                }
            }
        }
        return test;
    }
    
    private boolean mergeRow(int[] row){
        boolean merge = false;
        if(row.length == 0){
            return merge;
        }
        
        int i = 0;
        do{
            if(row[i] == row[i+1] && row[i] != 0){
                row[i] = row[i] * 2;
                row[i+1] = 0;
                score = score + row[i];
                setScore(score);
                merge = true;
                
            }
        }while(++i < (row.length-1));
        
        return merge;
    }
    
    @Override
    public void onKeyPress(Key key){
        super.onKeyPress(key);
        
        if(!canUserMove()){
            gameOver();
        }
        if(isGameStopped){
            if(key == Key.SPACE){
                createGame();
                drawScene();
                isGameStopped = false;
                return;
            }
        }else if(key == Key.LEFT){
            moveLeft();
            drawScene();
        }else if(key == Key.RIGHT){
            moveRight();
            drawScene();
        }else if(key == Key.UP){
            moveUp();
            drawScene();
        }else if(key == Key.DOWN){
            moveDown();
            drawScene();
        }
        
    }
    
    private void moveUp(){
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();    
    }
    
    private void moveDown(){
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void moveLeft(){
        boolean move = false;
        for(int i = 0; i < gameField.length; i++){
            boolean move1 = compressRow(gameField[i]);
            boolean move2 = mergeRow(gameField[i]);
            boolean move3 = compressRow(gameField[i]);
            if(move1 || move2 || move3){
                move = true;
            }
        }
        if(move){
            createNewNumber();
        }
    }
    
    private void moveRight(){
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }
    
    private void rotateClockwise(){
        int [][] rotateField = new int[SIDE][SIDE];
        for (int i = 0; i < rotateField.length; i++) {
            for (int j = 0; j < rotateField.length; j++) {
                rotateField[i][j] = gameField[SIDE-1-j][i];
            }
        }
        for (int i = 0; i < rotateField.length; i++) {
            for (int j = 0; j < rotateField.length; j++) {
                gameField[i][j] = rotateField[i][j];
            }
        }
    }
    
    private int getMaxTileValue(){
        int maxValue = 0;
        for(int i = 0; i < SIDE; i++){
            for(int j = 0; j < SIDE; j++){
                if(gameField[i][j] > maxValue){
                    maxValue = gameField[i][j];
                }
            }
        }
        return maxValue;
    }
    
    private void win(){
        isGameStopped = true;
        showMessageDialog(Color.BLACK, "You win!!!", Color.YELLOW, 16);
    }
    
    private boolean canUserMove(){
        boolean result = false;
        
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == 0){
                   return true; 
                } 
            }
        }
        for (int i = 0; i < SIDE-1; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (gameField[i][j] == gameField[i+1][j]){
                    return true;
                } 
            }
        }

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE-1; j++) {
                if (gameField[i][j]==gameField[i][j+1]){
                    return true;
                } 
            }
        }
        return result;
    }
    
    private void gameOver(){
        isGameStopped = true;
        showMessageDialog(Color.GREEN, "You lost!!!", Color.YELLOW, 16);
    }
    
}
