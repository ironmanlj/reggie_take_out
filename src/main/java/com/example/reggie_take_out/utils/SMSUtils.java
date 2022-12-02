package com.example.reggie_take_out.utils;

/**
 * @创建人 ironman_lj
 * @创建时间 2022-11-30
 * @描述
 */


import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 短信发送工具类
 */
public class SMSUtils {

    /**
     * 发送短信
     * @param signName 签名
     * @param templateCode 模板
     * @param phoneNumbers 手机号
     * @param param 参数
     */
    public static void sendMessage(String signName, String templateCode,String phoneNumbers,String param){
        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", "LTAI5tBTrTukAgRfQaeJ1teJ8bNDcaQMeXNE8", "pOuqmxPnmAs8r8UHw");
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setSysRegionId("cn-shanghai");
        request.setPhoneNumbers(phoneNumbers);
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setTemplateParam("{\"code\":\""+param+"\"}");
        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(response.toString());
            System.out.println("短信发送成功");
        }catch (ClientException e) {
            e.printStackTrace();
        }
    }

}

