package brobot.mudae;

public class MudaeRoll {
    private final String name;
    private final String show;
    private final int createMinute;
    private final int createSecond;
    private final String link;
    private int expiryTime;

    public MudaeRoll(final String name, final String show, final int createMinute, final int createSecond, final String link) {
        this.name = name;
        this.show = show;
        this.createMinute = createMinute;
        this.createSecond = createSecond;
        this.link = link;
        this.expiryTime = 30;
    }

    public boolean isExpired(final int minute, final int second) {
        this.expiryTime = 30 - ((minute - createMinute) * 60 + (second - this.createSecond) % 60);
        return expiryTime <= 0;
    }

    @Override
    public String toString() {
        return "**" + expiryTime + "** - **" + name + "** / " + show + "\n" + link + "\n";
    }
}
