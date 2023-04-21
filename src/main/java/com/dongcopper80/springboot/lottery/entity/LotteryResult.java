/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongcopper80.springboot.lottery.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author dongc
 */
public class LotteryResult implements Serializable {
    
    private BigDecimal AreaId,PrizeId;
    private String Alias,ResultNumber;
    private Date ResultDate;
    
    public LotteryResult(BigDecimal AreaId, BigDecimal PrizeId, String Alias, 
            String ResultNumber, Date ResultDate){
        this.AreaId = AreaId;
        this.PrizeId = PrizeId;
        this.Alias = Alias;
        this.ResultNumber = ResultNumber;
        this.ResultDate = ResultDate;
    }

    public BigDecimal getAreaId() {
        return AreaId;
    }

    public void setAreaId(BigDecimal AreaId) {
        this.AreaId = AreaId;
    }

    public BigDecimal getPrizeId() {
        return PrizeId;
    }

    public void setPrizeId(BigDecimal PrizeId) {
        this.PrizeId = PrizeId;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String Alias) {
        this.Alias = Alias;
    }

    public String getResultNumber() {
        return ResultNumber;
    }

    public void setResultNumber(String ResultNumber) {
        this.ResultNumber = ResultNumber;
    }

    public Date getResultDate() {
        return ResultDate;
    }

    public void setResultDate(Date ResultDate) {
        this.ResultDate = ResultDate;
    }
}
