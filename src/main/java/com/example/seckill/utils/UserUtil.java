package com.example.seckill.utils;

import com.example.seckill.pojo.User;
import com.example.seckill.vo.RespBean;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//产生用户
public class UserUtil {

    public static void createUser(int count) throws Exception {
        List<User>users=new ArrayList<>();
        for(int i=0;i<count;i++){
            User user=new User();
            user.setId(13000000000L+i);
            user.setNickname("user"+i);
            user.setSlat("F9B1187306841F6B93COB5DCEA8B163F");
            user.setPassword(MD5Util.inputPassToDBPass("123456",user.getSlat()));
            user.setRegisterDate(new Date());
            user.setLastLoginDate(new Date());
            user.setLoginCount(0);
            users.add(user);
        }
        Connection connection= getConn();
        String sql="insert into t_user(id,nickname,password,slat,head,register_date,last_login_date,login_count)values(?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement=connection.prepareStatement(sql);
        for(User user:users){
            preparedStatement.setLong(1,user.getId());
            preparedStatement.setString(2,user.getNickname());
            preparedStatement.setString(3,user.getPassword());
            preparedStatement.setString(4,user.getSlat());
            preparedStatement.setString(5,user.getHead());
            preparedStatement.setTimestamp(6,new Timestamp(user.getRegisterDate().getTime()));
            preparedStatement.setTimestamp(7,new Timestamp(user.getLastLoginDate().getTime()));
            preparedStatement.setInt(8,user.getLoginCount());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
        connection.close();
        String urlString="http://localhost:8080/login/doLogin";
        File file=new File("C:\\Users\\syk\\Desktop\\config.txt");
        if(file.exists()){
            file.delete();
        }
        RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
        file.createNewFile();
        randomAccessFile.seek(0);
        for(User user:users){
            URL url=new URL(urlString);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream out=httpURLConnection.getOutputStream();
            String params="mobile="+user.getId()+"&password="+MD5Util.inputPassToFormPass("123456");
            out.write(params.getBytes());
            out.flush();
            InputStream inputStream= httpURLConnection.getInputStream();
            ByteArrayOutputStream bout=new ByteArrayOutputStream();
            byte buff[]=new byte[1024];
            int len=0;
            while ((len=inputStream.read(buff))>=0){
                bout.write(buff,0,len);
            }
            inputStream.close();
            bout.close();
            String response =new String(bout.toByteArray());
            ObjectMapper mapper=new ObjectMapper();
            RespBean respBean=mapper.readValue(response,RespBean.class);
            String userTicket=(String)(respBean.getObject());
            String row=user.getId()+","+userTicket;
            randomAccessFile.seek(randomAccessFile.length());
            randomAccessFile.write(row.getBytes());
            randomAccessFile.write("\r\n".getBytes());
        }
        randomAccessFile.close();



    }
    private static Connection getConn() throws Exception {
        String url="jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username="root";
        String password="root";
        String driver="com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url,username,password);
    }
    public static void main(String[] args) throws Exception {
        createUser(1000);
    }

}
