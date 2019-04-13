# zdl-spider-mixed
各种爬虫、网站api封装
说明：http访问主要使用了java11的异步http api，所以整个封装以CompletableFuture异步操作为主

## 当前有的
* 知乎爬虫(com.zdl.spider.mixed.zhihu)
  * 知乎通用搜索(SearchParser.java)
  * 知乎用户搜索
  * 知乎获取问题下的答案(QuestionParser.java)
  * 知乎用户回答内容获取
* 12306(com.zdl.spider.mixed.railway)
  * 火车票查询
  
## 计划完成
* 数据库功能
* 知乎api组合使用
* 封装CompletableFuture对象化支持自定义线程池