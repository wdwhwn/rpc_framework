# 自定义RPC框架
## 项目结构
 - **rpc_common** `rpc`框架的通用代码
 - **rpc_consumer** `rpc`服务的消费方
 - **rpc_provider** `rpc`服务的提供方
 > 注意: 项目结构是框架的标准结构,使用maven的高级特性(聚合\继承\依赖)
## 项目特点
 - `rpc_common` 通用代码
    - pojo 通用数据模型
    - interface 通用的接口 
    - impl 通用接口的实现
    - ....
    
 > 涉及到的技术点:  
    1. netty  
    2. 对象的序列化  
    3. 网络协议  
    4. 动态代理  
    ....
## 应用
  - 可以将`rpc_common`工程打包,供需要rpc功能的项目使用
  - 加深对RPC框架底层的了解
  
    