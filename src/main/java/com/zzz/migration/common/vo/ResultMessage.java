package com.zzz.migration.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: Zzz
 * @date: 2023/7/7 11:27
 * @description: 前后端交互VO
 */
@Data
public class ResultMessage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标志
     */
    private boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 结果对象
     */
    private T result;


    public ResultMessage<T> success(T result) {
        this.success = true;
        this.code = 200;
        this.message = "success";
        this.result = result;
        return this;
    }

    public ResultMessage<T> fail(T result) {
        this.success = false;
        this.code = 500;
        this.message = "fail";
        this.result = result;
        return this;
    }

    public ResultMessage<T> fail() {
        this.success = false;
        this.code = 500;
        this.message = "fail";
        return this;
    }

    public ResultMessage<T> success() {
        this.success = true;
        this.code = 200;
        this.message = "success";
        return this;
    }
}
