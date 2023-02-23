package com.tencent.wxcloudrun.controller;

import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.utils.SHA1;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @className: MessageController
 * @author: weishihuan
 * @date: 2022-07-15 21:50
 **/
@RestController
@RequestMapping("/api/wechat/message")
public class MessageController {
    private static final String token = "hhkt";

    /**
     * 1.服务器有效性验证      在测试号管理中的接口配置信息修改中填写URL和Token的保存的时候会调用 https://39au046856.zicp.fun/api/wechat/message
     *
     * @param request
     * @return
     */
    @GetMapping
    public String verifyToken(HttpServletRequest request) {
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        if (this.checkSignature(signature, timestamp, nonce)) {
            System.out.println("token ok");
            return echostr;
        }
        return echostr;
    }

    //检查签名
    private boolean checkSignature(String signature, String timestamp, String nonce) {
        String[] str = new String[]{token, timestamp, nonce};
        //排序
        Arrays.sort(str);
        //拼接字符串
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            buffer.append(str[i]);
        }
        //进行sha1加密
        String temp = SHA1.encode(buffer.toString());
        //与微信提供的signature进行匹对
        return signature.equals(temp);
    }

    /**
     * 接收微信服务器发送来的消息
     *
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping
    public String receiveMessage(HttpServletRequest request) throws Exception {

        WxMpXmlMessage wxMpXmlMessage = WxMpXmlMessage.fromXml(request.getInputStream());
        System.out.println(JSONObject.toJSONString(wxMpXmlMessage));
        return "success test";
    }
}
