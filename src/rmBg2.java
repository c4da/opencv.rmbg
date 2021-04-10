import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class rmBg2 {

    public static Scalar WHITE = new Scalar(255, 255, 255);
    public static Scalar BLACK = new Scalar(0, 0, 0);

    public static void drawBgFgLines(Mat m){
        double x0 = 60;
        double y0 = 2;
        double x1 = m.width()-60;
        double y1 = m.height()-2;
        Point pt1 = new Point(x0, y0);
        Point pt2 = new Point(x0, y1);
        Point pt3 = new Point(x1, y0);
        Point pt4 = new Point(x1, y1);
        Point ptMid0 = new Point(m.width()/2, 15);
        Point ptMid1 = new Point(m.width()/2, m.height()-15);
        Imgproc.line(m, pt1, pt2, new Scalar(Imgproc.GC_BGD), 30);
        Imgproc.line(m, pt2, pt4, new Scalar(Imgproc.GC_BGD), 5);
        Imgproc.line(m, pt4, pt3, new Scalar(Imgproc.GC_BGD), 30);
        Imgproc.line(m, pt1, pt3, new Scalar(Imgproc.GC_BGD), 5);
        Imgproc.line(m, ptMid0, ptMid1, new Scalar(Imgproc.GC_FGD), 5);

    }

    public static void extractForeground(Mat image, String fileNameWithCompletePath) {

        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        int x1 = 20;
        int y1 = 2;
        int x2 = image.width()-20;
        int y2 = image.height()-1;
        Rect rectangle = new Rect(x1, y1, x2-x1, y2-y1);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGBA2RGB);
        Mat mask = new Mat(image.size(), CvType.CV_8UC1, new Scalar(Imgproc.GC_PR_FGD));
        Mat newMask = new Mat(image.size(), CvType.CV_8UC1, new Scalar(Imgproc.GC_PR_FGD));
        drawBgFgLines(newMask);
        mask = setPixel(newMask, mask, 255, Imgproc.GC_FGD);
        mask = setPixel(newMask, mask, 0, Imgproc.GC_BGD);
        Imgcodecs.imwrite("C:\\cada\\productsImages\\mask.jpg", mask);
        Imgcodecs.imwrite("C:\\cada\\productsImages\\newMask.jpg", newMask);
        Mat bgdModel = new Mat();
        Mat fgdModel = new Mat();
        Mat source = new Mat(1, 1, CvType.CV_8U, new Scalar(3));
        Imgproc.grabCut(image, mask, rectangle, bgdModel, fgdModel, 2, Imgproc.GC_INIT_WITH_MASK);
        Core.compare(mask, source, mask, Core.CMP_EQ);
        Mat foreground = new Mat(image.size(), CvType.CV_8UC3, new Scalar(255, 255, 255));
        image.copyTo(foreground, mask);
        Imgcodecs.imwrite(fileNameWithCompletePath, foreground);

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
