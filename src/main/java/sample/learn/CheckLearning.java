package sample.learn;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import java.util.ArrayList;

public class CheckLearning {

    public static void startCheck() {

        String inputFileName = "C:\\TheGame\\dataSet\\checkSet.csv";
        DataSet checkSet;
        checkSet = DataSet.createFromFile(inputFileName, 784, 10, ";");
        NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("C:\\TheGame\\myMlPerceptron.nnet");
        testNeuralNetworkWithCheck(loadedMlPerceptron, checkSet);
    }

    public static void testNeuralNetworkWithCheck(NeuralNetwork nnet, DataSet testSet) {

        ArrayList<Double> arrayListWithErrors = new ArrayList();
        double error;
        double averageError;
        double sumOfErrors=0.0;
        int numAnswer = 0;

        for (DataSetRow dataRow : testSet.getRows()) {
            nnet.setInput(dataRow.getInput());
            nnet.calculate();
            double[] networkOutput = nnet.getOutput();
            double[] arrayWithAnswer = dataRow.getDesiredOutput();

            for (int i = 0; i < arrayWithAnswer.length; i++) {
                if (arrayWithAnswer[i] == 1.0) {
                    numAnswer = i;
                }
            }
            error = 1.0 - networkOutput[numAnswer];
            arrayListWithErrors.add(error);
        }

        for (int i = 0; i <arrayListWithErrors.size() ; i++) {
            sumOfErrors=sumOfErrors+ arrayListWithErrors.get(i);
        }

        int cntAnswer=0;
        for (int i = 0; i <arrayListWithErrors.size() ; i++) {
            if(arrayListWithErrors.get(i)<0.5){
                cntAnswer++;
            }
        }

        System.out.println("correct Answer "+cntAnswer+" out of 100");
        averageError=sumOfErrors/arrayListWithErrors.size();
        System.out.println("mean Of Error: "+averageError);
        System.out.println();
    }
}
