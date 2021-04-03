import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class DataAugmentationRmBg {

    public static void extractForeground(Mat image, String fileNameWithCompletePath) {

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int x1 = (int) (image.width()*0.05);
        int y1 = (int) (image.height()*0.05);
        int x2 = (int) (image.width()*0.95);
        int y2 = (int) (image.height()*0.95);
        Rect rectangle = new Rect(x1, y1, x2, y2);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGBA2RGB);
        Mat mask = new Mat();
        Mat bgdModel = new Mat();
        Mat fgdModel = new Mat();
        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3));
        Imgproc.grabCut(image, mask, rectangle, bgdModel, fgdModel, 8, Imgproc.GC_INIT_WITH_RECT);
        Core.compare(mask, source, mask, Core.CMP_EQ);
        Mat foreground = new Mat(image.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        image.copyTo(foreground, mask);
        Imgcodecs.imwrite(fileNameWithCompletePath, foreground);
        //foreground.dump();

    }
}
