import java.security.InvalidAlgorithmParameterException;
import java.util.Arrays;

public class Classroom {
	private String school_name;
	private String class_name;
	
	private String []	skills;
	private int [] 		min_skill_value;
	private int [] 		min_skill_size;
	private int [] 		skill_value;
	private int [] 		skill_size;
	private int 		max_size;
	private int []		volunteers;
	
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
		
		volunteers 	= new int [this.max_size];
		skill_value = new int [this.max_size];
		skill_size 	= new int [this.max_size];
	}

	@Override
	public    String 	toString() {
		return "Classroom [school_name=" + school_name + ", class_name=" + class_name + ", skills="
				+ Arrays.toString(skills) + ", min_skill_value=" + Arrays.toString(min_skill_value)
				+ ", min_skill_size=" + Arrays.toString(min_skill_size) + ", max_size=" + max_size + ", volunteers="
				+ Arrays.toString(volunteers) + "]";
	}
	public int getSkill_value(int i) {
		return skill_value[i];
	}

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
	
	
	
	
}
