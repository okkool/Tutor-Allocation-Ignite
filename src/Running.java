import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Running {
	
	public static void main(String[] args) throws InvalidAlgorithmParameterException {

		Volunteer	[] volunteers;
		Classroom	[] classrooms;
		String 		[] Skills;
		int number_skills;
		int number_classrooms;
		int number_volunteers;

		try {
			FileInputStream fileInputStream = new FileInputStream("Book1.xls");
			@SuppressWarnings("resource")
			HSSFWorkbook workbook1 = new HSSFWorkbook(fileInputStream);

			number_skills 		= (int) workbook1.getSheetAt(0).getRow(1).getCell(0).getNumericCellValue();
			number_classrooms 	= (int) workbook1.getSheetAt(0).getRow(3).getCell(0).getNumericCellValue();
			number_volunteers 	= (int) workbook1.getSheetAt(0).getRow(5).getCell(0).getNumericCellValue();

			System.out.println("There are "+number_skills+" different skills");

			Skills 		= new String 	[number_skills	  ];
			volunteers	= new Volunteer [number_volunteers];
			classrooms	= new Classroom	[number_classrooms];

			for (int i = 0; i < number_skills	 ; i++) {
				Skills [i]     = workbook1.getSheetAt(0).getRow(1).getCell(1+i).getStringCellValue();
				System.out.println(Skills [i]);
			}
			System.out.println("There are "+number_classrooms+" different classrooms");
			for (int i = 0; i < number_classrooms; i++) {
				String school_name = workbook1.getSheetAt(1).getRow(1+2*i).getCell(0).getStringCellValue();
				String class_name  = workbook1.getSheetAt(1).getRow(1+2*i).getCell(1).getStringCellValue();
				int max_size = (int) workbook1.getSheetAt(1).getRow(2+2*i).getCell(1).getNumericCellValue();

				int [] 		min_skill_value = new int [number_skills];
				int [] 		min_skill_size  = new int [number_skills];
				for (int j = 0; j < number_skills	 ; j++) {
					min_skill_value [j]     = (int) workbook1.getSheetAt(1).getRow(1+2*i).getCell(3+j).getNumericCellValue();
					min_skill_size	[j]		= (int) workbook1.getSheetAt(1).getRow(2+2*i).getCell(3+j).getNumericCellValue();
				}
				classrooms [i] = new Classroom(school_name, class_name, Skills, min_skill_value, min_skill_size, max_size);
				System.out.println(i+", "+classrooms [i].toString());
			}
			System.out.println("There are "+number_volunteers+" different Volunteers");
			for (int i = 0; i < number_volunteers; i++) {
				String name  = workbook1.getSheetAt(2).getRow(1+i).getCell(0).getStringCellValue();
				String email = workbook1.getSheetAt(2).getRow(1+i).getCell(1).getStringCellValue();
				int [] 	skill = new int [number_skills];
				for (int j = 0; j < number_skills	 ; j++) {
					skill [j]     = (int) workbook1.getSheetAt(2).getRow(1+i).getCell(2+j).getNumericCellValue();
				}
				int [] 	preferences = new int [number_classrooms];

				for (int j = 0; j < number_classrooms	 ; j++) {
					preferences [j]     = (int) workbook1.getSheetAt(2).getRow(1+i).getCell(2+number_skills+j).getNumericCellValue();
				}
				/*
				boolean [] seen = new boolean [number_classrooms];
				int 	[] temp = new int 	  [number_classrooms];
				
				int l = 1;
				for (int j = 0; j < number_classrooms	 ; j++) {
					int min = 0 ;
					int min_index = 0 ;
					for (int k = 0; k < seen.length; k++) {
						if(!seen[k]){
							min = preferences[k];
							min_index = k ;
							break;
						}
					}
					for (int k = 0; k < seen.length; k++) {
						if(!seen[k]){
							if(min>preferences[k]){
								min = preferences[k];
								min_index = k;
							}
						}
					}
					if(min<=0){
						preferences[min_index] = -1;
						seen[min_index] = true;
					}else{
						preferences[min_index] = l++;
						temp [l-2]= min_index;
						seen[min_index] = true;
					}
					
				}
				for (int j = l; j < temp.length; j++) {
					temp [j] = -1 ; 
				}
				//*///
				volunteers[i] = new Volunteer(name, email, Skills, skill, preferences);
				System.out.println(i+"\t "+volunteers[i].toString());
			}
			//input done 




			boolean [][] asked = new boolean [number_classrooms][number_volunteers];
			boolean end [] = new boolean [number_classrooms];
			while(askme(asked,classrooms,volunteers,number_skills,end));
			print_sets(classrooms, volunteers, number_skills);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static int [] get_demand(Classroom	[] classrooms, int number_skills ) {
		int [] skill_demand = new int [number_skills];
		for (int i = 0; i < classrooms.length; i++) {
			for (int j = 0; j < number_skills; j++) {
				skill_demand[j] += classrooms[i].getMin_skill_size(j);
				skill_demand[j] -= classrooms[i].getSkill_value(j);
				if(skill_demand[j]<0){
					skill_demand[j]=0;
				}
			}
		}
		return skill_demand;
	}
	private static int [] get_supplie(Volunteer	[] volunteers, int number_skills ) {
		int [] skill_supplie = new int [number_skills];
		for (int i = 0; i < volunteers.length; i++) {
			for (int j = 0; j < number_skills; j++) {
				if(volunteers[i].getClassid()==-1){
					skill_supplie[j] += volunteers[i].getSkill_value(j);
				}
			}
		}
		return skill_supplie;
	}
	private static int[] next (Classroom [] classroom ,Volunteer [] volunteer,int number_skills){

		int [] skill_demand  = get_demand(classroom,number_skills );
		boolean [] fulled = new boolean [number_skills];
		for (int i = 0; i < fulled.length; i++) {
			fulled[i] = skill_demand[i]==0;
		}
		int [] skill_supplie = get_supplie(volunteer,number_skills);
		for (int j = 0; j < number_skills; j++) {
			skill_demand[j] = skill_supplie[j]-skill_demand[j];
			if(fulled[j]){
				skill_demand[j] = 1;
			}
		}
		int min = skill_demand[0];
		int min_index = 0;
		for (int i = 0; i < skill_supplie.length; i++) {
			if(skill_supplie[i]>0){
				min = skill_demand[i];
				min_index = i ;
				break;
			}
		}
		for (int i = 0; i < skill_supplie.length; i++) {
			if(min<skill_demand[i]&&skill_supplie[i]>0){
				min_index = i;
				min = skill_demand[i];
			}
		}
		ArrayList<Integer> equalmin = new ArrayList<Integer>();
		for (int i = 0; i < skill_supplie.length; i++) {
			if(min==skill_demand[i]&&skill_supplie[i]>0){
				equalmin.add(i);
			}
		}
		min_index = equalmin.get(new Random().nextInt(equalmin.size()));
		int max = classroom[0].getMin_skill_size(min_index)- classroom[0].getSkill_value(min_index);
		int max_index = 0;
		for (int i = 0; i < classroom.length; i++) {
			if(classroom[i].getsize()<classroom[i].getMax_size()){
				max = classroom[i].getMin_skill_size(min_index)- classroom[i].getSkill_value(min_index);
				max_index = i;
				break;
			}
		}
		for (int i = 0; i < classroom.length; i++) {
			if(max< classroom[i].getMin_skill_size(min_index)- classroom[i].getSkill_value(min_index)&&classroom[i].getsize()<classroom[i].getMax_size()){
				max_index = i;
				max =  classroom[i].getMin_skill_size(min_index)- classroom[i].getSkill_value(min_index);
			}
		}
		int[]  a =  {  min_index , max_index};
		if (classroom[max_index].getMax_size()-classroom[max_index].getsize()<=0){
			a[0] = -1;
			a[1] = -1;
		}

		return a;
	}

	private static int newnext (Classroom [] classroom ,Volunteer [] volunteer,int number_skills,boolean [] end){
		int max = 0;
		int max_index = 0;
		for (int i = 0; i < classroom.length; i++) {
			int temp = 0 ; 
			for (int j = 0; j < number_skills; j++) {
				temp += classroom[i].getMin_skill_size(j) - classroom[i].getSkill_value(j)- classroom[i].getSkill_size(j);
				if(classroom[i].getsize()==classroom[i].getMax_size()||end[i]){
					temp = Integer.MIN_VALUE;
				}
			}
			if(max_index == 0&&i==0){
				max = temp ;
				max_index = i;
			}
			if(max<temp) {
				max = temp ;
				max_index = i;
			}
			//System.out.println(i+" "+temp);
		}
		//System.out.println("==="+max_index+" "+max);
		if(max ==  Integer.MIN_VALUE)
			return -1;
		return max_index;
	}
	
	

	private static void addtoclass(Classroom classroom[], int classsid,int volunterr_id ,Volunteer[] volunteer,int number_skills) {
		volunteer[volunterr_id].setClassid(classsid);
		classroom[classsid].setVolunteers(classroom[classsid].getsize(),volunterr_id);
		classroom[classsid].setsize(classroom[classsid].getsize()+1);
		for (int j = 0; j < number_skills; j++) {
			classroom[classsid].setSkill_value(j,classroom[classsid].getSkill_value(j)+volunteer[volunterr_id].getSkill_value(j));
		}
	}
	private static void removefromclass(Classroom classroom[], int volunterr_id ,Volunteer[] volunteer,int number_skills,boolean [] end) {
		end[volunteer[volunterr_id].getClassid()] = false;
		for (int i = 0; i < number_skills; i++) {
			classroom[volunteer[volunterr_id].getClassid()].setSkill_size(i,0);
			
		}
		for (int j = 0; j < number_skills; j++) {
			classroom[volunteer[volunterr_id].getClassid()].setSkill_value(j,classroom[volunteer[volunterr_id].getClassid()].getSkill_value(j)-volunteer[volunterr_id].getSkill_value(j));
		}
		for ( int k = 0 ;k<classroom[volunteer[volunterr_id].getClassid()].getsize();k++){
			if(classroom[volunteer[volunterr_id].getClassid()].getVolunteers(k)==volunterr_id){
				for (int j = k+1; j < classroom[volunteer[volunterr_id].getClassid()].getMax_size(); j++) {
					classroom[volunteer[volunterr_id].getClassid()].setVolunteers(j-1,classroom[volunteer[volunterr_id].getClassid()].getVolunteers(j));
				}
				classroom[volunteer[volunterr_id].getClassid()].setVolunteers(classroom[volunteer[volunterr_id].getClassid()].getMax_size()-1,-1);
				classroom[volunteer[volunterr_id].getClassid()].setsize(classroom[volunteer[volunterr_id].getClassid()].getsize()-1);
				volunteer[volunterr_id].setClassid(-1);
				break;
			}
		}

	}
	private static int score_remove(Classroom classroom[], int volunterr_id ,Volunteer[] volunteer,int number_skills){
		int score = 0 ;
		for (int i = 0; i < number_skills; i++) {
			if(volunteer[volunterr_id].getSkill_value(i)>0&&classroom[volunteer[volunterr_id].getClassid()].getMin_skill_size(i)-classroom[volunteer[volunterr_id].getClassid()].getSkill_value(i)>=0){
				score++;
			}
		}
		return score;
	}
	private static int score_add(Classroom classroom[],int class_id, int volunterr_id ,Volunteer[] volunteer,int number_skills){
		int score = 0 ;
		for (int i = 0; i < number_skills; i++) {
			if(volunteer[volunterr_id].getSkill_value(i)>0&&classroom[class_id].getMin_skill_size(i)-classroom[class_id].getSkill_value(i)>0){
				score++;
			}
		}
		return score;
	}
	private static boolean askme(boolean [][] asked , Classroom [] classroom ,Volunteer [] volunteer,int number_skills,boolean [] end) {
		int a = newnext(classroom, volunteer, number_skills,end);
		if(a!=-1){
			//System.out.println(classroom[a].getClass_name()+" needs the most skills ");
			int [] skill_demand  = get_demand(classroom,number_skills );
			int [] skill_supplie = get_supplie(volunteer,number_skills);

			//System.out.println("demand  "+Arrays.toString(skill_demand));
			//System.out.println("supplie "+Arrays.toString(skill_supplie));

			PriorityQueue<Integer[]> best = new PriorityQueue<Integer[]>(new quecomp());

			for (int i = 0; i < volunteer.length; i++) {
				if(volunteer[i].getPreferences(a)!=-1){
					if(volunteer[i].getClassid()!=a){
						Integer[] tempscore = new Integer[2];
						tempscore[0]	 = score_add(classroom, a, i, volunteer, number_skills);
						tempscore[1]	 = i;
						best.add(tempscore);
					}
				}
			}

			while (!best.isEmpty()){
				Integer[] temp = best.poll();
				int class_id = temp [1];
				if(volunteer[class_id].getClassid()==-1){//not in a class yet
					if(volunteer[class_id].getPreferences(a)!=-1){//able to go to this class
						//System.out.println(volunteer[class_id].toString());
						//System.out.println("was in none...");
						//System.out.println(classroom[a].toString());
						addtoclass(classroom, a, class_id, volunteer, number_skills);
						//System.out.println(classroom[a].toString());
						asked[a][class_id]=true;
						return true;
					}
				}else{
					if(volunteer[class_id].getPreferences(a)!=-1){
						if((volunteer[class_id].getPreferences(a)<volunteer[class_id].getPreferences(volunteer[class_id].getClassid())&&score_add(classroom, a, class_id, volunteer, number_skills)-score_remove(classroom, class_id, volunteer, number_skills)>=0)||score_add(classroom, a, class_id, volunteer, number_skills)-score_remove(classroom, class_id, volunteer, number_skills)>0){
							//System.out.println(volunteer[class_id].toString());
							//System.out.println("was in this ...");

							int oldid = volunteer[class_id].getClassid();
							//System.out.println(classroom[oldid].toString());

							removefromclass(classroom, class_id, volunteer, number_skills,end);

							//System.out.println(classroom[oldid].toString());
							//System.out.println("now in ");
							//System.out.println(classroom[a].toString());

							addtoclass(classroom, a, class_id, volunteer, number_skills);

							//System.out.println(classroom[a].toString());
							asked[a][class_id]=true;
							return true;
						}
					}
				}
			}
			System.out.println("no configeration that will full all classes compromises were made to calss "+classroom[a].getClass_name());
			for (int i = 0; i < number_skills; i++) {
				classroom[a].setSkill_size(i, classroom[a].getMin_skill_size(i)-classroom[a].getSkill_value(i));
				
			}
			
			for (int i = 0; i < skill_supplie.length; i++) {
				if(classroom[a].getSkill_size(i)<0){
					classroom[a].setSkill_size(i, 0);
				}
			}
			end[a]=true;
			System.out.println(classroom[a].printSkill_size());

			return true;
		}else{
		System.out.println("everyone was full !!!!");
		return false;
		}
	}

	/*
	private static boolean newasknext(boolean [][] asked , Classroom [] classroom ,Volunteer [] volunteer,int number_skills) {
		int[]  a = next(classroom, volunteer, number_skills);
		if(a[1]!=-1){
			System.out.println(classroom[a[1]].getClass_name()+" needs skill "+classroom[a[1]].getSkills(a[0]));
			int [] skill_demand  = get_demand(classroom,number_skills );
			int [] skill_supplie = get_supplie(volunteer,number_skills);

			System.out.println("demand  "+Arrays.toString(skill_demand));
			System.out.println("supplie "+Arrays.toString(skill_supplie));

			for (int i = 0; i < volunteer.length; i++) {
				if(!asked[a[1]][i]||asked[a[1]][i]){////////<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<think really hard about this...

					if(volunteer[i].getSkill_value(a[0])>0){//has the skill we are looking for
						if(volunteer[i].getClassid()==-1){//not in a class yet
							if(volunteer[i].getPreferences(a[1])!=-1){//able to go to this class
								System.out.println(volunteer[i].toString());
								System.out.println("was in none...");
								System.out.println(classroom[a[1]].toString());
								addtoclass(classroom, a[1], i, volunteer, number_skills);
								System.out.println(classroom[a[1]].toString());
								asked[a[1]][i]=true;
								return true;
							}
						}else{
							if(volunteer[i].getPreferences(a[1])!=-1){
								if((volunteer[i].getPreferences(a[1])<volunteer[i].getPreferences(volunteer[i].getClassid())&&score_add(classroom, a[1], i, volunteer, number_skills)-score_remove(classroom, i, volunteer, number_skills)>=0)||score_add(classroom, a[1], i, volunteer, number_skills)-score_remove(classroom, i, volunteer, number_skills)>0){
									System.out.println(volunteer[i].toString());
									System.out.println("was in this ...");

									int oldid = volunteer[i].getClassid();
									System.out.println(classroom[oldid].toString());

									removefromclass(classroom, i, volunteer, number_skills);

									System.out.println(classroom[oldid].toString());
									System.out.println("now in ");
									System.out.println(classroom[a[1]].toString());

									addtoclass(classroom, a[1], i, volunteer, number_skills);

									System.out.println(classroom[a[1]].toString());
									asked[a[1]][i]=true;
									return true;
								}
							}
						}
					}
				}
			}
			System.out.println("dont know what to do i asked every one they all sea no ... ");
			return false;
		}
		System.out.println("everyone was full !!!!");
		return false;
	}	
	private static boolean asknext(boolean [][] asked , Classroom [] classroom ,Volunteer [] volunteer,int number_skills) {
		int[]  a = next(classroom, volunteer, number_skills);
		System.out.println(classroom[a[1]].getClass_name()+" needs skill "+classroom[a[1]].getSkills(a[0]));
		int [] skill_demand  = get_demand(classroom,number_skills );
		int [] skill_supplie = get_supplie(volunteer,number_skills);

		System.out.println("demand  "+Arrays.toString(skill_demand));
		System.out.println("supplie "+Arrays.toString(skill_supplie));

		for (int i = 0; i < volunteer.length; i++) {
			if(!asked[a[1]][i]){
				if(volunteer[i].getSkill_value(a[0])>0){
					if(volunteer[i].getClassid()==-1){//not in a class yet
						if(volunteer[i].getPreferences(a[1])!=-1){//able to go to this class
							System.out.println(volunteer[i].toString());
							System.out.println("was in none...");
							System.out.println(classroom[a[1]].toString());
							addtoclass(classroom, a[1], i, volunteer, number_skills);
							System.out.println(classroom[a[1]].toString());
							asked[a[1]][i]=true;
							return true;
						}
					}else{
						if(volunteer[i].getPreferences(a[1])!=-1){
							if(volunteer[i].getPreferences(a[1])<volunteer[i].getPreferences(volunteer[i].getClassid())){
								System.out.println(volunteer[i].toString());
								System.out.println("was in this ...");

								int oldid = volunteer[i].getClassid();
								System.out.println(classroom[oldid].toString());

								removefromclass(classroom, i, volunteer, number_skills);

								System.out.println(classroom[oldid].toString());
								System.out.println("now in ");
								System.out.println(classroom[a[1]].toString());

								addtoclass(classroom, a[1], i, volunteer, number_skills);

								System.out.println(classroom[a[1]].toString());
								asked[a[1]][i]=true;
								return true;
							}
						}
					}
				}
			}
		}
		System.out.println("dont know what to do i asked every one they all sea no ... ");
		return false;
	}
	/*/

	/*
	private static boolean newnewasknext(boolean [][] asked , Classroom [] classroom ,Volunteer [] volunteer,int number_skills) {
		int[]  a = next(classroom, volunteer, number_skills);
		if(a[1]!=-1){
			System.out.println(classroom[a[1]].getClass_name()+" needs skill "+classroom[a[1]].getSkills(a[0]));
			int [] skill_demand  = get_demand(classroom,number_skills );
			int [] skill_supplie = get_supplie(volunteer,number_skills);

			System.out.println("demand  "+Arrays.toString(skill_demand));
			System.out.println("supplie "+Arrays.toString(skill_supplie));

			ArrayList<Integer> best = new ArrayList<Integer>();
			int score = 0 ;
			for (int i = 0; i < volunteer.length; i++) {
				if(volunteer[i].getPreferences(a[1])!=-1){
					if(volunteer[i].getClassid()!=a[1]){
						int tempscore = score_add(classroom, a[1], i, volunteer, number_skills);
						if(score<=tempscore){
							if(score==tempscore){
								best.add(i);
							}else{
								score = tempscore;
								best = new ArrayList<Integer>();
								best.add(i);
							}
						}
					}
				}
			}
			Collections.shuffle(best);
			for (int i = 0; i < best.size(); i++) {
				if(volunteer[best.get(i)].getSkill_value(a[0])>0){//has the skill we are looking for
					if(volunteer[best.get(i)].getClassid()==-1){//not in a class yet
						if(volunteer[best.get(i)].getPreferences(a[1])!=-1){//able to go to this class
							System.out.println(volunteer[best.get(i)].toString());
							System.out.println("was in none...");
							System.out.println(classroom[a[1]].toString());
							addtoclass(classroom, a[1], best.get(i), volunteer, number_skills);
							System.out.println(classroom[a[1]].toString());
							asked[a[1]][best.get(i)]=true;
							return true;
						}
					}else{
						if(volunteer[best.get(i)].getPreferences(a[1])!=-1){
							if((volunteer[best.get(i)].getPreferences(a[1])<volunteer[best.get(i)].getPreferences(volunteer[best.get(i)].getClassid())&&score_add(classroom, a[1], best.get(i), volunteer, number_skills)-score_remove(classroom, best.get(i), volunteer, number_skills)>=0)||score_add(classroom, a[1], best.get(i), volunteer, number_skills)-score_remove(classroom, best.get(i), volunteer, number_skills)>0){
								System.out.println(volunteer[best.get(i)].toString());
								System.out.println("was in this ...");

								int oldid = volunteer[best.get(i)].getClassid();
								System.out.println(classroom[oldid].toString());

								removefromclass(classroom, best.get(i), volunteer, number_skills);

								System.out.println(classroom[oldid].toString());
								System.out.println("now in ");
								System.out.println(classroom[a[1]].toString());

								addtoclass(classroom, a[1], best.get(i), volunteer, number_skills);

								System.out.println(classroom[a[1]].toString());
								asked[a[1]][best.get(i)]=true;
								return true;
							}
						}
					}
				}
			}
			System.out.println("no configeration that will full all classes");
			return false;
		}
		System.out.println("everyone was full !!!!");
		return false;
	}
	//*/

	private static void print_sets( Classroom [] classroom ,Volunteer [] volunteer,int number_skills) {
		int [] skill_demand  = get_demand(classroom,number_skills );
		int [] skill_supplie = get_supplie(volunteer,number_skills);

		System.out.println("demand  "+Arrays.toString(skill_demand));
		System.out.println("supplie "+Arrays.toString(skill_supplie));

		System.out.println();
		for (int i = 0; i < classroom.length; i++) {
			System.out.println(classroom[i].getSchool_name()+"\t\t"+classroom[i].getClass_name());
			System.out.println("looked for    :"+classroom[i].printMin_skill_size());
			System.out.println("got           :"+classroom[i].printskill_value());
			
			for (int j = 0; j < classroom[i].getsize(); j++) {
				System.out.println(volunteer[classroom[i].getVolunteers(j)].summary());
			}
		}

		System.out.println();
		System.out.println();


		for (int j = 0; j < volunteer.length; j++) {
			System.out.println(volunteer[j].summary());
		}


	}
}
class quecomp implements Comparator<Integer[]>{

	public int compare(Integer[] arg0, Integer[] arg1) {
		return arg1[0] -arg0[0];
	}

}

