package com.racetime.xsad;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import com.racetime.xsad.util.JsonUtil;


public class AppAddTest implements Runnable{

    public static final String ADD_URL = "http://xssp.maxbit.cn/adapi/ad/getAd";

    public void run() {

        try {
            //创建连接
            URL url = new URL(ADD_URL);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.connect();

            //POST请求
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");

            String s = "{";
	            s += "\"device_id\": \"3cf80811cb9b\",";
	            s += "\"app_id\": \"app123\",";
	            s += "\"adslot_id\": \"12345678\",";
	            s += "\"api_version\": \"1.2\",";
	            s += "\"ad_uuid\": \"7834hfs983h923ue823e78e2hd9c9v22\",";
	            s += "\"ad_time\": \"15\",";
	            s += "\"gps\": {";
		            s += "\"lon\": 1.234,";
		            s += "\"lat\": 4.321,";
		            s += "\"timestamp\": 1234567";
	            s += "},";
	            s += "\"media\": {";
		            s += "\"channel\": \"复旦大学数字屏11\",";
		            s += "\"tags\": \"学生,网购\",";
		            s += "\"os_type\": \"Android\",";
		            s += "\"os_version\": \"4.4.1\",";
		            s += "\"screen_size\": \"1080*960\"";
	            s += "}";
            s += "}";
            
            out.write(s);
            out.flush();
            out.close();

            //读取响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String lines;
            StringBuffer sb = new StringBuffer("");
            while ((lines = reader.readLine()) != null) {
                lines = new String(lines.getBytes());
                sb.append(lines);
            }
            
            String result = URLDecoder.decode(sb.toString(), "utf-8");
            System.out.println(result);
            
            
            if(JsonUtil.getJsonValue(result, "code") != null && JsonUtil.getJsonValue(result, "code").equals("1002")){
            	Map dataMap = (Map) JsonUtil.getJsonValue(result, "data");
            	List<String> callbackList = (List<String>) dataMap.get("callback_url");
            	
            	String callbackUrl = callbackList.get(0) + "&type=1";
            	
//            	String callbackUrl = "http://192.168.1.45:8080/ad-service/ad/callback?request_id=96adef730ae44662a3fae451cc6c2cfc&win_notice_url=http://www.baidu.com/juping.php?url=IHtK00K-SeM_BGU0iuK1fJo9-B6z-Jq15Yk8D_FGviBDbkZCZ5euqh7tsqSvjPRInDQr7OlRL6muhuShZxv1av1BJMTehb68DNq8ZMQHctDluv2hgbyqyuPTrfHAFmMs1ePTta3SmQxy_WJvO_NOKj2J37g6JF68IgEeJQ2V_4etQvQC1s.DR_i5Y_lbYgLSRPYg-xahqYpcQcnU3mPveGzX5uT1kvfvTIOiMIvIbPLAzOGELsvTIOW4uTXGmh79h9mzIhOuv20.U1Ys0ZDqpMNspy4MFHNAFHNAIvqzu0KY5yGdTA-8uzRdwMw9ThI-IA-8usKGUHYvnHT0u1dbry71ILn0Iybq0ZKGujYY0APGujY3r0KVIjY0pvbqn0KzIjYzPjT0uy-b5fKBpHYznjuxnW0snj7xnW0sPWn0Uynq0Z7spyfqn0Kkmv-b5H00ThIYmyTqn0KEIhsqrj63QHb0mycqn7ts0ANzu1Ys0ZKs5H00UMus5H08nj0snj0snj00Ugws5H00uAwETjYs0ZFJ5H00uMfqn6KspjYs0Aq15HD0mMTqn0K8IjYs0ZPl5fKYIgnqrj6kPWnzPWR4PWmvP1bdrjfY0ZF-TgfqnHRsn10znWbdPWfYP6K1pyfqrymdn1TdPWKBuhcknHu-m6K9m1Yk0ZK85H00TydY5H00Tyd15H00XMfqn0KVmdqhThqV5fKbmy4dmhNxTAk9Uh-bT1Y0TA7Ygvu_myTqn0KbIA-b5H00ugwGujYVnfK9TLKWm1Ys0ZNspy4Wm1Ys0Z7VuWYs0AuWIgfqn0KhXh6qn0Khmgfqn0KlTAkdT1Ys0A7buhk9u1Yznjf30Akhm1Ys0ZIhThqV5H00IvbqnWD0TLPs5Hc0mywWUA71T1Yvn0KYIHYYPWfzPjnL0ZwV5ycvuWnLrHD30APC5fK1uAVxIWYY0ANzTAPEuARqRjc3rjf0UNq1pAqLT1Yk0APzm1YkPHfzP0&third_monitor_url=http://rainbowad.baidu.com:8030/t.js?test_m=bashi&type=1";
            	
            	URL u = new URL(callbackUrl);
            	HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            	int responseCode = conn.getResponseCode();
            	if(responseCode != 200)
            		System.out.println("callback错误");
            	conn.disconnect();
            }
            
    		
            reader.close();
            // 断开连接
            connection.disconnect();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
//    	ExecutorService executor = Executors.newFixedThreadPool(1000);
    	
//		for (int i = 1; i < 1001; i++) {
////			try {
////				Thread.sleep(10);
////			} catch (InterruptedException e) {
////				e.printStackTrace();
////			}
//			
//			AppAddTest test = new AppAddTest();
//			executor.execute(test);
//			
//			
////			Thread t = new Thread(new AppAddTest());
////			t.start();
//		}
    	
    	
		Thread t = new Thread(new AppAddTest());
		t.start();
		
		
//    	while(true){
//    		Thread t = new Thread(new AppAddTest());
//    		t.start();
//    		try {
//				Thread.sleep(1000*10);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//    	}
    	
    	
    	
    	
    	/*String callbackUrl = "http://localhost:8080/ad-service/ad/callback?request_id=75f3f115e61f425b8206fe4d67c3b63f&win_notice_url=7ac341a141909fd7a3784cb42ac6cf7004c36eedfc79a9995c24f1e53fe88e46d58282b8ebe9d696d1c5b0c5dff5375ded97b907787492bdfa9b0e223a006ac30bec1c7d693c8104535ca91138b412f08bc7534752116aef5972b22b34266dd8a662667f488c874dcfb1f86e8306748b094d13687eb2f3320ed7dba08e09495b71ff1df6c52d8cd80ad1fd5f9026923bd19252e71fc44fc22e301b8766ff6369c9317525be81e67400fd850308eed4daa6efd27f66328dfd03b19b5dd6cdb82d9618c6de6b6d1ec4a4cc4a8a4495c9435541454e77461fe711544c7a7aec1560a7b68cbc98c163777cf29a39e8930df80192dba66ea6bcb1226ed0345fde8a51060eb397b834e239d8f1da98c95f0f2f0a5a3b648945521be47a7c82f3ba5a268cc452c2405e1733b4584a975ac50aadfd9dc2830e68691747904e3baceaeb4110a975cbc806429c5264143562bb11d589c4ee74f48f402f9c31cef01a632ee760d9630a7fc02aae1bec54b27a5d60e72bcac241627d5c0216a31588ca5c1076007a88dd90afd7c50805afde83c0ca7b5eefdd17b83d32d5d3bc3244ef78c014dacc90ab54c4732886f36f6dcf3cf20760a47d69eab86faa862f09648693081e8599c2e29208785102349fd0c440bceb54115cd69e29ccd1c8c1e38c5645cbf75eeeb84d62bfd4dca89fdebef718609a079c7ffc0db976d99b52fc7e894307fef794a186332724ecb1a59330aa5c6fcf8233d7aca504c580ad57a19ac85af8b74fb8b72abf41e9b4efdb32e5a17d7e996302446c09b1a34f27153f6bdd55f3fd01ab896430b2fde215b86cdbd54c328cffba4c1d7f6e8b4b1d3335e34a8dd846f8bef6c1e5b723a591c6e0e17fe4a72374dba2bff6073154f9512cc35f70d3595237ca06e6f9b473a3637752172db74eb3b32b046d9f32becc8070e44edf498cd701e1171e996333edfd77e7b3546038ddf3b826bb160e15a738eca8ae47ff985e4027bbb24d189cf1127fb13af9d0558e027399900220cccee505c059994414dba72b548a13f1545a9e631a1611db1cd656047004c83d095f3f5379a109ce151c15779f96340111e3c6a363b23b1f81784bb77b2643fe9dbc453fe6041d274833ec69bc895fd2566a81e2ae0125b75f610e136abbf6ae647b39432309c8deeb0a1b0212b8a5c384218388a3f313bf7fe223431b5d2b43087e8b665c4f2cc59acf293c7d4987f0d00058023e70d5de4bd79c621ea8bd54cc6333387c4dc197067876408d3c098b9438c99c175b530e0aa024087a7de70faeff3730055a900697208d6b146c57c6e3eba5583412298883&third_monitor_url=7ac341a141909fd7a3784cb42ac6cf7004c36eedfc79a9995c24f1e53fe88e46d58282b8ebe9d696d1c5b0c5dff5375ded97b907787492bdfa9b0e223a006ac30bec1c7d693c8104535ca91138b412f08bc7534752116aef5972b22b34266dd8a662667f488c874dcfb1f86e8306748b094d13687eb2f3320ed7dba08e09495b71ff1df6c52d8cd80ad1fd5f9026923bd19252e71fc44fc22e301b8766ff6369c9317525be81e67400fd850308eed4daa6efd27f66328dfd03b19b5dd6cdb82d9618c6de6b6d1ec4a4cc4a8a4495c9435541454e77461fe711544c7a7aec1560a7b68cbc98c163777cf29a39e8930df80192dba66ea6bcb1226ed0345fde8a51060eb397b834e239d8f1da98c95f0f2f0a5a3b648945521be47a7c82f3ba5a268cc452c2405e1733b4584a975ac50aadfd9dc2830e68691747904e3baceaeb4110a975cbc806429c5264143562bb11d589c4ee74f48f402f9c31cef01a632ee760d9630a7fc02aae1bec54b27a5d60e72bcac241627d5c0216a31588ca5c1076007a88dd90afd7c50805afde83c0ca7b5eefdd17b83d32d5d3bc3244ef78c014dacc90ab54c4732886f36f6dcf3cf20760a47d69eab86faa862f09648693081e8599c2e29208785102349fd0c440bceb54115cd69e29ccd1c8c1e38c5645cbf75eeeb84d62bfd4dca89fdebef718609a079c7ffc0db976d99b52fc7e894307fef794a186332724ecb1a59330aa5c6fcf8233d7aca504c580ad57a19ac85af8b74fb8b72abf41e9b4efdb32e5a17d7e996302446c09b1a34f27153f6bdd55f3fd01ab896430b2fde215b86cdbd54c328cffba4c1d7f6e8b4b1d3335e34a8dd846f8bef6c1e5b723a591c6e0e17fe4a72374dba2bff6073154f9512cc35f70d3595237ca06e6f9b473a3637752172db74eb3b32b046d9f32becc8070e44edf498cd701e1171e996333edfd77e7b3546038ddf3b826bb160e15a738eca8ae47ff985e4027bbb24d189cf1127fb13af9d0558e027399900220cccee505c059994414dba72b548a13f1545a9e631a1611db1cd656047004c83d095f3f5379a109ce151c15779f96340111e3c6a363b23b1f81784bb77b2643fe9dbc453fe6041d274833ec69bc895fd2566a81e2ae0125b75f610e136abbf6ae647b39432309c8deeb0a1b0212b8a5c384218388a3f313bf7fe223431b5d2b43087e8b665c4f2cc59acf293c7d4987f0d00058023e70d5de4bd79c621ea8bd54cc6333387c4dc197067876408d3c098b9438c99c175b530e0aa024087a7de70faeff3730055a900697208d6b146c57c6e3eba5583412298883&source_type=RTB";
    	URL u;
		try {
			u = new URL(callbackUrl);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			int responseCode = conn.getResponseCode();
			System.out.println(responseCode);
			if(responseCode != 200)
				System.out.println("callback错误");
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    	
    }

}