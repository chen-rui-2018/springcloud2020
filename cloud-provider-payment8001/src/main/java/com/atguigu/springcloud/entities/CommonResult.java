package com.atguigu.springcloud.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author： chenr
 * @date： Created on 2021/6/18 17:39
 * @version： v1.0
 * @modified By:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> implements Serializable {
    private Integer code;
    private String message;
    private T data;
    public CommonResult(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
