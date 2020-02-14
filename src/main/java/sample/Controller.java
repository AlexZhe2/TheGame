package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import sample.utils.Utils;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class Controller {

    @FXML
    private TextField textField_6;
    @FXML
    private ImageView currentFrame;
    @FXML
    public MediaView view;
    @FXML
    private ImageView filterFrame_1;
    @FXML
    private Slider slider_1;
    @FXML
    private Slider slider_2;
    @FXML
    private ImageView testFrame_1;
    @FXML
    private Button buttonTest_1;
    @FXML
    private Button buttonShowFilter_1;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Button button;
    @FXML
    private RadioButton radioButton_1;
    @FXML
    private RadioButton radioButton_2;
    @FXML
    private RadioButton radioButton_3;
    @FXML
    private RadioButton testRadioButton_0;
    @FXML
    private RadioButton testRadioButton_1;
    @FXML
    private RadioButton testRadioButton_2;
    @FXML
    private RadioButton testRadioButton_3;
    @FXML
    private RadioButton testRadioButton_4;
    @FXML
    private RadioButton testRadioButton_5;
    @FXML
    private RadioButton testRadioButton_6;
    @FXML
    private RadioButton testRadioButton_7;
    @FXML
    private RadioButton testRadioButton_8;
    @FXML
    private RadioButton testRadioButton_9;
    @FXML
    private ToggleGroup radButGroup;
    @FXML
    private TextField textField_1;
    @FXML
    private TextField textField_2;
    @FXML
    private TextField textField_3;
    @FXML
    private TextField textField_4;
    @FXML
    private TextField textField_5;
    @FXML
    private Button savePath_1;
    @FXML
    protected CheckBox Arithmeticalmode_1;

    MediaPlayer player;
    String parth_1;
    String parth_2;
    String parth_3;
    String parth_4;
    String parth_5;
    String[] arrPath = new String[5];
    String example;
    String stringTextField_1;
    String stringTextField_2;
    String stringTextField_3;
    String stringTextField_4;
    String stringTextField_5;

    boolean arrPathIsEmpty = true;
    boolean testOn = false;
    boolean showFilterOn = false;
    boolean load = false;
    int testNumber = 0;
    static boolean stopSpace = false;
    static int slider;
    static double slider2;
    private VideoCapture camera = new VideoCapture();
    private static int cameraId = 0;
    boolean playMovie = false;
    boolean correctAnswer;
    Duration durationOfUser;
    Duration currentDurationOfMovie;
    Duration stopTime = Duration.ZERO;
    Duration sumOfDuration;
    double pauseCounter = 1;
    static int random_0_to_9;
    static int randomForArithmetic_1;
    static int randomForArithmetic_2;
    static double randomValue;
    boolean arithmeticIsOn = false;
    static File saveFile = new File("C:\\TheGame\\savePath.txt");

    private void makeMediaView() {

        try {
        for (int i = 0; i < arrPath.length; i++) {
            if (!arrPath[i].equals("file:///")) {
                arrPathIsEmpty = false;
                break;
            }
        }

        } catch (Exception e) {
            System.out.println("ошибка чтения видео файла");
            textField_6.setText("ошибка чтения видео файла");
            textField_6.setVisible(true);
        }

        if (arrPathIsEmpty == false) {
            while (true) {
                int random = (int) (Math.random() * 5);
                System.out.println("random " + random);
                if (!arrPath[random].equals("file:///")) {
                    System.out.println("выбран файл: " + arrPath[random]);
                    try {
                    player = new MediaPlayer(new Media(arrPath[random]));
                    } catch (Exception e) {
                        System.out.println("ошибка чтения видео файла");
                        textField_6.setText("ошибка чтения видео файла");
                        textField_6.setVisible(true);
                    }
                    break;
                }
            }
        }
    }

    public void sliderAct() {
        slider = (int) slider_1.getValue();
    }

    public void sliderAct2() {
        slider2 = slider_2.getValue();
    }

    public void pushButtonTest() {
        if (testOn == false) {
            testOn = true;
            buttonTest_1.setText("Stop Test");
            showFilterOn = false;  // отключение Filter Video
            buttonShowFilter_1.setText("Show Filter Video");
            filterFrame_1.setVisible(false);
            random_0_to_9 = testNumber;
        } else {
            testOn = false;
            buttonTest_1.setText("Launch Test");
            testFrame_1.setVisible(false);
        }
    }

    public void pushButtonFilter() {
        if (showFilterOn == false) {
            showFilterOn = true;
            buttonShowFilter_1.setText("Hide Filter Video");
            testOn = false;         // отключение Launch Test
            buttonTest_1.setText("Launch Test");
            testFrame_1.setVisible(false);
        } else {
            showFilterOn = false;
            buttonShowFilter_1.setText("Show Filter Video");
            filterFrame_1.setVisible(false);
        }
    }

    public void getSelectedTestRadBut() {
        RadioButton tempTestRadBut = (RadioButton) radButGroup.getSelectedToggle();
        testNumber = Integer.parseInt(tempTestRadBut.getText());
        System.out.println("testNumber " + testNumber);
        if (testOn == true) {
            random_0_to_9 = testNumber;
            textField_6.setText(String.valueOf(random_0_to_9));
        }
    }

    public void selectedArithmeticalMode() {
        if (arithmeticIsOn == false) {
            arithmeticIsOn = true;
        } else {
            arithmeticIsOn = false;
        }
    }

    public String arithmeticalExample() {
        String tempExample = "";
        randomValue = Math.random();
        if (randomValue > 0.5) {
            while (true) {
                randomForArithmetic_1 = (int) (Math.random() * (random_0_to_9 + 1));
                randomForArithmetic_2 = (int) (Math.random() * 10);
                if (random_0_to_9 == randomForArithmetic_1 + randomForArithmetic_2) {
                    tempExample = String.valueOf(randomForArithmetic_1) + " + " + String.valueOf(randomForArithmetic_2) + " = ";
                    break;
                }
            }
        } else {
            while (true) {
                randomForArithmetic_1 = (int) (Math.random() * 10);
                randomForArithmetic_2 = (int) (Math.random() * (10 - random_0_to_9));
                if (random_0_to_9 == randomForArithmetic_1 - randomForArithmetic_2) {
                    tempExample = String.valueOf(randomForArithmetic_1) + " - " + String.valueOf(randomForArithmetic_2) + " = ";
                    break;
                }
            }
        }
        return tempExample;
    }

    public void generateRandomNumber() {
        random_0_to_9 = (int) (Math.random() * 10);
        example = arithmeticalExample();
        System.out.println("example: " + example);

        if (arithmeticIsOn == false) {
            textField_6.setText(String.valueOf(random_0_to_9));
        } else {
            textField_6.setText(example);
        }
        textField_6.setVisible(true);

        if (testOn == true) {
            random_0_to_9 = testNumber;
            if (arithmeticIsOn == false) {
                textField_6.setText(String.valueOf(random_0_to_9));
            } else {
                textField_6.setText(example);
            }
        }
    }



    public void selectSettings(Event event) {
        if (load == false) {
            methodRead();
            System.out.println("load savePath");
            load = true;
        }
    }


    @FXML
    protected void startCamera(ActionEvent event) {
        methodRead();
        generateRandomNumber();
        System.out.println("random_0_to_9 =  " + random_0_to_9);
        if (arithmeticIsOn == false) {
            textField_6.setText(String.valueOf(random_0_to_9));
        } else {
            textField_6.setText(example);
        }
        textField_6.setVisible(true);
        if (testOn == true) {
            random_0_to_9 = testNumber;
            if (arithmeticIsOn == false) {
                textField_6.setText(String.valueOf(random_0_to_9));
            } else {
                textField_6.setText(example);
            }
        }
        button.setVisible(false);
        readerFromPanel();
        camera.open(cameraId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    sliderAct();
                    sliderAct2();
                    correctAnswer = Picture.checkAnswer();

                    if (correctAnswer == false && playMovie == false) {  // условие показа видео с камеры
                        currentFrame.setVisible(true);
                        Mat frame = new Mat();
                        Mat changeFrame = new Mat();
                        camera.read(frame);
                        Picture.pic(frame);
                        changeFrame = Picture.foto;
                        Image imageToShow = SwingFXUtils.toFXImage(Utils.matToBufferedImage(changeFrame), null);
                        updateImageView(currentFrame, imageToShow);

                        if (showFilterOn == true) {
                            filterFrame_1.setVisible(true);
                            Mat filterChangeFrame = Picture.fotoInWhiteFilter;
                            Image imageToShow_2 = SwingFXUtils.toFXImage(Utils.matToBufferedImage(filterChangeFrame), null);
                            updateImageView(filterFrame_1, imageToShow_2);
                        }

                        if (testOn == true) {
                            testFrame_1.setVisible(true);
                            updateImageView(testFrame_1, imageToShow);
                        }
                    } else {
                        if (playMovie == false && testOn == false) {   // запуск мультика
                            try {
                            currentFrame.setVisible(false);
                            textField_6.setVisible(false);
                            view.setVisible(true);  // делаем  экран с мультиком видимым
                            if (stopTime.greaterThan(Duration.ZERO)) {
                                player.setStartTime(stopTime);
                                player.play();
                                playMovie = true;
                            }
                            if (stopTime.equals(Duration.ZERO)) {
                                makeMediaView();                 // выбор мультика
                                view.setMediaPlayer(player);
                                player.setStartTime(Duration.ZERO);
                                player.play();
                                playMovie = true;
                                view.setVisible(true);
                            }
                            player.setOnEndOfMedia(new Runnable() {

                                @Override
                                public void run() {
                                    player.stop();
                                    stopTime = Duration.ZERO;
                                    player.setStartTime(Duration.ZERO);
                                    pauseCounter = 1;
                                    playMovie = false;
                                    view.setVisible(false);  // убираем экран с мультиком
                                    correctAnswer = false;
                                    generateRandomNumber();
                                }
                            });
                            } catch (Exception e) {
                                System.out.println("ошибка чтения видео файла");
                                textField_6.setText("ошибка чтения видео файла");
                                textField_6.setVisible(true);
                            }
                        }
                    }
                    if (stopSpace == true) {   // остановка при нажатии "пробела"
                        if (playMovie == true) {
                            player.stop();
                            stopTime = Duration.ZERO;
                            player.setStartTime(Duration.ZERO);
                            pauseCounter = 1;
                            playMovie = false;
                            view.setVisible(false);  // убираем экран с мультиком
                            correctAnswer = false;
                            generateRandomNumber();
                            stopSpace = false;
                        }
                    }
                    if (playMovie == true) {
                        if (radioButton_1.isSelected() == true) {
                            durationOfUser = Duration.millis(60_000); // 3 min
                        }
                        if (radioButton_2.isSelected() == true) {
                            durationOfUser = Duration.millis(300_000); // 5 min
                        }
                        if (radioButton_3.isSelected() == true) {
                            durationOfUser = Duration.millis(600_000);  // 10 min
                        }
                        currentDurationOfMovie = player.getCurrentTime();
                        sumOfDuration = durationOfUser.multiply(pauseCounter);
                        if (currentDurationOfMovie.greaterThan(sumOfDuration)) {  // сравнение продолительностей
                            player.pause();
                            view.setVisible(false);  // убираем экран с мультиком
                            playMovie = false;
                            stopTime = player.getCurrentTime();
                            pauseCounter++;
                            generateRandomNumber();
                        }
                    }
                    // частота кардов
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    @FXML
    protected void selectedRadioButton_1() {
        radioButton_2.setSelected(false);
        radioButton_3.setSelected(false);
    }

    @FXML
    protected void selectedRadioButton_2() {
        radioButton_1.setSelected(false);
        radioButton_3.setSelected(false);
    }

    @FXML
    protected void selectedRadioButton_3() {
        radioButton_1.setSelected(false);
        radioButton_2.setSelected(false);
    }

    public void readerFromPanel() {
        stringTextField_1 = textField_1.getText();
        stringTextField_2 = textField_2.getText();
        stringTextField_3 = textField_3.getText();
        stringTextField_4 = textField_4.getText();
        stringTextField_5 = textField_5.getText();

        String tempSt_1 = new String(stringTextField_1);
        String tempSt_2 = new String(stringTextField_2);
        String tempSt_3 = new String(stringTextField_3);
        String tempSt_4 = new String(stringTextField_4);
        String tempSt_5 = new String(stringTextField_5);

        parth_1 = "file:///" + tempSt_1.replace('\\', '/');
        parth_2 = "file:///" + tempSt_2.replace('\\', '/');
        parth_3 = "file:///" + tempSt_3.replace('\\', '/');
        parth_4 = "file:///" + tempSt_4.replace('\\', '/');
        parth_5 = "file:///" + tempSt_5.replace('\\', '/');

        arrPath[0] = parth_1;
        arrPath[1] = parth_2;
        arrPath[2] = parth_3;
        arrPath[3] = parth_4;
        arrPath[4] = parth_5;
    }

    public void methodSave() {
        arrPath[0]=null;
        arrPath[1]=null;
        arrPath[2]=null;
        arrPath[3]=null;
        arrPath[4]=null;
        System.out.println(saveFile.toString());

        try {
            writeWithBuffer2(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ошибка сохранения файла");
            textField_6.setText("ошибка сохранения файла");
            textField_6.setVisible(true);
        }

        methodRead();
        readerFromPanel();
        makeMediaView();
    }

    public void methodRead() {
        try {
            String allPath = readWithRandom(saveFile);
            String[] arrWithPath = allPath.split("\n");
            LinkedList<String> linkedList = new LinkedList<>();
            linkedList.addAll(Arrays.asList(arrWithPath));

            for (int i = 0; i < 6; i++) { // добавляем пустые ячейки в лист, чтоб не было Exception
                linkedList.add("");
            }

            for (int i = 0; i < linkedList.size(); i++) {  //  ""  чтоб null  не попал в строку
                if (linkedList.get(i).equals("null") || linkedList.get(i).equals("\n")) {
                    linkedList.set(i, "");
                }
            }

            String string1 = linkedList.get(0);
            String string2 = linkedList.get(1);
            String string3 = linkedList.get(2);
            String string4 = linkedList.get(3);
            String string5 = linkedList.get(4);

            textField_1.setText(string1);
            textField_2.setText(string2);
            textField_3.setText(string3);
            textField_4.setText(string4);
            textField_5.setText(string5);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ошибка чтения видео файла");
            textField_6.setText("ошибка чтения видео файла");
            textField_6.setVisible(true);
        }
    }


    public void writeWithBuffer2(File file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedOutputStream outputStream = new BufferedOutputStream(fileOutputStream);
        ) {
            stringTextField_1 = textField_1.getText();
            stringTextField_2 = textField_2.getText();
            stringTextField_3 = textField_3.getText();
            stringTextField_4 = textField_4.getText();
            stringTextField_5 = textField_5.getText();

            String stringForSave = stringTextField_1 + "\n"
                    + stringTextField_2 + "\n"
                    + stringTextField_3 + "\n"
                    + stringTextField_4 + "\n"
                    + stringTextField_5 + "\n";
            outputStream.write(stringForSave.getBytes());
        }
    }

    public static String readWithRandom(File file) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stringBuilder.append(raf.readLine()).append("\n");
        }
        String text = stringBuilder.toString();
        return text;
    }
}