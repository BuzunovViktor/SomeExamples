import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.RecursiveTask;

public class LevelBuilder extends RecursiveTask<Level> {

    private Level level;
    private static final String referer = "http://www.google.com";

    public LevelBuilder(Level level) {
        this.level = level;
    }

    @Override
    protected Level compute() {

        String url = level.getUrl();

        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .referrer(referer)
                    .maxBodySize(1000000000)
                    .timeout(6000000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (document != null) {
            Elements links = document.getElementsByTag("a");
            ArrayList<Level> subLevels = new ArrayList<>();
            for (Element el : links) {
                Level subLevel = new Level((byte)(level.getLevelNumber() + 1),el.attr("href"));
                LevelBuilder levelBuilder = new LevelBuilder(subLevel);
                levelBuilder.fork();
                subLevels.add(subLevel);
            }
            level.setSubLevels(subLevels);
        }
        return level;
    }

}
