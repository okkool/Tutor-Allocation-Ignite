import java.security.InvalidAlgorithmParameterException;
import java.util.Arrays;
/**
 * A representation of each classroom in the Ignite Mentoring System holding
 * @author Mark Robson
 * @author Phat comments brough to you by Nicholas Pritchard
 */
public class Classroom {
	private String school_name;
	private String class_name;
	
	private String []	skills;
			int			size;
	private int [] 		min_skill_value;
	private int [] 		min_skill_size;
	private int [] 		skill_value;
	private int [] 		skill_size;
	private int 		max_size;
	private int []		volunteers;
	
	/**
	 * A constructor method for a classroom
	 * 
	 * @param school_name		The name of the school
	 * @param class_name		The name of the class
	 * @param skills			An string array representation of the skills available for manipulation
	 * @param min_skill_value	Redundant? TODO: Rename as it has been used elsewhere
	 * @param min_skill_size	An array representation of the 'amount' of each skill required for a class to be filled by one or more volunteers with this skill
	 * @param max_size			The maximum number of volunteers the class can take
	 * 
	 * TODO: Check throwing of errors, this should be done elsewhere but this is okay for now
	 */
	public Classroom(String school_name, String class_name, String[] skills, int[] min_skill_value,
			int[] min_skill_size, int max_size) throws InvalidAlgorithmParameterException {
		super();
		this.school_name 		= school_name;
		this.class_name 		= class_name;
		this.skills 			= skills;
		this.min_skill_value 	= min_skill_value;
		this.min_skill_size 	= min_skill_size;
		this.max_size			= max_size;
		
		if (this.skills.length<1){throw new InvalidAlgorithmParameterException("class \""+this.class_name+"\" in school \""+this.school_name+"\" can not have a total of "+this.skills.length+" different skills.");}
		if (this.skills.length!=this.min_skill_value.length||this.skills.length!=this.min_skill_size.length){throw new InvalidAlgorithmParameterException("class \""+this.class_name+"\" in school \""+this.school_name+"\" has no value for one or more skills.");}
		if (max_size<1){throw new InvalidAlgorithmParameterException("class \""+this.class_name+"\" in school \""+this.school_name+"\" can not have max size "+max_size+".");}
		
		this.size = 0;
		volunteers 	= new int [this.max_size];
		skill_value = new int [this.skills.length];
		for (int i = 0; i < volunteers.length; i++) {
			volunteers[i]=-1;
		}
		skill_size 	= new int [this.skills.length];
	}

	/**
	 * Returns the skill value for a provided skill index
	 * 
	 * @param i	The index of the skill requested
	 * @return 	The total skill value of the classroom's ith skill
	 */
	public int getSkill_value(int i) {
		return skill_value[i];
	}

	/**
	 * A standard toString method
	 * 
	 * @return 	Returns a string representation of all the class variables
	 */
	@Override
	public String toString() {
		return "Classroom [school_name=" + school_name + ", class_name=" + class_name + ", skills="
				+ Arrays.toString(skills) + ", size=" + size + ", min_skill_value=" + Arrays.toString(min_skill_value)
				+ ", min_skill_size=" + Arrays.toString(min_skill_size) + ", skill_value="
				+ Arrays.toString(skill_value) + ", max_size="
				+ max_size + ", volunteers=" + Arrays.toString(volunteers) + "]";
	}
	
	/**
	 * Various getters and setters for each class variable
	 * Array variables accept an index variable as a parameter
	 */
	public void setSkill_value(int i, int skill_value) {
		this.skill_value[i] = skill_value;
	}

	public int getSkill_size(int i) {
		return skill_size[i];
	}

	public void setSkill_size(int i, int skill_size) {
		this.skill_size[i] = skill_size;
	}
	protected String 	getSchool_name		() {
		return school_name;
	}
	protected String 	getClass_name		() {
		return class_name;
	}
	protected String	getSkills			(int i) {
		return skills[i];
	}
	protected int 		getMin_skill_value	(int i) {
		return min_skill_value[i];
	}
	protected int 		getMin_skill_size	(int i) {
		return min_skill_size[i];
	}
	protected int		getVolunteers		(int i) {
		return volunteers[i];
	}
	protected void 		setVolunteers		(int i, int volunteers) {
		this.volunteers[i] = volunteers;
	}
	public int getMax_size() {
		return max_size;
	}

	public void setMax_size(int max_size) {
		this.max_size = max_size;
	}

	public void setsize(int size ){
		this.size = size;
	}
	public int getsize() {
		return this.size;
	}
	
	/**
	 * Seperate getters for the array variables to print them all
	 * Implemented for flexibility
	 */
	public String printMin_skill_size() {
		return Arrays.toString(min_skill_size)+" max "+max_size;
	}
	public String printskill_value() {
		return Arrays.toString(skill_value);
	}
	public String printSkill_size() {
		return Arrays.toString(skill_size);
	}
	
	
}
