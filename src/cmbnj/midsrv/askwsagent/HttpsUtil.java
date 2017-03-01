package cmbnj.midsrv.askwsagent;

import org.apache.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
//import org.apache.commons.
//import org.apache.log4j.Logger;

import com.google.gson.Gson;

//import com.cmb.cmcc.offline.listener.Bootstrap;

/**
 * Created by 80274861 on 2015/3/16.
 */
public class HttpsUtil {
	private static final Logger logger = Logger.getLogger(HttpsUtil.class);

	public static String SendAndResp(String jsonmsg) {
		CloseableHttpClient client = null;
		try {

			StringEntity entity = new StringEntity(jsonmsg, "UTF-8");
			entity.setContentType("application/json");
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
//			HttpPost post = new HttpPost("https://99.40.15.72:8081/DRTPAY");
			HttpPost post = new HttpPost("https://99.40.26.79:8081/DRTPAY");
			post.setHeader(HTTP.CONTENT_TYPE, "application/json");
			post.setEntity(entity);


			client = createSSLClientDefault();
			CloseableHttpResponse response = client.execute(post);
			// if (response.getAllHeaders() != null)
			// for (Header h : response.getAllHeaders()) {
			// log.info("收到返回HEADER:" + h.getName() + " -> "
			// + h.getValue());
			// }

			 String resmsg = EntityUtils.toString(response.getEntity(), "UTF-8");
			 String encoderMsg = URLDecoder.decode(resmsg, "UTF-8");

			 return encoderMsg;
			 
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (client != null) {
				try {
					client.close();
					client = null;
				} catch (IOException e) {
//					log.error("关闭HTTPS客户端异常", e);
					System.out.println("关闭HTTPS客户端异常"+e);
				}
			}
		}
	}

	private static CloseableHttpClient createSSLClientDefault() {

		try {		
//			
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						// 信任所有
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			RequestConfig defaultRequestConfig = RequestConfig.custom()
					.setSocketTimeout(30000).setConnectTimeout(5000)
					.setConnectionRequestTimeout(5000).build();

//			log.info("返回自定义HTTPS客户端");
			System.out.println("返回自定义HTTPS客户端");
			return HttpClients.custom().setSSLSocketFactory(sslsf)
					.setDefaultRequestConfig(defaultRequestConfig)
					.setRetryHandler(new HttpRequestRetryHandler() {
						@Override
						public boolean retryRequest(IOException arg0, int arg1,
								HttpContext arg2) {
							// 不retry
							return false;
						}
					}).build();

		} catch (Exception e) {
//			log.error("创建HTTPS客户端异常!!!!!!", e);
			System.out.println("创建HTTPS客户端异常!!!!!!"+e);
			return HttpClients.createDefault();
		}

	}
	
	
	
	
	
	public static void testCase1(){
		logger.debug("\n\n\n\nThis is CASE1 START----------------------------------------------------------------------------------.");

		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 1, 10001);
		PayJson.paydetail onetrs = pj.new paydetail();
		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.merser = merser;
		//不存在的户口号
		onetrs.accno = "6226095710002466";
		onetrs.accnam ="测试";
		onetrs.trnamt = 10001;
		onetrs.agrnbr = "987654321";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE1 END----------------------------------------------------------------------------------.");

	}
	
	public static void testCase2(){
		logger.debug("\n\n\n\nThis is CASE2 START----------------------------------------------------------------------------------.");
		int trnamt = 10002;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 1, trnamt);
		PayJson.paydetail onetrs = pj.new paydetail();
		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.merser = merser;
		//户名不匹配
		onetrs.accno = "6226095710002465";
		onetrs.accnam ="测试";
		onetrs.trnamt = trnamt;
		onetrs.agrnbr = "987654321";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE2 END----------------------------------------------------------------------------------.");

	}

	public static void testCase3(){
		int trnamt = 10000004;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 1, trnamt);
		PayJson.paydetail onetrs = pj.new paydetail();
		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.merser = merser;
		//余额不足
		onetrs.accno = "6225880250347888";
		onetrs.accnam ="鼓测四";
		onetrs.trnamt = trnamt;
		onetrs.agrnbr = "testCase3";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}

	//汇总金额不对
	public static void testCase4(){
		int trnamt = 10000004;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 1, trnamt);
		PayJson.paydetail onetrs = pj.new paydetail();
		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.merser = merser;
		//
		onetrs.accno = "6225880250347888";
		onetrs.accnam ="鼓测四";
		onetrs.trnamt = 10004;
		onetrs.agrnbr = "testCase4";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}
	
	//汇总笔数不对
	public static void testCase5(){
		int trnamt = 10000004;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 2, trnamt);
		PayJson.paydetail onetrs = pj.new paydetail();
		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.merser = merser;
		//
		onetrs.accno = "6225880250347888";
		onetrs.accnam ="鼓测四";
		onetrs.trnamt = trnamt;
		onetrs.agrnbr = "testCase5";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}

	
	//正常测试
	public static void testCase6(){
		int trnamt = 20007;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 1, trnamt);
		PayJson.paydetail onetrs = pj.new paydetail();
		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.merser = merser;
		//
		onetrs.accno = "6226095710002465";
		onetrs.accnam ="邬小维03";
		onetrs.trnamt = trnamt;
		onetrs.agrnbr = "testCase6";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}

	
	/*
	 * 1.含2笔扣款，户口号A金额111.1；户口号B金额111.2
2.户口A的扣款信息一切正常
3.户口B不存在
	 * */
	public static void testCase7(){
		int trnamt1 = 11120;
		int trnamt2 = 11121;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 2, trnamt1+trnamt2);
		
		//正常
		PayJson.paydetail onetrs = pj.new paydetail();		
		onetrs.merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.accno = "6226095710002465";
		onetrs.accnam ="邬小维03";
		onetrs.trnamt = trnamt1;
		onetrs.agrnbr = "testCase7";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		//正常
		PayJson.paydetail twotrs = pj.new paydetail();		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		twotrs.merser = merser;
		twotrs.accno = "6225880250347889";
		twotrs.accnam ="鼓测四";
		twotrs.trnamt = trnamt2;
		twotrs.agrnbr = "testCase7";
		twotrs.telnbr = "745896325";
		pj.details.add(twotrs);

		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}
	
/*1.含2笔扣款，户口号A6225880250347888金额11200.1；户口号B6226095710002465金额112.2
2.户口A的扣款余额不足
3.户口B户名不符*/
	public static void testCase8(){
		int trnamt1 = 1120010;
		int trnamt2 = 11220;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 2, trnamt1+trnamt2);
		
		//
		PayJson.paydetail onetrs = pj.new paydetail();		
		onetrs.merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.accno = "6225880250347888";
		onetrs.accnam ="鼓测四";
		onetrs.trnamt = trnamt1;
		onetrs.agrnbr = "testCase8";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		//
		PayJson.paydetail twotrs = pj.new paydetail();		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		twotrs.merser = merser;
		twotrs.accno = "6226095710002465";
		twotrs.accnam ="鼓测";
		twotrs.trnamt = trnamt2;
		twotrs.agrnbr = "testCase8";
		twotrs.telnbr = "745896325";
		pj.details.add(twotrs);

		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}

	
	
	/*1.含2笔扣款，户口号A金额114.1；户口号B金额114.2
2.汇总笔数与明细不符*/
	public static void testCase9(){
		int trnamt1 = 11410;
		int trnamt2 = 11420;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 3, trnamt1+trnamt2);
		
		//
		PayJson.paydetail onetrs = pj.new paydetail();		
		onetrs.merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.accno = "6225880250347888";
		onetrs.accnam ="鼓测四";
		onetrs.trnamt = trnamt1;
		onetrs.agrnbr = "testCase8";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		//
		PayJson.paydetail twotrs = pj.new paydetail();		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		twotrs.merser = merser;
		twotrs.accno = "6226095710002465";
		twotrs.accnam ="鼓测";
		twotrs.trnamt = trnamt2;
		twotrs.agrnbr = "testCase8";
		twotrs.telnbr = "745896325";
		pj.details.add(twotrs);

		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}

/*
 * 1.含2笔扣款，户口号A金额115.1；户口号B金额115.2
2.汇总金额与明细不符*/
	public static void testCase10(){
		int trnamt1 = 11510;
		int trnamt2 = 11520;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 2, trnamt1+trnamt2+1);
		
		//
		PayJson.paydetail onetrs = pj.new paydetail();		
		onetrs.merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.accno = "6225880250347888";
		onetrs.accnam ="鼓测四";
		onetrs.trnamt = trnamt1;
		onetrs.agrnbr = "testCase8";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		//
		PayJson.paydetail twotrs = pj.new paydetail();		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		twotrs.merser = merser;
		twotrs.accno = "6226095710002465";
		twotrs.accnam ="鼓测";
		twotrs.trnamt = trnamt2;
		twotrs.agrnbr = "testCase8";
		twotrs.telnbr = "745896325";
		pj.details.add(twotrs);

		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}

	
	/*1.含2笔扣款，户口号A金额116.1；户口号B金额11600.2
2.户口A的扣款信息一切正常
3.户口B扣款余额不足*/
	public static void testCase11(){
		int trnamt1 = 11610;
		int trnamt2 = 1160020;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 2, trnamt1+trnamt2);
		
		//
		PayJson.paydetail onetrs = pj.new paydetail();		
		onetrs.merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.accno = "6226095710002465";
		onetrs.accnam ="邬小维03";
		onetrs.trnamt = trnamt1;
		onetrs.agrnbr = "testCase11";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		//
		PayJson.paydetail twotrs = pj.new paydetail();		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		twotrs.merser = merser;
		twotrs.accno = "6225880250347888";
		twotrs.accnam ="鼓测四";
		twotrs.trnamt = trnamt2;
		twotrs.agrnbr = "testCase11";
		twotrs.telnbr = "745896325";
		pj.details.add(twotrs);

		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}

	
	
	/*1.含2笔扣款，户口号A金额117.1；户口号B金额117.2
2.户口A和户口B的扣款信息都正常*/
	public static void testCase12(){
		int trnamt1 = 11710;
		int trnamt2 = 11720;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 2, trnamt1+trnamt2);
		
		//
		PayJson.paydetail onetrs = pj.new paydetail();		
		onetrs.merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		onetrs.accno = "6226095710002465";
		onetrs.accnam ="邬小维03";
		onetrs.trnamt = trnamt1;
		onetrs.agrnbr = "testCase12";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		//
		PayJson.paydetail twotrs = pj.new paydetail();		
		String merser = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		twotrs.merser = merser;
		twotrs.accno = "6225880250347789";
		twotrs.accnam ="营测壹";
		twotrs.trnamt = trnamt2;
		twotrs.agrnbr = "testCase12";
		twotrs.telnbr = "745896325";
		pj.details.add(twotrs);

		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}

	
	
	//流水号重复测试
	public static void testCase13(){
		int trnamt = 10008;
		String serial = String.format("%s%.0f", new SimpleDateFormat("hhmmss").format(new Date()), Math.random()*100);
		PayJson pj = new PayJson(serial, 1, trnamt);

		PayJson.paydetail onetrs = pj.new paydetail();
		
//		与cast6的成功交易流水号重复		
		onetrs.merser = "03112129";
		//
		onetrs.accno = "6226095710002465";
		onetrs.accnam ="邬小维03";
		onetrs.trnamt = trnamt;
		onetrs.agrnbr = "testCase6";
		onetrs.telnbr = "745896325";
		pj.details.add(onetrs);
		
		Gson gson = new Gson();
		String jsonmsg = gson.toJson(pj);
		System.out.println(jsonmsg);
		logger.debug(jsonmsg);
		String resp = SendAndResp(jsonmsg);
		logger.debug(resp);
		
		logger.debug("This is CASE END----------------------------------------------------------------------------------.");

	}
	
	public static void main(String[] args){
//		log.
		testCase12();
		return ;
	}
}

