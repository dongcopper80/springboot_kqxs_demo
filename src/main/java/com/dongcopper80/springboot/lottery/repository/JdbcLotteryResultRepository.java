/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongcopper80.springboot.lottery.repository;

import com.dongcopper80.springboot.lottery.entity.LotteryResult;
import com.dongcopper80.springboot.lottery.entity.Service;
import com.dongcopper80.springboot.lottery.entity.Thongke;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 *
 * @author dongc
 */
@Repository
public class JdbcLotteryResultRepository implements LotteryResultRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<LotteryResult> findAll(int p_MaTinh, String p_fromDate, String p_toDate) {
        return jdbcTemplate.query("select id,area_id,trunc(result_date,'dd') result_date,prize_id,"
                + "result_number,alias,s_date from content.lottery where area_id = " + p_MaTinh
                + " and trunc(result_date,'dd') >= trunc(to_date('" + p_fromDate + "', 'dd-MM-yyyy'),'dd') "
                + "and result_date < to_date('" + p_toDate + "', 'dd-MM-yyyy') + 1  "
                + "order by trunc(result_date,'dd') desc, prize_id asc, id asc",
                (rs, rowNum)
                -> new LotteryResult(
                        rs.getBigDecimal("AREA_ID"),
                        rs.getBigDecimal("PRIZE_ID"),
                        rs.getString("ALIAS"),
                        rs.getString("RESULT_NUMBER"),
                        rs.getDate("RESULT_DATE")
                )
        );
    }

    @Override
    public List<LotteryResult> findByDate(int p_MaTinh, String date) {
        return jdbcTemplate.query("select id,area_id,trunc(result_date,'dd') result_date,prize_id,"
                + "result_number,alias,s_date from content.lottery where area_id = " + p_MaTinh
                + " and trunc(result_date,'dd') = trunc(to_date('" + date + "', 'dd-MM-yyyy'),'dd') "
                + "order by trunc(result_date,'dd') desc, prize_id asc, id asc",
                (rs, rowNum)
                -> new LotteryResult(
                        rs.getBigDecimal("AREA_ID"),
                        rs.getBigDecimal("PRIZE_ID"),
                        rs.getString("ALIAS"),
                        rs.getString("RESULT_NUMBER"),
                        rs.getDate("RESULT_DATE")
                )
        );
    }

    @Override
    public List<BigDecimal> findProvinces(String p_date, int p_region) {
        return jdbcTemplate.query("select distinct id from area where DAY_BET like "
                + "'%'||to_char(to_date('" + p_date + "', 'dd-MM-yyyy'), 'd')||'%' "
                + "and REGION_ID = " + p_region + " order by id",
                (rs, rowNum)
                -> rs.getBigDecimal("ID")
        );
    }

    @Override
    public List<BigDecimal> findAllProvinces(String p_date) {
        return jdbcTemplate.query("select distinct id from area where DAY_BET like "
                + "'%'||to_char(to_date('" + p_date + "', 'dd-MM-yyyy'), 'd')||'%' order by id",
                (rs, rowNum)
                -> rs.getBigDecimal("ID")
        );
    }

    @Override
    public void updateResult(String result_str, int p_prize_id, int p_area_id, String p_alias) {
        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withSchemaName("content")
                .withCatalogName("LOTTERY_PKG")
                .withFunctionName("UPDATE_RESULT")
                //.withoutProcedureColumnMetaDataAccess()
                .declareParameters(
                        new SqlParameter("result_str", Types.VARCHAR),
                        new SqlParameter("p_prize_id", Types.NUMERIC),
                        new SqlParameter("p_area_id", Types.NUMERIC),
                        new SqlParameter("p_alias", Types.VARCHAR)
                );

        Map<String, Object> inParamMap = new HashMap<>();
        inParamMap.put("result_str", result_str.replaceAll("-", " "));
        inParamMap.put("p_prize_id", p_prize_id);
        inParamMap.put("p_area_id", p_area_id);
        inParamMap.put("p_alias", p_alias);

        SqlParameterSource in = new MapSqlParameterSource(inParamMap);

        Long simpleJdbcCallResult = simpleJdbcCall.executeFunction(BigDecimal.class, in).longValue();

        System.out.println("simpleJdbcCallResult: " + simpleJdbcCallResult);

//        Iterator<Entry<String, Object>> it = simpleJdbcCallResult.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
//            String key = (String) entry.getKey();
//            Object value = (Object) entry.getValue();
//            System.out.println("Key: " + key);
//            System.out.println("Value: " + value);
//        }
    }

    @Override
    public List<Service> getLotteryService(int p_area_id, int p_ma_dichvu, String dauso) {
        return jdbcTemplate.query("select content,appcontent,replace(nvl(qc.noi_dung,''),'%alias',alias) noi_dung_qc ,"
                + "qc.service_number dau_so_qc,replace(nvl(qc.message,''),'%alias',alias) tin_nhan_qc,area_id,"
                + "mt.service_id,mt.service_number,a.region_id from CONTENT.TBL_SUB_MT_DONGNT mt inner join "
                + "content.area a on mt.area_id = a.id left join content.tb_qc_app qc on mt.service_id = qc.service_id "
                + "where area_id = " + p_area_id + " and mt.service_id = " + p_ma_dichvu + " and mt.service_number ='" + dauso + "'",
                (rs, rowNum)
                -> new Service(
                        rs.getBigDecimal("service_id"),
                        rs.getBigDecimal("area_id"),
                        rs.getString("content"),
                        rs.getString("noi_dung_qc"),
                        rs.getString("dau_so_qc"),
                        rs.getString("tin_nhan_qc")
                )
        );
    }

    @Override
    public List<Thongke> getLotteryThongkeLoto(int p_area_id, int p_songaylay, String p_loai) {
        switch (p_loai) {
            case "tkdau":

                break;
            case "tkduoi":

                break;
            case "dacbiet":

                break;
            case "venhieu":

                break;
            case "veit":

                break;
            case "lientiep":

                break;
            case "gan":

                break;
        }

        return null;
    }
}
