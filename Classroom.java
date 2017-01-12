import java.security.InvalidAlgorithmParameterException;
import java.util.Arrays;

public class Classroom {
	private String school_name;
	private String class_name;
	int 				size;
	private int [] 		min_skill_value;
	private int [] 		min_skill_size;
	private int [] 		skill_value;
	private int [] 		skill_size;
	private int 		max_size;
	private int []		volunteers;
	
	public Classroom(String school_name, String class_name,int[] min_skill_value,
			int[] min_skill_size, int max_size) throws InvalidAlgorithmParameterException {
		super();
		this.school_name 		= school_name;
		this.class_name 		= class_name;
		this.min_skill_value 	= min_skill_value;
		this.min_skill_size 	= min_skill_size;
		this.max_size			= max_size;
		
		if (this.min_skill_value.length<1){throw new InvalidAlgorithmParameterException("Class \""+this.class_name+"\" in school \""+this.school_name+"\" can not have a total of "+this.min_skill_value.length+" different skills.");}
		if (this.min_skill_value.length!=this.min_skill_size.length){throw new InvalidAlgorithmParameterException("Class \""+this.class_name+"\" in school \""+this.school_name+"\" has no value for one or more skills.");}
		if (max_size<1){throw new InvalidAlgorithmParameterException("Class \""+this.class_name+"\" in school \""+this.school_name+"\" can not have max size "+max_size+".");}
		
		this.size = 0;
		volunteers 	= new int [this.max_size];
		skill_value = new int [this.min_skill_value.length];
		for (int i = 0; i < volunteers.length; i++) {
			volunteers[i]=-1;
		}
		skill_size 	= new int [this.min_skill_value.length];
	}

	public int getSkill_value(int i) {
		return skill_value[i];
	}

	@Override
	public String toString() {
		return "Classroom [School Name = " + school_name + ",  Class Name = " + class_name + ",  Size = " + size + ",  Min. Skill Value =" + Arrays.toString(min_skill_value)
				+ ",  Min. Skill Size = " + Arrays.toString(min_skill_size) + ",  Skill Value = "
				+ Arrays.toString(skill_value) + ",  Max. Size = "
				+ max_size + ",  Volunteers = " + Arrays.toString(volunteers) + "]";
	}
	public String toStringSummary() {
		return  String.format("%-64s",school_name) + " " + String.format("%-32s",class_name) + " Max Size = " +String.format("%-2s", max_size) + " " + String.format("%-"+(this.min_skill_size.length*4+1)+"s", Arrays.toString(min_skill_size))+",  Skills Needed = "
				+ Arrays.toString(skill_value) ;
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
		// TODO Auto-generated method stub
		return this.size;
	}

	public String printMin_skill_size() {
		return Arrays.toString(min_skill_size)+",  max "+max_size;
	}
	public String printskill_value() {
		return Arrays.toString(skill_value);
	}
	public String printSkill_size() {
		return Arrays.toString(skill_size);
	}
	
	
}
