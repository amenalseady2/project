package com.mysada.news.service;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 作者：张玉水 on 16/6/2 19:54
 */
public class SocketHttpRequester {

    /**
     *多文件上传
     * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     *   <form method="POST" action="http://192.168.1.101:8083/upload/servlet/UploadServlet" enctype="multipart/form-data">
     <input type="text" name="name">
     <input type="text" name="id">
     <input type="file" name="imagefile">
     <input type="file" name="zip">
     </form>
     * @param path 上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file 上传文件
     */
    public static String post(String path, Map<String, String> params, Map<String , File> files) throws Exception{
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志

        int fileDataLength = 0;
        for(Entry<String , File> entry : files.entrySet()){//得到文件类型数据的总长度
            StringBuilder fileExplain = new StringBuilder();
            fileExplain.append("--");
            fileExplain.append(BOUNDARY);
            fileExplain.append("\r\n");
            fileExplain.append("Content-Disposition: form-data;name=\""+ entry.getKey()+"\";filename=\""+ entry.getValue() + "\"\r\n");
            fileExplain.append("Content-Type: application/octet-stream"+"\r\n\r\n");
            fileExplain.append("\r\n");
            fileDataLength += fileExplain.length();
            FileInputStream in = new FileInputStream(entry.getValue());
            if(in!=null){
                fileDataLength += entry.getValue().length();
            }else{
                fileDataLength += getBytesFromFile(entry.getValue()).length;
            }
        }

        StringBuilder textEntity = new StringBuilder();
        for (Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
            textEntity.append("--");
            textEntity.append(BOUNDARY);
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");
            textEntity.append(entry.getValue());
            textEntity.append("\r\n");
        }
        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength +  endline.getBytes().length;
        URL url = new URL(path);
        int port = url.getPort()==-1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST "+ url.getPath()+" HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary="+ BOUNDARY+ "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: "+ dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: "+ url.getHost() +":"+ port +"\r\n";
        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        //把所有文件类型的实体数据发送出来
        for(Entry<String , File> entry : files.entrySet()){
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\""+ entry.getKey()+"\";filename=\""+ entry.getValue().getPath() + "\"\r\n");
            fileEntity.append("Content-Type: application/octet-stream"+"\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            FileInputStream in = new FileInputStream(entry.getValue());
            if(in!=null){
                byte[] buffer = new byte[1024];
                int len = 0;
                while((len = in.read(buffer, 0, 1024))!=-1){
                    outStream.write(buffer, 0, len);
                }
                in.close();
            }else{
                outStream.write(getBytesFromFile(entry.getValue()), 0, getBytesFromFile(entry.getValue()).length);
            }
            outStream.write("\r\n".getBytes());
        }
        //下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        String str = Utils.removeBOM(ToString(reader));
        String str = ToString(reader);
        /*if(reader.readLine().indexOf("200")==-1){//读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
        	// 读取每一行的数据.
        	//BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	LogUtils.i("读取失败");
            return "";
        }*/
        outStream.flush();
        outStream.close();
        reader.close();
        socket.close();
        return str;
    }


    public static byte[] getBytesFromFile(File file) {
        byte[] ret = null;
        try {
            if (file == null) {
                // log.error("helper:the file is null!");
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException e) {
            // log.error("helper:get bytes from file process error!");
            e.printStackTrace();
        }
        return ret;
    }

    private static String  ToString(BufferedReader data){

        String temp = "";
        String s = "";
        try {
            while((temp = data.readLine()) != null)
            {
                s = s + temp;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }
}
