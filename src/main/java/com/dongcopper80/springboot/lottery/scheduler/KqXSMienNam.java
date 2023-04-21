/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongcopper80.springboot.lottery.scheduler;

import com.dongcopper80.springboot.lottery.ConfigProperties;
import com.dongcopper80.springboot.lottery.repository.LotteryResultRepository;
import com.dongcopper80.springboot.lottery.WebUtils;
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
public class KqXSMienNam {

    private static final Logger log = LoggerFactory.getLogger(KqXSMienNam.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private static final SimpleDateFormat dateFormat2 = new SimpleDateFormat("ddMMyyyy");

    @Autowired
    @Qualifier("jdbcLotteryResultRepository")
    private LotteryResultRepository lotteryResultRepository;

    @Autowired
    ConfigProperties configProp;

    private String datetime = "";

    static {
        HttpsURLConnection.setDefaultHostnameVerifier((String hostname, SSLSession session) -> hostname.equals("113.190.252.186"));
    }

    String t1[][] = {
        {"24", "Đà Lạt", "44", "LD"},
        {"23", "Kiên Giang", "43", "KG"},
        {"22", "Tiền Giang", "55", "TG"}
    };
    String t2[][] = {
        {"3", "Cà Mau", "38", "CM"},
        {"2", "Đồng Tháp", "41", "DT"},
        {"1", "TP. HCM", "56", "TP"}
    };

    String t3[][] = {
        {"9", "Bạc Liêu", "33", "BL"},
        {"7", "Bến Tre", "34", "BTR"},
        {"8", "Vũng Tàu", "54", "VT"}
    };
    String t4[][] = {
        {"11", "Cần Thơ", "39", "CT"},
        {"10", "Đồng Nai", "40", "DN"},
        {"12", "Sóc Trăng", "53", "ST"}
    };
    String t5[][] = {
        {"14", "An Giang", "32", "AG"},
        {"15", "Bình Thuận", "37", "BTH"},
        {"13", "Tây Ninh", "64", "TN"}
    };
    String t6[][] = {
        {"17", "Bình Dương", "35", "BD"},
        {"18", "Trà Vinh", "58", "TV"},
        {"16", "Vĩnh Long", "59", "VL"}
    };
    String t7[][] = {
        {"21", "Bình Phước", "36", "BP"},
        {"20", "Hậu Giang", "42", "HG"},
        {"19", "Long An", "45", "LA"}
    };

    String ketqua = "";
    String link = "";

    @Autowired
    private JavaMailSender javaMailSender;

    @Scheduled(fixedRate = 3000)
    public void reportCurrentTime() {
        //TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Calendar from = Calendar.getInstance();
        from.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        from.set(Calendar.HOUR_OF_DAY, 16);
        from.set(Calendar.MINUTE, 10);

        Calendar to = Calendar.getInstance();
        to.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        to.set(Calendar.HOUR_OF_DAY, 17);
        to.set(Calendar.MINUTE, 0);

        if (new Date().after(from.getTime()) && new Date().before(to.getTime())) {

            log.info("KqXSMienNam The time 2 is now {}", dateFormat.format(new Date()));

            if (datetime.equals("")) {
                new WebUtils().sendEmail(javaMailSender,
                        "Chạy kết quả xổ số Miền Nam ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                        "Kết quả xổ số Miền Nam bắt đầu chạy lúc " + dateFormat.format(new Date()));
                datetime = dateFormat2.format(new Date());

                try {
                    link = new WebUtils().linkKQXS("https://www.minhngoc.net.vn/xo-so-truc-tiep/mien-nam.html");
                } catch (Exception e) {
                    new WebUtils().sendEmail(javaMailSender,
                            "Chạy kết quả xổ số Miền Nam ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                            e.fillInStackTrace().toString());
                }
            } else {
                if (!datetime.equals(dateFormat2.format(new Date()))) {
                    new WebUtils().sendEmail(javaMailSender,
                            "Chạy kết quả xổ số Miền Nam ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                            "Kết quả xổ số Miền Nam bắt đầu chạy lúc " + dateFormat.format(new Date()));
                    datetime = dateFormat2.format(new Date());

                    try {
                        link = new WebUtils().linkKQXS("https://www.minhngoc.net.vn/xo-so-truc-tiep/mien-nam.html");
                    } catch (Exception e) {
                        new WebUtils().sendEmail(javaMailSender,
                                "Chạy kết quả xổ số Miền Nam ngày " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
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

            String tinh[][] = null;

            Calendar cal = Calendar.getInstance();
            int date_of_week = cal.get(Calendar.DAY_OF_WEEK);

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

            for (int i = 0; i < tinh.length; i++) {
                String province[] = tinh[i];

                String regex = ":\\{(.*?)\\}";
                regex = province[0] + regex;

                Matcher m = Pattern.compile(regex).matcher(content);

                while (m.find()) {
                    String giai = m.group(1);

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
        } else {
            log.info("KqXSMienNam The time 1 is now {}", dateFormat.format(new Date()));
        }
    }
}
