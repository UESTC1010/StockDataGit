import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class PageHandle {
	public static String downloadpage(String url){
		RequestConfig requestConfig =  RequestConfig.custom()
			.setConnectTimeout(30000)
			.setConnectionRequestTimeout(30000)
			.setSocketTimeout(50000)
			.setExpectContinueEnabled(true).build();
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(requestConfig);
		
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response)
					throws IOException {
				int statuscode = response.getStatusLine().getStatusCode();
				if(HttpStatus.SC_OK == statuscode ){
					
					HttpEntity entity = response.getEntity();
					return entity != null? EntityUtils.toString(entity,"utf-8"):null;
				}else {
					return null;
//	                throw new ClientProtocolException("Unexpected response status: " + statuscode);
	            } 
			}
		};
		
		String responseBody = null;
		try {
			responseBody = httpclient.execute(httpget, responseHandler);
			//HandlerBody(responseBody);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Exception e){
			System.out.println();
		}
		finally{
			httpget.releaseConnection();
		}
		
		return responseBody;
	}
	
	public static int[] GetMaxPageNum(String json){
//		System.out.println(json);
		int[] value = new int[2];
		int maxpage = 0;
		int count = 0;
//		String regex1 = "\"count\":(.*?),\"maxPage\":(.*?),";
		String regex1 = "\"maxPage\":(.*?),.*?\"count\":(.*?),";
		Pattern pattern = Pattern.compile(regex1);
		Matcher matcher = pattern.matcher(json);
		
		while(matcher.find()){		
			count = Integer.parseInt(matcher.group(2));
			maxpage = Integer.parseInt(matcher.group(1));
//			System.out.println();
		}
		value[0] = count;
		value[1] = maxpage;
		return value;
	}
	public static void main(String args[]){
		String url = "http://xueqiu.com/statuses/search.json?count=10&comment=0&symbol=SZ002610&hl=0&source=user&page=7";
	}
}
