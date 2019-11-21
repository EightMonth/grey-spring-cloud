### Grey-Spring-Cloud

基于spring-boot 2.X spring cloud netflix实现的灰度功能，具备以下特性：

- 可以通过配置自定义灰度策略
- 可以通过配置自定义灰度标识
- 可以配合spring-cloud-config做灰度配置的动态刷新

#### Maven:

~~~xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.eightmonth.cloud</groupId>
            <artifactId>grey-spring-cloud-dependencies</artifactId>
            <version>${latest.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
~~~

子功能分别有：

consul: grey-spring-cloud-consul-starter

feign：grey-spring-cloud-openfeign-starter

zuul: grey-spring-cloud-zuul-starter

目前只支持以Consul为注册中心

#### 配置说明

| 名称               | 类型   | 说明                                                         | 默认值       |
| ------------------ | ------ | ------------------------------------------------------------ | ------------ |
| grey.header        | String | 获取请求指定请求头中的值，作为灰度路线选择                   | Service-Type |
| grey.rules         | map    | 过于复杂，在表格后细讲                                       | {}           |
| grey.innerChoose   | String | Service to Service之间调用时，作为灰度路线选择               | all          |
| grey.defaultChoose | string | 作为http目标请求头和innerChoose的缺省                        | all          |
| grey.tag           | string | 以consul为例，当consul实例有tag为grey.tag的值时，则认为此实例为灰度实例。可以通过spring.cloud.consul.discovery.tags注册tag | grey=true    |

##### grey.rules讲解

规则具备两种机制：

举例：

~~~yaml
grey:
  rules: {grey: "= grey=true", normal: "! grey=true", all: "all"}
~~~

这里面的key将成为grey.innerChoose和grey.header请求头对应的值，作为灰度路线选择。

在值里面，存在“=”（等于）、“！”（取反），跟值保留一个空格分开。

由于grey.defaultChoose的默认值为：all，同样这里也可以自定义默认all的范围。

上面的意思为：

grey范围为，在consul上注册的实例中具备grey=true的tag的实例。

normal范围为，在consul上注册的实例不具备grey=true的实例。

all范围为，在consul注册的所有实例。