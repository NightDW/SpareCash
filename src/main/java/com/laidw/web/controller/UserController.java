package com.laidw.web.controller;

import com.laidw.entity.user.Cow;
import com.laidw.entity.user.Student;
import com.laidw.entity.user.User;
import com.laidw.service.MailService;
import com.laidw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/user")
public class UserController {
    /**
     * 注入必要的组件
     */
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private UserService service;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private MailService mailService;

    @Value("${my-config.upload-photo-dir}")
    private String tarDir;

    /**
     * 用于返回用户详细信息页面
     * @param id 目标用户的id
     * @return detail.html和目标用户数据
     */
    @GetMapping("/{id}")
    public ModelAndView toDetailPage(@PathVariable("id") Integer id){
        ModelAndView mav = new ModelAndView("user/detail");
        mav.addObject("user", service.selectUserById(id));
        return mav;
    }

    /**
     * 负责返回用户信息编辑页面
     * @return edit.html页面及其需要的数据
     */
    @GetMapping
    public ModelAndView toEditPage() {
        ModelAndView mav = new ModelAndView("user/edit");

        //如果用户登录了，说明此时是修改用户信息操作，需要把用户数据和提示信息传给编辑页面
        //只有当获取到的Authentication不是AnonymousAuthenticationToken，才能说明用户登录了
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            mav.addObject("user", auth.getPrincipal());
            mav.addObject("msg", "注意，修改信息后需要重新登录！");
        }
        return mav;
    }

    /**
     * 添加一条Cow数据，主要用于处理注册请求
     * @param cow 用户提交的Cow对象
     * @param request HttpServletRequest对象
     * @return 处理结果
     */
    @PostMapping("/cow")
    public ModelAndView insertCow(Cow cow, HttpServletRequest request) {
        return insertUser(cow, request);
    }

    /**
     * 添加一条Student数据，主要用于处理注册请求
     * @param stu 用户提交的Student对象
     * @param request HttpServletRequest对象
     * @return 处理结果
     */
    @PostMapping("/stu")
    public ModelAndView insertStu(Student stu, HttpServletRequest request) {
        return insertUser(stu, request);
    }

    /**
     * 用于返回找回密码页面
     * @return forget.html页面
     */
    @GetMapping("/forget")
    public String toForgetPage() {
        return "user/forget";
    }

    /**
     * 负责处理找回密码业务的方法，注意4个参数至少有1个不为空，且查询用户信息时只看第一个不为空的参数
     * @param uid 用户提交的UID
     * @param name 用户提交的账户名
     * @param phone 用户提交的电话
     * @param email 用户提交的邮箱
     * @return 处理结果
     */
    @PostMapping("/forget")
    public ModelAndView doFindBack(@RequestParam(required = false) String uid, @RequestParam(required = false) String name, @RequestParam(required = false) String phone, @RequestParam(required = false) String email, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        //注意，uid,name,phone,email这4个参数必定不为空，原因不明
        //如果用户未提交任何数据，则转发回forget页面，并提示错误信息，这一步基本不会执行，因为页面有JS代码检查输入是否为空
        if (uid.isEmpty() && name.isEmpty() && phone.isEmpty() && email.isEmpty()) {
            mav.addObject("msg", "请填写UID/账户名/电话/邮箱！");
            mav.setViewName("user/forget");
            return mav;
        }

        User user = service.selectUserWhoHas(uid, name, phone, email);
        //如果根据用户指定的信息在数据库中找不到对应的用户数据，则转发回forget页面，并提示错误信息
        if (user == null) {
            mav.addObject("msg", "无法根据提交的信息找到对应的用户！请检查您提交的信息，选择您最确定的一项来填写！");
            mav.setViewName("user/forget");
            return mav;
        }
        //如果找到了，则向找到的用户的邮箱发送邮件
        String requestUrl = request.getRequestURL().toString();
        String projectUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/user/forget") + 1);
        String verifyUrl = projectUrl + "user/verify?verifyCode=" + user.getVerifyCode() + "&user_id=" + user.getId();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("verifyUrl", verifyUrl);
        try {
            mailService.sendTemplateMail(user.getEmail(), "找回用户信息", "mail/find-back", map);
        } catch (Exception e) {
            //如果发送失败，则转发到错误页面
            mav.addObject("msg", "发送邮件失败，请稍后重试或联系作者！");
            mav.setViewName("error/error");
            return mav;
        }
        //如果发送成功，则转发到登录页面，注意不能把查到的用户数据传给登录页面
        mav.addObject("msg", "账户信息已成功发送至该账号绑定的邮箱，请前往邮箱查看！");
        mav.setViewName("base/login");
        return mav;
    }

    /**
     * 用于返回上传头像页面
     * @return icon.html页面及其需要的数据
     */
    @GetMapping("/icon")
    public String toIconUploadPage() {
        return "user/icon";
    }

    /**
     * 用于处理用户上传头像的请求
     * @param file 用户上传的头像
     * @param request HttpServletRequest对象
     * @return 处理结果
     */
    @PostMapping("/icon")
    public ModelAndView doUpdateIcon(MultipartFile file, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        //如果未登录，则转发到登录页面
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            mav.setViewName("base/login");
            mav.addObject("msg", "请先登录！");
            return mav;
        }
        //如果用户没有上传文件，那么直接重定向到首页
        if (file.getSize() == 0) {
            mav.setViewName("redirect:/");
            return mav;
        }
        //如果用户上传的文件不是图片，则把错误信息告知用户并转发回上传头像页面
        String fileType = file.getContentType();
        if (!fileType.startsWith("image/")) {
            mav.setViewName("user/icon");
            mav.addObject("msg", "您上传的文件不是图片！请重新上传！");
            return mav;
        }
        //获取上传文件的名称；主要是为了获取到它的后缀名
        String filename = file.getOriginalFilename();
        //生成唯一的UUID，从而生成唯一的图片名
        filename = generateVerifyCode() + filename.substring(filename.lastIndexOf('.'));

        //保存文件
        try {
            file.transferTo(new File(tarDir + "\\icon", filename));
        }
        //如果图片保存失败，则把错误信息告知用户并转发回上传头像页面
        catch (Exception e) {
            mav.setViewName("/user/icon");
            mav.addObject("msg", "保存文件失败！请稍候重试！");
            return mav;
        }
        //如果图片上传成功，则获取用户id，用于修改数据库数据
        Integer id = ((User) auth.getPrincipal()).getId();

        //不管当前登录的是学生还是奶牛，统一当成奶牛处理，因为这里只是修改头像，不涉及学生的属性
        Cow cow = new Cow();
        cow.setId(id);
        cow.setIconUrl(filename);
        service.updateCowByIdSelectively(cow);

        //更新后查出该用户的新数据，转发到登录页面并自动登录
        mav.addObject("user", service.selectUserById(id));
        mav.addObject("auto_login", true);
        mav.setViewName("base/login");
        return mav;
    }

    /**
     * 负责处理更改用户（奶牛）信息业务的方法
     * @param cow 用户提交的信息
     * @param request HttpServletRequest对象
     * @return 处理结果
     */
    @PutMapping("/cow")
    public ModelAndView updateCow(Cow cow, HttpServletRequest request) {
        return updateUser(cow, request);
    }

    /**
     * 负责处理更改用户（学生）信息业务的方法
     * @param stu 用户提交的信息
     * @param request HttpServletRequest对象
     * @return 处理结果
     */
    @PutMapping("/stu")
    public ModelAndView updateStu(Student stu, HttpServletRequest request) {
        return updateUser(stu, request);
    }

    /**
     * 负责处理激活账户的请求，通过校验验证码来确定是否激活
     * @param verifyCode 用户发送的验证码
     * @param user_id 用户发送的用户id
     * @return 验证成功则转发到登录页面；验证失败则转发到注册页面
     */
    @GetMapping("/verify")
    public ModelAndView doVerify(String verifyCode, Integer user_id) {
        //不管验证是否成功，都会转发到登录页面
        ModelAndView mav = new ModelAndView("base/login");

        //取出真正的验证码，和用户提交的验证码比较
        User user = service.selectUserById(user_id);
        String trueCode = user.getVerifyCode();

        //验证成功则激活该账户，把账户信息保存到Request域中并转发到登录页面
        if (trueCode.equals(verifyCode)) {
            service.setUserIsActive(user_id, true);
            mav.addObject("msg", "验证成功！");
            mav.addObject("user", user);
        } else
            mav.addObject("msg", "邮箱验证失败，请检查验证链接或联系作者！");
        return mav;
    }

    /**
     * 用于在用户注册时检测用户名等数据是否已存在，客户端提交的参数是"uid=xxx"形式的，且只提交UID/用户名/电话/邮箱中的其中一个参数
     * @return 用户名等数据尚未被注册时返回Null，否则返回提示信息
     */
    @ResponseBody
    @GetMapping("/check")
    public String checkData(@RequestParam(value = "uid", required = false) String uid, @RequestParam(value = "name", required = false) String name, @RequestParam(value = "phone", required = false) String phone, @RequestParam(value = "email", required = false) String email, @RequestParam(value = "id", required = false) String id) {
        if (!service.checkIfExistExceptMe(uid, name, phone, email, id))
            return null;
        String ret = "";
        ret += uid == null ? "" : " UID";
        ret += name == null ? "" : " 名称";
        ret += phone == null ? "" : " 电话";
        ret += email == null ? "" : " 邮箱";
        return ret.trim() + "已被注册！";
    }

    /**
     * 用于给用户设置一些默认的数据
     * @param user 需要设置数据的用户
     * @return 把期间生成的验证码返回
     */
    private String setDefaultData(User user) {
        //设置默认的头像
        user.setIconUrl("default.png");
        //设置默认的闲钱币
        user.setSpareCashCoin(8d);
        //默认未激活
        user.setIsActive(false);
        //生成验证码并设置给该用户
        String verifyCode = generateVerifyCode();
        user.setVerifyCode(verifyCode);

        //返回验证码
        return verifyCode;
    }

    /**
     * 用于生成验证码
     * @return 生成的验证码
     */
    private String generateVerifyCode() {
        return UUID.randomUUID().toString();
    }

    /**
     * 插入用户数据
     * @param user 用户提交的数据
     * @param request HttpServletRequest对象
     * @return 处理结果
     */
    private ModelAndView insertUser(User user, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        //表单填写的数据并不完整，需要给用户设置默认的数据并拿到该用户的验证码
        String verifyCode = setDefaultData(user);

        //把用户数据存到数据库中
        //注意，用户填写表单时，如果UID等信息在数据库中已存在，此时错误信息会在注册页面显示
        //但此时用户还是可以提交数据的，这样一来，插入用户提交的数据就会报错
        //所以需要在这里try-catch，如果插入失败，我们需要提醒用户数据重复了
        //其实检查重复这一步可以用JS实现，比如页面在检查到数据冲突时让提交按钮变灰等
        //放在这里检查的原因：在演示整个项目时需要注册多个账号，到时，为了方便，需要允许同一个邮箱被多个账号绑定
        //在这里检查的话，只需让user表的email字段不为unique即可，JS不用改（虽然页面会提醒邮箱重复，但实际是可以插入数据的）
        //如果用JS来检查重复，则还需要修改再JS代码，比较麻烦
        try {
            if (user instanceof Cow)
                service.insertCow((Cow) user);
            else
                service.insertStudent((Student) user);
        } catch (Exception e) {
            //插入失败则转回注册页面，并告知用户错误信息
            //注意ViewName不能设置为"forward:/user"，因为这里用户是通过POST方式提交数据的
            //设置为"forward:/user"时转发的时候会以POST方式访问/user地址，从而导致405错误
            //所以这里用重定向的方式来跳转，此时目标页面需要到request中取数据
            mav.addObject("error", "");
            mav.setViewName("redirect:/user");
            return mav;
        }
        //插入成功则发送模板邮件；由于模板不能解析@{}表达式
        //所以不能直接给模板传递verifyCode和user_id参数让模板来拼接验证链接
        //只能自己把链接拼接起来传递给模板，让模板直接取出
        String requestUrl = request.getRequestURL().toString();
        String projectUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/user/") + 1);
        String verifyUrl = projectUrl + "user/verify?verifyCode=" + verifyCode + "&user_id=" + user.getId();

        //准备发送邮件，注意不管邮件是否发送成功，用户数据都已经添加到了数据库中
        //它们的区别只是用户有没有收到验证邮件而已
        //因此固定转发到登录页面，并且把此次注册的用户数据传给登录页面，只是此时账户尚未激活
        mav.setViewName("base/login");
        mav.addObject("user", user);

        Map<String, Object> map = new HashMap<>();
        map.put("projectUrl", projectUrl);
        map.put("verifyUrl", verifyUrl);
        try {
            mailService.sendTemplateMail(user.getEmail(), "验证邮件", "mail/verify", map);
        } catch (Exception e) {
            //如果发送失败，则还需要告知用户错误信息
            mav.addObject("msg", "已经成功注册该账户，但发送验证邮件时出错，请稍候手动激活账户！");
            return mav;
        }
        //如果发送成功，则还需要告知用户前去查看邮件
        mav.addObject("msg", "账户注册成功！但该账户需要激活后才可以正常使用！激活链接已发送至绑定邮箱！");
        return mav;
    }

    /**
     * 更新用户数据
     * @param user 用户提交的数据
     * @param request HttpServletRequest对象
     * @return 处理结果
     */
    private ModelAndView updateUser(User user, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();

        Integer id = user.getId();
        boolean changeEmail = !(service.selectUserById(id).getEmail().equals(user.getEmail()));
        String verifyCode = null;

        //如果改变了绑定邮箱，则需锁定该账户，生成新的验证码，让用户重新激活
        if(changeEmail){
            user.setIsActive(false);
            verifyCode = generateVerifyCode();
            user.setVerifyCode(verifyCode);
        }
        //不管有没有改变绑定邮箱，都先尝试把用户提交的信息保存到数据库
        try {
            if (user instanceof Cow)
                service.updateCowByIdSelectively((Cow) user);
            else
                service.updateStudentByIdSelectively((Student) user);
        } catch (Exception e) {
            //如果修改失败，则转发回修改页面，并告知UID等数据有冲突
            mav.addObject("error", "");
            mav.setViewName("redirect:/user");
            return mav;
        }
        //如果修改成功，且改变了绑定的邮箱，则发送验证邮件
        if(changeEmail){
            //不管邮件有没有发送成功，此时都已经更改了绑定的邮箱，都需要退出登录
            mav.setViewName("base/logout-soon");

            //发送邮件
            String requestUrl = request.getRequestURL().toString();
            String projectUrl = requestUrl.substring(0, requestUrl.lastIndexOf("/user/") + 1);
            String verifyUrl = projectUrl + "user/verify?verifyCode=" + verifyCode + "&user_id=" + user.getId();
            Map<String, Object> map = new HashMap<>();
            map.put("projectUrl", projectUrl);
            map.put("verifyUrl", verifyUrl);
            try {
                mailService.sendTemplateMail(user.getEmail(), "验证邮件", "mail/verify", map);
            } catch (Exception e) {
                mav.addObject("msg", "发送验证邮件时出错！即将退出登录，请稍后手动激活账户!");
                return mav;
            }
            mav.addObject("msg", "修改成功，即将退出登录！您的账户需要重新激活后才可以使用！激活链接已发送至绑定的新邮箱！");
            return mav;
        }
        //如果修改成功，且没有更改绑定的邮箱，此时可以自动登录
        //只需要转发到登录页面，同时指定auto_login为true，并把用户数据传过去即可
        mav.addObject("user", service.selectUserById(id));
        mav.addObject("auto_login", true);
        mav.setViewName("base/login");
        return mav;
    }
}