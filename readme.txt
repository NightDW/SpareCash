本工程和SpareCash工程是相似的，只是在后者的基础上进行一些修改，使其可以在阿里云服务器上运行：

在服务器上将会创建一个域名为"www.laidw.com"（域名随便取，反正是直接用IP地址访问的）的网站，并将其设置为默认站点；
创建该网站后系统盘中将会出现"/www/wwwroot/www.laidw.com"目录，该网站的资源就放在该目录下；
该项目打包成的jar包将会放到服务器的"/www/wwwroot/www.laidw.com"目录下；
注意本工程的测试类均已删除，这是为了防止在打包时执行这些测试类（经过之前的多次修改，这些测试类都已经过时了）。

上传的文件（即图片）将放到"/www/wwwroot/www.laidw.com/upload/icon"目录下，
由于上传的文件的存放路径和原来工程的文件存放路径不同，因此需要修改"my-config.upload-photo-dir"属性。

由于阿里云服务器禁止了25端口（存疑），导致原来工程的邮件发送业务不可用，因此需要改变邮件的发送方式：
只需手动指定"spring.mail.port"属性和"spring.mail.properties.mail.smtp.socketFactory.class"属性，详见配置文件。

另外，在测试过程中发现UserController中第198行有误，ViewName应该是"user/icon"而不是"/user/icon"，已修正。
同时该类的第194行，在拼接字符串时，由于Linux系统的路径分隔符为'/'而不是'\'，因此拼接在后面的字符串应该为"/icon"而不是"\\icon"，已修正。

为了方便，我把数据库中user表的email字段的unique限制取消了，也就是允许一个邮箱被多个账户绑定；
因为演示时需要注册多个账号，我总不能去申请多个邮箱来给这些账号绑定吧；邮箱重复时网页会提示邮箱已被注册，直接忽视就行了。
当然实际开发中肯定不允许这么做。

为了使该项目能正常工作，记得打开服务器上的相应端口，命令为：
firewall-cmd --zone=public --add-port=xxx/tcp --permanent
firewall-cmd --reload

要启动SpringBoot工程，需要进入/www/wwwroot/www.laidw.com目录，然后执行以下命令：
nohup java -jar xxx-0.0.1-SNAPSHOT.jar > log.txt &

要停止SpringBoot工程，需要先查看相关进程，然后关闭该进程，命令为：
ps -def | grep jar
kill xxx
