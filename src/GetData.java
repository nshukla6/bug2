import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetData
 */
@WebServlet(description = "get the bugzilla data", urlPatterns = { "/GetData" })
public class GetData extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection con = null;
	private ResultSet resultSet=null;
	private PreparedStatement preparedStatement=null;
	private  Set<String> totalBugSet=new HashSet<String>();
	private Set<String>branchs;
	
	
	
	/*String queryAllBugs = new StringBuilder()
	           .append("SELECT b.bug_id FROM bugs b ")
	           .append("JOIN profiles p ON p.userid=b.assigned_to ")
	           .append("JOIN products pr ON pr.id=b.found_in_product_id ")
	           .append("JOIN versions v ON v.id=b.found_in_version_id ")
	           .append("WHERE pr.name=? and p.login_name=? and v.name=? ")
	           .append("and b.resolution ='fixed' and b.bug_status in ('closed','resolved') ")
	           .toString();*/
	
	String queryAllBugs=new StringBuilder()
			.append("SELECT b.bug_id FROM bugs b ")
			.append("JOIN profiles p ON p.userid=b.assigned_to ")
			.append(" where p.login_name=? ")
			.append("and b.resolution ='fixed' ")
			.append(" and b.bug_status in ('closed','resolved') ")
			.append("and b.bug_id in ( ")
			.append("SELECT bug_id from bug_fix_by_map bmap ")
			.append("join versions v  on v.id = bmap.version_id ")
			.append("join products pr ON pr.id=bmap.product_id ")
			.append("where pr.name=? and v.name=? ) ")
			.toString();
	
	
	
	
	 
    
   
      
   
            







    /**
     * Default constructor. 
     * @throws UnavailableException 
     */
    public GetData() throws UnavailableException {
        // TODO Auto-generated constructor stub
    
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		
		super.init(config);
	       
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		System.out.println("inside service");
		branchs=new HashSet<>();
		String product=(String)request.getParameter("product");
		String email=(String)request.getParameter("email");
		String version=(String)request.getParameter("version");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("***********class not found********");
			e1.printStackTrace();
		}
 	      try {
			con = DriverManager.getConnection(
			    "jdbc:mysql://bz3-m-db3.eng.vmware.com:3306/bugzilla", "mts", "mts");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("******DB not found**********");
			e1.printStackTrace();
		}
		List<Bug>bugList=new ArrayList<Bug>();
		List<String> list;
		
		try {
			list = getBugList(queryAllBugs,product,email,version);
			for (String bugNo : list) {
				
				
			
			
			Bug bug=new Bug(bugNo);
			List<String> childList=getChildList(bugNo);
			bug.setBase(getBase(bugNo));
			bug.setBranchs(getCommitedBranch(bugNo));
			//bug.setBranchFilesMap(getCommitedBranchFiles(bugNo));
			//bug.setFilesCount(getAffectedFilesCount(bugNo));
			bug.setParent(childList.size()>0);
			
			if(bug.getBase()!=null){
				List<String>childs=getBaseChildList(bug.getBase().getBugNo());
				List<Bug> childBugs=new ArrayList<Bug>();
				for (String ch : childs) {
					Bug chBug=new Bug(ch);
					//chBug.setBranchFilesMap(getCommitedBranchFiles(ch));
					//chBug.setFilesCount(getAffectedFilesCount(ch));
					//chBug.setParent(getChildList(ch).size()>0);
					chBug.setBranchs(getCommitedBranch(ch));
					childBugs.add(chBug);
				}
				bug.setBaseChilds(childBugs);
				
				
			}
			
			
			
			bug.setBranchs(getCommitedBranch(bugNo));
			//bug.setBranchFilesMap(getCommitedBranchFiles(bugNo));
			//bug.setFilesCount(getAffectedFilesCount(bugNo));
			bug.setParent(childList.size()>0);
			if(bug.isParent()){
				List<String>childs=childList;
				List<Bug> childBugs=new ArrayList<Bug>();
				for (String ch : childs) {
					Bug chBug=new Bug(ch);
					//chBug.setBranchFilesMap(getCommitedBranchFiles(ch));
					//chBug.setFilesCount(getAffectedFilesCount(ch));
					//chBug.setParent(getChildList(ch).size()>0);
					chBug.setBranchs(getCommitedBranch(ch));
					childBugs.add(chBug);
				}
				bug.setChilds(childBugs);
				
				
				
				
			}
			bugList.add(bug);
			//System.out.println(bug);
			}
			con.close();
			request.setAttribute("email", email);
			request.setAttribute("product", product);
			request.setAttribute("fixBy", version);
			
			request.setAttribute("bugList", bugList);
			request.setAttribute("branchs", branchs.toArray());
			request.setAttribute("blength", branchs.toArray().length);
			RequestDispatcher dispatcher = request.getRequestDispatcher("result2.jsp");  
			if (dispatcher != null){  
			dispatcher.forward(request, response);  
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private List<String> getBugList(String query,String product,String email,String version) throws SQLException {
		List<String> bugList=new ArrayList<>();
		preparedStatement = con.prepareStatement(query);
		
		preparedStatement.setString(1, email);//get from req
	    preparedStatement.setString(2, product);//get from req
	    preparedStatement.setString(3, version);//get from req
	    
	    resultSet = preparedStatement.executeQuery();
	    while (resultSet.next()) {
	      
	      String bugID = resultSet.getString("bug_id");
	      bugList.add(bugID);
	      
	    }
	    return bugList;
	  }
	
	private  List<String> getChildList(String bug) throws SQLException{
	    
	   
		String childQuery=new StringBuilder().append("SELECT child from related where base=?").toString();
		
        preparedStatement = con.prepareStatement(childQuery);
        
		
			List<String> childBugList=new ArrayList<>();
			preparedStatement.setString(1,bug);
	        resultSet = preparedStatement.executeQuery();
	        totalBugSet.add(bug);
	        while (resultSet.next()) {
	  	      
	  	      String bugID = resultSet.getString("child");
	  	      childBugList.add(bugID);
	  	      
	  	      
	  	    }
	        
	        
	        
			
		
		preparedStatement=null;
		resultSet=null;
		return childBugList;
		
		
	}
	
	private String getText(String bug) throws SQLException{
		
		 
		 String theTextQuery=new StringBuilder().append("SELECT thetext from longdescs where bug_id=? and thetext like ?").toString();
		 preparedStatement = con.prepareStatement(theTextQuery);
		 
			 StringBuffer sb=new StringBuffer();
			 preparedStatement.setString(1,bug);
			 preparedStatement.setString(2,"%Affected files (%");
			 resultSet = preparedStatement.executeQuery();
			 while (resultSet.next()) {
		  	      
		  	      String text = resultSet.getString("thetext");
		  	      //theText.add(text);
		  	      sb.append(text);
		  	     
		  	      
		  	    }
		       
		        
			
		 preparedStatement=null;
		 resultSet=null;
		return sb.toString();
		
	}
	
	
	/*private Map<String,Integer>getCommitedFilesCountMap(Map<String,String> textMap){
		int count=0;
		Map<String,Integer>map=new HashMap<String, Integer>();
		for (Map.Entry<String, String> entry : textMap.entrySet()) {
			
			count=getAffectedFilesCount(entry.getValue());
			map.put(entry.getKey(), count);
  	}
		
		return map;
	}*/
	
	/*private int getAffectedFilesCount(String bug){
		int count=0;
		String text="";
		try {
			text = getText(bug);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		 Pattern pattern = Pattern.compile("\\((\\d|\\d\\d)\\)");
		 Matcher matcher = pattern.matcher(text);
		 while (matcher.find()) {
	            
	         count+=Integer.parseInt(matcher.group(1));
	         
	         
	          }
		return count;
	}*/
	/*private Map<String,Map<String,ArrayList<String>>> getCommitedBranchFilesMap(Map<String,String> textMap) {
		Map<String,Map<String,ArrayList<String>>> map=new HashMap<>();
		Map<String,ArrayList<String>> fileMap=new HashMap<>();
        for (Map.Entry<String, String> entry : textMap.entrySet()) {
			
			fileMap=getCommitedBranchFiles(entry.getValue());
			map.put(entry.getKey(), fileMap);
  	}
		return map;
	}*/
	

	
	/*private Map<String,ArrayList<String>> getCommitedBranchFiles(String bug){
		
		String text="";
		try {
			text = getText(bug);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pattern1="... //depot/";
		String pattern2="#";
		String regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
		Pattern pattern=Pattern.compile(regexString);
		Matcher matcher = pattern.matcher(text);
		Map<String,ArrayList<String>>map=new HashMap<>();
		
		while (matcher.find()) {
            
             String[] arr = matcher.group(1).split("/");
             //System.out.println(arr[1]+"  "+arr[arr.length-1]);
             //map.put(arr[1], arr[arr.length-1]);
             if(map.get(arr[1])!=null)
             map.get(arr[1]).add(arr[arr.length-1]);
             else{
            	 ArrayList<String>list=new ArrayList<>();
            	 list.add(arr[arr.length-1]);
                 map.put(arr[1], list);
             }
          }
		return map;
		
	}*/
private Set<String>getCommitedBranch(String bug){
	
	String branch=null;
		String text="";
		try {
			text = getText(bug);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String pattern1="... //depot/";
		String pattern2="#";
		String regexString = Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2);
		Pattern pattern=Pattern.compile(regexString);
		Matcher matcher = pattern.matcher(text);
		HashSet<String>set=new HashSet<>();
		
		
		while (matcher.find()) {
            
             String[] arr = matcher.group(1).split("/");
             
            // System.out.println(arr[1]+"  "+arr[arr.length-1]);
             branch=arr[1];
             branchs.add(branch);
             //map.put(arr[1], arr[arr.length-1]);
             set.add(branch);
          }
		return set;
		
	}

private  Bug getBase(String bug) throws SQLException{
    
	String baseBug=null;
	String baseQuery=new StringBuilder().append("SELECT base from related where child=?").toString();
	
    preparedStatement = con.prepareStatement(baseQuery);
    
	
		
		preparedStatement.setString(1,bug);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
  	      
  	      baseBug = resultSet.getString("base");
  	      
  	      
  	      
  	    }
        
	preparedStatement=null;
	resultSet=null;
	return new Bug(baseBug);
	
	
}
private  List<String> getBaseChildList(String bug) throws SQLException{
    
	   
	String childQuery=new StringBuilder().append("SELECT child from related where base=?").toString();
	
    preparedStatement = con.prepareStatement(childQuery);
    
	
		List<String> childBugList=new ArrayList<>();
		preparedStatement.setString(1,bug);
        resultSet = preparedStatement.executeQuery();
        
        while (resultSet.next()) {
  	      
  	      String bugID = resultSet.getString("child");
  	      childBugList.add(bugID);
  	      
  	      
  	    }
        
        
        
		
	
	preparedStatement=null;
	resultSet=null;
	return childBugList;
	
	
}
	

}