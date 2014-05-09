import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;



public class NNTrain {
	NeuralNetwork neuralNet;
	TrainingSet<SupervisedTrainingElement> trainingSet;
	TrainingSet<SupervisedTrainingElement> testingSet;
	static int inputnum = 125;
	static String[] keyword = new String[inputnum];
	static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
	
	private double[] handleText(String text) {
		//need to improve search speed
		double[] x = new double[inputnum];
		try {
			Map wordfrenmap = WordFren.getTextDef(text);
			for(int i = 0; i<inputnum ; i++){
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
	
	public double[] getInputX(String code, Date start, Date end){
		String text = DBControl.GetText(code, start, end);
		double[] inputX = handleText(text);
		return inputX;
	}

	public double getInputY(String code, Date start){
		double inputY = DBControl.GetZhangfu(code, start);
		return inputY;
	}
	
	public TrainingSet<SupervisedTrainingElement> getdataset(String code, Date start, Date end){
		TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<SupervisedTrainingElement>(125, 1);
		Date tempday = start;
		Date nextday = start;
		do{
			tempday = nextday;
			nextday = nextday(tempday);
			double[] inputX = getInputX(code,tempday,nextday);
			double inputy = getInputY(code, nextday);
			if(inputy != 2){
				double[] inputY = new double[]{inputy};
				trainingSet.addElement(new SupervisedTrainingElement(inputX, inputY));
			}
		}while(nextday.before(end));
		return trainingSet;	
	}
	public Date nextday(Date date){
		Date nextday = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 
		cal.add(Calendar.DAY_OF_YEAR, 1);
		nextday = cal.getTime(); 
		return nextday;
	}
	public void nntrain(String code, Date start, Date end){
		neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH, inputnum, 4,1);
		BackPropagation learningRule = (BackPropagation) neuralNet
				.getLearningRule();
		learningRule.setLearningRate(0.3);
		learningRule.setMaxError(0.1);

		// create training set
		trainingSet = getdataset(code,start, end);
		System.out.println("--------------------get the training data------------------");
//		testingSet = getdataset(code,start, end);
		// train the network with training set
		neuralNet.learn(trainingSet);
		neuralNet.save("or_perceptron.nnet");
	}
	
	public void testvalidate() throws ParseException{
		// load the saved network
		NeuralNetwork neuralNetwork = NeuralNetwork.load("or_perceptron.nnet");
		Date start = format.parse("2014-04-01 00:00:00");
		Date nextday = start;
		for(int i =0;i<20;i++){
			Date tempdate = nextday;
			nextday = nextday(tempdate);
			
			double[] inputx = getInputX("SZ300027",start, nextday);
			// set network input
			neuralNetwork.setInput(inputx);
			// calculate network
			neuralNetwork.calculate();
			// get network output
			double[] networkOutput = neuralNetwork.getOutput();
			
			System.out.println("trainresult = "+networkOutput[0]+" realdata = "+DBControl.GetZhangfu("SZ300027", nextday));
		}
	}
	public static void main(String args[]){
		DBControl.init("xueqiu");
		NNTrain nn = new NNTrain();
		
		String KWpath = "C://Users//rushshi//Desktop//dic.txt"; 
		loadKW(KWpath);
//		System.out.println(NNTrain.keyword.length);
//		for(String n:keyword){
//			System.out.println(n);}
		
		try {
			Date start = format.parse("2014-01-01 00:00:00");
			Date end = format.parse("2014-03-23 00:00:00");
//			System.out.println(start);
//			System.out.println(end);
//			double[] i = nn.getInputX("SZ300027",start,end);
//			for(double x:i){
//				System.out.println(x);}
			nn.nntrain("SZ300027", start, end);
			nn.testvalidate();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	
}
