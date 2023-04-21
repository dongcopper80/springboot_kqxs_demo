/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongcopper80.springboot.lottery;

import com.dongcopper80.springboot.lottery.entity.LotteryResult;
import com.dongcopper80.springboot.lottery.repository.LotteryResultRepository;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 *
 * @author dongc
 */
public class WebUtils {
    
    public String linkKQXS(String URL) throws IOException{
        
        Document document = Jsoup.connect(URL).get();
        
        String html = document.toString();
        String link = html.substring(html.indexOf("var strDomain"));
        link = link.substring(0, link.indexOf("';"));
        link = link.replace("var strDomain = '", "");
        
        String subfix = html.substring(html.indexOf("var strDomain"));
        subfix = subfix.substring(subfix.indexOf("strUrl = '") + "strUrl = '".length());
        subfix = subfix.substring(subfix.indexOf("strUrl = '") + "strUrl = '".length());
        subfix = subfix.substring(0, subfix.indexOf("';"));
        
        return link + subfix;
    }

    public String reverseLines(File file) {
        ReversedLinesFileReader object = null;
        try {
            object = new ReversedLinesFileReader(file);
            return object.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                object.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static boolean isNumeric(String maybeNumeric) {
        return maybeNumeric != null && maybeNumeric.matches("[0-9]+");
    }
    
    public String getContent(String url) {
        StringBuilder response = new StringBuilder();
        try {
            disableSSL();
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0.1; SAMSUNG SM-G925F Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/4.0 Chrome/44.0.2403.133 Mobile Safari/537.36");
            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            }
        } catch (Exception e) {

        }

        return response.toString();
    }

    public void disableSSL() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new MyTrustManager()};

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HostnameVerifier allHostsValid = (String hostname, SSLSession session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
        }
    }

    public void UpdateKQXS(String send) {
        try {
            disableSSL();
            URL obj2 = new URL(send);
            HttpURLConnection con = (HttpURLConnection) obj2.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0.1; SAMSUNG SM-G925F Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/4.0 Chrome/44.0.2403.133 Mobile Safari/537.36");
            int responseCode = con.getResponseCode();
            
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;

                StringBuilder data = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    data.append(inputLine);
                }
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendEmail(JavaMailSender javaMailSender, String title, String content) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("", "", "", "", "");
        msg.setSubject(title);
        msg.setText(content);

        javaMailSender.send(msg);
    }
    
    public boolean WriteData56(String filename, String MT, ConfigProperties configProp) {

        try {
            Date today = new Date();
            String filename2 = filename;
            String MT2 = MT;
            filename = filename + ".properties";

            String user = configProp.getConfigValue("samba.user56");

            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);

            SmbFile f = new SmbFile(configProp.getConfigValue("samba.root56") + filename, auth);

            if (!f.exists()) {
                f.createNewFile();
            }

            SmbFileOutputStream out = new SmbFileOutputStream(f);

            out.write(("date=" + new SimpleDateFormat("yyyyMMdd").format(today) + "\r\n").getBytes());

            if (MT.endsWith("\\n\\")) {
                MT = MT.substring(0, MT.lastIndexOf("\\n\\"));
            }

            out.write(("message=" + MT + "\r\n").getBytes());

            out.close();

            WriteData102(filename2, MT2, configProp);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean WriteData102(String filename, String MT, ConfigProperties configProp) {
        try {
            Date today = new Date();
            filename = filename + ".properties";

            String user = configProp.getConfigValue("samba.user102");

            NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(user);

            SmbFile f = new SmbFile(configProp.getConfigValue("samba.root102") + filename, auth);

            if (!f.exists()) {
                f.createNewFile();
            }

            SmbFileOutputStream out = new SmbFileOutputStream(f);

            out.write(("date=" + new SimpleDateFormat("yyyyMMdd").format(today) + "\r\n").getBytes());

            if (MT.endsWith("\\n\\")) {
                MT = MT.substring(0, MT.lastIndexOf("\\n\\"));
            }

            out.write(("message=" + MT + "\r\n").getBytes());

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public JSONObject getKQXS(int p_MaTinh, String p_fromDate, String p_toDate, LotteryResultRepository lotteryResultRepository){
        JSONObject objReturn = new JSONObject();

        String uId = "7231";
        List<LotteryResult> result = lotteryResultRepository.findAll(p_MaTinh, p_fromDate, p_toDate);

        if (result != null && result.size() > 0) {
            List<Date> lstNgay = new ArrayList<>();
            Date dteTemp = new Date();
            for (int j = 0; j < result.size(); j++) {
                LotteryResult item = (LotteryResult) result.get(j);
                if (!dteTemp.equals(item.getResultDate())) {
                    dteTemp = item.getResultDate();
                    lstNgay.add(dteTemp);
                }
            }

            JSONObject jsResult = new JSONObject();
            JSONObject[] jslstDay = new JSONObject[lstNgay.size()];
            Iterator it = lstNgay.iterator();
            int d = 0;
            while (it != null && it.hasNext()) {
                Date rsDate = (Date) it.next();
                JSONObject jsDay = new JSONObject();
                jsDay.put("Date", new SimpleDateFormat("dd-MM-yyyy").format(rsDate));

                JSONObject[] lstData = new JSONObject[9];
                for (int i = 0; i <= 8; i++) {
                    JSONObject jsData = new JSONObject();
                    jsData.put("prize_id", i);
                    String data = "";
                    for (int j = 0; j < result.size(); j++) {
                        LotteryResult item = (LotteryResult) result.get(j);
                        if (item.getPrizeId().intValue() == i && item.getResultDate().equals(rsDate)) {
                            if (data.equals("")) {
                                data = data + item.getResultNumber();
                            } else {
                                data = data + "-" + item.getResultNumber();
                            }
                        }
                    }
                    jsData.put("result", data);
                    lstData[i] = jsData;
                }
                jsDay.put("result", lstData);

                jslstDay[d] = jsDay;
                d++;
            }

            jsResult.put("uid", uId);
            jsResult.put("title", "");
            jsResult.put("message", "");
            objReturn.put("result", jslstDay);
            objReturn.put("error", "");
        } else {
            JSONObject jsResult = new JSONObject();
            jsResult.put("uid", uId);
            jsResult.put("title", "Có lỗi phát sinh");
            jsResult.put("message", "Không lấy được dữ liệu");
            objReturn.put("result", "");
            objReturn.put("error", jsResult);
        }

        objReturn.put("id", 2);
        objReturn.put("version", "1");
        objReturn.put("method", "AreaResultByDate");
        return objReturn;
    }
    
    public void UpdateKQXS(int p_MaTinh, LotteryResultRepository lotteryResultRepository){
        String p_fromDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -7);
        String p_toDate = new SimpleDateFormat("dd-MM-yyyy").format(cal.getTime());
        
        String resultKQAPP = "areaId=" + p_MaTinh + "&ketqua=" + "[" + getKQXS(p_MaTinh, p_fromDate, p_toDate, lotteryResultRepository).toString() + "]";
        
        updateKQApp(resultKQAPP);
    }
    
    private void updateKQApp(String resultKQAPP) {
        String url = "https://soicaumb.net/cached/update.php"; 
        try {
            disableSSL();
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            byte[] postData = resultKQAPP.getBytes( StandardCharsets.UTF_8 );
            int postDataLength = postData.length;

            //add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0.1; SAMSUNG SM-G925F Build/MMB29K) AppleWebKit/537.36 (KHTML, like Gecko) SamsungBrowser/4.0 Chrome/44.0.2403.133 Mobile Safari/537.36");
            con.setInstanceFollowRedirects(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
            con.setRequestProperty("charset", "utf-8");
            con.setRequestProperty("Content-Length", Integer.toString(postDataLength ));
            con.setUseCaches(false);
            // Send post request
            con.setDoOutput(true);
            
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.write( postData );

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + resultKQAPP);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
