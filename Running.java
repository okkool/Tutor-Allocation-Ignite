/**
 * The main class which actually performs the allocations
 * @author Mark Robson
 */
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;

public class Running {
	static Volunteer	[] volunteers;
	static Classroom	[] classrooms;
	static String 		[] skills;
	static int number_skills;
	static int number_classrooms;
	static int number_volunteers;
	static int [] skill_demand ;
	static int [] skill_supply ;

	private static String log;
	public String returnLog() {
		return Running.log+"\n\n";
	}


	public static void printError(Exception e) {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH_mm_ss");
		Calendar calobj = Calendar.getInstance();
		System.out.println();
		String s = "Ignite_Error_Log "+df.format(calobj.getTime())+".txt";
		File f = new File (s);
		try {
			PrintWriter pw = new PrintWriter(f);
			pw.write(log+"\n\n\n\n");
			e.printStackTrace(pw);
			pw.close();
		} catch (FileNotFoundException ep) {
			ep.printStackTrace();
		}
		System.out.println(f.getPath());
	}

	/**
	 * The initial algorithm which performs the matching of volunteers to classrooms via a Stable Marriage approach
	 * TODO: Remove this from final release if possible
	 * Firstly, the algorithm attempts to read-in data from the excel sheet
	 * TODO: Implement checking of inputs and throwing of errors. This is currently done within the class-itself but should be done here
	 * The algorithm determines which volunteer skills are in the highest, lowest demand and supply
	 * Then allocations are repeatedly made attempting to take the skills the most in demand in least supply and allocating individuals with these skills to classrooms with the highest demand
	 * The algorithm terminates when all allocations made are stable
	 * @param args Standard argument parameters
	 * @throws InvalidAlgorithmParameterException	The obvious
	 */
/*	public static void main(String[] args) throws InvalidAlgorithmParameterException {
		parseInput("Book1.xls");
		//Input done
		//Demand and supply are now determined
		skill_demand  = get_demand(classrooms,number_skills);
		skill_supply = get_supply(volunteers,number_skills);
		//A boolean array representation of allocations made
		boolean [][] asked = new boolean [number_classrooms][number_volunteers];
		boolean end [] = new boolean [number_classrooms];
		while(askme(asked,classrooms,volunteers,number_skills,end));
		print_sets(classrooms, volunteers, number_skills);
		System.out.println("_++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
		System.out.println(DescString(skill_demand,skill_supply,classrooms,volunteers,number_skills,skills));


	}*/

	/**
	 * A method used to initially parse the data from an excel document, a simple cut and paste job
	 * TODO: Remove this for final release
	 * @param volunteers		An array of volunteers to be filled
	 * @param classrooms		An array of classrooms to be filled		
	 * @param skills			An string array of skills to be filled and used by all volunteers e.g. {"M",F"}
	 * @param number_skills		A count of the number of skills
	 * @param number_classrooms	A count of the number of classrooms
	 * @param number_volunteers	A count of the number of volunteers
	 * @throws IOException 
	 * @see Volunteer
	 * @see Classroom
	 */
	
	/*
	 * public static void parseInputStatic(Volunteer[] volunteers, Classroom[] classrooms, String[] skills, int number_skills, int number_classrooms, int number_volunteers) throws IOException{
		//TODO: Dynamic parsing of input
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream("Book1.xls");

			HSSFWorkbook workbook1 = new HSSFWorkbook(fileInputStream);
			HSSFSheet Sheet0 = workbook1.getSheetAt(0);

			//It is paramount to note that the algorithm relies on a standard input format which will need to be specified to the clients
			number_skills 		= (int) Sheet0.getRow(1).getCell(0).getNumericCellValue();
			number_classrooms 	= (int) Sheet0.getRow(3).getCell(0).getNumericCellValue();
			number_volunteers 	= (int) Sheet0.getRow(5).getCell(0).getNumericCellValue();

			log = log + "There are "+number_skills+" different skills\n";
			System.out.println("There are "+number_skills+" different skills");

			skills 		= new String 	[number_skills	  ];
			volunteers	= new Volunteer [number_volunteers];
			classrooms	= new Classroom	[number_classrooms];

			for (int i = 0; i < number_skills	 ; i++) {
				skills [i]     = Sheet0.getRow(1).getCell(1+i).getStringCellValue();
				log = log + skills [i] +"\n";
				System.out.println(skills [i]);
			}
			log = log+ "There are "+number_classrooms+" different classrooms \n";

			System.out.println("There are "+number_classrooms+" different classrooms");

			Sheet0 = workbook1.getSheetAt(1);
			for (int i = 0; i < number_classrooms; i++) {
				String school_name = Sheet0.getRow(1+i).getCell(0).getStringCellValue();
				String class_name  = Sheet0.getRow(1+i).getCell(1).getStringCellValue();
				int max_size = (int) Sheet0.getRow(1+i).getCell(3).getNumericCellValue();

				int [] 		min_skill_value = new int [number_skills];
				int [] 		min_skill_size  = new int [number_skills];
				for (int j = 0; j < number_skills	 ; j++) {

					min_skill_size	[j]		= (int) Sheet0.getRow(1+i).getCell(3+j).getNumericCellValue();

				}
				classrooms [i] = new Classroom(school_name, class_name, skills, min_skill_value, min_skill_size, max_size);
				log = log + i+", "+classrooms [i].toString()+"\n";
				System.out.println(i+", "+classrooms [i].toString());
			}
			log = log +"There are "+number_volunteers+" different Volunteers\n";
			System.out.println("There are "+number_volunteers+" different Volunteers");
			Sheet0 = workbook1.getSheetAt(2);
			for (int i = 0; i < number_volunteers; i++) {
				String name  = Sheet0.getRow(1+i).getCell(0).getStringCellValue();
				String email = Sheet0.getRow(1+i).getCell(1).getStringCellValue();
				int [] 	skill = new int [number_skills];
				for (int j = 0; j < number_skills	 ; j++) {
					skill [j]     = (int) Sheet0.getRow(1+i).getCell(2+j).getNumericCellValue();
				}
				int [] 	preferences = new int [number_classrooms];

				for (int j = 0; j < number_classrooms	 ; j++) {
					preferences [j]     = (int) Sheet0.getRow(1+i).getCell(2+number_skills+j).getNumericCellValue();
				}
				volunteers[i] = new Volunteer(name, email, skills, skill, preferences);
				System.out.println(i+"\t "+volunteers[i].toString());
			}
			fileInputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			printError(e) ;
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			printError(e) ;
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	*/
	/**
	 * The proper input parser with a dynamic filePath
	 * @param filePath			The filePath of the excel document to be read from
	 * @param volunteers		An array of volunteers to be filled
	 * @param classrooms		An array of classrooms to be filled		
	 * @param skills			An string array of skills to be filled and used by all volunteers e.g. {"M",F"}
	 * @param number_skills		A count of the number of skills
	 * @param number_classrooms	A count of the number of classrooms
	 * @param number_volunteers	A count of the number of volunteers
	 * @see Volunteer
	 * @see Classroom
	 */
	public static Exception parseInput(String filePath){
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(filePath);
			HSSFWorkbook workbook1 ;
			try {
				workbook1 = new HSSFWorkbook(fileInputStream);
				try{
					HSSFSheet Sheet0 = workbook1.getSheetAt(0);
					try{
						number_skills 		= (int) Sheet0.getRow(1).getCell(0).getNumericCellValue();
						if(number_skills <= 0 ){
							throw new IOException ("number_skills was set to "+number_skills);
						}
						try{
							number_classrooms 	= (int) Sheet0.getRow(3).getCell(0).getNumericCellValue();
							if(number_classrooms <= 0 ){
								throw new IOException ("number_classrooms was set to "+number_classrooms);
							}
							try{
								number_volunteers 	= (int) Sheet0.getRow(5).getCell(0).getNumericCellValue();
								if(number_volunteers <= 0 ){
									throw new IOException ("number_volunteers was set to "+number_volunteers);
								}
								log = log + "There are "+number_skills+" different skills:\n\n";
								System.out.println("There are "+number_skills+" different skills");

								skills 		= new String 	[number_skills	  ];
								volunteers	= new Volunteer [number_volunteers];
								classrooms	= new Classroom	[number_classrooms];

								int i = 0;
								try{
									for (i = 0; i < number_skills	 ; i++) {
										skills [i]     = Sheet0.getRow(1).getCell(1+i).getStringCellValue();
										log = log + "\t" + skills[i] + "\n";
										System.out.println(skills [i]);
									}
									try{
										log = log + "\nThere are "+number_classrooms+" different classrooms:\n\n";
										System.out.println("There are "+number_classrooms+" different classrooms");
										Sheet0 = workbook1.getSheetAt(1);
										int  x = 0 ;
										try{
											for ( i = 0; i < number_classrooms; i++) {
												String school_name = Sheet0.getRow(1+i).getCell(0).getStringCellValue();
												x++;
												String class_name  = Sheet0.getRow(1+i).getCell(1).getStringCellValue();
												x++;
												int max_size = (int) Sheet0.getRow(1+i).getCell(2).getNumericCellValue();
												if(max_size <= 0 ){
													throw new IOException ("max_size of classroom "+school_name+" was set to "+max_size);
												}
												x++;
												int [] 		min_skill_value = new int [number_skills];
												int [] 		min_skill_size  = new int [number_skills];

												for (int j = 0; j < number_skills	 ; j++) {
													x++;
													min_skill_size	[j]		= (int) Sheet0.getRow(1+i).getCell(3+j).getNumericCellValue();
													if(min_skill_size[j] < 0 ){
														throw new IOException ("The number of people with skill "+skills[j]+" needed in classroom "+school_name+" was set to "+min_skill_size[j]);
													}
												}

												try {
													classrooms [i] = new Classroom(school_name, class_name,min_skill_value, min_skill_size, max_size);
												} catch (InvalidAlgorithmParameterException e) {
													// TODO Auto-generated catch block
													printError(e); 
													e.printStackTrace();

												}
		
												log = log +String.format("%3d", (i+1))+"  "+classrooms [i].toStringSummary()+"\n";
												System.out.println(i+", "+classrooms [i].toString());
											}

											try{
												x = 0;
												int k = 0;
												int j = 0;
												log = log +"\nThere are "+number_volunteers+" different Volunteers:\n\n";
												System.out.println("There are "+number_volunteers+" different Volunteers");
												Sheet0 = workbook1.getSheetAt(2);
												try{
													for (i = 0; i < number_volunteers; i++) {
														k = 0;
														String name  = Sheet0.getRow(1+i).getCell(0).getStringCellValue();
														k++;
														String email = Sheet0.getRow(1+i).getCell(1).getStringCellValue();
														k++;
														int [] 	skill = new int [number_skills];
														for (x = 0; x < number_skills	 ; x++) {
															skill [x]     = (int) Sheet0.getRow(1+i).getCell(2+x).getNumericCellValue();
															if(skill[x] < 0 ){
																throw new IOException ("A skill value "+skills[x]+" for "+name+" was set to "+skill[x]);
															}
														}
														int [] 	preferences = new int [number_classrooms];

														for (j = 0; j < number_classrooms ; j++) {

															if( Sheet0.getRow(1+i).getCell(2+number_skills+j) == null){
																preferences [j]     = -1;
															}else{
																if(Sheet0.getRow(1+i).getCell(2+number_skills+j).getCellType() == HSSFCell.CELL_TYPE_NUMERIC){
																	preferences [j]     = (int) Sheet0.getRow(1+i).getCell(2+number_skills+j).getNumericCellValue();	
																}else{
																	preferences [j]     = Integer.parseInt(Sheet0.getRow(1+i).getCell(2+number_skills+j).getStringCellValue());
																}

															}

														}
														volunteers[i] = new Volunteer(name, email,skill, preferences);
														log = log + String.format("%3d",(i+1)) +"  "+volunteers[i].toString()+"\n";
														System.out.println(i+"\t "+volunteers[i].toString());
														}
													log = log + "\n";
												}catch(Exception e){
													printError(e);
													e.printStackTrace();
													errorBox("Sheet 3 , row "+(i+1)+" column "+(x+k+1+j)+" is not a valid name.","Error in file");
													return e;
												}
											}catch(Exception e){
												printError(e);
												e.printStackTrace();
												errorBox("Could not find the 3rd sheet of the file "+filePath+".","Error in file");
												return e;
											}
										}catch(Exception e){
											printError(e);
											e.printStackTrace();
											errorBox("Sheet 2 , row "+(i+1)+" column "+(x+1)+" is not a valid name.","Error in file");
											return e;
										}
									}catch(Exception e){
										printError(e);
										e.printStackTrace();
										errorBox("Could not find the 2nd sheet of the file "+filePath+".","Error in file");
										return e;
									}
								}catch(Exception e){
									printError(e);
									e.printStackTrace();
									errorBox("Sheet 1 , row 1 column "+(i+1)+" is not a valid name.","Error in file");
									return e;
								}
							}catch(Exception e){
								printError(e);
								e.printStackTrace();
								errorBox("Sheet 1 , A2 is not a positive whole number.","Error in file");
								return e;
							}
						}catch(Exception e){
							printError(e);
							e.printStackTrace();
							errorBox("Sheet 1 , A4 is not a positive whole number.","Error in file");
							return e;
						}
					}catch(Exception e){
						printError(e);
						e.printStackTrace();
						errorBox("Sheet 1 , A2 is not a positive whole number.","Error in file");
						return e;
					}
				}catch(Exception e){
					printError(e);
					errorBox("Could not find the 1st sheet of the file "+filePath+".","Error in file");
					return e;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				printError(e);
				e.printStackTrace();
				return e;
			} catch (NegativeArraySizeException e){
				// TODO Auto-generated catch block
				printError(e);
				e.printStackTrace();
				return e;
			}
			fileInputStream.close();
			return null;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			printError(e);
			e.printStackTrace();
			return e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			printError(e);
			e.printStackTrace();
			return e;
		}
		
	}
	/**
	 * The actual algorithm used to make matches 
	 * @param file_path	The fileName passed from the GUI determining where the source data is
	 * @return			An error/success string depending on performance
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException 
	 */
	public static String pairingSystem(String file_path) throws InvalidAlgorithmParameterException, IOException{

		log = "";

		Exception e = parseInput(file_path);
		//Input done 
		if(e ==null){
		
		boolean [][] asked = new boolean [number_classrooms][number_volunteers];
		boolean end [] = new boolean [number_classrooms];

		int [] skill_demand  = get_demand(classrooms,number_skills);
		int [] skill_supply = get_supply(volunteers,number_skills);

		while(askme(asked,classrooms,volunteers,number_skills,end));
		print_sets(classrooms, volunteers, number_skills);
		log_sets(classrooms, volunteers, number_skills);

		return ("0" + DescString(skill_demand,skill_supply,classrooms,volunteers,number_skills,skills));
		}else
		{
			String temp = e.getClass().getSimpleName()+"  "+e.getMessage();
			StackTraceElement a[] = e.getStackTrace();
			for (int i = 0; i < a.length; i++) {
				temp = temp+"\n\tat "+a[i].getMethodName()+(a[i].getLineNumber()>-1?" line "+a[i].getLineNumber():"")+(a[i].getFileName()!=null?" "+a[i].getFileName():"");
			}
			
			
			return ("1" + temp + "\n");
		}
	}

	/**
	 * A method used to simply printout information about the execution environment
	 * @param skill_demand
	 * @param skill_supply
	 * @param classrooms
	 * @param volunteers
	 * @param number_skills
	 * @param skills
	 * @return 				A string representation of the arguments above
	 * @see	Volunteer
	 * @see Classroom
	 */

	private static String DescString() {
		return DescString(skill_demand,skill_supply, classrooms, volunteers,number_skills ,skills);
	}

	private static String DescString(int[] skill_demand, int[] skill_supply, Classroom[] classrooms, Volunteer[] volunteers, int number_skills ,String[] skills) {

		int max = 0;
		for (int i = 0; i < skills.length; i++){
			max = Math.max(skills[i].length(), max);
		}
		for(int i = 0; i < skills.length; i++){
			while (skills[i].length()<=max){
				skills[i]=skills[i]+" ";
			}
		}
		
		String out = " Supply:  \n\n";
		for (int i = 0; i < skill_supply.length; i++) {
			out = out +"  "+skills[i]+"\t: "+skill_supply[i]+"\n";
		}
		out += " \nDemand:\n\n";
		for (int i = 0; i < skill_supply.length; i++) {
			out = out +"  "+skills[i]+"\t: "+skill_demand[i]+"\n";
		}
		out += "\n\nThe following schools are missing a set of requested skills"+":\n\n";
		for (int i = 0; i < classrooms.length; i++) {
			int [] missing = new int [number_skills];
			boolean full = true;
			for (int j = 0; j < skill_supply.length; j++) {
				missing[j] = classrooms[i].getMin_skill_size(j)-classrooms[i].getSkill_value(j);
				if(missing[j] > 0){
					full = false;

					if(!full){
						out = out + "  "+classrooms[i].getSchool_name()+" "+classrooms[i].getClass_name()+"\n";
						for (int j2 = 0; j2 < missing.length; j2++) {
							if(missing[j2]>0)
								out = out +"\t"+skills[j2]+"\t: "+missing[j2]+"\n";
						}
						out = out + "\n";
					}
				}
			}

		}
		String vol = "\t";
		int number_of_unsed = 0 ;
		for (int i = 0; i < volunteers.length; i++) {
			if(volunteers[i].getClassid()==-1){
				vol = vol + "\n\t" + volunteers[i].getName();
				number_of_unsed++;
			}
		}
		if (number_of_unsed == 0)
			out = out + "\nAll volunteers were allocated\n"+vol;
		else if (number_of_unsed == 1)
			out = out + "\n" + vol + " is the only unallocated volunteer\n";
		else
			out = out + "\nThere are "+number_of_unsed+" unallocated volunteers:\n"+vol;

		return out;
	}
	public static void save(String path){
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("volunteers");  


		HSSFRow rowhead = sheet.createRow((short)0);
		rowhead.createCell(0).setCellValue("Name");
		rowhead.createCell(1).setCellValue("E-mail");
		rowhead.createCell(2).setCellValue("School Name");
		rowhead.createCell(3).setCellValue("Class Name");

		for (int j = 0; j < volunteers.length; j++) {

			rowhead = sheet.createRow(j);
			rowhead.createCell(0).setCellValue(volunteers[j].getName());
			rowhead.createCell(1).setCellValue(volunteers[j].getEmail());
			if(volunteers[j].getClassid()!=-1){
				rowhead.createCell(2).setCellValue(classrooms[volunteers[j].getClassid()].getSchool_name());
				rowhead.createCell(3).setCellValue(classrooms[volunteers[j].getClassid()].getSchool_name());
			}else{
				rowhead.createCell(2).setCellValue("Not Allocated");
				rowhead.createCell(3).setCellValue("Not Allocated");
			}
		}

		for (int i = 0; i < classrooms.length; i++) {
			sheet = workbook.createSheet((classrooms[i].getClass_name()+" "+classrooms[i].getSchool_name()).replaceAll("[^a-zA-Z0-9]+"," ").trim());//.replaceAll("-+.^:;@#$%^&*|=`',\"\\>< ",""));

			rowhead = sheet.createRow((short)0);
			rowhead.createCell(0).setCellValue("Name");
			rowhead.createCell(1).setCellValue("E-mail");
			for (int j = 0; j < classrooms[i].getsize(); j++) {

				rowhead = sheet.createRow(j);
				rowhead.createCell(0).setCellValue(volunteers[classrooms[i].getVolunteers(j)].getName());
				rowhead.createCell(1).setCellValue(volunteers[classrooms[i].getVolunteers(j)].getEmail());
			}
		}

		sheet = workbook.createSheet("Unallocated volunteers"); 
		rowhead = sheet.createRow((short)0);
		rowhead.createCell(0).setCellValue("Name");
		rowhead.createCell(1).setCellValue("E-mail");

		for (int j = 0, k = 0; j < volunteers.length; j++) {
			if(volunteers[j].getClassid()==-1){
				rowhead = sheet.createRow(k);
				rowhead.createCell(0).setCellValue(volunteers[j].getName());
				rowhead.createCell(1).setCellValue(volunteers[j].getEmail());
				k++;
			}
		}


		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			printError(e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			printError(e);
			e.printStackTrace();
		}

	}

	/**
	 * A method to determine the demand of all skills present
	 * @param classrooms	An array of classrooms which have demand for skills
	 * @param number_skills	The count of skills present, used to set loop bounds
	 * @return				An integer array representing the demand for each skill (represented by the index) corresponding to the common skill string
	 */
	private static int [] get_demand(Classroom	[] classrooms, int number_skills ) {
		int [] skill_demanda = new int [number_skills];
		for (int i = 0; i < classrooms.length; i++) {
			for (int j = 0; j < number_skills; j++) {
				skill_demanda[j] += classrooms[i].getMin_skill_size(j);
				skill_demanda[j] -= classrooms[i].getSkill_value(j);
				if(skill_demanda[j]<0){
					skill_demanda[j]=0;
				}
			}
		}
		return skill_demanda;
	}
	/**
	 * A method to determine the supply of all skills present across all volunteers
	 * @param volunteers	An array of volunteers each of which may or may not have skills
	 * @param number_skills	The count of skills present, used to set loop bounds
	 * @return				An integer array of skills supplied, in the same index format as the standard skill string
	 */
	private static int [] get_supply(Volunteer	[] volunteers, int number_skills ) {
		int [] skill_supply = new int [number_skills];
		for (int i = 0; i < volunteers.length; i++) {
			for (int j = 0; j < number_skills; j++) {
				if(volunteers[i].getClassid()==-1){
					skill_supply[j] += volunteers[i].getSkill_value(j);
				}
			}
		}
		return skill_supply;
	}
	/**
	 * A method to find the next classroom to allocate to 
	 * @param classroom		An array of classrooms to look at
	 * @param volunteer		The array of volunteers to choose from
	 * @param number_skills	The number of skills in play
	 * @param end			A boolean array of whether a classroom has been totally supplied or not
	 * @return	The integer index of the next classroom to allocate to
	 */
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
			if(max_index==0 && i==0){
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
	/**
	 * A method to allocate a volunteer to a particular classroom
	 * @param classroom		The array of classes 
	 * @param classsid		The array index of the class allocating to
	 * @param volunterr_id	The ID of the current volunteer 
	 * @param volunteer		The array of volunteers
	 * @param number_skills	The count of the number of skills in play
	 */
	private static void addtoclass(Classroom classroom[], int classsid,int volunterr_id ,Volunteer[] volunteer,int number_skills) {
		volunteer[volunterr_id].setClassid(classsid);
		classroom[classsid].setVolunteers(classroom[classsid].getsize(),volunterr_id);
		classroom[classsid].setsize(classroom[classsid].getsize()+1);
		for (int j = 0; j < number_skills; j++) {
			classroom[classsid].setSkill_value(j,classroom[classsid].getSkill_value(j)+volunteer[volunterr_id].getSkill_value(j));
		}
	}
	/**
	 * A method to remove an allocation to a class
	 * @param classroom		The array of classes
	 * @param volunterr_id	The ID of the volunteer being removed
	 * @param volunteer		The array of volunteers
	 * @param number_skills	A count of the skills in play
	 * @param end			The boolean representation of whether a class is full or not		
	 */
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
	/**
	 * A method used to remove the skill value of a volunteer from the skill total of a classroom, part of the removal process of a volunteer
	 * @param classroom		The array of classes
	 * @param volunterr_id	The ID of the volunteer to be removed
	 * @param volunteer		The array of volunteers 
	 * @param number_skills	A count of the number of skills in play
	 * @return				Return the new score of the classroom after the volunteer has been removed
	 * TODO: I need a little help understanding what this actually does
	 */
	private static int score_remove(Classroom classroom[], int volunterr_id ,Volunteer[] volunteer,int number_skills){
		int score = 0 ;
		for (int i = 0; i < number_skills; i++) {
			if(volunteer[volunterr_id].getSkill_value(i)>0&&classroom[volunteer[volunterr_id].getClassid()].getMin_skill_size(i)-classroom[volunteer[volunterr_id].getClassid()].getSkill_value(i)>=0){
				score++;
			}
		}
		return score;
	}
	/**
	 * Another mysterious method used to add a volunteer's skill value to the class's total
	 * @param classroom
	 * @param class_id
	 * @param volunterr_id
	 * @param volunteer
	 * @param number_skills
	 * @return
	 * TODO: Check equality > 0, is >=0 in score_remove method
	 */
	private static int score_add(Classroom classroom[],int class_id, int volunterr_id ,Volunteer[] volunteer,int number_skills){
		int score = 0 ;
		for (int i = 0; i < number_skills; i++) {
			if(volunteer[volunterr_id].getSkill_value(i)>0&&classroom[class_id].getMin_skill_size(i)-classroom[class_id].getSkill_value(i)>0){
				score++;
			}
		}
		return score;
	}
	/**
	 * An imperative method which makes the next allocation available
	 * @param asked			A boolean representation of the allocations already made
	 * @param classroom		The  array of classes
	 * @param volunteer		The array of volunteers 
	 * @param number_skills	The count of skills in play
	 * @param end			A boolean representation full classes
	 * @return				Returns true if there is at least one more match that can be made, i.e. returns false only when all classrooms are full
	 */
	private static boolean askme(boolean [][] asked , Classroom [] classroom ,Volunteer [] volunteer,int number_skills,boolean [] end) {
		int a = newnext(classroom, volunteer, number_skills,end);
		if(a!=-1){
			//System.out.println(classroom[a].getClass_name()+" needs the most skills ");
			//int [] skill_demand  = get_demand(classroom,number_skills );
			int [] skill_supply = get_supply(volunteer,number_skills);

			//System.out.println("demand  "+Arrays.toString(skill_demand));
			//System.out.println("supply "+Arrays.toString(skill_supply));

			PriorityQueue<Integer[]> best = new PriorityQueue<Integer[]>(new QueueComparator());

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
							//int oldid = volunteer[class_id].getClassid();
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
			System.out.println("No possible configuration that will fill all classes. Compromises were made to classes "+classroom[a].getClass_name());
			for (int i = 0; i < number_skills; i++) {
				classroom[a].setSkill_size(i, classroom[a].getMin_skill_size(i)-classroom[a].getSkill_value(i));

			}

			for (int i = 0; i < skill_supply.length; i++) {
				if(classroom[a].getSkill_size(i)<0){
					classroom[a].setSkill_size(i, 0);
				}
			}
			end[a]=true;
			System.out.println(classroom[a].printSkill_size());

			return true;
		}else{
			System.out.println("Everyone was full !!!!");
			return false;
		}
	}
	/**
	 * A rather straightforward method which prints all the variables being used
	 * @param classroom		The array of classes
	 * @param volunteer		The array of volunteers
	 * @param number_skills	The count of skills available
	 */
	private static void print_sets( Classroom [] classrooms ,Volunteer [] volunteer,int number_skills) {
		int [] skill_demand  =get_demand(classrooms,number_skills );
		int [] skill_supply = get_supply(volunteer,number_skills);

		System.out.println("  "+Arrays.toString(skill_demand));
		System.out.println(" "+Arrays.toString(skill_supply));

		System.out.println();
		for (int i = 0; i < classrooms.length; i++) {
			System.out.println(classrooms[i].getSchool_name()+"\t\t"+classrooms[i].getClass_name());
			System.out.println("Looked for    :"+classrooms[i].printMin_skill_size());
			System.out.println("got           :"+classrooms[i].printskill_value());

			for (int j = 0; j < classrooms[i].getsize(); j++) {
				System.out.println(volunteer[classrooms[i].getVolunteers(j)].summary());
			}
		}
		System.out.println();
		System.out.println();
		for (int j = 0; j < volunteer.length; j++) {
			System.out.println(summary(volunteer[j]));
		}
	}
	private static void log_sets( Classroom [] classrooms ,Volunteer [] volunteer,int number_skills) {
		int [] skill_demand  =get_demand(classrooms,number_skills );
		int [] skill_supply = get_supply(volunteer,number_skills);

		log = log + "\nRequired :  "+Arrays.toString(skill_demand) + "\t\tRemaining:  "+Arrays.toString(skill_supply)+ "\n";

		
		for (int i = 0; i < classrooms.length; i++) {
			log = log + "\n\n" + (classrooms[i].getSchool_name()+"\t\t"+classrooms[i].getClass_name())+"\n";
			log = log +("  Looked for:  "+classrooms[i].printMin_skill_size())+",\t";
			log = log +("Achieved:  "+classrooms[i].printskill_value())+"\n\n";

			for (int j = 0; j < classrooms[i].getsize(); j++) {
				log = log + (volunteer[classrooms[i].getVolunteers(j)].summary()) + "\n";
			}
		}
		log += "\n\n Summary:\n\n";
		for (int j = 0; j < volunteer.length; j++) {
			log = log + (summary(volunteer[j]))+"\n";
		}
	}
	public static void errorBox(String errorMessage, String titleBar){
		/* By specifying a null headerMessage String, we cause the dialog to
           not have a header */
		errorBox(errorMessage, titleBar, null);
	}

	public static void errorBox(String errorMessage, String titleBar, String headerMessage){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(errorMessage);
		alert.initStyle(StageStyle.UTILITY);
		alert.showAndWait();
	}
	
	public static String summary(Volunteer v) {
		if(v.getClassid() == -1){
			return (String.format("\t%1$-32s ", v.getName()) + Arrays.toString(v.getSkill_values()) + " was Not Allocated");

		}
		return (String.format("\t%1$-32s ", v.getName()) + Arrays.toString(v.getSkill_values()) + " was Allocated to "+ classrooms[v.getClassid()].getSchool_name()+" "+classrooms[v.getClassid()].getClass_name());

	}

}

/**
 * A simple comparator used in the priority queue. Very standard.
 */
class QueueComparator implements Comparator<Integer[]>{
	public int compare(Integer[] arg0, Integer[] arg1) {
		return arg1[0] -arg0[0];
	}
}
