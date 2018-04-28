[TOC]

### 理论

#### 简述

Elasticsearch是一个基于[Apache Lucene(TM)](https://lucene.apache.org/core/)的开源搜索引擎。Elasticsearch不仅仅是Lucene和全文搜索，我们还能这样去描述它：

- 分布式的实时文件存储，每个字段都被索引并可被搜索
- 分布式的实时分析搜索引擎
- 可以扩展到上百台服务器，处理PB级结构化或非结构化数据



Elasticsearch 集群可以包含多个索引(indices)（数据库），每一个索引可以包含多个类型(types)（表），
每一个类型包含多个文档(documents)（行），然后每个文档包含多个字段(Fields)（列）。

#### ES Restful API

 **GET、POST、PUT、DELETE、HEAD**含义：

1）GET：获取请求对象的当前状态。
2）POST：改变对象的当前状态。
3）PUT：创建一个对象。
4）DELETE：销毁对象。
5）HEAD：请求获取对象的基础信息。



#### es集群里的master node、data node和client node分别有何特点？

**master node**

主要功能是维护元数据，管理集群各个节点的状态，数据的导入和查询都不会走master节点，所以master节点的压力相对较小，因此master节点的内存分配也可以相对少些；但是master节点是最重要的，如果master节点挂了或者发生脑裂了，你的元数据就会发生混乱，那样你集群里的全部数据可能会发生丢失，所以一定要保证master节点的稳定性。
**data node**

是负责数据的查询和导入的，它的压力会比较大，它需要分配多点的内存，选择服务器的时候最好选择配置较高的机器（大内存，双路CPU，SSD... 土豪~）；data node要是坏了，可能会丢失一小份数据。
**client node**

是作为任务分发用的，它里面也会存元数据，但是它不会对元数据做任何修改。client node存在的好处是可以分担下data node的一部分压力；为什么client node能分担data node的一部分压力？因为es的查询是两层汇聚的结果，第一层是在data node上做查询结果汇聚，然后把结果发给client node，client node接收到data node发来的结果后再做第二次的汇聚，然后把最终的查询结果返回给用户；所以我们看到，client node帮忙把第二层的汇聚工作处理了，自然分担了data node的压力。
这里，我们可以举个例子，当你有个大数据查询的任务（比如上亿条查询任务量）丢给了es集群，要是没有client node，那么压力直接全丢给了data node，如果data node机器配置不足以接受这么大的查询，那么就很有可能挂掉，一旦挂掉，data node就要重新recover，重新reblance，这是一个异常恢复的过程，这个过程的结果就是导致es集群服务停止... 但是如果你有client node，任务会先丢给client node，client node要是处理不来，顶多就是client node停止了，不会影响到data node，es集群也不会走异常恢复。





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



#### 如何选择查询与过滤

通常的规则是，使用 查询（query）语句来进行 *全文* 搜索或者其它任何需要影响 *相关性得分* 的搜索。除此以外的情况都使用过滤（filters)。



#### 查询类型SearchType



### 分页查询From&Size VS scroll

**from&size**

默认from为0，size为10，即所有的查询默认仅仅返回前10条数据。

分页的偏移值越大，执行分页查询时间就会越长！

**scroll**

使用scroll可以模拟一个传统数据的游标，记录当前读取的文档信息位置。这个分页的用法，不是为了实时查询数据，而是为了一次性查询大量的数据（甚至是全部的数据）。

因为这个scroll相当于维护了一份当前索引段的快照信息，这个快照信息是你执行这个scroll查询时的快照。在这个查询后的任何新索引进来的数据，都不会在这个快照中查询到。但是它相对于from和size，不是查询所有数据然后剔除不要的部分，而是记录一个读取的位置，保证下一次快速继续读取。

```http
http://10.32.32.22:9200/classification_faq/_search?pretty=true&scroll=1m&size=20
```







> 注意：size的大小不能超过index.max_result_window这个参数的设置，默认为10,000。



**优缺点：**

方式一，当结果足够大的时候，会大大加大内存和CPU的消耗。使用非常方便。

方式二： 当结果足够大的时候， scroll 性能更加。但是不灵活和 scroll_id 难管理问题存在。当结果足够大的时候 产生 scroll_id 性能也不低。如果只是一页页按照顺序，scroll是极好的，但是如果是无规则的翻页，那也是性能消耗极大的。







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