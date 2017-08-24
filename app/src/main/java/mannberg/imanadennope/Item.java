package mannberg.imanadennope;

/**
 * Created by mannb on 8/21/2017.
 */

public class Item {
    public String username;
    public String itemtext;
    public String location;
    public int pos;
    public int time;
    public int votes;
    public boolean og;

    public Item(String username, String itemtext, String location, int time, int votes, int pos, boolean og) {
        this.username = username;
        this.itemtext = itemtext;
        this.location = location;
        this.time = time;
        this.votes = votes;
        this.pos = pos;
        this.og = og;
    }
}
