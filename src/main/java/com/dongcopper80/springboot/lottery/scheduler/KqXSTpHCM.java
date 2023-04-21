/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongcopper80.springboot.lottery.scheduler;

import com.dongcopper80.springboot.lottery.ConfigProperties;
import com.dongcopper80.springboot.lottery.WebUtils;
import com.dongcopper80.springboot.lottery.repository.LotteryResultRepository;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author dongc
 */
@Component
public class KqXSTpHCM {

    @Autowired
    ConfigProperties configProp;

    @Autowired
    @Qualifier("jdbcLotteryResultRepository")
    private LotteryResultRepository lotteryResultRepository;

    private static final Logger log = LoggerFactory.getLogger(KqXSTpHCM.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private String datetime = "";

    private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("ddMMyyyy");

    @Autowired
    private JavaMailSender javaMailSender;

    static {
        HttpsURLConnection.setDefaultHostnameVerifier((String hostname, SSLSession session) -> hostname.equals("113.190.252.186"));
    }

    String t7[][] = {
        {"1", "TP. HCM", "56", "TP"}
    };

    String ketqua = "";
    String link = "";

    @Scheduled(fixedRate = 3000)
    public void reportCurrentTime() {
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar from = Calendar.getInstance();
        from.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        from.set(Calendar.HOUR_OF_DAY, 16);
        from.set(Calendar.MINUTE, 10);

        Calendar to = Calendar.getInstance();
        to.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        to.set(Calendar.HOUR_OF_DAY, 19);
        to.set(Calendar.MINUTE, 0);

        if (new Date().after(from.getTime()) && new Date().before(to.getTime())) {
            log.info("KqXSTpHCM The time 2 is now {}", dateFormat.format(new Date()));

            if (datetime.equals("")) {
                new WebUtils().sendEmail(javaMailSender,
                        "Chạy kết quả xổ số Tp.HCM ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                        "Kết quả xổ số Tp.HCM bắt đầu chạy lúc " + dateFormat.format(new Date()));
                datetime = dateFormat2.format(new Date());
                try {
                    link = new WebUtils().linkKQXS("https://www.minhngoc.net.vn/xo-so-truc-tiep/mien-nam.html");
                } catch (Exception e) {
                    new WebUtils().sendEmail(javaMailSender,
                            "Chạy kết quả xổ số Tp.HCM ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                            e.fillInStackTrace().toString());
                }
            } else {
                if (!datetime.equals(dateFormat2.format(new Date()))) {
                    new WebUtils().sendEmail(javaMailSender,
                            "Chạy kết quả xổ số Tp.HCM ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                            "Kết quả xổ số Tp.HCM bắt đầu chạy lúc " + dateFormat.format(new Date()));
                    datetime = dateFormat2.format(new Date());
                    try {
                        link = new WebUtils().linkKQXS("https://www.minhngoc.net.vn/xo-so-truc-tiep/mien-nam.html");
                    } catch (Exception e) {
                        new WebUtils().sendEmail(javaMailSender,
                                "Chạy kết quả xổ số Tp.HCM ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                                e.fillInStackTrace().toString());
                    }
                }
            }

            //String url = configProp.getConfigValue("lottery.xsmn") + System.currentTimeMillis();
            if (link == null || link.equals("")) {
                return;
            }

            String url = link + "?_=" + System.currentTimeMillis();

            log.info(url);
            String content = new WebUtils().getContent(url);
            log.info(content);

            Calendar cal = Calendar.getInstance();
            int date_of_week = cal.get(Calendar.DAY_OF_WEEK);

            String tinh[][] = null;

            switch (date_of_week) {
                case 7:
                    tinh = t7;
                    break;
            }

            if (tinh == null) {
                return;
            }

            for (int i = 0; i < tinh.length; i++) {
                String province[] = tinh[i];

                String regex = ":\\{(.*?)\\}";
                regex = province[0] + regex;

                Matcher m = Pattern.compile(regex).matcher(content);

                Matcher m2 = Pattern.compile("21:\\{(.*?)\\}").matcher(content);

                String giai21 = null;

                while (m2.find()) {
                    giai21 = m2.group(1);
                }

                while (m.find()) {
                    String giai = m.group(1);

                    if (!giai.equals(giai21)) {

                        for (int g = 0; g <= 8; g++) {

                            giai = giai.replaceAll(g + ":", "g" + g + ":");
                        }

                        giai = "{" + giai + "}";

                        try {
                            JSONObject obj = new JSONObject(giai);

                            String result = "";
                            String subsms = "";

                            for (int g = 0; g <= 8; g++) {

                                String sChiTiet = "";

                                if (g == 0) {
                                    subsms += "\n" + "DB:";
                                } else {
                                    if (subsms != null && !subsms.equals("")) {
                                        subsms = subsms.trim() + "\n";
                                    }
                                    subsms += g + ":";
                                }

                                try {
                                    JSONArray arr = obj.getJSONArray("g" + g);

                                    for (int stt = 0; stt < arr.length(); stt++) {
                                        try {
                                            Integer.parseInt(arr.get(stt).toString());
                                            if (!sChiTiet.equals("")) {
                                                sChiTiet += "-";
                                            }
                                            sChiTiet += arr.get(stt).toString();
                                        } catch (Exception er) {
                                            if (!sChiTiet.equals("")) {
                                                sChiTiet += "-";
                                            }
                                            sChiTiet += arr.get(stt).toString();
                                        }
                                    }
                                } catch (Exception ex) {
                                    try {
                                        Integer.parseInt(obj.get("g" + g).toString());
                                        if (!sChiTiet.equals("")) {
                                            sChiTiet += "-";
                                        }
                                        sChiTiet += obj.get("g" + g);
                                    } catch (Exception ex2) {
                                        if (!sChiTiet.equals("")) {
                                            sChiTiet += "-";
                                        }
                                        sChiTiet += obj.get("g" + g);
                                    }
                                }

                                if (!sChiTiet.equals("")) {
                                    if (!result.equals("")) {
                                        result += ",";
                                    }
                                    result += "\"G" + g + "\":\"" + sChiTiet + "\"";

                                    String tempPrize[] = sChiTiet.split("-");

                                    String kqxs = "";

                                    for (int kq = 0; kq < tempPrize.length; kq++) {
                                        if (WebUtils.isNumeric(tempPrize[kq])) {
                                            if (!kqxs.equals("")) {
                                                kqxs += "-";
                                            }
                                            kqxs += tempPrize[kq];
                                        }
                                    }

                                    subsms += sChiTiet.replaceAll("-", " ");

                                    if (!kqxs.equals("")) {
                                        lotteryResultRepository.updateResult(kqxs, g, Integer.parseInt(province[2]), province[3]);
                                    }
                                }
                            }

                            //ghi log file to cache kqxs
                            String date = new SimpleDateFormat("dd/MM").format(new Date());
                            String MT = "\n ...dang XO...tiep... ";

                            if (subsms.equals("")) {
                                MT = "\n *HDXS Dang Kiem Tra Bong...\n *T.Thuat TrucTiep SieuToc\n >Soan: XS" + province[3] + " gui 7725";
                            }

                            String prize_2 = "";

                            if (("XS" + province[3] + " " + date + "\n" + subsms.trim().replaceAll(" ", "-") + MT).length() < 160) {
                                prize_2 += "XS" + province[3] + " " + date + "\n" + subsms.trim().replaceAll(" ", "-") + MT;
                            } else {
                                prize_2 += "XS" + province[3] + " " + date + "\n" + subsms.trim().replaceAll(" ", "-");
                            }

                            new WebUtils().WriteData56(province[3], prize_2, configProp);

                            new WebUtils().UpdateKQXS(Integer.parseInt(province[2]), lotteryResultRepository);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
