import java.util.ArrayList;

public class Level {

    private Byte levelNumber;
    private String url;
    private ArrayList<Level> subLevels = null;

    public Level(Byte levelNumber, String url) {
        this.levelNumber = levelNumber;
        this.url = url;
    }

    public Byte getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(Byte levelNumber) {
        this.levelNumber = levelNumber;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Level> getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(ArrayList<Level> subLevels) {
        this.subLevels = subLevels;
    }

}
