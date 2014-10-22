package com.sannong.presentation.controller;

import com.sannong.infrastructure.persistance.entity.SMS;
import com.sannong.infrastructure.persistance.entity.User;
import com.sannong.service.ISmsService;
import com.sannong.service.IUserService;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("myinfo")
public class PersonalCenterController {
    private static final String MY_APPLICATION_PAGE = "myapplication";
    private static final String MY_INFO_PAGE = "myinfo";
    private static final String MY_PASSWORD_PAGE = "mypassword";
    private static final String APPLICANTS_PAGE = "applicants";

    @Resource
    private IUserService userService;
    @Resource
    private ISmsService smsService;


    @RequestMapping(value = "myapplication", method = RequestMethod.GET)
    public ModelAndView myApplication(HttpServletRequest request, HttpServletResponse response) {

        Map<String, Object> models = new HashMap<String, Object>();
        models.put("myapplication", new Object());

        return new ModelAndView(MY_APPLICATION_PAGE, models);
    }

    @RequestMapping(value = "updatesms", method = RequestMethod.GET)
    public @ResponseBody boolean updateSMS(HttpServletRequest request) {
        return smsService.updateSMS(request);
    }

    @RequestMapping(value = "getnewsms", method = RequestMethod.GET)
    public @ResponseBody List<SMS> getNewSMS() {
        return smsService.getNewSMS();
    }

    @RequestMapping(value = "regcode", method = RequestMethod.GET)
    public @ResponseBody boolean generateCode(HttpServletRequest request) {
        return smsService.generateCode(request);
    }

    @RequestMapping(value = "modifyMyinfo", method = RequestMethod.POST)
	public ModelAndView updateUser(HttpServletRequest request,@ModelAttribute("myinfo")	User user, BindingResult result) {
    	 Map<String, Object> models = new HashMap<String, Object>();
    	 String username;
         Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if (principal instanceof UserDetails) {
             username = ((UserDetails) principal).getUsername();
         } else {
             username = principal.toString();
         }

         User dbuser = userService.getUserByName(username);
         if(!dbuser.getCellphone().toString().equals(user.getCellphone().toString()))
         {
	    	 String code=request.getParameter("validationcode").toString();
	    	 if(!request.getSession().getAttribute("regcode").equals(code))
	    	 {
	    		 models.put("myinfomessage", "mobile change need to set confirm code!");
	    	 }
	    	 return new ModelAndView(MY_INFO_PAGE, models);
         }
    	Timestamp ts  =new Timestamp(System.currentTimeMillis()); 
		user.setUpdateTime(ts);
		try {
			userService.updateUser(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		models.put("myinfomessage", "save!");
		
		return new ModelAndView(MY_INFO_PAGE, models);
	}

    @RequestMapping(value = "myinfo")
    public ModelAndView myInfo() {

        Map<String, Object> models = new HashMap<String, Object>();

        String username;

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("cellphone", username);

        List<User> users = userService.getUserByUserNameOrCellphone(map);

        models.put("myinfo", users.get(0));
      
       // models.addAttribute("myinfo",users.get(0));

        return new ModelAndView(MY_INFO_PAGE, models);
    }

    @RequestMapping(value = "mypassword", method = RequestMethod.GET)
    public ModelAndView myPassword() {

        Map<String, Object> models = new HashMap<String, Object>();
        models.put("mypassword", new Object());

        return new ModelAndView(MY_PASSWORD_PAGE, models);
    }

    @RequestMapping(value = "applicants", method = RequestMethod.GET)
    public ModelAndView showList() {

        Map<String, Object> models = new HashMap<String, Object>();
        models.put("applicants", new Object());
        return new ModelAndView(APPLICANTS_PAGE, models);
    }

    @RequestMapping(value = "updatepassword", method = RequestMethod.POST)
    public ModelAndView updatePassword(HttpServletRequest request) throws Exception {

        Map<String, Object> models = new HashMap<String, Object>();
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();

        String username;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmedPassword = request.getParameter("confirmedPassword");

        User user = userService.getUserByName(username);

        if ( !user.getPassword().equals(md5.encodePassword(oldPassword, username)) ){
            models.put("myPasswordAuth", "oldPasswordAuthFailure");
            return new ModelAndView(MY_PASSWORD_PAGE, models);
        }else if ( ! newPassword.equals(confirmedPassword) ){
            models.put("myPasswordAuth", "newPasswordAuthFailure");
            return new ModelAndView(MY_PASSWORD_PAGE, models);
        }

        String encryptedNewPassword = md5.encodePassword(newPassword, username);
        user.setPassword(encryptedNewPassword);
        userService.updatePassword(user);

        models.put("myPasswordAuth", "passwordChanged");
        return new ModelAndView(MY_PASSWORD_PAGE, models);
    }

}