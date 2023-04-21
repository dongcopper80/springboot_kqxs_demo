/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongcopper80.springboot.lottery.scheduler;

import com.dongcopper80.springboot.lottery.ConfigProperties;
import com.dongcopper80.springboot.lottery.repository.LotteryResultRepository;
import com.dongcopper80.springboot.lottery.WebUtils;
import com.dongcopper80.springboot.lottery.entity.LotteryResult;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class KqXSMienBac {

    private static final Logger log = LoggerFactory.getLogger(KqXSMienBac.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    @Qualifier("jdbcLotteryResultRepository")
    private LotteryResultRepository lotteryResultRepository;

    @Autowired
    ConfigProperties configProp;

    @Autowired
    private JavaMailSender javaMailSender;

    private String datetime = "";
    String link = "";

    private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("ddMMyyyy");

    String t1[] = {"51", "Miền bắc", "1"};
    String t2[] = {"46", "Miền bắc", "1"};
    String t3[] = {"47", "Miền bắc", "1"};
    String t4[] = {"48", "Miền bắc", "1"};
    String t5[] = {"46", "Miền bắc", "1"};
    String t6[] = {"49", "Miền bắc", "1"};
    String t7[] = {"50", "Miền bắc", "1"};
    String ketqua = "";

    @Scheduled(fixedRate = 3000)
    public void reportCurrentTime() {
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar from = Calendar.getInstance();
        from.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        from.set(Calendar.HOUR_OF_DAY, 18);
        from.set(Calendar.MINUTE, 10);

        Calendar to = Calendar.getInstance();
        to.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        to.set(Calendar.HOUR_OF_DAY, 19);
        to.set(Calendar.MINUTE, 0);

        if (new Date().after(from.getTime()) && new Date().before(to.getTime())) {
            log.info("KqXSMienBac The time 2 is now {}", dateFormat.format(new Date()));

            if (datetime.equals("")) {
                new WebUtils().sendEmail(javaMailSender,
                        "Chạy kết quả xổ số Miền Bắc ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                        "Kết quả xổ số Miền Bắc bắt đầu chạy lúc " + dateFormat.format(new Date()));
                datetime = dateFormat2.format(new Date());
                try {
                    link = new WebUtils().linkKQXS("https://www.minhngoc.net.vn/xo-so-truc-tiep/mien-bac.html");
                } catch (Exception e) {
                    new WebUtils().sendEmail(javaMailSender,
                            "Chạy kết quả xổ số Miền Bắc ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                            e.fillInStackTrace().toString());
                }
            } else {
                if (!datetime.equals(dateFormat2.format(new Date()))) {
                    new WebUtils().sendEmail(javaMailSender,
                            "Chạy kết quả xổ số Miền Bắc ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                            "Kết quả xổ số Miền Bắc bắt đầu chạy lúc " + dateFormat.format(new Date()));
                    datetime = dateFormat2.format(new Date());
                    try {
                        link = new WebUtils().linkKQXS("https://www.minhngoc.net.vn/xo-so-truc-tiep/mien-bac.html");
                    } catch (Exception e) {
                        new WebUtils().sendEmail(javaMailSender,
                                "Chạy kết quả xổ số Miền Bắc ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                                e.fillInStackTrace().toString());
                    }
                }
            }

            //String url = configProp.getConfigValue("lottery.xsmb") + System.currentTimeMillis();
            if (link == null || link.equals("")) {
                return;
            }

            String url = link + "?_=" + System.currentTimeMillis();
            log.info(url);
            String content = new WebUtils().getContent(url);
            log.info(content);

            Calendar cal = Calendar.getInstance();
            int date_of_week = cal.get(Calendar.DAY_OF_WEEK);

            String tinh[] = null;
            String regex = ":\\{(.*?)\\}";

            switch (date_of_week) {
                case 1:
                    tinh = t1;
                    break;
                case 2:
                    tinh = t2;
                    break;
                case 3:
                    tinh = t3;
                    break;
                case 4:
                    tinh = t4;
                    break;
                case 5:
                    tinh = t5;
                    break;
                case 6:
                    tinh = t6;
                    break;
                case 7:
                    tinh = t7;
                    break;
            }

            regex = tinh[0] + regex;

            Matcher m = Pattern.compile(regex).matcher(content);
            while (m.find()) {

                String giai = m.group(1);

                for (int g = 0; g <= 7; g++) {

                    giai = giai.replaceAll(g + ":", "g" + g + ":");
                }

                giai = "{" + giai + "}";

                try {
                    JSONObject obj = new JSONObject(giai);

                    String subsms = "";

                    String result = "";

                    for (int g = 0; g <= 7; g++) {

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
                                lotteryResultRepository.updateResult(kqxs, g, 1, "MB");
                            }
                        }
                    }

                    String date = new SimpleDateFormat("dd/MM").format(new Date());
                    String MT = "\n ...dang XO...tiep... ";

                    if (subsms.equals("")) {
                        MT = "\n *HDXS Dang Kiem Tra Bong...\n *T.Thuat TrucTiep SieuToc\n >Soan: XSMB gui 7725";
                    }

                    String prize_2 = "";

                    if (("XSMB " + date + "\n" + subsms.trim().replaceAll(" ", "-") + MT).length() < 160) {
                        prize_2 += "XSMB " + date + "\n" + subsms.trim().replaceAll(" ", "-") + MT;
                    } else {
                        prize_2 += "XSMB " + date + "\n" + subsms.trim().replaceAll(" ", "-");
                    }

                    new WebUtils().WriteData56("MB", prize_2, configProp);

                    new WebUtils().UpdateKQXS(Integer.parseInt(tinh[2]), lotteryResultRepository);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
