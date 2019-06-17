package com.laidw.web.controller;

import com.laidw.entity.task.Task;
import com.laidw.entity.user.User;
import com.laidw.service.TaskService;
import com.laidw.service.UserAccTaskService;
import com.laidw.web.properties.PageShowProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserAccTaskController {
    /**
     * 注入必要的组件
     */
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired private UserAccTaskService service;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired private TaskService taskService;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired private PageShowProperties properties;

    /**
     * 负责查找我接收的任务并展示给用户
     * @param id 用户的id
     * @param pageNum 当前页数，默认为1
     * @return list.html页面及其需要的数据
     */
    @GetMapping("/acc_tasks/{id}")
    public ModelAndView findMyAcceptTask(@PathVariable("id") Integer id, @RequestParam(required=false, defaultValue="1") Integer pageNum){
        ModelAndView mav = new ModelAndView("acc_task/list");
        mav.addObject("pageBean", service.findUserAcceptedTasksLimits(pageNum, properties.getPageSize(), properties.getPageSize(), id));
        return mav;
    }

    /**
     * 用户接收任务时调用这个方法
     * @param uid 用户的id
     * @param tid 任务的id
     * @param url 用户点击接收按钮时页面的url
     * @return 返回url指定的页面及其需要的数据
     */
    @PostMapping("/acc_task/{uid}/{tid}")
    public ModelAndView userAcceptTask(@PathVariable("uid") Integer uid, @PathVariable("tid") Integer tid, String url){
        ModelAndView mav = new ModelAndView("redirect:/" + url);
        try{
            service.userAcceptTask(uid, tid);
        }catch (Exception e){
            mav.addObject("msg", e.getMessage());
            return mav;
        }
        mav.addObject("msg", "接收成功！");
        return mav;
    }

    /**
     * 用户放弃任务时调用这个方法
     * 注意，用户接收按钮是在task/list.html页面中的，而放弃按钮是在acc_task/list.html页面中的
     * 因此，上面的方法中可以直接获得url，而这个方法只能获得用户点击放弃按钮时的页码
     * @param uid 用户的id
     * @param tid 任务的id
     * @param pageNum 用户点击放弃按钮时页面的页码
     * @return 返回“我接收的任务”的第pageNum页
     */
    @DeleteMapping("/acc_task/{uid}/{tid}")
    public ModelAndView userCancelTask(@PathVariable("uid") Integer uid, @PathVariable("tid") Integer tid, Integer pageNum){
        service.userCancelTask(uid, tid);
        return new ModelAndView("redirect:/acc_tasks/" + uid + "?pageNum=" + pageNum);
    }

    /**
     * 用户完成任务时调用这个方法
     * 注意该方法必须由发布者来调用
     * @param uid 用户的id
     * @param tid 任务的id
     * @param url 用户点击确认按钮时页面的url
     * @return 返回url指定的页面及其需要的数据
     */
    @PutMapping("/acc_task/nor/{uid}/{tid}")
    public ModelAndView userFinishNorTask(@PathVariable("uid") Integer uid, @PathVariable("tid") Integer tid, String url){
        ModelAndView mav = new ModelAndView("redirect:/" + url);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth instanceof AnonymousAuthenticationToken) {
            mav.setViewName("redirect:/login");
            mav.addObject("msg", "请先登录！");
            return mav;
        }
        //查看当前登录的用户是否为目标任务的发布者
        Task task = taskService.selectTaskById(tid);
        User user = (User) auth.getPrincipal();
        if(user.getId() != task.getPublisher().getId().intValue()){
            mav.addObject("msg", "没有权限！");
            return mav;
        }
        service.userFinishTask(uid, tid);
        mav.addObject("msg", "已确认接收者已完成！");
        return mav;
    }
}
