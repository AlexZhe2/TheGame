package sample.learn;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;


public class Neu {


    public static void startNeu() {


        String inputFileName = "C:\\TheGame\\dataSet\\trainSet.csv";
        DataSet trainingSet;
        trainingSet = DataSet.createFromFile(inputFileName, 784, 10, ";");

        System.out.println("start learning");

        MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 784, 49, 196, 10);
        System.out.println("количество весов для нейронов= " + (myMlPerceptron.getWeights().length));


        LearningEventListener testEvaluator = new LearningEventListener() {
            int epoch = 0;

            @Override
            public void handleLearningEvent(LearningEvent event) {
                event.getEventType();
                epoch++;

                myMlPerceptron.save("C:\\TheGame\\myMlPerceptron.nnet");

                CheckLearning.startCheck();

                System.out.println("event.getEventType() " + event.getEventType() + "№ " + epoch);

            }
        };

        MomentumBackpropagation bp = new MomentumBackpropagation();
        bp.setLearningRate(0.5);
        bp.setMomentum(1.4);
        bp.addListener(testEvaluator);
        bp.setMaxIterations(10);


        myMlPerceptron.learn(trainingSet, bp); // начало обучения


    }


}
