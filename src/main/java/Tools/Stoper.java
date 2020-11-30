package Tools;

import javafx.application.Platform;

public class Stoper {
    private long start;
    private long stop;

    public void start(){
        start = System.currentTimeMillis();
    }
    public void stop(){
        stop = System.currentTimeMillis();
    }
    public double getSeconds(){
        return (stop-start)/1000;
    }
    public String getTime(){
        long minutes = (long)(getSeconds()/60);
        long seconds = (long) (getSeconds() - (minutes * 60));
        return minutes + " min, "+seconds+" sec";
    }

}

