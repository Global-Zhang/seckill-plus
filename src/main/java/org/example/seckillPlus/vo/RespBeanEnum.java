package org.example.seckillPlus.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public enum RespBeanEnum {

    //通用状态码
    SUCCESS(200,"SUCCESS"),
    ERROR(200,"服务器异常"),

    //登录模块5002xx
    SESSION_ERROR(500210,"session不存在或者已经失效"),
    LOGINVO_ERROR(500211,"用户名或密码错误"),
    ORDER_NOT_EXIST(500215,"商品不存在"),
    MOBILE_ERROR(500212,"手机号码格式错误"),
    REPEATE_ERROR(500214,"不能重复购买"),
    MOBILE_NOT_EXIST(500213, "手机号码不存在"),
    PASSWORD_UPDATE_FAIL(500214, "密码更新失败"),
    BIND_ERROR(300210,"参数校验错误"),
    EMPTY_STOCK(300220,"库存为空");

    private final Integer code;
    private final  String msg;


}
