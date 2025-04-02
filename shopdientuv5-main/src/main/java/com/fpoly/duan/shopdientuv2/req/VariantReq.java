package com.fpoly.duan.shopdientuv2.req;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class VariantReq{
    
    private Integer productId;
    private Map<Integer, List<Integer>> attributeValues;


}