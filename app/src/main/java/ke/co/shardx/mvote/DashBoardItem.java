package ke.co.shardx.mvote;

public class DashBoardItem {
    private String item;
    private int Thumbnail;

    public DashBoardItem() {
    }

    public DashBoardItem(String item, int thumbnail) {
        this.item = item;
        Thumbnail = thumbnail;
    }

    public String getItem() {
        return item;
    }

    public int getThumbnail() {
        return Thumbnail;
    }
}
