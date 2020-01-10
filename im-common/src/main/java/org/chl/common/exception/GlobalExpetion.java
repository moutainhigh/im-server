package org.chl.common.exception;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.chl.common.util.ResultUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@Component
public class GlobalExpetion implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object o, Exception e) {
        JSONObject result = null;
        if (e instanceof Exception) {
            log.error("全局异常:", e);
            result = ResultUtil.failure("系统繁忙");
        }
        PrintWriter writer = null;
        try {
            resp.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
            writer = resp.getWriter();
            writer.write(result.toString());
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return new ModelAndView();
    }
}
