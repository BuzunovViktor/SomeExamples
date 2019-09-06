import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Level {

    private URI uri;
    private Byte levelNumber;
    private List<Level> subLevels = null;

    public Level(Byte levelNumber, URI uri) {
        this.levelNumber = levelNumber;
        this.uri = uri;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public Byte getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(Byte levelNumber) {
        this.levelNumber = levelNumber;
    }

    public void addSubLevel(Level subLevel) {
        if (subLevels == null) {
            subLevels = new ArrayList<>();
        }
        subLevels.add(subLevel);
    }

    public List<Level> getSubLevels() {
        return subLevels;
    }

    public void setSubLevels(List<Level> subLevels) {
        this.subLevels = subLevels;
    }

    @Override
    public String toString() {
        return "Level{" +
                "uri=" + uri +
                ", levelNumber=" + levelNumber +
                ", subLevels=" + subLevels +
                '}';
    }

}
