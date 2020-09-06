package com.weweibuy.framework.common.mvc.advice;

import com.weweibuy.framework.common.core.exception.MethodKeyFeignException;
import com.weweibuy.framework.common.core.model.ResponseCodeAndMsg;
import com.weweibuy.framework.common.core.model.dto.CommonCodeJsonResponse;
import com.weweibuy.framework.common.core.model.eum.CommonErrorCodeEum;
import com.weweibuy.framework.common.log.logger.HttpLogger;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * mvc 全局异常处理
 *
 * @author durenhao
 * @date 2020/3/2 17:39
 **/
@RestControllerAdvice
@Slf4j
public class FeignExceptionAdvice {

    @Autowired(required = false)
    private FeignExceptionHandler exceptionHandler;


    /**
     * Feign 调用异常 处理
     *
     * @param request
     * @param e
     * @return
     * @throws IOException
     */
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonCodeJsonResponse> handler(HttpServletRequest request, FeignException e) throws IOException {
        HttpLogger.determineAndLogForJsonRequest(request);

        log.warn("调用外部接口异常: ", e);
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CommonCodeJsonResponse.response(CommonErrorCodeEum.NETWORK_EXCEPTION));
        }
        if (exceptionHandler != null && e instanceof MethodKeyFeignException) {
            return exceptionHandler.handlerException(request, (MethodKeyFeignException) e);
        }

        ResponseCodeAndMsg codeAndMsg = null;
        if (e.status() < 500) {
            codeAndMsg = CommonErrorCodeEum.REQUEST_EXCEPTION;
        } else {
            codeAndMsg = CommonErrorCodeEum.UNKNOWN_SERVER_EXCEPTION;
        }

        return ResponseEntity.status(e.status()).body(CommonCodeJsonResponse.response(codeAndMsg));
    }


}
