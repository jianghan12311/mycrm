package com.wu.crm;

import com.alibaba.fastjson.JSON;
import com.wu.crm.base.ResultInfo;
import com.wu.crm.exceptions.AuthException;
import com.wu.crm.exceptions.NoLoginException;
import com.wu.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 全局异常统一处理
 */
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    /**
     * 异常处理方法
     *
     * 方法的返回值:
     *      1.返回视图
     *      2.返回数据(JSON数据)
     *
     *  如何判断方法的返回值？
     *      通过判断方法上是否声明@ResponseBody注解
     *         如果未声明，则表示返回视图
     *         如果声明，则表示返回数据
     *
     * @param httpServletRequest request请求对象
     * @param httpServletResponse response响应对象
     * @param handler 方法对象
     * @param e 异常对象
     * @return
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) {

        /**
         * 非法请求拦截
         * 判断是否抛出未登录异常 如果抛出该异常则要求用户登录，重定向跳转到登录页面
         */
        if (e instanceof NoLoginException){
            //重定向到登录页面
            ModelAndView modelAndView = new ModelAndView("redirect:/index");
            return modelAndView;
        }


        /**
         * 设置默认的异常处理返回视图
         */
        ModelAndView modelAndView = new ModelAndView("error");

        //设置异常信息
        modelAndView.addObject("code",500);
        modelAndView.addObject("msg","系统异常，请重试");

        //判断HandlerMethod
        if (handler instanceof HandlerMethod){
            //类型转换
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //获取方法上声明的responseBody注解对象
            ResponseBody responseBody = handlerMethod.getMethod().getDeclaredAnnotation(ResponseBody.class);
            //判断responseBody是否为空 如果对象为空则表示返回的是视图 如果不为空表示返回的是数据
            if (responseBody == null){
                //返回视图
                //判断异常类型
                if (e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    //设置异常信息
                    modelAndView.addObject("code",pe.getCode());
                    modelAndView.addObject("msg",pe.getMsg());
                }else if (e instanceof AuthException){ //认证异常
                    AuthException ae = (AuthException) e;
                    //设置异常信息
                    modelAndView.addObject("code",ae.getCode());
                    modelAndView.addObject("msg",ae.getMsg());
                }

                return modelAndView;
            }else {
                //返回数据
                //设置默认的异常处理
                ResultInfo resultInfo = new ResultInfo();
                resultInfo.setCode(500);
                resultInfo.setMsg("系统异常请重试");

                //判断异常类型是否是自定义异常
                if (e instanceof ParamsException){
                    ParamsException pe = (ParamsException) e;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                }else if (e instanceof AuthException){ //认证异常
                    AuthException ae = (AuthException) e;
                    resultInfo.setCode(ae.getCode());
                    resultInfo.setMsg(ae.getMsg());
                }

                //设置响应格式及编码格式 响应JSON格式的数据
                httpServletResponse.setContentType("application/json;charset=UTF-8");

                PrintWriter out = null;
                try {
                    //得到字符输出流
                    out = httpServletResponse.getWriter();

                    //将需要返回的对象转换成json格式的字符串
                    String json = JSON.toJSONString(resultInfo);

                    //输出数据
                    out.write(json);

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }finally {
                    //如果对象不为空则关闭对象
                    if (out != null){
                        out.close();
                    }
                }

                return null;
            }
        }

        return modelAndView;
    }
}
