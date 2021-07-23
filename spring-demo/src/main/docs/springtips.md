## Spring questions
### 谈谈Spring中都用到了那些设计模式?
https://mp.weixin.qq.com/s?__biz=Mzg2OTA0Njk0OA==&mid=2247485303&idx=1&sn=9e4626a1e3f001f9b0d84a6fa0cff04a&chksm=cea248bcf9d5c1aaf48b67cc52bac74eb29d6037848d6cf213b0e5466f2d1fda970db700ba41&token=255050878&lang=zh_CN%23rd

### JPA 如果某个字段不想被持久化  
```java
    static String transient1; // not persistent because of static
    final String transient2 = “Satish”; // not persistent because of final
    transient String transient3; // not persistent because of transient
    @Transient
    String transient4; // not persistent because of @Transient
 ```


