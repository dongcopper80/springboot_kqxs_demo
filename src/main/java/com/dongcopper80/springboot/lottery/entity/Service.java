/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dongcopper80.springboot.lottery.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author dongc
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Service implements Serializable {
    
    private BigDecimal ServiceId,AreaId;
    private String Message,DauSoQC,NoiDungQC,TinNhanQC;
}
