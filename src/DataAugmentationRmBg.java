import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class DataAugmentationRmBg {

     public static Scalar WHITE = new Scalar(255, 255, 255);
     public static Scalar BLACK = new Scalar(0, 0, 0);

    public static void drawBgFgLines(Mat m, double dx, double dy){
        double x0 = m.width()*dx;
        double y0 = m.height()*dy;

        double x1 = m.width()*(1-dx);
        double y1 = m.height()*(1-dy);

        Point pt1 = new Point(x0, y0);
        Point pt2 = new Point(x0, y1);
        Point pt3 = new Point(x1, y0);
        Point pt4 = new Point(x1, y1);

        Point ptMid0 = new Point(m.width()/2, m.height()/2);
        Point ptMid1 = new Point(m.width()/2, m.height()/2-m.height()*0.2);

        Imgproc.line(m, pt1, pt2, BLACK);
        Imgproc.line(m, pt2, pt3, BLACK);
        Imgproc.line(m, pt3, pt4, BLACK);
        Imgproc.line(m, ptMid0, ptMid1,WHITE);

    }

    public static void extractForeground(Mat image, String fileNameWithCompletePath) {

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int x1 = (int) (image.width()*0.1);
        int y1 = (int) (image.height()*0.1);
        int x2 = (int) (image.width()*0.9);
        int y2 = (int) (image.height()*0.9);
        Rect rectangle = new Rect(x1, y1, x2-x1, y2-y1);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGBA2RGB);
        Mat mask = new Mat();
        Mat bgdModel = new Mat();
        Mat fgdModel = new Mat();
        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3));
        Imgproc.grabCut(image, mask, rectangle, bgdModel, fgdModel, 3, Imgproc.GC_INIT_WITH_RECT);
        Core.compare(mask, source, mask, Core.CMP_EQ);
        Mat foreground = new Mat(image.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        image.copyTo(foreground, mask);
        Imgcodecs.imwrite(fileNameWithCompletePath, foreground);

        Mat newMask = new Mat();
        newMask.setTo(new Scalar(51, 51, 51));
        drawBgFgLines(newMask, 0.1, 0.1);
        mask = setPixel(newMask, mask, 255, 1);
        mask = setPixel(mask, mask, 255, 1);
        mask = setPixel(newMask, mask, 0, 0);
        double[][] array = matToArray(mask);
        Imgproc.grabCut(image, mask, rectangle, bgdModel, fgdModel, 2, Imgproc.GC_INIT_WITH_MASK);
        Core.compare(mask, source, mask, Core.CMP_EQ);
        foreground = new Mat(image.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        image.copyTo(foreground, mask);
        Imgcodecs.imwrite("C:\\cada\\productsImages\\test2.jpg", foreground);
        //foreground.dump();

    }

    public static double[][] matToArray(Mat frame) {
        double array[][] = new double[frame.height()][frame.width()];
        for (int i=0; i < frame.height(); i++)
        {
            for (int j=0; j < frame.width(); j++)
            {
                double pixel = frame.get(i, j)[0];
                if ((pixel != 0)&&(pixel != 1)){
                    System.out.println(pixel);
                    System.out.println(i);
                    System.out.println(j);
                    System.out.println("\n");
                }
                array[i][j] = pixel;
            }
        }
        return array;
    }

    public static Mat setPixel(Mat mSource, Mat mDest, int pxSource, int pxDest) {
        if ((mSource.width() != mDest.width())&&(mSource.height() != mDest.height())) {
            return mDest;
        }
        for (int i=0; i < mSource.height(); i++)
        {
            for (int j=0; j < mSource.width(); j++)
            {
                double pixel = mSource.get(i, j)[0];
                if (pixel == pxSource){
                    mDest.put(i,j,pxDest);
                }
            }
        }
        return mDest;
    }
}
