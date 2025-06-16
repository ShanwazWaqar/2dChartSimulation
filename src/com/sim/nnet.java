package com.sim;

import java.io.*;
import java.util.*;

public class nnet {
	public static String[] params;
	public static void main(String args[]) throws Exception {
		
		params = args;
/*		params = new String[10];
		System.out.println("params 7:"+params[7]);
		System.out.println("params 1:"+params[0]);
		System.out.println("params 2:"+params[1]);
		System.out.println("params 3:"+params[2]);
		
		
		params[7] = "test";
		params[0] = "3";
		params[1] = "1";
		params[2] = "3";
		params[3] = "15";
		params[4] = "4";
		params[5] = "37";
		params[6] = "100";
		params[8] = System.getProperty ("user.dir")+File.separator;
		
*/		 System.out.println(System.getProperty ("user.dir"));  
		
		Sda sda=new Sda(params);
		String inputfile = null;
		char choice, choice_dis;
		int choice1= '1';
		int status1, status2;
		boolean Stop;
		float MinTrainError;
		int n,i,j;
		sda.GenerateNetwork();


		//BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		//BufferedReader in = new BufferedReader(System.in); // change made by bhavana
		
		//System.out.println("\nEnter choice of Learning / Testing / Inverse Map (l/t/i) : ");
		//@		read choice
		//choice1 = System.in.read();
		//choice='l';				//remove this when reading "choice"
		//if(choice=='l') {
		if(params[7].equals("train")) {
			
			sda.fpt2=new File(sda.serverpath+"nnet_sia/EFile.txt");
			sda.fpt3= new File(sda.serverpath+"nnet_sia/WFile.txt");
			sda.fpt5=new File(sda.serverpath+"nnet_sia/WtFile.txt");
			sda.fpt1=new File(sda.serverpath+"nnet_sia/results.txt");
			sda.fpt1.delete();
			
			sda.fpt2o=new FileWriter(sda.fpt2);
			sda.fpt3o=new FileWriter(sda.fpt3);
			sda.fpt5o=new FileWriter(sda.fpt5);
			sda.fpt3i=new FileReader(sda.fpt3);
			sda.fpt3buffer=new BufferedReader(sda.fpt3i);
			sda.e=sda.fpt3buffer.readLine();

			System.out.println("Training data");
			Stop=false;
			MinTrainError=sda.MAX_REAL;
			sda.RandomWeights();
			System.out.println("\n************Running Back Propagation Network****************\n");
			System.out.println("\nEnter the training set file name : traindata.txt");
			//@			read 'inputfile'
			inputfile="traindata.txt";
			//inputfile = console.readLine();
			n=0;
			do {
				sda.TrainNet(n);
				//System.out.println(sda.TrainError+"\t"+MinTrainError+"\t"+sda.MinErr);

				if(sda.TrainError<MinTrainError) {
					sda.SaveWeights();
					MinTrainError=sda.TrainError;
					if(sda.TrainError<sda.MinErr) {
						sda.RestoreWeights();
						Stop=true;
					}
				}
				else if((sda.TrainError>(1.5*MinTrainError))||(sda.TrainError<sda.MinErr)) {
					sda.RestoreWeights();
					Stop=true;
				}
				n++;
				if (n==500)   // no of iterations for efile  ?? why 30 check and change accordingly
					Stop=true;
			}while(!Stop);
			sda.WriteWeights();
			sda.TestNet();

			sda.fpt2o.close();
			sda.fpt3o.close();
			sda.fpt5o.close();

		}
		//else if(choice=='t') {
		else if(params[7].equals("test")) {
			
			sda.fpt1=new File(sda.serverpath+"nnet_sia/results.txt");	
			sda.fpt4=new File(sda.serverpath+"nnet_sia/OpError.txt");
			sda.fpt7=new File(sda.serverpath+"nnet_sia/DWFile.txt");
			
			System.out.println("\nEnter the testing set file name:testdata.txt ");
			//inputfile = console.readLine();
			//@			read inputfile

			//inputfile="traindata.txt";
			System.out.println("\nEnter the results file name:results.txt ");
			//@			read inputfile


			sda.ReadWeights();
			sda.RestoreWeights();
			System.out.println("\nDo you want to disturb weights? (Y/N) :n");
			//@			read choice_dis
			choice_dis='n';			//remove this line when reading 'choice_dis'
			sda.EvaluateNet();
			
			if(choice_dis=='y')
				sda.EvaluateNet();
		}

		/*		else if(choice=='i') {
		 sda.Optimize_Options();
		 sda.InverseMap(sda.n_runs,sda.m);
		 for(i=1;i<=sda.FLAGS;i++)
		 System.out.println("\n Desired Output ["+i+"] = "+sda.OptiVector[sda.FEATURES+i]);
		 }
		 */
	}
}
class LAYER {
	int Units;
	float Output[];
	float Error[];
	float Weight[][];
	float WeightSave[][];
	float dWeight[][];
}
class NET {
	LAYER Layer[];
	float Alpha;
	float Eta;
	float Gain;
	float Error;
}
class Sda {
	float Disturbance;

	File traindata, testdata, fpt1, fpt2, fpt3, fpt4, fpt5, fpt7;
	FileWriter fpt2o,fpt3o,fpt5o;
	FileReader traindatai, fpt3i,testdatai;
	BufferedReader trainbuffer, fpt3buffer, testbuffer;

	int VECTDIM, n_runs, m, BIAS;
	int opt[], Units[];
	NET Net;
	float TrainMatrix[][],TestMatrix[][];
	
	float dWeight[], vector[], dOutput[][], dSum[][], dOut[][], OptiVector[], MinVector[], MaxVector[];
	float Max_Err, TrainError, MIN_REAL, MAX_REAL;
	long RAND_MAX;
	
	//data in para.h	start
	int FEATURES, FLAGS, NUM_LAYERS, M, N, MID_NODES1, MID_NODES2, N_Traindata,N_Testdata;
	float N_Epochs, ALPHA, ETA, GAIN, MinErr;
	//data in para.h	end

	Random random;

	String d,e,f, serverpath;
	StringTokenizer st,ss,su;

	public Sda(String params[]) throws Exception {
		//values in para.h	start
		//FEATURES=2;   // Number of Input Nodes  // change made by bhavana
		
		System.out.println("In sda");
		FEATURES = Integer.parseInt(params[0]);
		//FLAGS=1;      // Number of Output Nodes // change made by bhavana
		FLAGS= Integer.parseInt(params[1]);
		//NUM_LAYERS=3;  //Total Number of Layers in the NN
		NUM_LAYERS= Integer.parseInt(params[2]);
		N=FEATURES;
		M=FLAGS;
		N_Epochs=105770;
		//MID_NODES1=15;  //Number of Nodes in the First Hidden Layer
		MID_NODES1= Integer.parseInt(params[3]);
		//MID_NODES2=4;  // Number of Nodes in the Second Hidden Layer
		MID_NODES2= Integer.parseInt(params[4]);

		ALPHA=(float)0.7506;  //Momentum Factor 
		ETA=(float)0.57861;   //Learning Rate

		GAIN=1;                 //Gain of the Network
		//N_Traindata=36;         //Number of data being provided in the Training Set // change made by bhavana earlier it was 300
		N_Traindata= Integer.parseInt(params[5]);
		
		//N_Testdata=300;
		N_Testdata= Integer.parseInt(params[6]);
		MinErr=(float)0.001;      //Minimum Mean Square Error Desired before stopping training
		//values in para.h	end
		
		serverpath = params[8];
		
		Disturbance=(float)0.3;		//Disturbance 30%
		
		traindata=new File(serverpath+"upload/traindata.txt");
		
		testdata=new File(serverpath+"upload/testdata.txt");
		System.out.println("params[7]:"+params[7]);
/*		fpt2=new File(serverpath+"nnet_sia/EFile.txt");
		fpt3=new File(serverpath+"nnet_sia/WFile.txt");
		fpt5=new File(serverpath+"nnet_sia/WtFile.txt");

		fpt2o=new FileWriter(fpt2);
		fpt3o=new FileWriter(fpt3);
		fpt5o=new FileWriter(fpt5);
		fpt3i=new FileReader(fpt3);
		fpt3buffer=new BufferedReader(fpt3i);
		e=fpt3buffer.readLine();*/

	
/*		fpt1=new File(serverpath+"nnet_sia/results.txt");	
		fpt4=new File(serverpath+"nnet_sia/OpError.txt");
		fpt7=new File(serverpath+"nnet_sia/DWFile.txt");*/

		//fpt2.delete();
		//fpt3.delete();
		//fpt5.delete();

		
		traindatai=new FileReader(traindata);
		trainbuffer=new BufferedReader(traindatai);
		d=trainbuffer.readLine();
		//System.out.println("d:"+d);
		st=new StringTokenizer(d);

		//ss=new StringTokenizer(e);

		testdatai=new FileReader(testdata);
		testbuffer=new BufferedReader(testdatai);
		f=testbuffer.readLine();

		VECTDIM=FEATURES + FLAGS;
		opt=new int[FEATURES+2];
		dWeight=new float[FEATURES];
		TrainMatrix=new float[N_Traindata][VECTDIM];
		TestMatrix = new float[N_Testdata][FEATURES];
		vector=new float[FEATURES];

		dOutput=new float[FLAGS][FEATURES];
		dSum=new float[FLAGS][FEATURES];
		dOut=new float[FLAGS][FEATURES];

		OptiVector=new float[VECTDIM+2];                //Input Vector for optimizing
		MinVector=new float[VECTDIM+1];
		MaxVector=new float[VECTDIM+1];
		Units=new int[3];Units[0]=N;Units[1]=MID_NODES1;Units[2]=M;

		MIN_REAL=Float.NEGATIVE_INFINITY;
		MAX_REAL=Float.POSITIVE_INFINITY;

		BIAS=1;

		RAND_MAX=2147483647;		//defined in C
		Net=new NET();

		random=new Random(8049);
	}
	public void GenerateNetwork() {
		int l, i;
		Net.Layer=new LAYER[NUM_LAYERS];
		for(l=0; l<NUM_LAYERS; l++) {
			Net.Layer[l]=new LAYER();
			Net.Layer[l].Units=Units[l];
			Net.Layer[l].Output=new float[Units[l]+1];
			Net.Layer[l].Error=new float[Units[l]+1];
			Net.Layer[l].Weight=new float[Units[l]+1][Units[l]+2];
			Net.Layer[l].WeightSave=new float[Units[l]+1][Units[l]+2];
			Net.Layer[l].dWeight=new float[Units[l]+1][Units[l]+2];
			Net.Layer[l].Output[0]=BIAS;
			if (l!=0) {
				for (i=1;i<=Units[l];i++) {
					Net.Layer[l].Weight=new float[Units[l]+1][Units[l-1]+1];
					Net.Layer[l].WeightSave=new float[Units[l]+1][Units[l-1]+1];
					Net.Layer[l].dWeight=new float[Units[l]+1][Units[l-1]+1];
				}
			}
		}
		Net.Alpha=ALPHA;
		Net.Eta=ETA;
		Net.Gain=GAIN;
		Net.Error=0;
	}
	public void RandomWeights() {
		int l, i, j, k;
//		for (l=1;l<NUM_LAYERS;l++)
//			for(i=1;i<=Net.Layer[l].Units;i++)
//				for(j=1;j<=Net.Layer[l-1].Units;j++)
//					Net.Layer[l].Weight[i][j]=(float)(random.nextDouble()/RAND_MAX-0.5);
		double temp1[]={0.013871, -0.324274, -0.191366, 0.034532, 0.447630, -0.328272, 0.202231, -0.273583, -0.005234, -0.375301, -0.416105, -0.110370, -0.222770, -0.131947, 0.483459, 0.035386, 0.265679, 0.146474, 0.267144, 0.280236, 0.322962, -0.348079, 0.125477, -0.185324, -0.153096, 0.417203, 0.019761, -0.098834, 0.106769, 0.285424, 0.431547, 0.369930, 0.366543, 0.174520, 0.258415, 0.081896, -0.110767, -0.144368, -0.299768, 0.326930, -0.084094, -0.036485, 0.479186, -0.373562, -0.287378, 0.458464, 0.237480, -0.090960, 0.280114, 0.257897, 0.456847, -0.471923, -0.181265, 0.256951, -0.257012, 0.089557, -0.456603, 0.456053, -0.180868, -0.440641, -0.058123, 0.415036, 0.072253, -0.381161, 0.069781, -0.247948, -0.004135, -0.263268, -0.023026, -0.093921, 0.373012, -0.073046, -0.141774, -0.118030, -0.456847, -0.339412, 0.022355, 0.196585, -0.402921, -0.099170, 0.273431, -0.255181, -0.157186, -0.270012, -0.202139, -0.195456, 0.387204, -0.463347, 0.151143, -0.101398, 0.176290, 0.232597, 0.437803, -0.266717, 0.338496, 0.467223, 0.278649, -0.068499, 0.174093, 0.309381, -0.341243, -0.220115, -0.364681, 0.364193, 0.250206};
		// change made by bhavana the random weights array size changes from 105 to 45
	//	double temp[]={0.013871,-0.324274,-0.191366,0.034532,0.44763,-0.328272,0.202231,-0.273583,-0.005234,-0.375301,-0.416105,-0.11037,-0.22277,-0.131947,0.483459,0.035386,0.265679,0.146474,0.267144,0.280236,0.322962,-0.348079,0.125477,-0.185324,-0.153096,0.417203,0.019761,-0.098834,0.106769,0.285424,0.431547,0.36993,0.366543,0.17452,0.258415,0.081896,-0.110767,-0.144368,-0.299768,0.32693,-0.084094,-0.036485,0.479186,-0.373562,-0.287378};
		double temp[]=new double[MID_NODES1*VECTDIM];
		for(k=1;k<temp.length;k++){
			temp[k] = temp1[k];
		}

		int a=0;
		for (l=1;l<NUM_LAYERS;l++)
			for(i=1;i<=Net.Layer[l].Units;i++)
				for(j=1;j<=Net.Layer[l-1].Units;j++)
					Net.Layer[l].Weight[i][j]=(float)temp[a++];
	}
	
	
	public void TrainNet(int n) throws Exception {
		int i,a;
		float MinTrainError,Prev_Err;
		float Output[]=new float[M];
		TrainError=0;
		Max_Err=0;
		MinTrainError=Float.POSITIVE_INFINITY;
		Prev_Err=0;
		FillTrainMatrix();

		float temp1[],temp2[];
		temp1=new float[VECTDIM];
		temp2=new float[FLAGS];
		for(i=0;i<N_Traindata;i++) {
			for(a=0;a<VECTDIM;a++) {
				temp1[a]=TrainMatrix[i][a];
			}
			for(a=FEATURES;a<VECTDIM;a++) {
				temp2[a-FEATURES]=TrainMatrix[i][a];
			}
			SimulateNet(temp1,Output,temp2,true,false,0);				//%%%%%%%%

			Max_Err=Math.max(Net.Error,Max_Err);
			TrainError=Max_Err;
		}
		Prev_Err=Max_Err;
		
		fpt2o.write(n+","+Max_Err+"\n"); // writing Into EFile the values
		if(TrainError<MinTrainError)
			MinTrainError=TrainError;
	}
	public void FillTrainMatrix() throws Exception {
		// change made by bhavana
		int i,j;   
		float doub[]=new float[N_Traindata*VECTDIM]; // no of row * columns traindatafile earlier it was 2100 and train data 300 * (3+4)
		for(i=0;i<doub.length;i++) {
			//System.out.println("doub[i]:"+doub[i]);
			doub[i]=Float.valueOf(st.nextToken().toString()).floatValue(); 
			if(st.hasMoreTokens()==false) { 
				d=trainbuffer.readLine();
				try {
					st=new StringTokenizer(d);
				}catch(Exception e) {}
			}
		}
		int c=0;

		for(i=0;i<N_Traindata;i++)
			for(j=0;j<VECTDIM;j++) {
				TrainMatrix[i][j]=doub[c];
				c++;
			}
		traindatai=new FileReader(traindata);
		trainbuffer=new BufferedReader(traindatai);
		d=trainbuffer.readLine();
		st=new StringTokenizer(d);
	}
	
	public void SimulateNet(float Input[], float Output[], float Target[], boolean Training, boolean Optimize, int n) {
		TestSimNet(Input,Output);
		if(Training) {
			//System.out.println("If training");
			ComputeOutputError(Target);
			BackPropagateNet();
			AdjustWeights();
		}
		else {
			if(Optimize) {
				//System.out.println("If optimizing");
				ComputeOutputError(Target);
				BackPropagateNet();
				AdjustInputs();
			}
			else {
				ComputeOutputError(Target);
				BackPropagateNet();
			}
		}
	}
	public void TestSimNet(float Input[], float Output[]) {
		SetInput(Input);
		PropagateNet();
		GetOutput(Output);
	}
	public void SetInput(float Input[]) {
		int i;
		for(i=1;i<=Net.Layer[0].Units;i++){
			Net.Layer[0].Output[i]=Input[i-1];
			//System.out.println("Net.Layer[0].Output[i]:" +Net.Layer[0].Output[i]);
		}
	}
	public void PropagateNet() {
		int l;
		for(l=0;l<NUM_LAYERS-1;l++)
			PropagateLayer(l,l+1);
	}
	public void PropagateLayer(int l, int u) {
		int i,j;
		float Sum;

		for(i=1;i<=Net.Layer[u].Units;i++) {
			Sum=0;
			for(j=1;j<=Net.Layer[l].Units;j++)
				Sum+=Net.Layer[u].Weight[i][j]*Net.Layer[l].Output[j];
			Net.Layer[u].Output[i]=(float)(1/(1+Math.exp(-Net.Gain*Sum)));
			//System.out.println("Net.Layer[u].Output[i]::"+Net.Layer[u].Output[i]);
		}
	}
	public void GetOutput(float Output[]) {
		int i;
		for(i=1;i<=Net.Layer[NUM_LAYERS-1].Units;i++){
			Output[i-1]=Net.Layer[NUM_LAYERS-1].Output[i];
			//System.out.println(Output[i-1]);
		}
	}
	public void ComputeOutputError(float Target[]) {
		int i;
		float Out, Err;
		Net.Error=0;
		for(i=1;i<=Net.Layer[NUM_LAYERS-1].Units;i++) {
			Out=Net.Layer[NUM_LAYERS-1].Output[i];
			Err=Target[i-1]-Out;
			Net.Layer[NUM_LAYERS-1].Error[i]=Net.Gain*Out*(1-Out)*Err;
			Net.Error+=0.5*Err*Err;
		}
	}
	public void BackPropagateNet() {
		int l;
		for(l=NUM_LAYERS-1;l>0;l--)
			BackPropagateLayer(l,l-1);
	}
	public void BackPropagateLayer(int u, int l) {
		int i,j;
		float Out,Err;
		for(i=1;i<=Net.Layer[l].Units;i++) {
			Err=0;
			Out=Net.Layer[l].Output[i];
			for(j=1;j<=Net.Layer[u].Units;j++)
				Err+=Net.Layer[u].Weight[j][i]*Net.Layer[u].Error[j];
			Net.Layer[l].Error[i]=Net.Gain*Out*(1-Out)*Err;
		}
	}
	public void AdjustWeights() {
		int l,i,j;
		float Out,Err,dWeight;
		for(l=1;l<NUM_LAYERS;l++)
			for(i=1;i<=Net.Layer[l].Units;i++)
				for(j=1;j<=Net.Layer[l-1].Units;j++) {
					Out=Net.Layer[l-1].Output[j];
					Err=Net.Layer[l].Error[i];
					dWeight=Net.Layer[l].dWeight[i][j];
					Net.Layer[l].Weight[i][j]+=Net.Eta*Err*Out+Net.Alpha*dWeight;
					Net.Layer[l].dWeight[i][j]=Net.Eta*Err*Out;
				}
	}
	public void AdjustInputs() {
		int l,i,j;
		float Out,Err;
		for(j=1;j<=FEATURES;j++) {
			if(opt[j]==1) {
				Out=OptiVector[j];
				Err=Net.Layer[0].Error[j];
				if(OptiVector[j]<=MinVector[j] && OptiVector[j]<=MaxVector[j]) {
					Net.Layer[0].Output[j]+=Net.Eta*Err*Out+Net.Alpha*dWeight[j];
					OptiVector[j]+=Net.Eta*Err*Out+Net.Alpha*dWeight[j];
					if((j==10)||(j==11)) {
						OptiVector[j]+=Math.round(400*Net.Eta*Err*Out)+Math.round(400*Net.Alpha*dWeight[j]);
						OptiVector[j]=Math.round(OptiVector[j]);
					}
					dWeight[j]=Net.Eta*Err*Out;
				}
			}
		}
	}
	public void SaveWeights() {
		int l,i,j;
		for(l=1;l<NUM_LAYERS;l++)
			for(i=1;i<=Net.Layer[l].Units;i++)
				for(j=1;j<=Net.Layer[l-1].Units;j++)
					Net.Layer[l].WeightSave[i][j]=Net.Layer[l].Weight[i][j];
	}
	public void RestoreWeights() {
		int l,i,j;
		for(l=1;l<NUM_LAYERS;l++)
			for(i=1;i<=Net.Layer[l].Units;i++)
				for(j=1;j<=Net.Layer[l-1].Units;j++)
					Net.Layer[l].Weight[i][j]=Net.Layer[l].WeightSave[i][j];
	}
	public void WriteWeights() throws Exception {
		int l,i,j;
		for(l=1;l<NUM_LAYERS;l++) {
			fpt5o.write("Weights from ["+l+"] layer to ["+(l+1)+"] layer\n"); // Writing to WtFile 
			for(i=1;i<=Net.Layer[l].Units;i++) {
				for(j=1;j<=Net.Layer[l-1].Units;j++) {
					fpt3o.write(Net.Layer[l].WeightSave[i][j]+"\n");
					fpt5o.write(Net.Layer[l].WeightSave[i][j]+"\t");
				}
				fpt5o.write("\n");
			}
		}
	}
	public void TestNet() throws Exception {
		int a;
		float Output[]=new float[M];
		Max_Err=0;
		FillTrainMatrix();
		float temp1[],temp2[];
		temp1=new float[VECTDIM];
		temp2=new float[FLAGS];
		for(int i=0;i<N_Traindata;i++) {
			for(a=0;a<VECTDIM;a++){
				temp1[a]=TrainMatrix[i][a];
				//System.out.println("temp1[a]:"+temp1[a]);
			}
			for(a=FEATURES;a<VECTDIM;a++){
				temp2[a-FEATURES]=TrainMatrix[i][a];
				//System.out.println("temp2[a-features]:"+temp2[a-FEATURES]);
			}
			SimulateNet(temp1,Output,temp2,false,false,0);
			Max_Err=Math.max(Max_Err,Net.Error);
		}
		//System.out.println("Maximum error for testing set = "+Max_Err);
	}
	public void ReadWeights() throws Exception {
//		int l,i,j;
//		String s;
//		for(l=1;l<NUM_LAYERS;l++) {
//			for(i=1;i<=Net.Layer[l].Units;i++) {
//				for(j=1;j<=Net.Layer[l-1].Units;j++) {
//				s=buf.readLine();
//					Net.Layer[l].WeightSave[i][j]=Float.valueOf(s).floatValue();
//				}
//			}
//		}
		
		int i;
		double doub[]=new double[MID_NODES1*VECTDIM];  // change made by bhavana - the array size changed from 105 to 45
		//System.out.print("In read:"+doub.length);
		for(i=0;i<doub.length;i++) {
			 // changed from ss to st as it gave error
			doub[i]=Float.valueOf(st.nextToken().toString()).floatValue();
		//	System.out.print("doub[i]:"+doub[i]);
			if(st.hasMoreTokens()==false) {
				d=trainbuffer.readLine();
		//		System.out.println("d in readweights:"+d);
				try {
					st=new StringTokenizer(d);
				}catch(Exception e) {}
			}
		}
		int l,j,c=0;

		for(l=1;l<NUM_LAYERS;l++) {
			for(i=1;i<=Net.Layer[l].Units;i++) {
				for(j=1;j<=Net.Layer[l-1].Units;j++) {
					Net.Layer[l].WeightSave[i][j]=(float)doub[c];
				}
			}
		}
	}
	public void EvaluateNet() throws Exception {
		System.out.println("In Evaulate net");
		float Output[]=new float[M];
		float temp1[];
		int i,c=0;
		int j;
		su=new StringTokenizer(f);
		FileWriter fpt1o=new FileWriter(fpt1);
		// Why 195 ? check and change accordingly
		FillTestVector();
		temp1=new float[FEATURES];
	
		for(i=0;i<N_Testdata;i++) {
			for(int a=0;a<FEATURES;a++) {
				temp1[a]=TestMatrix[i][a];
				//System.out.println("temp1[a]:"+temp1[a]);
			}
			TestSimNet(temp1,Output);
			
			for(j = 0;j <FLAGS;j++){
			fpt1o.write(Output[j]+"\t"); // writing to WFile
			}
			fpt1o.write("\n");
			
		}
		
		/*for (i=0 ;i<N_Testdata;i++){
			//FillTestVector();
			for(j=0;j<FEATURES;j++) { // output is 1
				vector[j]=Float.valueOf(su.nextToken().toString()).floatValue();
				System.out.println("vector[j]:"+vector[j]);
			}
			
			
			TestSimNet(vector,Output);
		}*/
		/*while(c<37) {   
			c++;
			//FillTestVector();
			if(c==36)
				System.out.println("END\n");
			//TestSimNet(vector,Output);
			fpt1o.write("\n");
			
			for(i=0;i<M;i++)
			{
				fpt1o.write(Output[i]+"\t"); // writing to WFile
				
			}
		}*/
		fpt1o.close();
	}
	public void FillTestVector() throws Exception {
		float Output[]=new float[M];
		/*int j;
		su=new StringTokenizer(f);
		for(j=0;j<FEATURES;j++) { // output is 1
			vector[j]=Float.valueOf(su.nextToken().toString()).floatValue();
			System.out.println("vector[j]:"+vector[j]);
		}*/
		int i,j;   
		float doub[]=new float[N_Testdata*FEATURES]; // no of row * columns traindatafile earlier it was 2100 and train data 300 * (3+4)
		for(i=0;i<doub.length;i++) {
			//System.out.println("doub[i]:"+doub[i]);
			doub[i]=Float.valueOf(su.nextToken().toString()).floatValue();
			//System.out.println("doub[i]:"+doub[i]);
			if(su.hasMoreTokens()==false) { 
				f=testbuffer.readLine();
				try {
					su=new StringTokenizer(f);
				}catch(Exception e) {}
			}
			//TestSimNet(doub,Output);
		}
		int c=0;

		for(i=0;i<N_Testdata;i++)
			for(j=0;j<FEATURES;j++) {
				TestMatrix[i][j]=doub[c];
				c++;
			}
		testdatai=new FileReader(testdata);
		testbuffer=new BufferedReader(testdatai);
		f=testbuffer.readLine();
		su=new StringTokenizer(f);
		
	}
}
/*	public void Optimize_Options() throws Exception {
 int i, j;
 ReadWeights();
 for(j=1;j<=FLAGS;j++) {
 System.out.println("Enter value of the output desired["+m+"] :");
 //@			read OptiVector[FEATURES+j]
 }
 for(i=1;i<=FEATURES;i++) {
 System.out.println("Do you want to optimize input # "+i+" (1 for YES; 0 for NO) :");
 //@			read opt[i]
 if(opt[i]==0) {
 System.out.println(" Enter the value of the input # "+i+" :");
 //@				read OptiVector[i]
 }
 else if(opt[i]==1) {
 OptiVector[i]=(float)0.5;
 if(i==9)
 OptiVector[i]=(float)0.99;
 if(i==10||i==11)
 OptiVector[i]=1;
 }
 }
 for(i=1;i<=FEATURES;i++) {
 MaxVector[i]=(float)0.9;
 MinVector[i]=(float)0.2;
 dWeight[i]=0;
 }
 MinVector[9]=(float)0.3;
 MinVector[10]=0;
 MinVector[11]=0;
 MaxVector[9]=(float)1.0;
 MaxVector[10]=4;
 MaxVector[11]=5;
 MaxVector[12]=1;
 MaxVector[13]=1;
 System.out.println(" How many runs :");
 //@		read n_runs
 System.out.println("**********************************");
 RestoreWeights();
 for(i=1;i<=FEATURES;i++)
 System.out.println(" Input # "+i+" : "+OptiVector[i]);
 }
 public void InverseMap(int n_runs, int m) throws Exception {
 int n,i,a;
 float Output[]=new float[M+1];
 Max_Err=MAX_REAL;
 n=0;
 fpt4o=new FileWriter(fpt4);
 do {
 float temp1[],temp2[];
 temp1=new float[VECTDIM+1];
 temp2=new float[VECTDIM-FEATURES+1];
 for(a=1;a<VECTDIM+2;a++)
 temp1[a-1]=OptiVector[a];
 for(a=FEATURES+1;a<VECTDIM+2;a++)
 temp2[FEATURES+1-a]=OptiVector[a];
 SimulateNet(temp1,Output,temp2,false,true,m);
 Max_Err=Math.min(Net.Error, Max_Err);
 if(n%100==0) {
 fpt4o.write("\n"+n+"\t"+Max_Err);
 for(i=1;i<=FEATURES;i++)
 fpt4o.write("\t"+Net.Layer[0].Output[i]);
 for(i=1;i<FLAGS;i++)
 fpt4o.write("\t"+Net.Layer[NUM_LAYERS-1].Output[i]);
 }
 n++;
 }while(Max_Err>0.0000000005&&n<n_runs);
 }
 }
 */
