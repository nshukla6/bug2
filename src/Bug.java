import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Bug {
	private String bugNo;
	private int filesCount;
	private List<Bug> childs;
	private boolean isParent;
	private Map<String,ArrayList<String>>branchFilesMap;
	private Set<String>branchs;
	private Bug base;
	private List<Bug>baseChilds;


	Bug(String bugNo){
		this.bugNo=bugNo;
	}
	
	
	
	public String getBugNo() {
		return bugNo;
	}
	
	public Map<String, ArrayList<String>> getBranchFilesMap() {
		return branchFilesMap;
	}
	public void setBranchFilesMap(Map<String, ArrayList<String>> branchFilesMap) {
		this.branchFilesMap = branchFilesMap;
	}
	public int getFilesCount() {
		return filesCount;
	}
	public void setFilesCount(int filesCount) {
		this.filesCount = filesCount;
	}
	public List<Bug> getChilds() {
		return childs;
	}
	public void setChilds(List<Bug> childs) {
		this.childs = childs;
	}
	public boolean isParent() {
		return isParent;
	}
	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}
	public Set<String>getBranchs() {
		return branchs;
	}

	public void setBranchs(Set<String> branchs) {
		this.branchs = branchs;
	}
	
	public Bug getBase() {
		return base;
	}



	public void setBase(Bug base) {
		this.base = base;
	}

	public List<Bug> getBaseChilds() {
		return baseChilds;
	}



	public void setBaseChilds(List<Bug> baseChilds) {
		this.baseChilds = baseChilds;
	}


	
	
	@Override
	public String toString() {
		return "Bug [bugNo=" + bugNo + ", filesCount=" + filesCount + ", childs=" + childs + ", isParent=" + isParent
				+ ", branchFilesMap=" + branchFilesMap + ", branchs=" + branchs + ", base=" + base + ", baseChilds="
				+ baseChilds + "]";
	}

	

	

}