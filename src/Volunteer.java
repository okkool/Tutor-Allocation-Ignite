import java.util.Arrays;
/**
 * A class contianing the representation of a volunteer in the Ignite mentoring system.
 * @author Mark Robson
 * @author Phat comments brought to you by Nicholas Pritchard
 */
public class Volunteer {
	private String name;
	private String email;
	private int classid;
	
	private String []	skills;
	private int [] 		skill_value;
	private int [] 		preferences;
	/**
	 * A constructor method returning an instance of volunteer taking the following parameters
	 * 
	 * @param name 			The name of the volunteer, no restrictions for duplicity as we use an ID system later on
	 * @param email			The contact email of the volunteer
	 * @param skills		An array of multiple strings describing attributes of the volunteer. E.g. ["E", "M", "F"] etc. Mosty turned to binary representations later
	 * @param skill_value 	An integer array corresponding to the skills in the 'skills' array. E.g. [1, 1] indicating an experienced male etc.
	 * @param preferences	A zero-index array indicating which schools an individual would like to attend. E.g. [1,0,2,-1] 1st = School2, 2nd = School1, 3rd = School3, N/A = School4 
	 * @param classid		An Integer representing the class this volunteer is currently assigned to. Defaulted to -1.
	 */
	public Volunteer(String name, String email, String[] skills, int[] skill_value, int [] preferences) {
		super();
		this.name 		 = name;
		this.email 		 = email;
		this.skills 	 = skills;
		this.skill_value = skill_value;
		this.preferences = preferences;
		this.classid = -1;
	}


	@Override
	/**
	 * Various getters and setters which have been automatically generated for ease
	 */
	public String 		toString		() {
		return "Volunteer [name=" + name + ", email=" + email + ", skills=" + Arrays.toString(skills) + ", skill_value="
				+ Arrays.toString(skill_value) + ", preferences=" + Arrays.toString(preferences) + "]";
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
	protected String[] 	getSkills		() {
		return skills;
	}
	protected int	 	getSkill_value	(int i) {
		return skill_value[i];
	}	
	protected int 		getPreferences	(int i) {
		return preferences[i];
	}
	protected void 		setSkills		(int i, String skills) {
		this.skills[i] = skills;
	}
	protected void 		setSkill_value	(int i, int skill_value) {
		this.skill_value[i] = skill_value;
	}

	/**
	 * A summary method which can be called at any time to return the state of all class variables
	 * 
	 * @return	A standard string representation of all variables present with 
	 * @see		Arrays.toString
	 */
	public String summary() {
		/* If they have not been assigned to a class from either:
		 * Not being assigned yet
		 * Finding no assignment
		*/
		if(classid ==-1){
			return name+"\t "+Arrays.toString(skill_value)+" was not used";
		}
		return name+"\t "+Arrays.toString(skill_value)+" was their "+
		(preferences[classid]==0?"1st":
			(preferences[classid]==1?"2nd":
				(preferences[classid]==2?"3rd":
					(preferences[classid]+1+"th"))))+" preferences";
	}	
}
