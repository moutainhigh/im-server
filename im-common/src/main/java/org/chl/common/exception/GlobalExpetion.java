package org.chl.common.exception;

import com.alibaba.fastjson.JSONObject;
import org.chl.common.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class GlobalExpetion implements HandlerExceptionResolver {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExpetion.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse resp, Object o, Exception e) {
        JSONObject result = null;
        if (e instanceof Exception) {
            LOG.error("全局异常:", e);
            result = ResultUtil.failure("系统繁忙");
        }
        PrintWriter writer = null;
        try {
            resp.setHeader("Content-type", MediaType.APPLICATION_JSON_VALUE);
            writer = resp.getWriter();
            writer.write(result.toString());
        } catch (IOException ex) {
            LOG.error(ex.getMessage(), ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return new ModelAndView();
    }
}
