package com.titancore.enums;

import com.titancore.framework.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ----------- 通用异常状态码 -----------
    SYSTEM_ERROR("AUTH-10000", "出错啦，正在努力修复中..."),
    PARAM_NOT_VALID("AUTH-10001", "参数错误"),
    VERIFICATION_CODE_SEND_FREQUENTLY("AUTH-20000", "请求太频繁，请3分钟后再试"),
    VERIFICATION_CODE_ERROR("AUTH-20001", "验证码错误"),
    ACCOUNT_DELETE("AUTH-30001","账号已被删除,请联系管理员！"),
    PASSWORD_ERROR("AUTH-30002","密码错误"),
    ACCOUNT_LOCKED("AUTH-30003","账号被锁定"),
    ;

    // ----------- 业务异常状态码 -----------

    // 异常码
    private final String errorCode;
    // 错误信息
    private final String errorMessage;

    // ----------- 业务异常状态码 -----------
}