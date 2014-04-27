import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;



public class NNTrain {
	NeuralNetwork neuralNet;
	TrainingSet<SupervisedTrainingElement> trainingSet;
	TrainingSet<SupervisedTrainingElement> testingSet;
	static String[] keyword = new String[125];
	
	private static void loadKW(String fileName) {
		File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int i = 0;
            while ((tempString = reader.readLine()) != null) {
            	keyword[i] = tempString;
            	i++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
	
	private int[] handleText(String text) {
		//need to improve search speed
		int[] x = new int[125];
		try {
			Map wordfrenmap = WordFren.getTextDef(text);
			for(int i = 0; i<125 ; i++){
				if(wordfrenmap.containsKey(keyword[i]))
					x[i] = 1;
				else 
					x[i] = 0;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return x;
	}
	
	public int[] getInputX(String code, Date start, Date end){
		String text = DBControl.GetText(code, start, end);
		int[] inputX = handleText(text);
		return inputX;
	}

	public void getInputY(){
		
	}
	
	public TrainingSet<SupervisedTrainingElement>[] getdataset(){
		return null;
		//DataSet dataSet = new DataSet(125, 1);	
	}
	
	public void nntrain(){
		neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH, 10, 16,1);
		MomentumBackpropagation learningRule = (MomentumBackpropagation) neuralNet
				.getLearningRule();
		learningRule.setLearningRate(0.2);
		learningRule.setMomentum(0.5);
		learningRule.setMaxError(0.5);

		// create training set from file
//		trainingSet = DataSet.createFromFile(inputFilePath, 10, 1, ",");
//		testingSet = DataSet.createFromFile(testCsvFilePath, 10, 1, ",");
		// train the network with training set
		neuralNet.learn(trainingSet);
	}
	
	public static void main(String args[]){
		String KWpath = "C://Users//rushshi//Desktop//dic.txt"; 
		loadKW(KWpath);
		System.out.println(NNTrain.keyword.length);
	}

	
}
