import java.io.IOException;

import org.apache.http.HttpEntity;
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
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom()
		.setConnectTimeout(30000)
		.setConnectionRequestTimeout(30000)
		.setSocketTimeout(30000)
		.setExpectContinueEnabled(true).build();
		httpget.setConfig(requestConfig);
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
			@Override
			public String handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				int statuscode = response.getStatusLine().getStatusCode();
				if(HttpStatus.SC_OK == statuscode ){
					
					HttpEntity entity = response.getEntity();
					return entity != null? EntityUtils.toString(entity,"utf-8"):null;
				}else {
	                throw new ClientProtocolException("Unexpected response status: " + statuscode);
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
}
