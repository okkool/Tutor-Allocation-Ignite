import java.util.Arrays;

public class Volunteer {
	private String name;
	private String email;
	private int classid;
	
	private String []	skills;
	private int [] 		skill_value;
	private int [] 		preferences;
	
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
	
}
