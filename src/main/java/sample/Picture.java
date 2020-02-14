package sample;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;

public class Picture {

    static double[] arrForCheck = new double[784];
    static NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("C:\\TheGame\\myMlPerceptron.nnet");  // загрузка обученной сетки TODO основной
    static Mat foto;
    static Mat fotoWithContours;
    static Mat fotoInHSV;
    static Mat fotoInWhiteFilter;
    static Mat canny_cut;
    static double area;
    static double length;
    static Rect rectangle;
    static float dev;
    static int answerCount = 0;
    static int countForGuess;

    public static Mat pic(Mat controllFrame) {

        nu.pattern.OpenCV.loadLocally();

        foto = new Mat();
        Mat canny = new Mat();
        Mat MatContours = new Mat();
        Mat onlyContours = new Mat(480, 640, CvType.CV_8UC1);
        Mat darkMat = new Mat(480, 640, CvType.CV_8UC1);
        Mat oneContour = new Mat();
        Mat cutFotoInWithFilter = new Mat();
        fotoInHSV = new Mat();
        fotoInWhiteFilter = new Mat(480, 640, CvType.CV_8UC1);
        fotoWithContours = new Mat();
        canny_cut = new Mat();
        double[] arr = null;
        ArrayList<Double> arrayList = new ArrayList();
        ArrayList<MatOfPoint> ArrContours = new ArrayList();

        controllFrame.copyTo(foto);
        Imgproc.Canny(foto, canny, 100, 350);
        foto.copyTo(fotoWithContours);

        Imgproc.findContours(canny, ArrContours, MatContours, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);  // все контура копируются в массив и в отдельный Mat
        Imgproc.drawContours(fotoWithContours, ArrContours, -1, new Scalar(0, 255, 0), 8);
        darkMat.copyTo(onlyContours);
        Imgproc.drawContours(onlyContours, ArrContours, -1, new Scalar(255, 255, 255), 15);

        for (int i = 0; i < ArrContours.size(); i++) {

            area = Imgproc.contourArea(ArrContours.get(i));
            rectangle = Imgproc.boundingRect(ArrContours.get(i));
            length = Imgproc.arcLength(new MatOfPoint2f(ArrContours.get(i).toArray()), true);

            int lenMin;
            int lenMax;
            int areaMin;
            int areaMax;

            if (countForGuess == 1) {   // отдельные размеры для числа 1
                lenMin = 10;
                lenMax = 1000;
                areaMin = 10;
                areaMax = 9000;

            } else {
                lenMin = 400;
                lenMax = 1500;
                areaMin = 80;
                areaMax = 9000;
            }

            if (length > lenMin && length < lenMax && area > areaMin && area < areaMax) {   //  отфильтровываем мелкие и крупные контуры

                // добавление выделительных рамок
                Imgproc.rectangle(fotoWithContours, new Point(rectangle.x, rectangle.y),

                        new Point(rectangle.x + rectangle.width - 1, rectangle.y + rectangle.height - 1),
                        new Scalar(0, 0, 255), 5);

                int val_x = rectangle.x;
                int val_y = rectangle.y;
                int width = (rectangle.x + rectangle.width - 1) - val_x;
                int height = (rectangle.y + rectangle.height - 1) - val_y;

                int max;
                int min;
                if (width > height) {
                    max = width;
                    min = height;
                } else {
                    max = height;
                    min = width;
                }
                int tempVal = ((max - min) / 2);

                // копирует часть изображения
                onlyContours.submat(new Rect(val_x, val_y, width, height)).copyTo(oneContour);
                Mat oneContourOnCenter = new Mat(max, max, CvType.CV_8UC3);

                // центровка изображения добавлением отступа от края с помощью черной рамки
                Core.copyMakeBorder(oneContour, oneContour, ((int) (oneContour.height() * 0.25)),
                        ((int) (oneContour.height() * 0.10)), ((int) (oneContour.width() * 0.20)),
                        ((int) (oneContour.width() * 0.20)), Core.BORDER_CONSTANT | Core.BORDER_ISOLATED, new Scalar(0, 0, 0));
                if (width >= height) {
                    Core.copyMakeBorder(oneContour, oneContourOnCenter, tempVal, tempVal, 0, 0,
                            Core.BORDER_CONSTANT | Core.BORDER_ISOLATED, new Scalar(0, 0, 0));
                } else {
                    Core.copyMakeBorder(oneContour, oneContourOnCenter, 0, 0, tempVal, tempVal,
                            Core.BORDER_CONSTANT | Core.BORDER_ISOLATED, new Scalar(0, 0, 0));
                }

                Imgproc.resize(oneContourOnCenter, canny_cut, new Size(28, 28));  // уменьшили интересующего участка изображение до 28х28
                for (int z = 0; z < canny_cut.rows(); z++) {  // запись в массив для передачи в нейронку
                    for (int s = 0; s < canny_cut.cols(); s++) {
                        arr = canny_cut.get(z, s); // z- строка / s- колонка
                        arrayList.add(arr[0]);
                    }
                }

                Double[] arr2 = arrayList.toArray(new Double[arrayList.size()]);
                for (int p = 0; p < arr2.length; p++) {
                    arrForCheck[p] = (double) arr2[p];
                }

                arrayList.clear();
                Imgproc.cvtColor(foto, fotoInHSV, Imgproc.COLOR_BGR2HSV);

                Scalar low = new Scalar(0, 0, Controller.slider);         // регулировка фильтра белого
                Scalar up = new Scalar(180, 255, 255);
                Core.inRange(fotoInHSV, low, up, fotoInWhiteFilter);  // поиск по цвету

                // копирование части изображения (фильтр белого)
                fotoInWhiteFilter.submat(new Rect(val_x, val_y, width, height)).copyTo(cutFotoInWithFilter); //  координаты и ширина с высотой + один пиксель в каждую сторону
                int countColor = 0;

                // поиск засвеченных пикселей на изображении для отбраковки фильтром белого
                for (int z = 0; z < cutFotoInWithFilter.rows(); z++) {
                    arr = cutFotoInWithFilter.get(z, 0);
                    if ((arr[0]) == (0.0)) {   // ищет по левому краю фотки
                        countColor++;
                    }
                    arr = cutFotoInWithFilter.get(z, cutFotoInWithFilter.cols() - 1);

                    if ((arr[0]) == (0.0)) {   // ищет по правому краю фотки
                        countColor++;
                    }
                }
                for (int z = 0; z < cutFotoInWithFilter.cols(); z++) {
                    arr = cutFotoInWithFilter.get(0, z);
                    if ((arr[0]) == (0.0)) {   // ищет по верхнему краю фотки
                        countColor++;
                    }
                    arr = cutFotoInWithFilter.get(cutFotoInWithFilter.rows() - 1, z);
                    if ((arr[0]) == (0.0)) {  // ищет по нижнему краю фотки
                        countColor++;
                    }
                }
                int sumOfseze = (cutFotoInWithFilter.rows() + cutFotoInWithFilter.cols()) * 2;
                dev = (float) countColor / sumOfseze;
                if (dev != 0 && dev < 0.09) {  // фильтр на пересечение контуров  // if (dev != 0 && dev < 0.04) {
                    create();  // запуск распознования
                }
            }
        }
        return foto;
    }

    public static void create() {
        double[] setAnswer = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
        DataSet trainingSetTest = new DataSet(784, 10);
        trainingSetTest.add(arrForCheck, setAnswer);
        testNeuralNetwork(loadedMlPerceptron, trainingSetTest);  // выполняет проверку обученной сети на данных из trainingSetTest
    }

    public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
        double max = 0.0;
        int numOfmax = 0;

        countForGuess = Controller.random_0_to_9;  // случайно загаданное число
        for (DataSetRow dataRow : testSet.getRows()) {
            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[] networkOutput = nnet.getOutput();

            for (int i = 0; i < networkOutput.length; i++) {   // поиск максимального
                if (i == 0) {
                    max = networkOutput[i];
                    numOfmax = i;
                }
                if (networkOutput[i] > max) {
                    max = networkOutput[i];
                    numOfmax = i;
                }
            }
            if ((max > Controller.slider2) && countForGuess == numOfmax) {   // если найдено загаданное число
                answerCount++;
                Imgproc.rectangle(foto, new Point(rectangle.x, rectangle.y),
                        new Point(rectangle.x + rectangle.width - 1, rectangle.y + rectangle.height - 1),
                        new Scalar(0, 0, 255), 6);
            }
        }
    }

    public static boolean checkAnswer() {
        boolean val = false;

        if (answerCount > 0) {
            val = true;
            answerCount = 0;
        }
        return val;
    }
}
