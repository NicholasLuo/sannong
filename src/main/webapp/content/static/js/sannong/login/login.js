/**
 * Created by apple on 10/21/14.
 */
define('signin', ['jquery', 'sannong', "bootstrap"], function ($, Sannong, Bootstrap) {

    "use strict";

    var Login = {};
    Login.Model = {};
    Login.View = {};

    function validateForm(){
        var validator = $("#signinForm").validate({
            rules: {
                realName: {
                    required: true
                },
                username: {
                    required: true,
                    isCellphone: true
                },
                password: {
                    required: true,
                    minlength: 6
                }
            },
            messages: {
                realName: {
                    required: "请先填写姓名",
                    remote: "姓名或手机号码不存在"
                },
                username: {
                    required: "请先填写手机号码",
                    remote: "姓名或手机号码不存在",
                    isCellphone: "请正确填写您的手机号码"
                },
                password: {
                    required: "请输入密码",
                    minlength: $.validator.format("密码不能小于{0}个字 符")
                }

            },
            errorPlacement:function(error,element) {
                if (element.attr("name") == "cellphone") {
                    error.insertAfter("#action-send-code");
                } else{
                    error.insertAfter(element);
                }
            },
            submitHandler:function(form){
                form.submit();
            }
        });

        return validator;

    }

    Login.Controller = {
        signin : function(){
            var signinJsonData = $('.form-signin').serialize();

            $.ajax({
                type: 'POST',
                url: 'j_spring_security_check',
                data: signinJsonData,
                success: function(response){
                    if (response == "authentication-failure") {

                        $("#authentication-failure").css("display","block");
                    }else{
                        $("#authentication-failure").css("display","none");
                        //window.location.href = response;
                    }
                },
                error: function(error){
                    alert("failed >>> " + error);
                },
                always:function(data){
                    alert("always >>> " + data);
                }
            });
        }

    };

    //------------------ Public functions -------------------------
    Login.temp = function(){

    }

    //------------------ Private functions -------------------------
    function init() {
        $.validator.addMethod("isCellphone", function(value, element) {
            var length = value.length;
            var mobile = /^(((13[0-9]{1})|(15[0-9]{1})||(18[0-9]{1}))+\d{8})$/;
            return this.optional(element) || (length == 11 && mobile.test(value));
        }, "请正确填写您的手机号码");
        /*
         $("#submit").click(function(){
         Login.Controller.signin();
         });
         */
    }


    $(function () {
        init();
        validateForm();

        /*
        var status = $("#auth").attr("status");
        if (status == "authentication-failure"){
            $("#auth-msg").css("display","block");
        }else{
            $("#auth-msg").css("display","none");
        }
        */
    });


    Sannong.Login = Login;
    return Login;
});



