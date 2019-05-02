package egg.them.all;

public class LikeListItem {
    private final String show;
    private final String character;

    public LikeListItem(String show, String character) {
        this.show = show;
        this.character = character;
    }
    public String getShow() {
        return show;
    }

    public String getCharacter() {
        return character;
    }
}
