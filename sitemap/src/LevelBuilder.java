import org.apache.commons.validator.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class LevelBuilder extends RecursiveTask<Level> {

    private static List<URI> allLinks = new ArrayList<>();

    private Level level;
    private URI rootUri;
    private static final String referer = "http://www.google.com";

    public LevelBuilder(Level level, URI rootUri) {
        this.level = level;
        this.rootUri = rootUri;
        allLinks.add(level.getUri());
    }

    @Override
    protected Level compute() {
        URI uri = level.getUri();
        System.out.println(uri);
        if (uri.equals(rootUri)) {
            UrlValidator urlValidator = new UrlValidator();
            if ( ! urlValidator.isValid(uri.toString()) ) {
                try {
                    throw new Exception("Invalid URL");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Document document = null;
        try {
            document = Jsoup.connect(uri.toString())
                    .header("Accept-Encoding", "gzip, deflate")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                    .referrer(referer)
                    .maxBodySize(1000000000)
                    .timeout(6000)
                    .ignoreContentType(true)
                    .get();
        } catch (IOException e) {
            return null;
        }
        List<LevelBuilder> levelBuilderList = new ArrayList<>();
        if (document != null) {
            Elements links = document.getElementsByTag("a");
            for (Element el : links) {
                try {
                    URI tempUri = new URI(el.attr("abs:href"));
                    String url;
                    if (tempUri.getPath() == null) continue;
                    if (tempUri.getPath().length() <= 1) {
                        url = rootUri.toString();
                    } else {
                        if (tempUri.getPath().charAt(0) == '#') {
                            url = rootUri.toString() + tempUri.getPath();
                        } else {
                            url = rootUri.toString() + tempUri.getPath().substring(1);
                        }
                    }
                    uri = new URI(url);
                    if  (uri == null) return null;
                    UrlValidator urlValidator = new UrlValidator();
                    if ( ! urlValidator.isValid(uri.toString()) ) {
                        continue;
                    }
                    if ( ! uri.getHost().equals(rootUri.getHost()) ) {
                        continue;
                    }
                    if (allLinks.contains(uri)) {
                        continue;
                    }
                    Level subLevel = new Level((byte)(level.getLevelNumber() + 1),uri);
                    level.addSubLevel(subLevel);
                    LevelBuilder subLevelBuilder = new LevelBuilder(subLevel,rootUri);
                    subLevelBuilder.fork();
                    levelBuilderList.add(subLevelBuilder);
                } catch (URISyntaxException e) {
                }
            }
        }
        levelBuilderList.forEach(levelBuilder -> levelBuilder.join());
        return level;
    }

}
