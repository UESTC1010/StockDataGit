import java.io.IOException;
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
//		HttpHost proxy = new HttpHost("60.190.138.151", 80, "http");
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()
//		.setProxy(proxy)
		.setConnectTimeout(30000)
		.setConnectionRequestTimeout(30000)
		.setSocketTimeout(30000)
		.setExpectContinueEnabled(true).build();
		httpget.setConfig(requestConfig);
		httpget.setHeader("Cookie", "Hm_lvt_1db88642e346389874251b5a1eded6e3=1397918319; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1397918324; __utma=1.1613885709.1397918319.1397918319.1397918319.1; __utmb=1.3.9.1397918323304; __utmc=1; __utmz=1.1397918319.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); xq_a_token=KoznewUwVBHfhlxt0PFFuN; xq_r_token=QSs18bNw65HYGpJ7aLsRMb");
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
		} finally{
			httpget.releaseConnection();
		}
		
		return responseBody;
	}
	
	public static int[] GetMaxPageNum(String json){
//		System.out.println(json);
		int[] value = new int[2];
		int maxpage = 0;
		int count = 0;
		String regex1 = "\"count\":(.*?),\"maxPage\":(.*?),";
		Pattern pattern = Pattern.compile(regex1);
		Matcher matcher = pattern.matcher(json);
		
		while(matcher.find()){		
			count = Integer.parseInt(matcher.group(1));
			maxpage = Integer.parseInt(matcher.group(2));
//			System.out.println();
		}
		value[0] = count;
		value[1] = maxpage;
		return value;
	}
	public static void main(String args[]){
		String url = "http://xueqiu.com/statuses/search.json?count=10&comment=0&symbol=SZ002610&hl=0&source=user&page=7";
		String json = PageHandle.downloadpage(url);
		PageHandle.GetMaxPageNum(json.substring(1, 60));
	}
}
