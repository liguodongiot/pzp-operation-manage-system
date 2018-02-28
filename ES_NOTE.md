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



text类型：支持分词、全文检索，不支持聚合、排序操作。 
适合大字段存储，如：文章详情、content字段等；

keyword类型：支持精确匹配，支持聚合、排序操作。 
适合精准字段匹配，如：url、name、title等字段。 

一般情况，text和keyword共存，设置mapping如下：

```json
{
	"mappings": {
		"book_type": {
			"properties": {
				"title": {
					"analyzer": "ik_max_word",
					"type": "text",
					"term_vector": "with_positions_offsets",
					"fields": {
						"keyword": {
							"ignore_above": 256,
							"type": "keyword"
						}
					}
				}
			}
		}
	}
}
```



ES5.X版本以后，keyword支持的最大长度为32766个UTF-8字符，text对字符长度没有限制。

设置ignore_above后，超过给定长度后的数据将不被索引，无法通过term精确匹配检索返回结果。



- **term_vector：**<https://www.elastic.co/guide/en/elasticsearch/reference/current/term-vector.html>



#### ignore_above

设置超过设定字符长度后，不被索引或者存储。

```shell


curl -XPUT '10.250.140.14:9200/my_index' -d '{
  "mappings": {
    "my_type": {
      "properties": {
        "message": {
          "type": "keyword",
          "ignore_above": 20 
        }
      }
    }
  }
}'

curl -XPUT '10.250.140.14:9200/my_index/my_type/1' -d '{
  "message": "Syntax error"
}'

# 文档会被索引，但是message不会被索引
curl -XPUT '10.250.140.14:9200/my_index/my_type/2' -d '{
  "message": "Syntax error with some long stacktrace"
}'



# 超过部分无法被精确查找和聚合
curl -XGET '10.250.140.14:9200/my_index/_search?pretty' -d '{
  "aggs": {
    "messages": {
      "terms": {
        "field": "message"
      }
    }
  }
}'

-----------------------------
{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 2,
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "my_index",
        "_type" : "my_type",
        "_id" : "2",
        "_score" : 1.0,
        "_source" : {
          "message" : "Syntax error with some long stacktrace"
        }
      },
      {
        "_index" : "my_index",
        "_type" : "my_type",
        "_id" : "1",
        "_score" : 1.0,
        "_source" : {
          "message" : "Syntax error"
        }
      }
    ]
  },
  "aggregations" : {
    "messages" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "Syntax error",
          "doc_count" : 1
        }
      ]
    }
  }
}



curl -XGET '10.250.140.14:9200/my_index/_search?pretty' -d '{
    "query" : {
        "match" : {
            "message" : "Syntax error"
        }
    }
}'

{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 1,
    "max_score" : 0.2876821,
    "hits" : [
      {
        "_index" : "my_index",
        "_type" : "my_type",
        "_id" : "1",
        "_score" : 0.2876821,
        "_source" : {
          "message" : "Syntax error"
        }
      }
    ]
  }
}



curl -XGET '10.250.140.14:9200/my_index/_search?pretty' -d '{
    "query" : {
        "match" : {
            "message" : "Syntax error with some long stacktrace"
        }
    }
}'

{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 5,
    "successful" : 5,
    "failed" : 0
  },
  "hits" : {
    "total" : 0,
    "max_score" : null,
    "hits" : [ ]
  }
}
```









### 搜索

####(match query) vs (term query)

match query，搜索的时候，首先会解析查询字符串，进行分词，然后查询；

而term query，输入的查询内容是什么，就会按照什么去查询，并不会解析查询内容，对它分词。



### 分词

#### ik_max_word 和 ik_smart 什么区别?

ik_max_word: 会将文本做最细粒度的拆分，比如会将“中华人民共和国国歌”拆分为“中华人民共和国,中华人民,中华,华人,人民共和国,人民,人,民,共和国,共和,和,国国,国歌”，会穷尽各种可能的组合；

ik_smart: 会做最粗粒度的拆分，比如会将“中华人民共和国国歌”拆分为“中华人民共和国,国歌”。







### 异常

**1、Fielddata is disabled on text fields by default. Set fielddata=true on [interests] in order to load fielddata in memory by uninverting the inverted index. Note that this can however use significant memory. Alternatively use a keyword field instead.**



解决方式：

Fielddata默认情况下禁用文本字段，因为Fielddata可以消耗大量的堆空间，特别是在加载高基数text字段时。一旦fielddata被加载到堆中，它将在该段的生命周期中保持在那里。此外，加载fielddata是一个昂贵的过程，可以导致用户体验延迟命中。

处理以上bug可以参考如下方式：

1、可以使用使用该my_field.keyword字段进行聚合，排序或脚本

```shell
curl -XPUT 'localhost:9200/my_index?pretty' -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "_doc": {
      "properties": {
        "my_field": { 
          "type": "text",
          "fields": {
            "keyword": { 
              "type": "keyword"
            }
          }
        }
      }
    }
  }
}
'

```



2、启用fielddata（不建议使用）

```shell
curl -XPUT 'localhost:9200/my_index/_mapping/_doc?pretty' -H 'Content-Type: application/json' -d'
{
  "properties": {
    "my_field": { 
      "type":     "text",
      "fielddata": true
    }
  }
}
'

```







### 参考文档

**Elasticsearch官方文档：**<https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html>

**Elasticsearch官方Java文档：**<https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html>