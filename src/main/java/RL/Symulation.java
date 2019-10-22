package RL;

import Models.Game.Game;
import Models.Game.Sign;
import Models.Game.Verdict;
import Models.Player.Computer;
import Models.Player.Player;

import java.io.*;
import java.util.HashMap;

public class Symulation {

    private int cross = 0;
    private int circle = 0;
    private int draw = 0;

    private Computer playerOne;
    private Computer playerTwo;

    private HashMap<String, Double> firstPlayerPolicy = null;
    private HashMap<String, Double> secondPlayerPolicy = null;

    private Game game;

    private int rounds = 0;

    public static void main(String[] args) {

        Game ticTacToe = new Game(3,3);
        Symulation symulation1 = new Symulation(ticTacToe);
        symulation1.train(500000);
        symulation1.showStatistics();
        HashMap<String, Double> firstPlayer = symulation1.getFirstPlayerPolicy();

        Computer randomPlayer = new Computer("Random", Sign.CIRCLE, ticTacToe);
        Computer smartPlayer = new Computer("Random", Sign.CROSS, ticTacToe);
        //smartPlayer.setPolicy(deserialize("file.ser"));
        smartPlayer.setPolicy(firstPlayer);
        Symulation symulation2 = new Symulation(ticTacToe, smartPlayer, randomPlayer);
        symulation2.play(50000);
        symulation2.showStatistics();
        System.out.println(symulation1.firstPlayerPolicy.toString());
        symulation1.serialize();

    }
    public void serialize(){
        String filename = "test.ser";

        // Serialization
        try
        {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(firstPlayerPolicy);

            out.close();
            file.close();

            System.out.println("Object has been serialized");

        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }
    public Symulation(Game game) {
        this.game = game;
        playerOne = new Computer("cross player", Sign.CROSS, this.game);
        playerTwo = new Computer("circle player", Sign.CIRCLE, this.game);
    }
    public Symulation(Game game, Computer playerOne, Computer playerTwo) {
        this.game = game;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;

        this.playerOne.setGame(this.game);
        this.playerTwo.setGame(this.game);

    }

    public HashMap<String, Double> getFirstPlayerPolicy() {
        return (HashMap<String, Double>) firstPlayerPolicy.clone();
    }

    public HashMap<String, Double> getSecondPlayerPolicy() {
        return (HashMap<String, Double>) secondPlayerPolicy.clone();
    }

    public void play(int rounds){
        this.rounds = rounds;

        for(int i=0;i<rounds;i++){


            Verdict verdict = Verdict.NOBODY;
            while(verdict==Verdict.NOBODY){
                playerOne.move(0);
                playerTwo.move(0);
                verdict = game.getVerdict();
            }
            if(verdict == Verdict.CROSS) cross++;
            if(verdict == Verdict.CIRCLE) circle++;
            if(verdict == Verdict.DRAW) draw++;

            game.reset();
        }
    }

    public void train(int rounds){
        this.rounds = rounds;
        firstPlayerPolicy = new HashMap<String, Double>();
        secondPlayerPolicy = new HashMap<String, Double>();

        for(int i=0;i<rounds;i++){


            Verdict verdict;
            while(true){

                playerOne.move(0.3);
                playerOne.addState(game.getResultMatrix().getHash());

                verdict = game.getVerdict();
                if(verdict!=Verdict.NOBODY){
                    giveReward(verdict);
                    break;
                }

                playerTwo.move(0.3);
                playerTwo.addState(game.getResultMatrix().getHash());

                verdict = game.getVerdict();
                if(verdict!=Verdict.NOBODY){
                    giveReward(verdict);
                    break;
                }
            }
            if(verdict == Verdict.CROSS) cross++;
            if(verdict == Verdict.CIRCLE) circle++;
            if(verdict == Verdict.DRAW) draw++;

            game.reset();
            playerOne.resetStates();
            playerTwo.resetStates();
        }
        firstPlayerPolicy = playerOne.getPolicy();
        secondPlayerPolicy = playerTwo.getPolicy();

    }
    public void giveReward(Verdict verdict){

        switch (verdict){
            case CROSS:
                this.playerOne.setReward(1);
                this.playerTwo.setReward(0);
                break;
            case CIRCLE:
                this.playerOne.setReward(0);
                this.playerTwo.setReward(1);
                break;
            default:
                this.playerOne.setReward(0.1);
                this.playerTwo.setReward(0.5);
                break;
        }

    }
    public void showStatistics(){

        System.out.println("In "+this.rounds+" games:");
        System.out.println("\tcirle won "+circle+" times\t("+(circle*100)/rounds+"%)");
        System.out.println("\tcross won "+cross+" times\t("+(cross*100)/rounds+"%)");
        System.out.println("\tdraw was "+draw+" times\t("+(draw*100)/rounds+"%)");
    }
    public static HashMap<String, Double> deserialize(String filename){
        HashMap<String, Double> policy;
        // Deserialization
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            policy = (HashMap<String, Double>) in.readObject();

            in.close();
            file.close();

            System.out.println("Object has been deserialized ");
            return policy;
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
        }
        return null;
    }
}
