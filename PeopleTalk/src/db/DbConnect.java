package db;

import java.sql.*;

public class DbConnect {
	private Connection c;
	private Statement st;
	public DbConnect() throws Exception{
        Class.forName("com.mysql.jdbc.Driver");
        c=DriverManager.getConnection(
"jdbc:mysql://localhost:3306/ptalk","root","incapp");
        st=c.createStatement();
    }
	public void disconnect()  throws Exception{
		if(c!=null && c.isClosed()) {
			c.close();
		}
	}
	public String doInsertUser(java.util.HashMap u)throws Exception{
        try{
        PreparedStatement insertUser=c.prepareStatement("insert into userinfo values(?,?,?,?,?,?,?,?,?,?)");
        insertUser.setString(1, (String)u.get("email"));
        insertUser.setString(2, (String)u.get("pass"));
        insertUser.setString(3, (String)u.get("name"));
        insertUser.setString(4, (String)u.get("phone"));
        insertUser.setString(5, (String)u.get("gender"));
        insertUser.setDate(6, (java.sql.Date)u.get("dob"));
        insertUser.setString(7, (String)u.get("state"));
        insertUser.setString(8, (String)u.get("city"));
        insertUser.setString(9, (String)u.get("area"));
        insertUser.setBinaryStream(10, (java.io.InputStream)u.get("photo"));
        int x=insertUser.executeUpdate();
        if(x!=0){
            return "Success";
        }else{
            return "Failed";
        }
        }catch(java.sql.SQLIntegrityConstraintViolationException ex){
            return "Already";
        }
    }
	public java.util.HashMap doCheckLogin(String e,String p)throws Exception{
		PreparedStatement checkLogin=c.prepareStatement("select * from userinfo where email=? and pass=?");
        checkLogin.setString(1, e);
        checkLogin.setString(2, p);
        ResultSet rs=checkLogin.executeQuery();
        if(rs.next()){
            java.util.HashMap userDetails=new java.util.HashMap();
            userDetails.put("name",rs.getString("name"));
            userDetails.put("email",rs.getString("email"));
            userDetails.put("phone",rs.getString("phone"));
            userDetails.put("gender", rs.getString("gender"));
            userDetails.put("dob", rs.getDate("dob"));
            userDetails.put("state", rs.getString("state"));
            userDetails.put("city", rs.getString("city"));
            userDetails.put("area", rs.getString("area"));
            return userDetails;
        }else{
            return null;
        }
    }
	public byte[] getPhoto(String e) throws Exception{
		PreparedStatement getPhoto=c.prepareStatement(
			    "select photo from userinfo where email=? ");
		getPhoto.setString(1, e);
        ResultSet rs=getPhoto.executeQuery();
        if(rs.next()){
            byte[] b=rs.getBytes("photo");
            if(b.length!=0)
                return b;
            else
                return null;
        }else{
            return null;
        }
    }
	public java.util.ArrayList<java.util.HashMap> searchPeople(String s,String ct,String e,String a) throws Exception{     
	PreparedStatement searchPeople=c.prepareStatement("select name,email,phone,dob,gender from userinfo where  state=? and city=? and email!=? and area like ? ");
	searchPeople.setString(1, s);
	searchPeople.setString(2, ct);
	searchPeople.setString(3, e);
	searchPeople.setString(4, "%"+a+"%");
	ResultSet r=searchPeople.executeQuery();
	java.util.ArrayList<java.util.HashMap> allUserDetails=
	        new java.util.ArrayList();
	while(r.next()){
	    java.util.HashMap UserDetails=new java.util.HashMap();
	    UserDetails.put("email",r.getString("email"));
	    UserDetails.put("name",r.getString("name"));
	    UserDetails.put("phone",r.getString("phone"));
	    UserDetails.put("gender",r.getString("gender"));
	    UserDetails.put("dob",r.getDate("dob"));
	    allUserDetails.add(UserDetails);
	    }
	return allUserDetails;
	}
	public java.util.HashMap getUserByEmail(String e)throws Exception{
		PreparedStatement getUserByEmail=c.prepareStatement("select * from userinfo where email=?");
		getUserByEmail.setString(1, e);
        ResultSet rs=getUserByEmail.executeQuery();
        if(rs.next()){
            java.util.HashMap userDetails=new java.util.HashMap();
            userDetails.put("name",rs.getString("name"));
            userDetails.put("email",rs.getString("email"));
            userDetails.put("phone",rs.getString("phone"));
            userDetails.put("gender", rs.getString("gender"));
            userDetails.put("dob", rs.getDate("dob"));
            userDetails.put("state", rs.getString("state"));
            userDetails.put("city", rs.getString("city"));
            userDetails.put("area", rs.getString("area"));
            return userDetails;
        }else{
            return null;
        }
	}
    public String sendMessage(String s,String r,String m,String f,java.io.InputStream in) throws SQLException {        
    	PreparedStatement sendMessage=c.prepareStatement(
    		    "insert into peoplemsg  (sid,rid,msg,filename,ufile,udate) values (?,?,?,?,?,now())");
    	sendMessage.setString(1, s);
        sendMessage.setString(2, r);
        sendMessage.setString(3, m);
        sendMessage.setString(4, f);
        sendMessage.setBinaryStream(5, in);
        int x=sendMessage.executeUpdate();
        if(x==1)
         return "Success";
        else 
         return "Error";
    }
    public java.util.ArrayList<java.util.HashMap> getMessages(String s,String r) throws SQLException{     
    	PreparedStatement getMessages=c.prepareStatement(
    		    "select * from peoplemsg where sid=? and rid=? ");
    	getMessages.setString(1, s);
    	getMessages.setString(2, r);
    	ResultSet rs=getMessages.executeQuery();
    	java.util.ArrayList<java.util.HashMap> allMessage=
    	        new java.util.ArrayList();
    	while(rs.next()){
    	    java.util.HashMap message=new java.util.HashMap();
    	    message.put("message",rs.getString("msg"));
    	    message.put("datetime",rs.getString("udate"));
    	    message.put("filename",rs.getString("filename"));
    	    message.put("file",rs.getBytes("ufile"));
    	    message.put("pid",rs.getString("pid"));
    	    allMessage.add(message);
    	    }
    	return allMessage;
    }
    public byte[] getFile(int e){
    	
        try{
        	PreparedStatement getFile=c.prepareStatement(
        		    "select ufile from peoplemsg where pid=? ");
            getFile.setInt(1, e);
            ResultSet rs=getFile.executeQuery();
            if(rs.next()){
                byte[] b=rs.getBytes("ufile");
                if(b.length!=0)
                    return b;
                else
                    return null;
            }else{
                return null;
            }
        }catch(Exception ex){
            return null;
        }
    }
}
