### springcloud stream binder for aliyun ons

* 下载源码并maven install


* 应用添加maven依赖

```
<dependency>
    <groupId>com.runssnail.springcloud</groupId>
    <artifactId>spring-cloud-stream-binder-ons</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

or

```
<dependency>
    <groupId>com.runssnail.springcloud</groupId>
    <artifactId>spring-cloud-starter-stream-ons</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

```

* binder配置项

    配置项 | 说明 | 
    :---: | :---: |
    spring.cloud.stream.ons.binder.accessKey | 阿里云accessKey |
    spring.cloud.stream.ons.binder.secretKey | 阿里云secretKey |
    spring.cloud.stream.ons.binder.onsAddress | ons接入点 |
    spring.cloud.stream.ons.binder.producerId | 生产者ID，接入应用必须设置 |
    spring.cloud.stream.ons.binder.consumerId | 消费者ID，zipkin-server必须设置 |
 