package cn.web.sign;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;  
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements; 
 public class AutoLogin {
 public static void main(String args[]) throws IOException{ 
  Scanner scanner = new Scanner(System.in);
  System.out.println("请输入用户名：");
  String user_name = scanner.next();
  System.out.println("请输入密码：");
  String password = scanner.next();
  testPost(user_name , password,"d:/fileDown2.txt");
  testJsoup();
 } 
  public static void testPost(String user_name , String password,String outPath) throws IOException{
  String login ="";  
  URL url = new URL("https://www.touna.cn/user-login.html");
  HttpURLConnection conn = null;
  conn = (HttpURLConnection) url.openConnection();//建立链接

  conn.setInstanceFollowRedirects(false);
  conn.setRequestProperty("Connection","keep-alive");
  conn.setRequestProperty("User-Agent", 
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");
  conn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
  conn.setDoInput(true);
  conn.setDoOutput(true);
//  connection.disconnect();
/*  String str = connection.getHeaderField("Location");//获得重定向的url地址
  URL newURL = new URL(str); 
//  String cookies = getCookies(connection);
  HttpURLConnection conn = (HttpURLConnection) newURL.openConnection();
  conn.setRequestProperty("Referer", str);//浏览器向 WEB 服务器表明自己是从哪个 网页/URL 获得/点击 当前请求中的网址/URL
//  conn.setRequestProperty("Cookie", cookies); //发送设置cookie： 
  conn.setRequestProperty("Connection","keep-alive");
  conn.setRequestProperty("User-Agent", 
    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1700.107 Safari/537.36");*/
  conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
  conn.setDoInput(true);
  conn.setDoOutput(true);
  OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(),"utf-8");
  login =login+"user_name="+user_name+"&"+"password="+password;
  out.write(login);
  out.flush();
  out.close();
  InputStream inputStream = conn.getInputStream();
  BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
  reader.close();
  //链接到personal页面
  String headerName = null; 
  StringBuilder myCookies = new StringBuilder();
//  myCookies.append(cookies + ";");
  for(int i =1;(headerName= conn.getHeaderFieldKey(i))!=null;i++){
    if(headerName.equals("Set-Cookie")){
     String cookie = conn.getHeaderField(i);
     cookie = cookie.substring(0, cookie.indexOf(";"));
     String cookieName = cookie.substring(0, cookie.indexOf("="));
     String cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
     myCookies.append(cookieName + "=");
     myCookies.append(cookieValue + ";");
    }
  }
  URL purl = new URL("http://www.touna.cn/bbs/bbs.do");
  HttpURLConnection pconn = (HttpURLConnection) purl.openConnection();
//  pconn.setRequestProperty("Referer", str);
  pconn.setRequestProperty("Cookie", myCookies.toString()); 
  pconn.connect();
  InputStream inputStream1 = pconn.getInputStream();
//  BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1,"utf-8"));
//  String line1 = reader1.readLine();
//  while(line1 != null){
//   System.out.println(line1);
//   line1 = reader1.readLine();
//  }
//  reader1.close();
  int chByte = 0;
  FileOutputStream fileOut = new FileOutputStream(new File(outPath));
  chByte = inputStream1.read();
  while(chByte != -1){
   fileOut.write(chByte); 
   chByte = inputStream1.read();
  }
 }
 private static String getCookies(HttpURLConnection conn) {
  // TODO Auto-generated method stub
//     StringBuffer cookies = new StringBuffer();   
     StringBuilder cookies = new StringBuilder();
           String headName;    
            for (int i = 1; (headName = conn.getHeaderField(i)) != null; i++) {    
                StringTokenizer st = new StringTokenizer(headName, "; ");    
                while (st.hasMoreTokens()) {    
                    cookies.append(st.nextToken() + "; ");   
               }   
           }              
           return cookies.toString();       
 }

 private static void testJsoup() throws IOException{
  //解析html文档  
     File input = new File("D:/fileDown2.txt");  
     Document doc = Jsoup.parse(input, "UTF-8"); 
//     for(Element ele : doc.getElementsByClass("zhnc").select("ul")){
//      if(!ele.select("li").toString().equals("")){
//       String text = ele.select("li").text();
//       System.out.println("user_name is:"+text);
//      }
//     }  
     Elements ele = doc.getElementsByClass("zhnc").select("ul");
     if(!ele.select("li").toString().equals("")){
      String text = ele.select("li").text();
      System.out.println("user_name is:"+text);
     }else{
      System.out.println("登录失败");
     }
 }
}
 