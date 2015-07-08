import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;

public class Hello extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws IOException, ServletException
	{
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.println("Hello Java World !<br />");
		
		// 課題１ ファイル読み込み
		String current = System.getProperty("user.dir");
        out.println("System.getProperty(user.dir)：" + current + "<br />");
        
		try{
			// servlet上だとcurrentからの相対パスが使えなかったので
			ServletContext context = this.getServletContext();
			String path = context.getRealPath("");
			out.println("context.getRealPath：" + path + "<br />");
			
			/*
			//  #### ①WEB-INF内(Webじゃない場合は特に関係なさそう) ####
			//File file = new File(current + "\\..\\src\\test.txt"); 文字化け
			FileInputStream fistream = new FileInputStream(current + "\\..\\src\\test.txt");
			InputStreamReader isreader = new InputStreamReader(fistream,"UTF-8");
			BufferedReader  bfreader = new BufferedReader(isreader);
			
			// 1文字ずつ出力。readの戻り値が整数の為intで定義
			int ch;
			while((ch = bfreader.read()) != -1){
				out.print((char)ch);
			}
			
			bfreader.close();
			fistream.close();
			*/
			// #### ②コンテキストルート直下 ####
			out.println("text読み込み(FileInputStream, InputStreamReader, BufferedReader)<br />");
			FileInputStream fistream = new FileInputStream(path + "\\test2.txt");
			InputStreamReader isreader = new InputStreamReader(fistream,"UTF-8");
			BufferedReader bfreader = new BufferedReader(isreader);
			
			// 1文字ずつ出力。readの戻り値が整数の為intで定義
			int ch;
			while((ch = bfreader.read()) != -1){
				out.print((char)ch);
			}
			
			bfreader.close();
			fistream.close();
			
			out.close();
			
		}catch(FileNotFoundException e){
			out.println(e);
		}catch(IOException e){
			out.println(e);
		}
	}
}