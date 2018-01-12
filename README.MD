# 基于slueth的参数透传插件
## 简介
1. 微服务下使用
2. 调用过程中用户信息，页面语言信息的透传
## 使用方式
### yaml配置：
```yaml
bizi:
  sleuth: 
    config:
      headers: lang_info #如果由多个，逗号隔开.这里配置从filter里需要获取的headerName
```
### 调用方式:
```java
@Service
public class LangInfoService {
    @Resource
    private SessionInfoOperator sessionInfoOperator;

    public String getLangInfo(){
        return sessionInfoOperator.getSessionInfo("lang_info");
    }
    public void setUserId(){
        sessionInfoOperator.setSessionInfo("user_id","bishion");
    }
}
```
