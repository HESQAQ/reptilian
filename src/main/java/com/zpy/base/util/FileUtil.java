package com.zpy.base.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileUtil {
    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);



	public static String downLoadFromUrl(String urlStr, String fileName,
									   String savePath) throws Exception {
		HttpsURLConnection httpsConn = httpsResult(urlStr);
		// 得到输入流
		InputStream inputStream = httpsConn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);
		String text = new String(getData);
		if (text.startsWith("{")) {
			return "no";
		}else {
			// 文件保存位置
			File saveDir = new File(savePath);
			if (!saveDir.exists()) {
				saveDir.mkdir();
			}
			File file = new File(saveDir + File.separator + fileName );
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(getData);
			if (fos != null) {
				fos.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			return "ok";
		}
	}

	/**
	 * 请求https的通用方法
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 * @throws Exception
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	private static HttpsURLConnection httpsResult(String urlStr)
			throws MalformedURLException, Exception, NoSuchAlgorithmException,
			NoSuchProviderException, KeyManagementException, IOException {
		URL url = new URL(urlStr);
		System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl");
		System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return urlHostName.equals(session.getPeerHost());
			}
		};
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("TLS", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());

		// 从上述SSLContext对象中得到SSLSocketFactory对象

		// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
		HttpsURLConnection httpsConn = (HttpsURLConnection) url
				.openConnection();

		// 设置超时间为3秒
		httpsConn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		httpsConn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		return httpsConn;
	}

	public static String convertStreamToString(InputStream is) throws  Exception{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}

	/**
	 * 从输入流中获取字节数组
	 *
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream)
			throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}


	/**
	 * 把文件从原始路径拷贝到新路径下并删除原来路径下的文件
	 *@author: jwdstef
	 *Date:Sep 28, 2010 4:45:39 PM
	 *@param oldPath 旧的文件目录
	 *@param newPath 新的文件目录
	 *@throws Exception
	 */
	public static void transferFile(String oldPath,String newPath) throws Exception {
		logger.info("oldPath={},newPath={}",oldPath,newPath);
		int byteread = 0;
		File oldFile = new File(oldPath);
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try{
			if(oldFile.exists()){
				boolean movfile = oldFile.renameTo(new File(newPath));
				logger.info("移动文件={} ={}",oldFile,newPath);
//				oldFile.renameTo(new File(newPath));
//				fin = new FileInputStream(oldFile);
//				fout = new FileOutputStream(newPath);
//				byte[] buffer = new byte[fin.available()];
//				while( (byteread = fin.read(buffer)) != -1){
//					fout.write(buffer,0,byteread);
//				}
//				if(fin != null){
//					fin.close();
//					delFile(oldFile);
//				}
			}else{
				throw new Exception("需要转移的文件不存在!");
			}
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			if(fin != null){
				fin.close();
			}
		}
	}


	/**
	 * 删除文件,只支持删除文件,不支持删除目录
	 * @param file 要删除的文件
	 * @throws Exception
	 */
	public static void delFile(File file) throws Exception {
		if(!file.exists()) {
			throw new Exception("文件"+file.getName()+"不存在,请确认!");
		}
		if(file.isFile()){
			if(file.canWrite()){
				file.delete();
			}else{
				throw new Exception("文件"+file.getName()+"只读,无法删除,请手动删除!");
			}
		}else{
			throw new Exception("文件"+file.getName()+"不是一个标准的文件,有可能为目录,请确认!");
		}
	}

	public static final String sendHttpsAndDown(String urlStr, String fileName,
													  String savePath) {
		String responseContent = null;
		HttpClient httpClient = new DefaultHttpClient();
		//创建TrustManager
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		//这个好像是HOST验证
		X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
			public void verify(String arg0, SSLSocket arg1) throws IOException {}
			public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {}
			public void verify(String arg0, X509Certificate arg1) throws SSLException {}
		};
		try {
			//TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");
			//使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);
			//创建SSLSocketFactory
			org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(ctx);
			socketFactory.setHostnameVerifier(hostnameVerifier);
			//通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
			HttpPost httpPost = new HttpPost(urlStr);
			List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 构建POST请求的表单参数
           /* for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }*/
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (entity != null) {
				// 得到输入流
				InputStream inputStream = entity.getContent();


				// 获取自己数组
				byte[] getData = readInputStream(inputStream);

				String text = new String(getData);
				if (text.startsWith("{")) {
					return "no";
				}else {
					// 文件保存位置
					File saveDir = new File(savePath);
					if (!saveDir.exists()) {
						saveDir.mkdir();
					}

					File file = new File(saveDir + File.separator + fileName );
					FileOutputStream fos = new FileOutputStream(file);
					fos.write(getData);
					if (fos != null) {
						fos.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
					return "ok";
				}
//				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
//			logger.info(responseContent);
			///*HttpsURLConnection httpsConn = httpsResult(urlStr);

		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpClient.getConnectionManager().shutdown();
		}
		return responseContent;
	}

	/**
	 * 发送HTTPS	POST请求
	 *
	 * @param //要访问的HTTPS地址,POST访问的参数Map对象
	 * @return  返回响应值
	 * */
	public static final String sendHttpsRequestByPost(String url, Map<String, String> params) {
		String responseContent = null;
		HttpClient httpClient = new DefaultHttpClient();
		//创建TrustManager
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		//这个好像是HOST验证
		X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
			public void verify(String arg0, SSLSocket arg1) throws IOException {}
			public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {}
			public void verify(String arg0, X509Certificate arg1) throws SSLException {}
		};
		try {
			//TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");
			//使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);
			//创建SSLSocketFactory
			org.apache.http.conn.ssl.SSLSocketFactory socketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(ctx);
			socketFactory.setHostnameVerifier(hostnameVerifier);
			//通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
            //System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 构建POST请求的表单参数
           /* for (Map.Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }*/
			httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpClient.getConnectionManager().shutdown();
		}
		return responseContent;
	}
}
