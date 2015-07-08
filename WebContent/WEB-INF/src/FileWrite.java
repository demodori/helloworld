/* COPYLIGHT sudo LIMITED 2015 作成年月日:2015/6/3
 * ファイル名:FileWrite.java
 */
 
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

/** 
 * 練習用：テキストファイルの読み込みとブラウザ出力
 * @auther :yutaka sudo
 * @version:1.0
 * @since  :JDK1.7.0_25
 */
public class FileWrite extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException
	{
		// ブラウザ出力用設定
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		try{
			// 書き込み場所
			ServletContext context = this.getServletContext();
			String path = context.getRealPath("");
			out.println(path);
			
			// 各Fileオブジェクト定義
			String filepath = path + "\\WEB-INF\\data\\write.txt";
			FileOutputStream fostream = new FileOutputStream(filepath , true);
			OutputStreamWriter oswriter = new OutputStreamWriter(fostream,"UTF-8");
			BufferedWriter bfwriter = new BufferedWriter(oswriter);
			
			// 書き込み
			String str1 = "これはテストです\n";
			bfwriter.write(str1, 0, str1.length());
			
			bfwriter.close();
			fostream.close();
			
			out. println(filepath + "に書き込みが成功しました。");
			
			out.close();
			
			
		}catch(FileNotFoundException e){
			out.println(e);
		}catch(IOException e){
			out.println(e);
		}
	}
}