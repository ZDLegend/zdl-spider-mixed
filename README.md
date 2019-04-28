# zdl-spider-mixed
各种爬虫、网站api封装
* http访问主要使用了java11的异步http api，所以整个封装以CompletableFuture异步操作为主

## 说明
### 知乎API(com.zdl.spider.mixed.zhihu)
* 单个知乎restful api调用(com.zdl.spider.mixed.zhihu.parser),调用demo可查看每个parser的主函数
  * 知乎通用搜索(SearchParser.java)
  * 知乎用户搜索(SearchPeopleParser.java)
  * 知乎获取问题下的答案(QuestionParser.java)
  * 知乎用户回答内容获取(PeopleAnswerParser.java)
  * 知乎用户动态内容获取(PeopleActivitiesParser.java)
  
* 知乎restful api组合调用(com.zdl.spider.mixed.zhihu.strategy),调用demo可查看类的主函数
  * 知乎回答内容中的图片获取(ImageStrategy.java)
  * 知乎回答内容中的视频获取(VideoStrategy.java)

### 123306 API (com.zdl.spider.mixed.railway)
* 当前只有一个查看余票功能，调用方式可查看主函数，启动后会不断去查看余票量，并在有票的时候跳出对话框提示

 
## 计划完成
* 结合spring的数据库等功能
* 更多的知乎api组合使用
* 封装CompletableFuture对象化支持自定义线程池
* javaFX界面化