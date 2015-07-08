/* COPYLIGHT sudo LIMITED 2015 作成年月日:2015/6/4
 * ファイル名:SessionCookie.java
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
 * 練習用：SessionとCookie
 * @auther :yutaka sudo
 * @version:1.0
 * @since  :JDK1.7.0_25
 */
public class SessionCookie extends HttpServlet {
	/**
	 * ブラウザ出力用
	 */
	private PrintWriter out;
	
	/**
	 * Servlet main GET
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
		
		// これを指定しないとGETが文字化けする(かも)
		request.setCharacterEncoding("UTF-8");
		
		// ブラウザ出力用設定
		response.setContentType("text/html; charset=UTF-8");
		this.out = response.getWriter();
		
		// ログイン判定用
		boolean isAuth = false;
		
		// 入力値取得
		String loginId = request.getParameter("loginid");
		String password = request.getParameter("password");
		
		// session開始
		HttpSession session = request.getSession(true);
		if (session.getAttribute("isAuth") == null) {
			session.setAttribute("isAuth", false);
		}
		
		// ログアウト判定
		if (request.getParameter("logout") != null) {
			session.setAttribute("isAuth", false);
		}
		
		// ログイン判定
		if (loginId != null && password != null) {
			if (loginId.equals("demodori") && password.equals("demo")) {
					this.out.println("OK");
					session.setAttribute("isAuth", true);
			}
		}
		
		try{
			// ログイン画面表示
			if (!(Boolean)session.getAttribute("isAuth")) {
				this.viewLogin();
			} else {
				this.out.println("ログイン済みです。<br />");
				this.out.println("<a href=\"/helloworld/index.html\">⇒このままTOPへ</a><br />");
				this.out.println("<a href=\"/helloworld/SessionCookie?logout=1\">⇒ログアウト</a><br />");
			}
			
			
		}catch(Exception e){
			this.out.println(e);
		}
	}
	
	/** 
	 * MySQL JDBCドライバの読み込み
	 * @auther :yutaka sudo
	 */
	private void viewLogin() {
		this.out.println("<html>");
		this.out.println("<head><title>session/cookie</title></head>");
		this.out.println("<body>");
		this.out.println("<form action=\"/helloworld/SessionCookie\" method=\"get\">");
		this.out.println("  ID:<input type=\"text\" name=\"loginid\" value=\"\" /><br />");
		this.out.println("  PW:<input type=\"password\" name=\"password\" value=\"\" /><br />");
		this.out.println("  <input type=\"submit\" name=\"btLogin\" value=\"　ログイン　\" /><br /><br />");
		this.out.println("</form>");
		this.out.println("<a href=\"/helloworld/index.html\">⇒TOPへ</a><br />");
		this.out.println("</body>");
		this.out.println("<html>");
	}
}