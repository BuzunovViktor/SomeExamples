import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResize {

    public static void resizeAllImages(String src, String dst, int treadCount) {

        File srcDir = new File(src);
        File[] files = srcDir.listFiles();
        if (files == null) return;
        int startIndex = 0;
        int countForThread = files.length / treadCount;
        int indexCorrection = 0;
        if (countForThread * treadCount != files.length) {
            indexCorrection = files.length - (countForThread * treadCount);
        }
        for (int i = 1; i <= treadCount; i++) {
            int currentStartIndex = startIndex;
            int currentEndIndex = countForThread * i;
            if (currentEndIndex + indexCorrection == files.length) {
                currentEndIndex += indexCorrection;
            }

            final int finalEndIndex = currentEndIndex;
            final int finalStartIndex = currentStartIndex;
            final int finalI = i;
            new Thread(()->{
                long start = System.currentTimeMillis();
                for (int j = finalStartIndex; j < finalEndIndex; j++) {
                    File file = files[j];
                    BufferedImage bufferedImage = null;
                    try {
                        bufferedImage = ImageIO.read(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (bufferedImage != null) {
                        BufferedImage scaledImage = Scalr.resize(bufferedImage, 300);
                        File newFile = new File(dst + File.separator + file.getName());
                        try {
                            ImageIO.write(scaledImage, "jpg", newFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println("Thread " + finalI + ": " + (System.currentTimeMillis() - start) + "ms");
            }).start();
            startIndex = currentEndIndex;
        }

    }

}
