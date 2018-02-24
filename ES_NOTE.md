[TOC]

### 简述

Elasticsearch是一个基于[Apache Lucene(TM)](https://lucene.apache.org/core/)的开源搜索引擎。Elasticsearch不仅仅是Lucene和全文搜索，我们还能这样去描述它：

- 分布式的实时文件存储，每个字段都被索引并可被搜索
- 分布式的实时分析搜索引擎
- 可以扩展到上百台服务器，处理PB级结构化或非结构化数据



Elasticsearch 集群可以包含多个索引(indices)（数据库），每一个索引可以包含多个类型(types)（表），
每一个类型包含多个文档(documents)（行），然后每个文档包含多个字段(Fields)（列）。



### ES Restful API 

 **GET、POST、PUT、DELETE、HEAD**含义：

1）GET：获取请求对象的当前状态。
2）POST：改变对象的当前状态。
3）PUT：创建一个对象。
4）DELETE：销毁对象。
5）HEAD：请求获取对象的基础信息。





### 数据类型

```shell
# Core datatypesedit
# string
text and keyword

# Numeric datatypes
long, integer, short, byte, double, float, half_float, scaled_float
# Date datatype
date
# Boolean datatype
boolean
# Binary datatype
binary
# Range datatypes
integer_range, float_range, long_range, double_range, date_range

# Complex datatypesedit
# Array datatype
Array support does not require a dedicated type
# Object datatype
object for single JSON objects
# Nested datatype
nested for arrays of JSON objects
```



#### text vs keyword

* Text：

会分词，然后进行索引

支持模糊、精确查询

不支持聚合



* keyword：

不进行分词，直接索引

支持模糊、精确查询

支持聚合



####(match query) vs (term query)

match query，搜索的时候，首先会解析查询字符串，进行分词，然后查询；

而term query，输入的查询内容是什么，就会按照什么去查询，并不会解析查询内容，对它分词。







### 参考文档

**Elasticsearch官方文档：**<https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html>

**Elasticsearch官方Java文档：**<https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html>