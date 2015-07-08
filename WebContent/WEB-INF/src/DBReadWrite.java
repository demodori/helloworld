/* COPYLIGHT sudo LIMITED 2015 作成年月日:2015/6/3
 * ファイル名:DBReadWrite.java
 */
 
import java.io.*;
import java.util.*;
import java.sql.*;
import java.security.MessageDigest;
import java.math.BigInteger;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Hex;

/** 
 * 練習用：DB(MySQL)のSELECTとINSERT
 * @auther :yutaka sudo
 * @version:1.0
 * @since  :JDK1.7.0_25
 */
public class DBReadWrite extends HttpServlet
{
	/**
	 * ブラウザ出力用
	 */
	private PrintWriter out;
	
	/**
	 * コネクタ
	 */
	private Connection conn = null;
	
	/**
	 * DB
	 */
	private String url = "jdbc:mysql://localhost/javatest?useUnicode=true&characterEncoding=utf8";
	
	/**
	 * MySQLユーザ
	 */
	private String user = "root";
	
	/**
	 * MySQLユーザパスワード
	 */
	private String password = "";
	
	/**
	 * Servlet main GET
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException
	{
		// これを指定しないとGETが文字化けする(かも)
		request.setCharacterEncoding("UTF-8");
		
		// ブラウザ出力用設定
		response.setContentType("text/html; charset=UTF-8");
		this.out = response.getWriter();
		
		// JDBCドライバロード
		this.loadJDBCdriver();
		
		try{
			// 接続
			//if (this.conn == null) {
				this.conn = DriverManager.getConnection(this.url, this.user, this.password);
			//}
			
			// 一覧を取得
			request.setAttribute("userList", this.getList());
			
			// 接続解除
			this.conn.close();
			
			
		}catch(SQLException e){
			this.out.println(e);
		}
		
		this.out.println("ここから転送処理<br />");
		
		// 出力閉じる(forward前に閉じるとforwardが反応しなくなる)
		//this.out.close();
		
		// JSPへ転送
		getServletConfig().getServletContext().
		  getRequestDispatcher("/__user.jsp").forward(request, response);
	}
	
	
	
	/**
	 * Servlet main POST
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException
	{
		// これを指定しないとPOSTが文字化けする
		request.setCharacterEncoding("UTF-8");
		
		// ブラウザ出力用設定
		response.setContentType("text/html; charset=UTF-8");
		this.out = response.getWriter();
		
		
		// POST値取得
		Map<String, String> userData = new HashMap<String, String>();
		userData.put("id", request.getParameter("id"));
		userData.put("password", request.getParameter("password"));
		userData.put("description", request.getParameter("description"));
		
		// JDBCドライバロード
		this.loadJDBCdriver();
		
		try{
			// 接続
				this.conn = DriverManager.getConnection(this.url, this.user, this.password);
			
			// 登録
			this.regUser(userData);
			
		}catch(SQLException e){
			this.out.println(e);
		}
		
		// 一覧を表示(GET時のmain処理そのまま)
		this.doGet(request, response);
	}
	
	
	
	
	
	
	
	/** 
	 * MySQL JDBCドライバの読み込み
	 * @auther :yutaka sudo
	 */
	private void loadJDBCdriver()
	{
		String msg = "";
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			this.out.println("JDBCドライバのロードに成功しました。<br />");
		}catch (ClassNotFoundException e){
			this.out.println("JDBCドライバのロードに失敗しました。(ClassNotFound)<br />");
		}catch (Exception e){
			this.out.println("JDBCドライバのロードに失敗しました。(その他)<br />");
		}
	}
	
	/** 
	 * 一覧の取得と表示
	 * @auther :yutaka sudo
	 */
	private List getList() throws SQLException
	{
		// ユーザデータ格納用
		List<Map> userList = new ArrayList<Map>();
		
		String sql = "SELECT id, password, description FROM user;";
		Statement stmt = this.conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			Map<String, String> user = new HashMap<String, String>();
			user.put("id", rs.getString("id"));
			user.put("password", rs.getString("password"));
			user.put("description", rs.getString("description"));
			userList.add(user);
		}
		
		return userList;
	}
	
	/** 
	 * 入力データ登録
	 * @auther :yutaka sudo
	 */
	private void regUser(Map userData) throws SQLException
	{
		String sql;
		
		sql  = "INSERT INTO user (id, password, md5, description)";
		sql += " VALUES (";
		sql += "\"" + userData.get("id") + "\", ";
		sql += "\"" + userData.get("password") + "\", ";
		sql += "\"" + this.createMD5((String)userData.get("password")) + "\", ";
		//sql += DigestUtils.md5Hex(md5) + "\",\"";
		//sql += new String(Hex.encodeHex(DigestUtils.md5(md5))) + "\",\"";
		sql += "\"" + userData.get("description") + "\");";
		this.out.println(sql);
		
		Statement stmt = this.conn.createStatement();
		Boolean rs = stmt.execute(sql);
	}
	
	/** 
	 * 文字列からMD5を生成。
	 * (DigestUtilsがエラーになる為。恐らくAndroid系のcommons-codecと衝突してる)
	 * @auther :yutaka sudo
	 */
	private String createMD5(String str)
	{
		String md5_str = "";
		
		try {
			byte[] str_bytes = str.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] md5_bytes = md.digest(str_bytes);
			BigInteger big_int = new BigInteger(1, md5_bytes);
			md5_str = big_int.toString(16);
		} catch(Exception e) {
			this.out.println(e);
		}
		
		return md5_str;
	}
}