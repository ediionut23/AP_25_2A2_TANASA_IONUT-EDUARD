import java.util.List;

class TimeKeeper extends Thread {
    private final long maxDurationMillis;
    private final long startTime = System.currentTimeMillis();
    private final List<Thread> playerThreads;

    public TimeKeeper(long maxDurationSeconds, List<Thread> playerThreads) {
        this.maxDurationMillis = maxDurationSeconds * 1000;
        this.playerThreads = playerThreads;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (System.currentTimeMillis() - startTime < maxDurationMillis) {
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("[TimeKeeper] Game running... " + elapsed + " seconds elapsed.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }

        System.out.println("[TimeKeeper] Time limit reached. Interrupting players...");
        for (Thread t : playerThreads) {
            t.interrupt();
        }
    }
}