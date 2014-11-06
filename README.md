#山大校园卡充值

##安装文件下载地址

  **[点我下载](https://code.csdn.net/zjx409/transfer2/tree/master/release/Transfer2.apk)或者在项目的release文件夹重下载。也可将项目clone到本地自行编译**

##简介


  由于在[card.sdu.edu.cn](http://card.sdu.edu.cn)上从银行卡到校园卡转账不是很方便
，因此模拟网站登录过程的http包简化登录的过程。全过程模拟浏览器动作，无任何后门代码。

##代码结构


  项目代码结构十分简单，分为在com.casin.transfer2.MainFrame的主界面和
com.casin.task包中的登录、转账请求以及在登录中用到的屏幕键盘的识别。在
com.casin.info包中负责用户信息的存储。

##具体实现


  com.caisin.transfer2.MainFrame主要实现App的各项界面功能。由于本人Android开发
经验较少，代码并不美观。具体有两个用于登录的`AsyncTask`和两个用于转账的`AsyncTask`组
成。其余的是界面相关。`AsyncTask`具体表现为四个内部类。


  com.casin.task负责登录、转账及屏幕键盘识别。登录、转账相对较为简
单。难度在屏幕键盘的识别上。由于转账过程中需要将校园卡查询密码由屏幕键盘映射为
一组数字后逆序POST到服务器中，为了简化转账过程，屏幕键盘的识别是必须的。作者尝试
过OCR开源的软件，如`Tesseract`等，但是效果不是很理想，经常出现5、6识别错误的情况。
因此按照自己的想法将屏幕键盘表示为横竖像素数的有序序列进行识别，最后将1、7
和5、6具体分别开来。


  com.casin.info包中主要是信息的存储。此处使用Android的`SharedPreference`进行保
存。

##现有问题


    1.代码不美观，结构也较混乱。
    
    
    2.屏幕键盘识别的代码也显得较为笨拙，方法不够通用。
    
    
    3.由于作者能力有限，整个程序的功能很单一。
    
    
##新添加功能
  添加了查询余额的功能。
