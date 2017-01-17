import java.util.Arrays;

public class Volunteer {
	private String name;
	private String email;
	private int classid;
	
	private String firstname;
	private String lastname;
	private String phone;
	private String areyoua;
	private String doyouhave;
	private String wwc;
	private String driving;
	
	

	private int [] 		skill_value;
	private int [] 		preferences;
	
	public Volunteer(String fristname , String lastname, String email, String phone,String areyoua,String doyouhave,String wwc,String driving, int[] skill_value, int [] preferences) {
		super();
		this.name 		 = fristname+" "+lastname;
		this.firstname	 = fristname;
		this.lastname 	 = lastname;
		this.phone 		 = phone;
		this.areyoua	 = areyoua;
		this.email 		 = email;
		this.doyouhave 	 = doyouhave;
		this.wwc 		 = wwc;
		this.driving 	 = driving;
		this.skill_value = skill_value;
		this.preferences = preferences;
		this.classid 	 = -1;
	}


	@Override
	public String 		toString		() {
		return "Volunteer [Name=" + String.format("%-32s", name) + ", E-mail=" +String.format("%-40s", email) +", skill_value="
				+ Arrays.toString(skill_value) + ", Preferences=" + Arrays.toString(preferences) + "]";
	}
	protected int		getClassid		() {
		return classid;
	}
	protected void 		setClassid		(int classid) {
		this.classid = classid;
	}
	protected String 	getName			() {
		return name;
	}
	protected String 	getEmail		() {
		return email;
	}
	
	public String getDriving() {
		return driving;
	}
	protected int	 	getSkill_value	(int i) {
		return skill_value[i];
	}	
	protected int 		getPreferences	(int i) {
		return preferences[i];
	}
	protected void 		setSkill_value	(int i, int skill_value) {
		this.skill_value[i] = skill_value;
	}


	public String summary() {
		if(classid == -1){
			return (String.format("\t%1$-32s ", name) + Arrays.toString(skill_value) + " was Not Allocated");
			//return ("\t" + name+ "\t " + Arrays.toString(skill_value) + " was Not Allocated");
		}
		return (String.format("\t%1$-32s ", name) + Arrays.toString(skill_value));
		/*return ("\t" + name + "\t " + Arrays.toString(skill_value) + " was Allocated");/*+" was their "+
		(preferences[classid]==0?"1st":
			(preferences[classid]==1?"2nd":
				(preferences[classid]==2?"3rd":
					(preferences[classid]+1+"th"))))+" preferences";
	*/
	}


	public int[] getSkill_values() {
		
		return skill_value;
	}


	public String getFirstname() {
		return firstname;
	}


	public String getLastname() {
		return lastname;
	}


	public String getPhone() {
		return phone;
	}


	public String getAreyoua() {
		return areyoua;
	}


	public String getDoyouhave() {
		return doyouhave;
	}


	public String getWwc() {
		return wwc;
	}
	
}
