package brobot.mudae;

public class MudaeRoll {
    private final String name;
    private final String show;
    private final int createMinute;
    private final int createSecond;
    private final String link;

    public MudaeRoll(final String name, final String show, final int createMinute, final int createSecond, final String link) {
        this.name = name;
        this.show = show;
        this.createMinute = createMinute;
        this.createSecond = createSecond;
        this.link = link;
    }

    public  boolean isExpired(final int second) {
        int diff = second - this.createSecond;
        return (diff > 0 ? diff : 60 - Math.abs(diff)) > 30;
    }

    @Override
    public String toString() {
        return "**" + name + "** / " + show + "\n" + link + "\n";
    }
}
