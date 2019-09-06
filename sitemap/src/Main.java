import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ForkJoinPool;

public class Main {

    private static final String url = "https://skillbox.ru/";
//    private static final String url = "https://lenta.ru/";
    private static final String referer = "http://www.google.com";
    private static StringBuilder resultString = new StringBuilder();

    public static void main(String[] args) {
        try {
            URI uri = new URI(url);
            Byte rootLevel = 0;
            Level root = new Level(rootLevel, uri);
            Level result = new ForkJoinPool().invoke(new LevelBuilder(root, uri));
            prepareResult(result);

            File file = new File("result.txt");
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(file));
                writer.write(resultString.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void prepareResult(Level level) {
        String tabs = "";
        for (int i = 0; i < level.getLevelNumber(); i++) {
            tabs += "\t";
        }
        resultString.append(tabs + level.getUri() + "\n");
        if (level.getSubLevels() != null) {
            level.getSubLevels().forEach(subLevel->{
                prepareResult(subLevel);
            });
        }
    }

}
