关于RestFulUrl的一些说明（注意下面提及的url不一定会在工程中使用到）：
    /users      [GET]   查询所有用户
    /user       [GET]   来到用户信息编辑页面
    /user/{id}  [GET]   查询指定id的用户，用于查看用户详细信息
    /user/stu   [PST]   添加学生信息
    /user/cow   [PST]   添加奶牛信息
    /user/stu   [PUT]   修改学生信息
    /user/cow   [PUT]   修改奶牛信息
    /user/{id}  [DEL]   删除指定id的用户
    /user/verify[GET]   验证邮箱时会访问该url
    /user/forget[GET]   返回找回用户信息页面
    /user/forget[PST]   处理找回用户信息业务
    /user/icon  [GET]   返回上传头像的页面
    /user/icon  [PST]   处理上传头像的请求
    /user/check [GET]   处理AJAX请求，用于判断用户名等是否已被注册

    /tasks      [GET]   查询所有任务
    /tasks/{id} [GET]   查询指定id的用户发布的所有任务
    /tasks/avl  [GET]   查询所有可接收的任务
    /task       [GET]   来到任务信息编辑页面，若有tid请求参数，则先查出该任务再转到编辑页面
    /task/{id}  [GET]   查询指定id的任务
    /task/nor   [PST]   添加普通任务信息
    /task/inv   [PST]   添加调查任务信息
    /task/nor   [PUT]   修改普通任务信息
    /task/inv   [PUT]   修改调查任务信息
    /task/{id}  [DEL]   删除指定id的任务

    /acc_tasks                  [GET]   查看所有用户的接收任务的情况
    /acc_tasks/{uid}            [GET]   查询指定id的用户接收的所有任务
    /acc_task/{uid}/{tid}       [PST]   用户接收一个任务
    /acc_task/nor/{uid}/{tid}   [PUT]   用户完成了接收的普通任务，需由发布者确认，也就是说由发布者调用该方法
    /acc_task/inv/{uid}/{tid}   [PUT]   用户完成了接收的调查任务，用户提交回答后，统计完回答后就调用该方法
    /acc_task/{uid}/{tid}       [DEL]   用户放弃接收任务

    /questionnaire          [GET]   来到问卷信息的编辑页面，若tid不为空，则为添加问卷；若id不为空，则为修改
    /questionnaire          [PST]   添加问卷信息
    /questionnaire          [PUT]   修改问卷信息
    /questionnaire/{id}     [DEL]   删除指定id的问卷
    /questionnaire/ans/{id} [GET]   用户请求填写指定id的问卷
    /questionnaire/ans/{id} [PST]   用户填写了指定id的问卷，提交的数据就是该用户的回答
    /questionnaire/res/{id} [GET]   用于返回展示统计结果的页面