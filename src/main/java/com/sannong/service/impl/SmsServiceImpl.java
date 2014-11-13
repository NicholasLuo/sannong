package com.sannong.service.impl;


import com.sannong.domain.sms.SmsUrlGenerator;
import com.sannong.infrastructure.persistance.entity.SMS;
import com.sannong.infrastructure.persistance.entity.User;
import com.sannong.infrastructure.persistance.repository.SmsRepository;
import com.sannong.infrastructure.sms.SmsSender;
import com.sannong.infrastructure.util.AppConfig;
import com.sannong.infrastructure.util.PasswordGenerator;
import com.sannong.service.ISmsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Bright Huang on 10/22/14.
 */
@Service
public class SmsServiceImpl implements ISmsService {
    private static final Logger logger = Logger.getLogger(SmsServiceImpl.class);

    @Autowired
    private SmsRepository smsRepository;
    @Autowired
    private AppConfig appConfig;

    public boolean updateSMS(HttpServletRequest request) {
        SMS sms = new SMS();
        String id = request.getParameter("smsid");
        //String time = request.getParameter("sendtime");
        Timestamp time = new Timestamp(System.currentTimeMillis());
        if (id.length() < 0) {
            return false;
        }
        long smsId = new Integer(id);
        /*
        Date now = new Date();
        try {
            now = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(now);
        }
        */
        sms.setSmsId(smsId);
        sms.setSendTime(time);
        sms.setSmsStatus(1);
        smsRepository.updateSMS(sms);
        return true;
    }

    public List<SMS> getNewSMS() {

        return smsRepository.getNewSMS();
    }

    public int validateSMSCode(HttpServletRequest request) {
        String smsCode = request.getParameter("validationcode");

        if (StringUtils.isEmpty(smsCode)) {
            return 0;
        }

        if (request.getSession().getAttribute(appConfig.getSessionSmsCodes()) == null) {
            return 1;
        }

        Map<Date, String> map = (HashMap<Date, String>) request.getSession().getAttribute(appConfig.getSessionSmsCodes());

        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            Date dt = (Date) mapEntry.getKey();
            String savecode = mapEntry.getValue().toString();
            if (savecode.equals(smsCode)) {
                Date dtNow = new Date(System.currentTimeMillis());
                Date dtSms = new Date(dt.getTime());

                @SuppressWarnings("deprecation")
                int diffInHours = dtNow.getHours() - dt.getHours();

                @SuppressWarnings("deprecation")
                int diffInMinuts = dtNow.getMinutes() - dt.getMinutes();
                if (diffInHours == 0 && diffInMinuts < 5) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override
    public Long getMaxSmsIdByCellphone(HttpServletRequest request) {
        String mobile = request.getParameter("mobile").toString();

        SMS sms = new SMS();
        sms.setCellphone(mobile);
        return smsRepository.getMaxSmsIdByCellphone(sms);
    }

    public boolean generateCode(HttpServletRequest request) {

        String mobile = request.getParameter("mobile").toString();
        String smstype = request.getParameter("smstype").toString();


        String regcode = PasswordGenerator.generateValidationCode(4);

        Map<Date, String> map = new HashMap<Date, String>();
        if (mobile.length() < 11)
            return false;
        else {
            SMS sms = new SMS();

            sms.setCellphone(mobile);
            sms.setSmsValidationCode(regcode);

            Date ts = new Date(System.currentTimeMillis());
            if (request.getSession().getAttribute(appConfig.getSessionSmsCodes()) != null) {
                map = (HashMap<Date, String>) request.getSession().getAttribute(appConfig.getSessionSmsCodes());
            }
            map.put(ts, regcode);

            request.getSession().setAttribute(appConfig.getSessionSmsCodes(), map);

            String content = "验证码为:" + regcode;
            if (smstype.equals("0")) {
                content = appConfig.getProperty("sms-welcome").replace("{0}", regcode);
            }
            if (smstype.equals("1")) {
                content = appConfig.getProperty("sms-changeMobile").replace("{0}", regcode);
                //content="改变你在三农网上的注册手机号码，验证码为:"+regcode;
            }
            if (smstype.equals("2")) {
                content = appConfig.getProperty("sms-newPassword").replace("{0}", regcode);
                //content="你在三农网上新密码为:"+regcode;
            }

            sms.setSmsContent(content);
            sms.setSendTime(new Timestamp(System.currentTimeMillis()));
            smsRepository.addNewSMS(sms);

            return true;
        }
    }


    @Override
    public String sendValidationCode(HttpServletRequest request) {
        String validationCode = PasswordGenerator.generateValidationCode(4);
        String cellphone = request.getParameter("mobile");

        String result = "";
        try{
            SmsSender smsSender = new SmsSender();
            String smsUrl = new SmsUrlGenerator().generateValidationCodeSmsUrl(cellphone, validationCode);

            result = smsSender.sendSms(smsUrl);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (result != ""){
                SMS sms = new SMS();
                sms.setCellphone(cellphone);
                sms.setSmsValidationCode(validationCode);
                sms.setSmsContent(result);
                sms.setSendTime(timestamp);
                sms.setSmsStatus(0);
                smsRepository.addNewSMS(sms);

            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public String sendLoginMessage(HttpServletRequest request){
        String validationCode = PasswordGenerator.generateValidationCode(6);
        String cellphone = request.getParameter("mobile");
        String result = "";
        try{
            SmsSender smsSender = new SmsSender();
            String smsUrl = new SmsUrlGenerator().generateLoginMessageSmsUrl(cellphone, validationCode);

            result = smsSender.sendSms(smsUrl);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            if (result != ""){
                SMS sms = new SMS();
                sms.setCellphone(cellphone);
                sms.setSmsValidationCode(validationCode);
                sms.setSmsContent(result);
                sms.setSendTime(timestamp);
                sms.setSmsStatus(0);
                smsRepository.addNewSMS(sms);
            }

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public String sendNewPasswordMessage(String url) {
        SmsSender smsSender = new SmsSender();
        return smsSender.sendSms(url);
    }

}
