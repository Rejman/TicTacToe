package Tools;

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
    public double getMinutes(){
        return getSeconds()/60;
    }
}
