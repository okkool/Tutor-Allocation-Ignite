import java.util.Arrays;
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
			FileInputStream fileInputStream = new FileInputStream("Test1.xls");
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
				
				for (int j = 0; j < number_skills	 ; j++) {
					preferences [j]     = (int) workbook1.getSheetAt(2).getRow(1+i).getCell(2+number_skills+j).getNumericCellValue();
				}
				boolean [] seen = new boolean [number_skills];
				int 	[] temp = new int 	  [number_skills];
				int l = 1;
				for (int j = 0; j < number_skills	 ; j++) {
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
				volunteers[i] = new Volunteer(name, email, Skills, skill, temp);
				System.out.println(i+"\t "+volunteers[i].toString());
			}
			//input done 
			
			
			int [] skill_demand  = get_demand(classrooms,number_skills );
			int [] skill_supplie = get_supplie(volunteers,number_skills);
			
			System.out.println("demand  "+Arrays.toString(skill_demand));
			System.out.println("supplie "+Arrays.toString(skill_supplie));
			
			

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
				skill_demand[j] -= classrooms[i].getSkill_size(j);
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
	//private static int next(Classroom [] classroom ,Volunteer [] volunteer){
		
	//}
	
	
}
