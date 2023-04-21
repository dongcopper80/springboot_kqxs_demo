/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongcopper80.springboot.lottery.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dongc
 */
@RestController
@RequestMapping("/api/update_kqxs")
@Api(value = "update_kqxs", description = "Chạy cập nhập kqxs cho các site")
public class UpdateKQXS {

    String[][] tinh = {
        {"46", "Miền bắc", "1", "MB"},
        {"47", "Miền bắc", "1", "MB"},
        {"48", "Miền bắc", "1", "MB"},
        {"46", "Miền bắc", "1", "MB"},
        {"49", "Miền bắc", "1", "MB"},
        {"50", "Miền bắc", "1", "MB"},
        {"51", "Miền bắc", "1", "MB"},
        {"1", "TP. HCM", "56", "TP"},
        {"2", "Đồng Tháp", "41", "DT"},
        {"3", "Cà Mau", "38", "CM"},
        {"9", "Bạc Liêu", "33", "BL"},
        {"7", "Bến Tre", "34", "BTR"},
        {"8", "Vũng Tàu", "54", "VT"},
        {"11", "Cần Thơ", "39", "CT"},
        {"10", "Đồng Nai", "40", "DN"},
        {"12", "Sóc Trăng", "53", "ST"},
        {"14", "An Giang", "32", "AG"},
        {"15", "Bình Thuận", "37", "BTH"},
        {"13", "Tây Ninh", "64", "TN"},
        {"17", "Bình Dương", "35", "BD"},
        {"18", "Trà Vinh", "58", "TV"},
        {"16", "Vĩnh Long", "59", "VL"},
        {"21", "Bình Phước", "36", "BP"},
        {"20", "Hậu Giang", "42", "HG"},
        {"19", "Long An", "45", "LA"},
        {"24", "Đà Lạt", "44", "LD"},
        {"23", "Kiên Giang", "43", "KG"},
        {"22", "Tiền Giang", "55", "TG"},
        {"27", "Phú Yên", "26", "PY"},
        {"26", "Thừa T. Huế", "31", "HUE"},
        {"29", "Đắk Lắk", "12", "DLK"},
        {"28", "Quảng Nam", "28", "QNM"},
        {"30", "Đà Nẵng", "10", "DNG"},
        {"31", "Khánh Hòa", "15", "KHA"},
        {"32", "Bình Định", "9", "BDI"},
        {"34", "Quảng Bình", "27", "QBN"},
        {"33", "Quảng Trị", "30", "QTR"},
        {"35", "Gia Lai", "14", "GL"},
        {"36", "Ninh Thuận", "25", "NTH"},
        {"38", "Đắk Nông", "13", "DNO"},
        {"37", "Quảng Ngãi", "29", "QNG"},
        {"39", "Kon Tum", "17", "KOT"}
    };

    String mtt1[][] = {
        {"31", "Khánh Hòa", "15", "KHA"},
        {"39", "Kon Tum", "17", "KOT"}
    };
    String mtt2[][] = {
        {"27", "Phú Yên", "26", "PY"},
        {"26", "Thừa T. Huế", "31", "HUE"}
    };

    String mtt3[][] = {
        {"29", "Đắk Lắk", "12", "DLK"},
        {"28", "Quảng Nam", "28", "QNM"}
    };
    String mtt4[][] = {
        {"30", "Đà Nẵng", "10", "DNG"},
        {"31", "Khánh Hòa", "15", "KHA"}
    };
    String mtt5[][] = {
        {"32", "Bình Định", "9", "BDI"},
        {"34", "Quảng Bình", "27", "QBN"},
        {"33", "Quảng Trị", "30", "QTR"}
    };
    String mtt6[][] = {
        {"35", "Gia Lai", "14", "GL"},
        {"36", "Ninh Thuận", "25", "NTH"}
    };
    String mtt7[][] = {
        {"30", "Đà Nẵng", "10", "DNG"},
        {"38", "Đắk Nông", "13", "DNO"},
        {"37", "Quảng Ngãi", "29", "QNG"}
    };

    String mnt1[][] = {
        {"24", "Đà Lạt", "44", "LD"},
        {"23", "Kiên Giang", "43", "KG"},
        {"22", "Tiền Giang", "55", "TG"}
    };
    String mnt2[][] = {
        {"3", "Cà Mau", "38", "CM"},
        {"2", "Đồng Tháp", "41", "DT"},
        {"1", "TP. HCM", "56", "TP"}
    };

    String mnt3[][] = {
        {"9", "Bạc Liêu", "33", "BL"},
        {"7", "Bến Tre", "34", "BTR"},
        {"8", "Vũng Tàu", "54", "VT"}
    };
    String mnt4[][] = {
        {"11", "Cần Thơ", "39", "CT"},
        {"10", "Đồng Nai", "40", "DN"},
        {"12", "Sóc Trăng", "53", "ST"}
    };
    String mnt5[][] = {
        {"14", "An Giang", "32", "AG"},
        {"15", "Bình Thuận", "37", "BTH"},
        {"13", "Tây Ninh", "64", "TN"}
    };
    String mnt6[][] = {
        {"17", "Bình Dương", "35", "BD"},
        {"18", "Trà Vinh", "58", "TV"},
        {"16", "Vĩnh Long", "59", "VL"}
    };
    String mnt7[][] = {
        {"21", "Bình Phước", "36", "BP"},
        {"20", "Hậu Giang", "42", "HG"},
        {"19", "Long An", "45", "LA"},
        {"1", "TP. HCM", "56", "TP"}
    };

    String mbt1[] = {"51", "Miền bắc", "1", "MB"};
    String mbt2[] = {"46", "Miền bắc", "1", "MB"};
    String mbt3[] = {"47", "Miền bắc", "1", "MB"};
    String mbt4[] = {"48", "Miền bắc", "1", "MB"};
    String mbt5[] = {"46", "Miền bắc", "1", "MB"};
    String mbt6[] = {"49", "Miền bắc", "1", "MB"};
    String mbt7[] = {"50", "Miền bắc", "1", "MB"};

    @ApiOperation(value = "Lấy kết quả xổ số Miền Bắc n ngày gần nhất theo tỉnh")
    @RequestMapping(value = "/xsmb/{p_fromDate}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> xsmb(
            @PathVariable String p_fromDate) {

        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("dd-MM-yyyy").parse(p_fromDate));
            int date_of_week = cal.get(Calendar.DAY_OF_WEEK);
            String tinhmb[] = null;

            switch (date_of_week) {
                case 1:
                    tinhmb = mbt1;
                    break;
                case 2:
                    tinhmb = mbt2;
                    break;
                case 3:
                    tinhmb = mbt3;
                    break;
                case 4:
                    tinhmb = mbt4;
                    break;
                case 5:
                    tinhmb = mbt5;
                    break;
                case 6:
                    tinhmb = mbt6;
                    break;
                case 7:
                    tinhmb = mbt7;
                    break;
            }

            String URL = "https://www.minhngoc.net.vn/tra-cuu-ket-qua-xo-so-tinh.html?tinh="
                    + tinhmb[0] + "&ngay=" + new SimpleDateFormat("dd").format(cal.getTime()) + "&thang="
                    + new SimpleDateFormat("MM").format(cal.getTime()) + "&nam="
                    + new SimpleDateFormat("yyyy").format(cal.getTime());

            Document document = Jsoup.connect(URL).get();

            Element table = document.select("table[class=box_kqxs_content]").first();

            String giaidb = table.select("td[class=giaidb]").first().text();
            String giai1 = table.select("td[class=giai1]").first().text();
            String giai2 = table.select("td[class=giai2]").first().text();
            String giai3 = table.select("td[class=giai3]").first().text();
            String giai4 = table.select("td[class=giai4]").first().text();
            String giai5 = table.select("td[class=giai5]").first().text();
            String giai6 = table.select("td[class=giai6]").first().text();
            String giai7 = table.select("td[class=giai7]").first().text();

        } catch (Exception e) {
            return new ResponseEntity<>(e.fillInStackTrace().toString(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
