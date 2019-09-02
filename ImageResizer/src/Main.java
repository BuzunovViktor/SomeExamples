public class Main
{
    public static void main(String[] args)
    {
        //works only for windows
        int cores = Runtime.getRuntime().availableProcessors();
        String srcFolder = "C:\\Users\\Viktor\\Desktop\\images";
        String dstFolder = "C:\\Users\\Viktor\\Desktop\\imagesScaled";

        ImageResize.resizeAllImages(srcFolder,dstFolder,cores);
    }
}
