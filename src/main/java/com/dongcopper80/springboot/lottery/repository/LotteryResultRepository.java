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
import java.util.List;

/**
 *
 * @author dongc
 */
public interface LotteryResultRepository {
    
    List<LotteryResult> findAll(int p_MaTinh,String p_fromDate,String p_toDate);
    
    List<LotteryResult> findByDate(int p_MaTinh,String date);
    
    List<BigDecimal> findProvinces(String p_date, int p_region);
    
    List<BigDecimal> findAllProvinces(String p_date);
    
    void updateResult(String result_str, int p_prize_id, int p_area_id, String p_alias);
    
    List<Service> getLotteryService(int p_area_id, int p_ma_dichvu, String dauso);
    
    List<Thongke> getLotteryThongkeLoto(int p_area_id, int p_songaylay, String p_loai);
}
