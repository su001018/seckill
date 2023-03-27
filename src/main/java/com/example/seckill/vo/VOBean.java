package com.example.seckill.vo;

import lombok.Data;
import org.apache.hbase.thirdparty.com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Data
public class VOBean {
    Map<String, Object> data;

    public VOBean(HttpServletRequest request) {
        data = getDataFromRequest(request);
    }

    public Object get(String name) {
        return data.get(name);
    }

    private Map<String, Object> getDataFromRequest(HttpServletRequest request) {
        Gson gson = new Gson();
        String type = request.getContentType();
        Map<String, Object> receiveMap = new HashMap<String, Object>();
//        if("application/x-www-form-urlencoded".equals(type)){
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String key = String.valueOf(enu.nextElement());
            String value = request.getParameter(key);
            receiveMap.put(key, value);
        }
//        }
//        else{	//else是text/plain、application/json这两种情况
//            BufferedReader reader = null;
//            StringBuilder sb = new StringBuilder();
//            try{
//                reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
//                String line = null;
//                while ((line = reader.readLine()) != null){
//                    sb.append(line);
//                }
//            } catch (IOException e){
//                e.printStackTrace();
//            } finally {
//                try{
//                    if (null != reader){
//                        reader.close();
//                    }
//                } catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//            receiveMap = gson.fromJson(sb.toString(), new TypeToken<Map<String, String>>(){}.getType());//把JSON字符串转为对象
//        }
        return receiveMap;
    }
}
