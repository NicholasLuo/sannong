package sannongtest;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;

import com.sannong.sms.*;

import org.junit.Test;

public class smstest {

	@Test
	public void testSms() throws UnsupportedEncodingException {
		
		String ret=SmsUtil.SendSms("13128818478", "welcome you register our website, �����֤����123456");
	    Integer result=new Integer(ret);
    	assertTrue("test success", result>0);
	}

}
