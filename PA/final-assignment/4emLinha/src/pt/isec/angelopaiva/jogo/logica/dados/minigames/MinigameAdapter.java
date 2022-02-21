package pt.isec.angelopaiva.jogo.logica.dados.minigames;

public abstract class MinigameAdapter implements IMinigame {
    private int maxSeconds;
    private long startTime;
    private long finishTime;

    protected boolean finished;

    protected MinigameAdapter() {}

    @Override
    public void setMaxSeconds(int maxSeconds) { this.maxSeconds = maxSeconds; }

    @Override
    public void startTimer() { startTime = System.currentTimeMillis(); }
    @Override
    public void stopTimer() { finishTime = System.currentTimeMillis(); }

    @Override
    public boolean hasWon() { return getElapsedTime() <= maxSeconds; }

    @Override
    public String getQuestion() { return ""; }
    @Override
    public String getWording() { return ""; }

    @Override
    public boolean isFinished() { return finished; }

    protected boolean hasTimedOut() { return ((System.currentTimeMillis() - startTime) / 1000) > maxSeconds; }

    private int getElapsedTime() { return (int) ((finishTime - startTime) / 1000); }
}
