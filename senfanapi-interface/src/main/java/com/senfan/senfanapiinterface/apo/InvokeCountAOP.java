package com.senfan.senfanapiinterface.apo;

import org.springframework.web.bind.annotation.RestControllerAdvice;

// todo 尝试使用 AOP 切面实现每调用一次接口增加一次调用次数
@RestControllerAdvice
public class InvokeCountAOP {
}
