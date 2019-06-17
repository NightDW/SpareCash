package com.laidw.web.controller;

import com.laidw.entity.questionnaire.Questionnaire;
import com.laidw.entity.task.InvestigatingTask;
import com.laidw.entity.task.NormalTask;
import com.laidw.entity.task.Task;
import com.laidw.entity.user.User;
import com.laidw.service.TaskService;
import com.laidw.web.properties.PageShowProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TaskController {
    /**
     * 注入必要的组件
     */
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired private TaskService service;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired private PageShowProperties properties;

    /**
     * 负责返回任务信息的编辑页面
     * @param tid （仅在修改任务信息时才不为空）用户点击修改按钮时对应任务的id
     * @param url 用户点击修改任务按钮或部分添加任务链接时的当前页面的url（包括当前是第几页），用于完成操作后重定向回之前的页面
     * @return edit.html页面及其需要的数据
     */
    @GetMapping("/task")
    public ModelAndView toEditPage(@RequestParam(required=false) Integer tid, @RequestParam(required=false) String url){
        ModelAndView mav = new ModelAndView("task/edit");
        //如果tid不为空，说明用户想执行修改任务操作，应该把目标任务查出来再转发到编辑页面
        if(tid != null)
            mav.addObject("task", service.selectTaskById(tid));
        //如果url不为空，则同样把它传给edit.html页面
        if(url != null)
            mav.addObject("url", url);
        //如果用户登录了，还需要把该用户的信息给编辑页面；因为普通用户不能发布调查任务，编辑页面需要知道当前的用户信息
        //其实不用判断用户是否登录，因为既然能执行发布/修改任务的操作了，说明已经登录了
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken))
            mav.addObject("user", auth.getPrincipal());
        return mav;
    }

    /**
     * 负责返回任务信息的展示页面，主要是显示我发布的任务
     * @param id 当前用户的id
     * @param pageNum 要显示第几页的数据，默认为1
     * @return list.html页面及其需要的数据
     */
    @GetMapping("/tasks/{id}")
    public ModelAndView toMyTasksListPage(@PathVariable("id") Integer id, @RequestParam(required=false, defaultValue="1") Integer pageNum){
        ModelAndView mav = new ModelAndView("task/list");
        mav.addObject("pageBean", service.selectAllMyPublishedTasksLimits(pageNum, properties.getPageSize(), properties.getNavigatePages(), id));
        //需要把url也传过去，导航条需要用到这个数据
        //因为list.html需要显示我发布的任务，所有任务，所有可接收的任务
        //这个url就可以用于区分该页面具体显示的是什么任务，同时还要求用户传递pageNum参数
        //考虑到有可能会出现/xxx/{pageNum}之类的url地址来用于分页查询，而这里则是通过/xxx?curPage=xxx的方式来分页查询的
        //为了让edit.html页面的导航条更加通用，这里需要自己拼接"?pageNum="字符串，编辑页面只需要在后面再拼接上页面数即可
        mav.addObject("url", "tasks/" + id + "?pageNum=");
        return mav;
    }

    /**
     * 负责返回任务信息的展示页面，主要是显示可被接收的任务
     * @param pageNum 要显示第几页的数据，默认为1
     * @return list.html页面及其需要的数据
     */
    @GetMapping("/tasks/avl")
    public ModelAndView toAvailableTasksListPage(@RequestParam(required=false, defaultValue="1") Integer pageNum){
        ModelAndView mav = new ModelAndView("task/list");
        mav.addObject("pageBean", service.selectAllAvailableTasksLimits(pageNum, properties.getPageSize(), properties.getNavigatePages()));
        mav.addObject("url", "tasks/avl?pageNum=");
        return mav;
    }

    /**
     * 负责返回任务信息的展示页面，主要是显示所有发布的任务
     * @param pageNum 要显示第几页的数据，默认为1
     * @return list.html页面及其需要的数据
     */
    @GetMapping("/tasks")
    public ModelAndView toTasksListPage(@RequestParam(required=false, defaultValue="1") Integer pageNum){
        ModelAndView mav = new ModelAndView("task/list");
        mav.addObject("pageBean", service.selectAllTasksLimits(pageNum, properties.getPageSize(), properties.getNavigatePages()));
        mav.addObject("url", "tasks?pageNum=");
        return mav;
    }

    /**
     * 负责处理添加普通任务的请求
     * @param task 用户提交的任务数据
     * @param uid 用户的id
     * @param url 用户点击添加链接时的页面的url地址，可以为空，因为只有部分添加链接提交了这个数据到edit.html页面
     * @return 如果获取到了url，则重定向到url指定的页面；否则重定向回“我发布的任务”页面
     */
    @PostMapping("/task/nor")
    public ModelAndView insertNorTask(NormalTask task, Integer uid, @RequestParam(required=false) String url){
        setDefaultData(task, uid);
        service.insertNormalTask(task);
        if(url != null)
            return new ModelAndView("redirect:/" + url);
        return new ModelAndView("redirect:/tasks/" + uid);
    }

    /**
     * 负责处理添加调查任务的请求
     * @param task 用户提交的任务数据
     * @param uid 用户的id
     * @param url 用户点击添加链接时的页面的url地址，可以为空，因为只有部分添加链接提交了这个数据到edit.html页面
     * @return 转发到问卷的编辑页面并提供相应的数据，因为该调查任务还没有问卷，需要发布者添加问卷
     */
    @PostMapping("/task/inv")
    public ModelAndView insertInvTask(InvestigatingTask task, Integer uid, @RequestParam(required=false) String url){
        ModelAndView mav = new ModelAndView("questionnaire/edit");
        setDefaultData(task, uid);

        //即使用户没有提交ageLimit等数据，这里task的ageLimit等属性也不为空，而是为""
        //因此，如果发现用户提交了空字符串，需要把它改为null，如果不是空字符串，则需校验格式是否正确
        //格式不正确则重定向回编辑页面
        if(task.getGradeLimit().isEmpty())
            task.setGradeLimit(null);
        if(task.getAgeLimit().isEmpty())
            task.setAgeLimit(null);
        if(task.getMajorLimit().isEmpty())
            task.setMajorLimit(null);
        if((task.getGradeLimit() != null && !task.getGradeLimit().matches("^[0-4]-[0-4]$")) || (task.getAgeLimit() != null && !task.getAgeLimit().matches("^[0-9]{1,2}-[0-9]{1,2}$")) || (task.getMajorLimit() != null && !task.getMajorLimit().matches("^[A-Za-z0-9]{1,}(,[A-Za-z0-9]{1,})*$"))){
            mav.setViewName("redirect:/task");
            mav.addObject("error", "");
            return mav;
        }

        service.insertInvestigatingTask(task);
        mav.addObject("tid", task.getId());

        //如果url不为空，则需把它传给问卷编辑页面，这样在完成问卷的编辑后可以返回原来的页面
        if(url != null)
            mav.addObject("url", url);
        return mav;
    }

    /**
     * 负责处理修改普通任务的请求
     * @param task 用户提交的任务数据
     * @param url 用户点击修改按钮时的url（点击修改按钮后url传给toEditPage()方法，该方法又把它传给edit.html页面，然后该页面又把它传给了本方法）
     * @return 重定向回原来的页面
     */
    @PutMapping("/task/nor")
    public ModelAndView updateNorTask(NormalTask task, String url){
        service.updateNormalTaskByIdSelectively(task);
        return new ModelAndView("redirect:/" + url);
    }

    /**
     * 负责处理修改调查任务的请求
     * @param task 用户提交的任务数据
     * @param url 用户点击修改按钮时的url（点击修改按钮后url传给toEditPage()方法，该方法又把它传给edit.html页面，然后该页面又把它传给了本方法）
     * @return 转发到问卷的编辑页面并提供相应的数据，因为用户可能同时需要修改问卷
     */
    @PutMapping("/task/inv")
    public ModelAndView updateInvTask(InvestigatingTask task, String url){
        ModelAndView mav = new ModelAndView("questionnaire/edit");

        //即使用户没有提交ageLimit等数据，这里task的ageLimit等属性也不为空，而是为""
        //因此，如果发现用户提交了空字符串，需要把它改为null，如果不是空字符串，则需校验格式是否正确
        //格式不正确则重定向回编辑页面，还需要把tid和url也传过去
        if(task.getGradeLimit().isEmpty())
            task.setGradeLimit(null);
        if(task.getAgeLimit().isEmpty())
            task.setAgeLimit(null);
        if(task.getMajorLimit().isEmpty())
            task.setMajorLimit(null);
        if((task.getGradeLimit() != null && !task.getGradeLimit().matches("^[0-4]-[0-4]$")) || (task.getAgeLimit() != null && !task.getAgeLimit().matches("^[0-9]{1,2}-[0-9]{1,2}$")) || (task.getMajorLimit() != null && !task.getMajorLimit().matches("^[A-Za-z0-9]{1,}(,[A-Za-z0-9]{1,})*$"))){
            mav.setViewName("redirect:/task");
            mav.addObject("error", "");
            mav.addObject("tid", task.getId());
            mav.addObject("url", url);
            return mav;
        }

        //更新前先把老的数据查出来，主要是为了获取该任务配套的问卷
        InvestigatingTask iTask = (InvestigatingTask)service.selectTaskById(task.getId());
        Questionnaire questionnaire = iTask.getQuestionnaire();
        //注意更新操作不能用updateInvestigatingTaskByIdSelectively()方法
        task.setPublisher(iTask.getPublisher());
        task.setIsAvailable(iTask.getIsAvailable());
        task.setCurCount(iTask.getCurCount());
        task.setQuestionnaire(iTask.getQuestionnaire());
        service.updateInvestigatingTaskById(task);

        //需要继续把url传递给下一个页面，以便于在修改了问卷信息后可以返回最开始的那个页面
        mav.addObject("url", url);

        //如果questionnaire不为空，说明在修改之前已经编辑过问卷了，这时需要把questionnaire传过去
        if(questionnaire != null)
            mav.addObject("questionnaire", questionnaire);
        //为什么questionnaire可能为空？比如说，奶牛发布了调查任务但未编辑问卷，然后又修改该任务，此时就会出现查询不到questionnaire的情况
        //如果questionnaire为空，此时虽然对Task来说已经执行了修改操作，但对Questionnaire来说，接下来需要执行添加操作
        //因此需要把任务的id传过去，这样在添加了Questionnaire后可以把它绑定到这个Task上
        else
            mav.addObject("tid", task.getId());
        return mav;
    }

    /**
     * 负责处理删除任务的请求
     * @param id 要删除的任务的id
     * @param url 用户点击修改按钮时的url
     * @return 重定向回发原来的页面
     */
    @DeleteMapping("/task/{id}")
    public ModelAndView deleteTask(@PathVariable("id") Integer id, String url){
        service.deleteTaskById(id);
        return new ModelAndView("redirect:/" + url);
    }

    /**
     * 用户提交的Task数据并不完整，需要给Task设置一些默认的属性
     * @param task 用户提交的数据
     * @param uid 用户的id
     */
    private void setDefaultData(Task task, Integer uid){
        //设置发布者
        User user = new User();
        user.setId(uid);
        task.setPublisher(user);

        //设置isAvailable属性和maxCount属性
        //如果是普通任务，isAvailable属性直接设为true，且maxCount属性设置为1
        //如果是调查任务，则只需设置isAvailable属性为false（因为还没有问卷，还不能发布）
        if(task instanceof NormalTask){
            task.setIsAvailable(true);
            task.setMaxCount(1);
        }
        else
            task.setIsAvailable(false);

        //设置接收的人数，固定为0
        task.setCurCount(0);
    }
}
