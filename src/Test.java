import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Test {

    // Compulsory
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static Mat loadImage(String pathToImg){
        Mat src = Imgcodecs.imread(pathToImg);
        return src;
    }

    public static void main(String[] args) {
        System.out.println("OpenCV " + Core.VERSION);
        Mat m = loadImage("C:\\cada\\productsImages\\front_de.jpg");
        System.out.println("OpenCV Mat: " + m);
        DataAugmentationRmBg.extractForeground(m, "C:\\cada\\productsImages\\result3.jpg");
        //System.out.println("OpenCV Mat data:\n" + m.dump());
    }
}

