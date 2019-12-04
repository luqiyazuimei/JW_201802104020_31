import java.io.Serializable;

public final class Teacher implements Comparable<Teacher>,Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private ProfTitle profTitle;
	private Degree degree;
	private Department department;
	private String no;

	public Teacher(Integer id,
				   String name,
				   ProfTitle title,
				   Degree degree,
				   Department department,
				   String no) {
		this(name, title, degree, department,no);
		this.id = id;

	}
	public Teacher(
			String name,
			ProfTitle title,
			Degree degree,
			Department department,
			String no) {
		super();
		this.name = name;
		this.profTitle = title;
		this.degree = degree;
		this.department = department;
		this.no = no;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ProfTitle getTitle() {
		return this.profTitle;
	}

	public void setTitle(ProfTitle title) {
		this.profTitle = title;
	}

	public Degree getDegree() {
		return degree;
	}

	public void setDegree(Degree degree) {
		this.degree = degree;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getNo(){return no;}

	public void setNo(String no) { this.no = no; }

	@Override
	public int compareTo(Teacher o) {
		return this.id-o.getId();
	}

	public String toString()
	{
		final String TAB = "    ";

		String retValue = "";

		retValue = "Teacher ( "
				+ super.toString() + TAB
				+ "id = " + this.id + TAB
				+ "name = " + this.name + TAB
				+ "title = " + this.profTitle + TAB
				+ "degree = " + this.degree + TAB
				+ "department = " + this.department + TAB
				+ " )";

		return retValue;
	}

}
