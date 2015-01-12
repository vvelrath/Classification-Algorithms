import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;

public class Algorithm {
	String fName = null;
	HashMap<String, Integer> diag1_Map,diag2_Map,diag3_Map,insulinMap,
							 changeMap,diabetesMedMap,metforminMap;
	HashMap<String, String> readmitted;
	BufferedReader br;
	String splitBy;
	String suffix_no;
	String suffix_lesser;
	String suffix_higher;
	int countTotal;
	int count_no;
	int count_lesser;
	int count_higher;
	//16,18,19,20,22,23,24,41,47,48
	
	//Instances in File
	List<String[]> instances = null;

	Algorithm(String fileName) {
		this.fName = fileName;
		instances = new ArrayList<String[]>();
		try {
			String line = null;
			br = new BufferedReader(new FileReader(fName));
			br.readLine();
			while ((line = br.readLine()) != null) {
				instances.add(line.split(","));
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void initializeStructures(){
		diag1_Map = new HashMap<String, Integer>();// 18
		diag2_Map = new HashMap<String, Integer>();// 19
		diag3_Map = new HashMap<String, Integer>();// 20
		
		metforminMap= new HashMap<String, Integer>();//24
		insulinMap = new HashMap<String, Integer>();// 41
		changeMap = new HashMap<String, Integer>();// 47
		diabetesMedMap = new HashMap<String, Integer>();// 48

		splitBy = ",";
		suffix_no = "_no";
		suffix_lesser = "_lesser";
		suffix_higher = "_higher";

		readmitted = new HashMap<String, String>();// 49
		readmitted.put("NO", suffix_no);
		readmitted.put("<30", suffix_lesser);
		readmitted.put(">30", suffix_higher);

		countTotal = 0;
		count_no = 0;
		count_lesser = 0;
		count_higher = 0;
	}

	void Train(List<String[]> training) {
		
		initializeStructures();
		String reSuffix = null;
		
		for (int i = 0; i < training.size(); i++) {
			String[] values = training.get(i);
			//increment counts
			countTotal++;
			reSuffix = readmitted.get(values[49]);
			
			if (reSuffix.equals(suffix_no)) {
				count_no++;
			} else if (reSuffix.equals(suffix_lesser)) {
				count_lesser++;
			} else if(reSuffix.equals(suffix_higher)){

				count_higher++;
			}
			
			//populate hash tables

			if (!diag1_Map.containsKey(values[18] + reSuffix)) {
				diag1_Map.put(values[18] + reSuffix, 0);
			} else {
				diag1_Map.put(values[18] + reSuffix,
						(diag1_Map.get(values[18] + reSuffix)) + 1);
			}

			if (!diag2_Map.containsKey(values[19] + reSuffix)) {
				diag2_Map.put(values[19] + reSuffix, 0);
			} else {
				diag2_Map.put(values[19] + reSuffix,
						(diag2_Map.get(values[19] + reSuffix)) + 1);
			}

			if (!diag3_Map.containsKey(values[20] + reSuffix)) {
				diag3_Map.put(values[20] + reSuffix, 0);
			} else {
				diag3_Map.put(values[20] + reSuffix,
						(diag3_Map.get(values[20] + reSuffix)) + 1);
			}
			if (!insulinMap.containsKey(values[41] + reSuffix)) {
				insulinMap.put(values[41] + reSuffix, 0);
			} else {
				insulinMap.put(values[41] + reSuffix,
						(insulinMap.get(values[41] + reSuffix)) + 1);
			}

			if (!changeMap.containsKey(values[47] + reSuffix)) {
				changeMap.put(values[47] + reSuffix, 0);
			} else {
				changeMap.put(values[47] + reSuffix,
						(changeMap.get(values[47] + reSuffix)) + 1);
			}

			if (!diabetesMedMap.containsKey(values[48] + reSuffix)) {
				diabetesMedMap.put(values[48] + reSuffix, 0);
			} else {
				diabetesMedMap.put(values[48] + reSuffix,
						(diabetesMedMap.get(values[48] + reSuffix)) + 1);
			}
			
			if (!metforminMap.containsKey(values[24] + reSuffix)) {
				metforminMap.put(values[24] + reSuffix, 0);
			} else {
				metforminMap.put(values[24] + reSuffix,
						(metforminMap.get(values[24] + reSuffix)) + 1);
			}	
		}
	}

	double classify(List<String[]> training, List<String[]> test) {
		Train(training);
		float prob_no=(float) count_no/countTotal;
		float prob_lesser=(float) count_lesser/countTotal;
		float prob_higher=(float) count_higher/countTotal;
		float res1,res2,res3=0.0f;
		
		double num_matching = 0;
		float num_missing = 0;
		float diag1=0, diag2=0, diag3=0, insulin=0, change=0, diabetes=0, metformin=0;
		
		for(int i = 0; i < test.size(); i++){
			
			String[] vals = test.get(i);
			TreeMap<Float, String> results=new TreeMap<Float,String>();

			// calculate probability of the input belonging to each of the three classes
			//class 1
			num_missing = 0;
			diag1=0;diag2=0;diag3=0;insulin=0;change=0;diabetes=0;metformin=0;
						
			if(diag1_Map.containsKey(vals[18]+suffix_no)){
				diag1 = (float)diag1_Map.get(vals[18]+suffix_no);
			}else{	diag1 = 1;num_missing += 1;	}
			
			if(diag2_Map.containsKey(vals[19]+suffix_no)){
				diag2 = (float)diag2_Map.get(vals[19]+suffix_no);
			}else{ 	diag2 = 1;num_missing += 1; }
			
			if(diag3_Map.containsKey(vals[20]+suffix_no)){
				diag3 = (float)diag3_Map.get(vals[20]+suffix_no);
			}else{	diag3 = 1;num_missing += 1;	}
			
			if(insulinMap.containsKey(vals[41]+suffix_no)){
				insulin = (float)insulinMap.get(vals[41]+suffix_no);
			}else{	insulin = 1;num_missing += 1;	}
			
			if(changeMap.containsKey(vals[47]+suffix_no)){
				change = (float)changeMap.get(vals[47]+suffix_no);
			}else{	change = 1;num_missing += 1;	}
	
			if(diabetesMedMap.containsKey(vals[48]+suffix_no)){
				diabetes = (float)diabetesMedMap.get(vals[48]+suffix_no);
			}else{	diabetes = 1;num_missing += 1;	}
			
			if(metforminMap.containsKey(vals[24]+suffix_no)){
				metformin = (float)metforminMap.get(vals[24]+suffix_no);
			}else{	metformin = 1; num_missing += 1;	}
		
			res1=prob_no*(diag1/(count_no+num_missing))*
						 (diag2/(count_no+num_missing))*
						 (diag3/(count_no+num_missing))*
						 (insulin/(count_no+num_missing))*
						 (change/(count_no+num_missing))*
						 (diabetes/(count_no+num_missing))*
						 (metformin/(count_no+num_missing));
			
			//class 2
			num_missing = 0;
			diag1=0;diag2=0;diag3=0;insulin=0;change=0;diabetes=0;metformin=0;
						
			if(diag1_Map.containsKey(vals[18]+suffix_lesser)){
				diag1 = (float)diag1_Map.get(vals[18]+suffix_lesser);
			}else{	diag1 = 1;num_missing += 1;	}
			
			if(diag2_Map.containsKey(vals[19]+suffix_lesser)){
				diag2 = (float)diag2_Map.get(vals[19]+suffix_lesser);
			}else{ 	diag2 = 1;num_missing += 1; }
			
			if(diag3_Map.containsKey(vals[20]+suffix_lesser)){
				diag3 = (float)diag3_Map.get(vals[20]+suffix_lesser);
			}else{	diag3 = 1;num_missing += 1;	}
			
			if(insulinMap.containsKey(vals[41]+suffix_lesser)){
				insulin = (float)insulinMap.get(vals[41]+suffix_lesser);
			}else{	insulin = 1;num_missing += 1;	}
			
			if(changeMap.containsKey(vals[47]+suffix_lesser)){
				change = (float)changeMap.get(vals[47]+suffix_lesser);
			}else{	change = 1;num_missing += 1;	}
	
			if(diabetesMedMap.containsKey(vals[48]+suffix_lesser)){
				diabetes = (float)diabetesMedMap.get(vals[48]+suffix_lesser);
			}else{	diabetes = 1;num_missing += 1;	}
			
			if(metforminMap.containsKey(vals[24]+suffix_lesser)){
				metformin = (float)metforminMap.get(vals[24]+suffix_lesser);
			}else{	metformin = 1; num_missing += 1;	}
		
			res2=prob_lesser*(diag1/(count_lesser+num_missing))*
						 (diag2/(count_lesser+num_missing))*
						 (diag3/(count_lesser+num_missing))*
						 (insulin/(count_lesser+num_missing))*
						 (change/(count_lesser+num_missing))*
						 (diabetes/(count_lesser+num_missing))*
						 (metformin/(count_lesser+num_missing));
			
			//class 3
			num_missing = 0;
			diag1=0;diag2=0;diag3=0;insulin=0;change=0;diabetes=0;metformin=0;
						
			if(diag1_Map.containsKey(vals[18]+suffix_higher)){
				diag1 = (float)diag1_Map.get(vals[18]+suffix_higher);
			}else{	diag1 = 1;num_missing += 1;	}
			
			if(diag2_Map.containsKey(vals[19]+suffix_higher)){
				diag2 = (float)diag2_Map.get(vals[19]+suffix_higher);
			}else{ 	diag2 = 1;num_missing += 1; }
			
			if(diag3_Map.containsKey(vals[20]+suffix_higher)){
				diag3 = (float)diag3_Map.get(vals[20]+suffix_higher);
			}else{	diag3 = 1;num_missing += 1;	}
			
			if(insulinMap.containsKey(vals[41]+suffix_higher)){
				insulin = (float)insulinMap.get(vals[41]+suffix_higher);
			}else{	insulin = 1;num_missing += 1;	}
			
			if(changeMap.containsKey(vals[47]+suffix_higher)){
				change = (float)changeMap.get(vals[47]+suffix_higher);
			}else{	change = 1;num_missing += 1;	}
	
			if(diabetesMedMap.containsKey(vals[48]+suffix_higher)){
				diabetes = (float)diabetesMedMap.get(vals[48]+suffix_higher);
			}else{	diabetes = 1;num_missing += 1;	}
			
			if(metforminMap.containsKey(vals[24]+suffix_higher)){
				metformin = (float)metforminMap.get(vals[24]+suffix_higher);
			}else{	metformin = 1; num_missing += 1;	}
		
			res3=prob_higher*(diag1/(count_higher+num_missing))*
						 (diag2/(count_higher+num_missing))*
						 (diag3/(count_higher+num_missing))*
						 (insulin/(count_higher+num_missing))*
						 (change/(count_higher+num_missing))*
						 (diabetes/(count_higher+num_missing))*
						 (metformin/(count_higher+num_missing));
			
			results.put(res1, "NO");
			results.put(res2, "<30");
			results.put(res3, ">30");
			
			if(results.get(results.lastKey()).equals(vals[49])){
				num_matching++;
			}
		}
		
		return num_matching/test.size(); 
	}//end func
	
	
	//Cross Validation function
	void crossValidation(){
		double accuracy = 0;
		double total_accuracy = 0;
		Collections.shuffle(instances);
		int num_rows = instances.size();
		List<String[]> testing = null;
		List<String[]> training = null;
		
		System.out.println("----------------Five Fold cross validation------------------");
		
		for(int i = 0; i < 5; i++){
			if(i > 0) training = new ArrayList<String[]>(instances.subList(0, (i*num_rows/5) - 1));
			testing = new ArrayList<String[]>(instances.subList((i*num_rows/5), ((i+1)*num_rows/5) - 1));
			if(i < 4) {
				if(training != null)
					training.addAll(new ArrayList<String[]>(instances.subList((i + 1)*num_rows/5, instances.size() - 1)));
				else
					training = new ArrayList<String[]>(instances.subList((i + 1)*num_rows/5, instances.size() - 1));
			}	
			
			accuracy = classify(training, testing);
			System.out.println("Classification Accuracy "+ (i+1) + ": "+(double)Math.round(accuracy * 100 * 100)/100 + "%");			
			total_accuracy += accuracy;
		}
		
		System.out.println();
		System.out.println("Average Accuracy of Naive Bayes: "+(double)Math.round(total_accuracy * 100)/5 + "%");
	}
}	
