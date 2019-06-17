package com.laidw.web.controller;

import com.laidw.entity.questionnaire.*;
import com.laidw.entity.task.InvestigatingTask;
import com.laidw.entity.user.User;
import com.laidw.service.QuestionnaireService;
import com.laidw.service.TaskService;
import com.laidw.service.UserAccTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {
    /**
     * 注入必要的组件
     */
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired private QuestionnaireService service;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired private TaskService taskService;
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired private UserAccTaskService userAccTaskService;

    /**
     * 负责返回问卷的编辑页面
     * @param tid 不为空时说明是添加问卷操作，添加了问卷后可以把该文件绑定到指定id的任务上
     * @param id 不为空时说明是修改操作
     * @return edit.html页面及其需要的数据
     */
    @GetMapping
    public ModelAndView toEditPage(@RequestParam(required=false) Integer tid, @RequestParam(required=false) Integer id){
        ModelAndView mav = new ModelAndView("questionnaire/edit");
        //如果是添加操作，则直接把tid传到编辑页面
        if(tid != null)
            mav.addObject("tid", tid);
        //如果是修改操作，则把相应的Questionnaire查出来传到编辑页面
        if(id != null)
            mav.addObject("questionnaire", service.selectQuestionnaireById(id));
        return mav;
    }

    /**
     * 负责处理添加问卷信息的请求
     * @param questionnaire 用户提交的问卷信息
     * @param tid 用于指定该问卷属于哪个任务
     * @param url 用户最开始点击添加/修改任务按钮/链接时的页面的url
     * @return 若url不为空，则重定向回它指定的页面，否则重定向回“我发布的任务”页面
     */
    @PostMapping
    public ModelAndView insertQuestionnaire(Questionnaire questionnaire, Integer tid, @RequestParam(required=false) String url){
        validateForQuestionnaire(questionnaire);
        createDefaultStatisticalAnswersForQuestionnaire(questionnaire);
        service.insertQuestionnaire(questionnaire);

        //添加成功后，需要把刚插入的问卷绑定到tid指定的任务上，并把该任务激活
        InvestigatingTask task = new InvestigatingTask();
        task.setId(tid);
        task.setIsAvailable(true);
        task.setQuestionnaire(questionnaire);
        taskService.updateInvestigatingTaskByIdSelectively(task);

        //根据url是否为空来确定转发到哪个页面
        if(url != null)
            return new ModelAndView("redirect:/" + url);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return new ModelAndView("redirect:/tasks/" + user.getId());
    }

    /**
     * 负责处理修改问卷信息的请求
     * @param questionnaire 用户提交的问卷信息
     * @param url 用户最开始点击修改任务按钮时的页面的url
     * @return url必不为空，所以重定向回它指定的页面
     */
    @PutMapping
    public ModelAndView updateQuestionnaire(Questionnaire questionnaire, String url){
        validateForQuestionnaire(questionnaire);
        createDefaultStatisticalAnswersForQuestionnaire(questionnaire);
        service.updateQuestionnaireByIdSelectively(questionnaire);
        return new ModelAndView("redirect:/" + url);
    }

    /**
     * 用户请求回答问卷时调用该方法，把相应的问卷查出来并展示给用户
     * @param qid 问卷的id
     * @param pageNum 用户点击前往按钮时当前页面的页码
     * @return 问卷的展示页面，以及查出来的问卷信息
     */
    @GetMapping("/ans/{qid}")
    public ModelAndView toAnswerPage(@PathVariable("qid") Integer qid, Integer pageNum){
        ModelAndView mav = new ModelAndView("questionnaire/show");
        mav.addObject("questionnaire", service.selectQuestionnaireById(qid));
        //把页码传过去，方便答完问卷后重定向回原来的页面
        mav.addObject("pageNum", pageNum);
        return mav;
    }

    /**
     * 用户提交问卷的回答后调用这个方法
     * @param qid 问卷的id
     * @param answersTO 其用户提交的答案
     * @param pageNum 用户点击前往按钮时当前页面的页码数
     * @return 返回“我接收的任务”指定页码的页面
     */
    @PostMapping("/ans/{qid}")
    public ModelAndView userSubmitAnswer(@PathVariable("qid") Integer qid, AnswersTO answersTO, Integer pageNum){
        ModelAndView mav = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //如果用户未登录，则转发到登录页面
        if (auth instanceof AnonymousAuthenticationToken) {
            mav.setViewName("redirect:/login");
            mav.addObject("msg", "请先登录！");
            return mav;
        }

        //如果用户已登录，则获取该用户的id和问卷对应的任务的id
        Integer uid = ((User) auth.getPrincipal()).getId();
        Integer tid = taskService.selectInvTaskByQueId(qid).getId();

        Boolean flag = userAccTaskService.isDone(uid, tid);

        //如果用户根本没有接收该任务或该任务已完成，则返回“我接收的任务”页面，并显示错误信息
        if(flag == null || flag){
            mav.setViewName("redirect:/acc_tasks/" + uid);
            mav.addObject("pageNum", pageNum);
            mav.addObject("msg", "您未接收或已完成相关的调查任务！");
            return mav;
        }

        //获取到原来的统计结果
        Questionnaire questionnaire = service.selectQuestionnaireById(qid);
        List<StatisticalAnswer> statisticalAnswers = questionnaire.getStatisticalAnswers();

        //把用户提交的数据统计进去
        for(int i = 0; i < statisticalAnswers.size(); i++)
            statisticalAnswers.get(i).addAnswer(answersTO.getAnswers().get(i));

        //把新的统计结果存到数据库，更新统计数据即可，问卷的问题不用改
        questionnaire.setQuestionsJSON(null);
        questionnaire.flushStatisticalAnswersJSON();
        service.updateQuestionnaireByIdSelectively(questionnaire);

        //设置用户完成该任务，并返回“我接收的任务”页面，并显示成功信息
        userAccTaskService.userFinishTask(uid, tid);
        mav.setViewName("redirect:/acc_tasks/" + uid);
        mav.addObject("pageNum", pageNum);
        mav.addObject("msg", "提交完成！");
        return mav;
    }

    /**
     * 负责返回某个问卷的统计结果的显示页面
     * @param qid 问卷的id
     * @return result.html页面及其需要的数据
     */
    @GetMapping("/res/{qid}")
    public ModelAndView toResultPage(@PathVariable("qid") Integer qid){
        ModelAndView mav = new ModelAndView("questionnaire/result");
        mav.addObject("questionnaire", service.selectQuestionnaireById(qid));
        return mav;
    }

    /**
     * 用于校正用户提交的Questionnaire数据
     * @param questionnaire 用户提交的数据
     */
    private void validateForQuestionnaire(Questionnaire questionnaire){
        //需要把用户提交的无用数据移除，比如说用户添加了3个问题，但又把第二个问题删除了
        //此时提交就会出现第二题所有数据都为空的现象，此时需要把第二题的数据删掉
        //另外，如果用户没有勾选isRequired选项，那么提交时该问题的isRequired属性为空，需要手动设置为false
        List<Question> questions = questionnaire.getQuestions();
        for(int i = questions.size() - 1; i >= 0; i--) {
            Question question = questions.get(i);
            if (question.getNum() == null)
                questions.remove(i);
            else if(question.getIsRequired() == null)
                question.setIsRequired(false);
        }

        //这里获得的Questionnaire对象的questionsJSON为空，需要刷新一下再插入数据库
        questionnaire.flushQuestionsJSON();
    }

    /**
     * 为Questionnaire数据生成其questions对应的默认的statisticalAnswers
     * @param questionnaire 用户提交的数据
     */
    private void createDefaultStatisticalAnswersForQuestionnaire(Questionnaire questionnaire){
        List<StatisticalAnswer> statisticalAnswers = new ArrayList<>();
        for(Question question : questionnaire.getQuestions())
             statisticalAnswers.add(new StatisticalAnswer(question.getNum(), question.getType()));
        questionnaire.setStatisticalAnswers(statisticalAnswers);
    }
}
