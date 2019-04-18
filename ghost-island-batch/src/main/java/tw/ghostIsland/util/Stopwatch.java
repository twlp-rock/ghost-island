package tw.ghostIsland.util;

public class Stopwatch {
    private long startTime = System.currentTimeMillis();
    public Stopwatch createStarted(){
        return this;
    }
    public long elapsed(){
        return System.currentTimeMillis() - startTime;
    }
}
