## 基础入门

### company index lib

```shell
{
	"employee": {
		"dynamic": "false",
		"properties": {
			"first_name": {
				"type": "text",
				"fields": {
					"keyword": {
						"type": "keyword",
						"ignore_above": 256
					}
				}
			},
			"about": {
				"type": "text",
				"analyzer": "ik_max_word",
				"search_analyzer": "ik_smart",
				"fields": {
					"keyword": {
						"type": "keyword",
						"ignore_above": 256
					}
				}
			},
			"last_name": {
				"type": "text",
				"fields": {
					"keyword": {
						"type": "keyword",
						"ignore_above": 256
					}
				}
			},
			"age": {
				"type": "long"
			},
			"lucky_num": {
				"type": "long"
			},
			"interests": {
				"type": "text",
				"fields": {
					"keyword": {
						"type": "keyword",
						"ignore_above": 256
					}
				}
			}
		}
	}
}
```



#### 索引文档

```shell
curl -i -XPUT '10.250.140.14:9200/alibaba_alias/employee/1?pretty' -d '{
    "first_name" : "John",
    "last_name" :  "Smith",
	"lucky_num" :   [ 8, 6 ],
    "age" :        25,
    "about" :      "I love to go rock climbing",
    "interests": [ "sports", "music" ]
}'


curl -i -XGET '10.250.140.14:9200/alibaba_alias/employee/1?pretty'


curl -i -XPUT '10.250.140.14:9200/alibaba_alias/employee/2?pretty' -d '{
    "first_name" : "科比",
    "last_name" :  "布莱恩特",
	"lucky_num" :   [ 9, 66 ],
    "age" :        33,
    "about" :      "我喜欢爬山和旅游。",
    "interests": [ "篮球", "听音乐" ]
}'


curl -i -XPUT '10.250.140.14:9200/alibaba_alias/employee/3?pretty' -d '{
    "first_name" : "詹姆斯",
    "last_name" :  "哈登",
	"lucky_num" :   [ 33, 77, 55 ],
    "age" :        23,
    "about" :      "用心做自己，永不言败。",
    "interests": [ "篮球", "逛街" ]
}'



curl -XPUT '10.250.140.14:9200/alibaba_alias/employee/4?pretty' -H 'Content-Type: application/json' -d '{
    "first_name" :  "Jane",
    "last_name" :   "Smith",
    "lucky_num" :   [ 16, 22, 7 ],
    "age" :         32,
    "about" :       "I like to collect rock albums",
    "interests":  [ "music" ]
}'

curl -XPUT '10.250.140.14:9200/alibaba_alias/employee/5?pretty' -H 'Content-Type: application/json' -d '{
    "first_name" :  "Douglas",
    "last_name" :   "Fir",
    "lucky_num" :   [ 3, 87, 32 ],
    "age" :         35,
    "about":        "I like to build cabinets",
    "interests":  [ "forestry" ]
}'


curl -i -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty'

----------------------------------

curl -XGET 'http://10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -d '
{
	"query" : {
		"match" : {
			"last_name" : "布莱恩"
		}
	},
    "from": 0, 
    "size": 10
}'

{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 1,
    "max_score" : 0.8630463,
    "hits" : [
      {
        "_index" : "alibaba",
        "_type" : "employee",
        "_id" : "2",
        "_score" : 0.8630463,
        "_source" : {
          "first_name" : "科比",
          "last_name" : "布莱恩特",
          "lucky_num" : [
            9,
            66
          ],
          "age" : 33,
          "about" : "我喜欢爬山和旅游。",
          "interests" : [
            "篮球",
            "听音乐"
          ]
        }
      }
    ]
  }
}



curl -XGET 'http://10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -d '
{
	"query" : {
		"term" : {
			"last_name" : "布莱恩"
		}
	},
    "from": 0, 
    "size": 10
}'

{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 0,
    "max_score" : null,
    "hits" : [ ]
  }
}


# 如果只想检查一个文档是否存在 --根本不想关心内容--那么用 HEAD 方法来代替 GET 方法。
# HEAD 请求没有返回体，只返回一个 HTTP 请求报头
curl -i -XHEAD http://10.250.140.14:9200/alibaba_alias/employee/1
```



#### 查询

```shell
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty'


# 查询字符串 （_query-string_） 搜索
# 10.250.140.14:9200/alibaba_alias/employee/_search?q=last_name:哈登&pretty
# 中文要编码
curl -XGET 'http://10.250.140.14:9200/alibaba_alias/employee/_search?q=last_name:%E5%93%88%E7%99%BB&pretty'


# 查询表达式搜索
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -H 'Content-Type: application/json' -d '
{
    "query" : {
        "match" : {
            "last_name" : "哈登"
        }
    }
}'

```






#### 过滤器

```shell
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -H 'Content-Type: application/json' -d '
{
    "query" : {
        "bool": {
            "must": {
                "match" : {
                    "last_name" : "smith" 
                }
            },
            "filter": {
                "range" : {
                    "age" : { "gt" : 20 } 
                }
            }
        }
    }
}'

```



#### [全文搜索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_full_text_search.html)

```shell
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "match" : {
            "about" : "rock climbing"
        }
    }
}
'


```



#### [短语搜索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_phrase_search.html)

```shell
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    }
}
'

```



#### [高亮搜索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/highlighting-intro.html)

```shell
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "match_phrase" : {
            "about" : "rock climbing"
        }
    },
    "highlight": {
        "fields" : {
            "about" : {}
        }
    }
}
'

```



#### [分析](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_analytics.html)

```shell
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "size": 0,
  "aggs": {
    "all_interests": {
      "terms": { "field": "interests.keyword" }
    }
  }
}
'


----------------
# 聚合并非预先统计，而是从匹配当前查询的文档中即时生成。
{
  "took" : 4,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 5,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "all_interests" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "music",
          "doc_count" : 2
        },
        {
          "key" : "篮球",
          "doc_count" : 2
        },
        {
          "key" : "forestry",
          "doc_count" : 1
        },
        {
          "key" : "sports",
          "doc_count" : 1
        },
        {
          "key" : "听音乐",
          "doc_count" : 1
        },
        {
          "key" : "逛街",
          "doc_count" : 1
        }
      ]
    }
  }
}


# Smith 的雇员中最受欢迎的兴趣爱好，可以直接添加适当的查询来组合查询
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "match": {
      "last_name": "smith"
    }
  },
  "aggs": {
    "all_interests": {
      "terms": {
        "field": "interests.keyword"
      }
    }
  }
}
'



# 聚合还支持分级汇总 。比如，查询特定兴趣爱好员工的平均年龄：
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "aggs" : {
        "all_interests" : {
            "terms" : { "field" : "interests.keyword" },
            "aggs" : {
                "avg_age" : {
                    "avg" : { "field" : "age" }
                }
            }
        }
    }
}
'


```



#### [集群健康](https://www.elastic.co/guide/cn/elasticsearch/guide/current/cluster-health.html)

status 字段指示着当前集群在总体上是否工作正常。它的三种颜色含义如下：

green
所有的主分片和副本分片都正常运行。
yellow
所有的主分片都正常运行，但不是所有的副本分片都正常运行。
red
有主分片没能正常运行。

```shell
curl -XGET '10.250.140.14:9200/_cluster/health?pretty'

{
  "cluster_name" : "es-cluster",
  "status" : "yellow",
  "timed_out" : false,
  "number_of_nodes" : 1,
  "number_of_data_nodes" : 1,
  "active_primary_shards" : 52,
  "active_shards" : 52,
  "relocating_shards" : 0,
  "initializing_shards" : 0,
  "unassigned_shards" : 52,
  "delayed_unassigned_shards" : 0,
  "number_of_pending_tasks" : 0,
  "number_of_in_flight_fetch" : 0,
  "task_max_waiting_in_queue_millis" : 0,
  "active_shards_percent_as_number" : 50.0
}

```



```shell
# 设置分片和副本数
curl -XPUT '10.250.140.14:9200/blogs?pretty' -H 'Content-Type: application/json' -d'
{
   "settings" : {
      "number_of_shards" : 3,
      "number_of_replicas" : 1
   }
}
'

# 更新副本数
curl -XPUT '10.250.140.14:9200/blogs/_settings?pretty' -H 'Content-Type: application/json' -d'
{
   "number_of_replicas" : 2
}
'

```



#### 数据输入和输出

```shell
# 创建新文档
curl -XPUT '10.250.140.14:9200/website/blog/123?pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "Just trying this out...",
  "date":  "2014/01/01"
}
'

# 自动生成的 ID 是 URL-safe、 基于 Base64 编码且长度为20个字符的 GUID 字符串。 
# 这些 GUID 字符串由可修改的 FlakeID 模式生成，这种模式允许多个节点并行生成唯一 ID ，
# 且互相之间的冲突概率几乎为零。
curl -XPOST '10.250.140.14:9200/website/blog/?pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My second blog entry",
  "text":  "Still trying this out...",
  "date":  "2014/01/01"
}
'

curl -XGET '10.250.140.14:9200/website/blog/123?pretty'

curl -i -XGET http://10.250.140.14:9200/website/blog/124?pretty

# 返回文档的一部分
curl -XGET '10.250.140.14:9200/website/blog/123?_source=title,text&pretty'

{
  "_index" : "website",
  "_type" : "blog",
  "_id" : "123",
  "_version" : 1,
  "found" : true,
  "_source" : {
    "text" : "Just trying this out...",
    "title" : "My first blog entry"
  }
}

# 返回文档_source字段
curl -XGET '10.250.140.14:9200/website/blog/123/_source?pretty'

{
  "title" : "My first blog entry",
  "text" : "Just trying this out...",
  "date" : "2014/01/01"
}

# 检查文档是否存在
curl -i -XHEAD http://10.250.140.14:9200/website/blog/123


# 更新文档
# 在 Elasticsearch 中文档是 不可改变 的，不能修改它们。
# 相反，如果想要更新现有的文档，需要 重建索引 或者进行替换
curl -XPUT '10.250.140.14:9200/website/blog/123?pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "I am starting to get the hang of this...",
  "date":  "2014/01/02"
}
'
-----------------------------
{
  "_index" : "website",
  "_type" : "blog",
  "_id" : "123",
  "_version" : 2,   @@@
  "result" : "updated",  @@@
  "_shards" : {
    "total" : 2,
    "successful" : 1,
    "failed" : 0
  },
  "created" : false  @@@
}


# 删除文档
curl -XDELETE '10.250.140.14:9200/website/blog/123?pretty'


```



#### 创建新文档

使用索引请求的 `POST` 形式让 Elasticsearch 自动生成唯一 `_id` :

```shell
POST /website/blog/
{ ... }
```



如果已经有自己的 `_id` ，那么我们必须告诉 Elasticsearch ，只有在相同的 `_index` 、 `_type` 和 `_id` 不存在时才接受我们的索引请求。这里有两种方式，他们做的实际是相同的事情。

第一种方法使用 `op_type` 查询 -字符串参数：

```
PUT /website/blog/123?op_type=create
{ ... }
```

第二种方法是在 URL 末端使用 `/_create` :

```
PUT /website/blog/123/_create
{ ... }
```

如果创建新文档的请求成功执行，Elasticsearch 会返回元数据和一个 `201 Created` 的 HTTP 响应码。

另一方面，如果具有相同的 `_index` 、 `_type` 和 `_id` 的文档已经存在，Elasticsearch 将会返回 `409 Conflict` 响应码

```shell
curl -XPUT '10.250.140.14:9200/website/blog/123?pretty=true&op_type=create' -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "I am starting to get the hang of this...",
  "date":  "2014/01/02"
}
'

curl -XPUT '10.250.140.14:9200/website/blog/123/_create?pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "I am starting to get the hang of this...",
  "date":  "2014/01/02"
}
'
```



#### [乐观并发控制](https://www.elastic.co/guide/cn/elasticsearch/guide/current/optimistic-concurrency-control.html)

```shell
curl -XPUT '10.250.140.14:9200/website/blog/1/_create?pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "Just trying this out..."
}
'

curl -XGET '10.250.140.14:9200/website/blog/1?pretty'


curl -XPUT '10.250.140.14:9200/website/blog/1?version=1&pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "Starting to get the hang of this..."
}
'


curl -XPUT '10.250.140.14:9200/website/blog/1?version=1&pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "Starting to get the hang of this..."
}
'


###################################
# 外部系统使用版本控制  指定版本号
# 当前版本号小于指定版本号
curl -XPUT '10.250.140.14:9200/website/blog/2?version=5&version_type=external&pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first external blog entry",
  "text":  "Starting to get the hang of this..."
}
'

curl -XPUT '10.250.140.14:9200/website/blog/2?version=10&version_type=external&pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first external blog entry",
  "text":  "This is a piece of cake..."
}
'


curl -XPUT '10.250.140.14:9200/website/blog/2?version=10&version_type=external&pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first external blog entry",
  "text":  "This is a piece of cake..."
}
'

```



#### 文档部分更新

```shell
curl -XGET '10.250.140.14:9200/website/blog/1?pretty'

{
  "_index" : "website",
  "_type" : "blog",
  "_id" : "1",
  "_version" : 2,
  "found" : true,
  "_source" : {
    "title" : "My first blog entry",
    "text" : "Starting to get the hang of this..."
  }
}



curl -XPOST '10.250.140.14:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
   "doc" : {
      "tags" : [ "testing" ],
      "views": 0
   }
}
'

curl -XGET '10.250.140.14:9200/website/blog/1?pretty'

{
  "_index" : "website",
  "_type" : "blog",
  "_id" : "1",
  "_version" : 3,
  "found" : true,
  "_source" : {
    "title" : "My first blog entry",
    "text" : "Starting to get the hang of this...",
    "views" : 0,
    "tags" : [
      "testing"
    ]
  }
}

# 使用脚本部分更新文档
# 脚本可以在 update API中用来改变 _source 的字段内容， 它在更新脚本中称为 ctx._source 。
curl -XPOST '10.250.140.14:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
   "script" : "ctx._source.views+=1"
}
'

curl -XGET '10.250.140.14:9200/website/blog/1?pretty'

{
  "_index" : "website",
  "_type" : "blog",
  "_id" : "1",
  "_version" : 4,
  "found" : true,
  "_source" : {
    "title" : "My first blog entry",
    "text" : "Starting to get the hang of this...",
    "views" : 1,
    "tags" : [
      "testing"
    ]
  }
}


-----------------


curl -XPOST '10.250.140.14:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
	"script": {
		"inline": "ctx._source.tags.add(params.new_tag)",
		"params": {
			"new_tag": "search"
		}
	}
}
'


curl -XGET '10.250.140.14:9200/website/blog/1?pretty'
{
  "_index" : "website",
  "_type" : "blog",
  "_id" : "1",
  "_version" : 8,
  "found" : true,
  "_source" : {
    "title" : "My first blog entry",
    "text" : "Starting to get the hang of this...",
    "views" : 0,
    "tags" : [
      "testing",
      "search"
    ]
  }
}


# 选择通过设置 ctx.op 为 delete 来删除基于其内容的文档
curl -XPOST '10.250.140.14:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
	"script": {
		"inline": "ctx.op = ctx._source.views == params.count ? \u0027delete\u0027 : \u0027none\u0027",
		"params": {
			"count": 1
		}
	}
}
'

curl -XGET '10.250.140.14:9200/website/blog/1?pretty'
{
  "_index" : "website",
  "_type" : "blog",
  "_id" : "1",
  "found" : false
}


# 更新的文档可能尚不存在
# 使用 upsert 参数，指定如果文档不存在就应该先创建它
# 我们第一次运行这个请求时， upsert 值作为新文档被索引，初始化 views 字段为 1 。 
# 在后续的运行中，由于文档已经存在， script 更新操作将替代 upsert 进行应用，对 views 计数器进行累加。
curl -XPOST '10.250.140.14:9200/website/pageviews/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
   "script" : "ctx._source.views+=1",
   "upsert": {
       "views": 1
   }
}
'

curl -XGET '10.250.140.14:9200/website/pageviews/1?pretty'

# 更新和冲突
# 通过 设置参数 retry_on_conflict 来自动完成， 
# 这个参数规定了失败之前 update 应该重试的次数，它的默认值为 0 。
# 失败之前重试该更新5次。
curl -XPOST '10.250.140.14:9200/website/pageviews/1/_update?retry_on_conflict=5&pretty' -H 'Content-Type: application/json' -d'
{
   "script" : "ctx._source.views+=1",
   "upsert": {
       "views": 0
   }
}
'

# 在增量操作无关顺序的场景，例如递增计数器等这个方法十分有效，
# 但是在其他情况下变更的顺序 是 非常重要的。 类似 index API ， update API 默认采用 
# 最终写入生效 的方案。


```



#### [取回多个文档](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_Retrieving_Multiple_Documents.html)

```shell
curl -XGET '10.250.140.14:9200/_mget?pretty' -H 'Content-Type: application/json' -d '{
   "docs" : [
      {
         "_index" : "website",
         "_type" :  "blog",
         "_id" :    2
      },
      {
         "_index" : "website",
         "_type" :  "pageviews",
         "_id" :    1,
         "_source": "views"
      }
   ]
}'


# 如果想检索的数据都在相同的 _index 中（甚至相同的 _type 中），
# 则可以在 URL 中指定默认的 /_index 或者默认的 /_index/_type 。
curl -XGET '10.250.140.14:9200/website/blog/_mget?pretty' -H 'Content-Type: application/json' -d '{
   "docs" : [
      { "_id" : 2 },
      { "_type" : "pageviews", "_id" :   1 }
   ]
}'

{
  "docs" : [
    {
      "_index" : "website",
      "_type" : "blog",
      "_id" : "2",
      "_version" : 10,
      "found" : true,
      "_source" : {
        "title" : "My first external blog entry",
        "text" : "This is a piece of cake..."
      }
    },
    {
      "_index" : "website",
      "_type" : "pageviews",
      "_id" : "1",
      "_version" : 2,
      "found" : true,
      "_source" : {
        "views" : 2
      }
    }
  ]
}


# 如果所有文档的 _index 和 _type 都是相同的，你可以只传一个 ids 数组，而不是整个 docs 数组
curl -XGET '10.250.140.14:9200/website/blog/_mget?pretty' -H 'Content-Type: application/json' -d '{
   "ids" : [ "2", "1" ]
}'

{
  "docs" : [
    {
      "_index" : "website",
      "_type" : "blog",
      "_id" : "2",
      "_version" : 10,
      "found" : true,
      "_source" : {
        "title" : "My first external blog entry",
        "text" : "This is a piece of cake..."
      }
    },
    {
      "_index" : "website",
      "_type" : "blog",
      "_id" : "1",
      "found" : false
    }
  ]
}


```



#### [代价较小的批量操作](https://www.elastic.co/guide/cn/elasticsearch/guide/current/bulk.html)

```shell
curl -XPOST '10.250.140.14:9200/_bulk?pretty' -H 'Content-Type: application/json' -d'
{ "delete": { "_index": "website", "_type": "blog", "_id": "123" }} 
{ "create": { "_index": "website", "_type": "blog", "_id": "123" }}
{ "title":    "My first blog post" }
{ "index":  { "_index": "website", "_type": "blog" }}
{ "title":    "My second blog post" }
{ "update": { "_index": "website", "_type": "blog", "_id": "123", "_retry_on_conflict" : 3} }
{ "doc" : {"title" : "My updated blog post"} }
'

{
  "took" : 22,
  "errors" : false,
  "items" : [
    {
      "delete" : {
        "found" : false,
        "_index" : "website",
        "_type" : "blog",
        "_id" : "123",
        "_version" : 1,
        "result" : "not_found",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "status" : 404
      }
    },
    {
      "create" : {
        "_index" : "website",
        "_type" : "blog",
        "_id" : "123",
        "_version" : 2,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "created" : true,
        "status" : 201
      }
    },
    {
      "index" : {
        "_index" : "website",
        "_type" : "blog",
        "_id" : "AWHV9UIF7cHMetqd_3rT",
        "_version" : 1,
        "result" : "created",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "created" : true,
        "status" : 201
      }
    },
    {
      "update" : {
        "_index" : "website",
        "_type" : "blog",
        "_id" : "123",
        "_version" : 3,
        "result" : "updated",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "status" : 200
      }
    }
  ]
}

# bulk 请求不是原子的： 不能用它来实现事务控制。每个请求是单独处理的，
# 因此一个请求的成功或失败不会影响其他的请求。


curl -XPOST '10.250.140.14:9200/_bulk?pretty' -H 'Content-Type: application/json' -d '
{ "create": { "_index": "website", "_type": "blog", "_id": "123" }}
{ "title":    "Cannot create - it already exists" }
{ "index":  { "_index": "website", "_type": "blog", "_id": "123" }}
{ "title":    "But we can update it" }
'

{
  "took" : 11,
  "errors" : true,
  "items" : [
    {
      "create" : {
        "_index" : "website",
        "_type" : "blog",
        "_id" : "123",
        "status" : 409,
        "error" : {
          "type" : "version_conflict_engine_exception",
          "reason" : "[blog][123]: version conflict, document already exists (current version [3])",
          "index_uuid" : "j-7Dag08SH2hzUv-V3MlYQ",
          "shard" : "0",
          "index" : "website"
        }
      }
    },
    {
      "index" : {
        "_index" : "website",
        "_type" : "blog",
        "_id" : "123",
        "_version" : 4,
        "result" : "updated",
        "_shards" : {
          "total" : 2,
          "successful" : 1,
          "failed" : 0
        },
        "created" : false,
        "status" : 200
      }
    }
  ]
}


# 在 bulk 请求的 URL 中接收默认的 /_index 或者 /_index/_type
curl -XPOST '10.250.140.14:9200/website/_bulk?pretty' -H 'Content-Type: application/json' -d'
{ "index": { "_type": "log" }}
{ "event": "User logged in" }
'

# 仍然可以覆盖元数据行中的 _index 和 _type , 但是它将使用 URL 中的这些元数据值作为默认值
curl -XPOST '10.250.140.14:9200/website/log/_bulk?pretty' -H 'Content-Type: application/json' -d '
{ "index": {}}
{ "event": "User logged in" }
{ "index": { "_type": "blog" }}
{ "title": "Overriding the default type" }
'


```

### [搜索——最基本的工具](https://www.elastic.co/guide/cn/elasticsearch/guide/current/search.html)

```shell
curl -XPUT 'http://localhost:9200/us/user/1?pretty=1' -d '
{
   "email" : "john@smith.com",
   "name" : "John Smith",
   "username" : "@john"
}
'

curl -XPUT 'http://localhost:9200/gb/user/2?pretty=1' -d '
{
   "email" : "mary@jones.com",
   "name" : "Mary Jones",
   "username" : "@mary"
}
'

curl -XPUT 'http://localhost:9200/gb/tweet/3?pretty=1' -d '
{
   "date" : "2014-09-13",
   "name" : "Mary Jones",
   "tweet" : "Elasticsearch means full text search has never been so easy",
   "user_id" : 2
}
'

curl -XPUT 'http://localhost:9200/us/tweet/4?pretty=1' -d '
{
   "date" : "2014-09-14",
   "name" : "John Smith",
   "tweet" : "@mary it is not just text, it does everything",
   "user_id" : 1
}
'

curl -XPUT 'http://localhost:9200/gb/tweet/5?pretty=1' -d '
{
   "date" : "2014-09-15",
   "name" : "Mary Jones",
   "tweet" : "However did I manage before Elasticsearch?",
   "user_id" : 2
}
'

curl -XPUT 'http://localhost:9200/us/tweet/6?pretty=1' -d '
{
   "date" : "2014-09-16",
   "name" : "John Smith",
   "tweet" : "The Elasticsearch API is really easy to use",
   "user_id" : 1
}
'

curl -XPUT 'http://localhost:9200/gb/tweet/7?pretty=1' -d '
{
   "date" : "2014-09-17",
   "name" : "Mary Jones",
   "tweet" : "The Query DSL is really powerful and flexible",
   "user_id" : 2
}
'

curl -XPUT 'http://localhost:9200/us/tweet/8?pretty=1' -d '
{
   "date" : "2014-09-18",
   "name" : "John Smith",
   "user_id" : 1
}
'

curl -XPUT 'http://localhost:9200/gb/tweet/9?pretty=1' -d '
{
   "date" : "2014-09-19",
   "name" : "Mary Jones",
   "tweet" : "Geo-location aggregations are really cool",
   "user_id" : 2
}
'

curl -XPUT 'http://localhost:9200/us/tweet/10?pretty=1' -d '
{
   "date" : "2014-09-20",
   "name" : "John Smith",
   "tweet" : "Elasticsearch surely is one of the hottest new NoSQL products",
   "user_id" : 1
}
'

curl -XPUT 'http://localhost:9200/gb/tweet/11?pretty=1' -d '
{
   "date" : "2014-09-21",
   "name" : "Mary Jones",
   "tweet" : "Elasticsearch is built for the cloud, easy to scale",
   "user_id" : 2
}
'

curl -XPUT 'http://localhost:9200/us/tweet/12?pretty=1' -d '
{
   "date" : "2014-09-22",
   "name" : "John Smith",
   "tweet" : "Elasticsearch and I have left the honeymoon stage, and I still love her.",
   "user_id" : 1
}
'

curl -XPUT 'http://localhost:9200/gb/tweet/13?pretty=1' -d '
{
   "date" : "2014-09-23",
   "name" : "Mary Jones",
   "tweet" : "So yes, I am an Elasticsearch fanboy",
   "user_id" : 2
}
'

curl -XPUT 'http://localhost:9200/us/tweet/14?pretty=1' -d '
{
   "date" : "2014-09-24",
   "name" : "John Smith",
   "tweet" : "How many more cheesy tweets do I have to write?",
   "user_id" : 1
}
'
```



```shell
curl -XPOST 'http://10.250.140.14:9200/_bulk?pretty' -d '
{ "create": { "_index": "us", "_type": "user", "_id": "1" }}
{ "email" : "john@smith.com", "name" : "John Smith", "username" : "@john" }
{ "create": { "_index": "gb", "_type": "user", "_id": "2" }}
{ "email" : "mary@jones.com", "name" : "Mary Jones", "username" : "@mary" }
{ "create": { "_index": "gb", "_type": "tweet", "_id": "3" }}
{ "date" : "2014-09-13", "name" : "Mary Jones", "tweet" : "Elasticsearch means full text search has never been so easy", "user_id" : 2 }
{ "create": { "_index": "us", "_type": "tweet", "_id": "4" }}
{ "date" : "2014-09-14", "name" : "John Smith", "tweet" : "@mary it is not just text, it does everything", "user_id" : 1 }
{ "create": { "_index": "gb", "_type": "tweet", "_id": "5" }}
{ "date" : "2014-09-15", "name" : "Mary Jones", "tweet" : "However did I manage before Elasticsearch?", "user_id" : 2 }
{ "create": { "_index": "us", "_type": "tweet", "_id": "6" }}
{ "date" : "2014-09-16", "name" : "John Smith",  "tweet" : "The Elasticsearch API is really easy to use", "user_id" : 1 }
{ "create": { "_index": "gb", "_type": "tweet", "_id": "7" }}
{ "date" : "2014-09-17", "name" : "Mary Jones", "tweet" : "The Query DSL is really powerful and flexible", "user_id" : 2 }
{ "create": { "_index": "us", "_type": "tweet", "_id": "8" }}
{ "date" : "2014-09-18", "name" : "John Smith", "user_id" : 1 }
{ "create": { "_index": "gb", "_type": "tweet", "_id": "9" }}
{ "date" : "2014-09-19", "name" : "Mary Jones", "tweet" : "Geo-location aggregations are really cool", "user_id" : 2 }
{ "create": { "_index": "us", "_type": "tweet", "_id": "10" }}
{ "date" : "2014-09-20", "name" : "John Smith", "tweet" : "Elasticsearch surely is one of the hottest new NoSQL products", "user_id" : 1 }
{ "create": { "_index": "gb", "_type": "tweet", "_id": "11" }}
{ "date" : "2014-09-21", "name" : "Mary Jones", "tweet" : "Elasticsearch is built for the cloud, easy to scale", "user_id" : 2 }
{ "create": { "_index": "us", "_type": "tweet", "_id": "12" }}
{ "date" : "2014-09-22", "name" : "John Smith", "tweet" : "Elasticsearch and I have left the honeymoon stage, and I still love her.", "user_id" : 1 }
{ "create": { "_index": "gb", "_type": "tweet", "_id": "13" }}
{ "date" : "2014-09-23", "name" : "Mary Jones", "tweet" : "So yes, I am an Elasticsearch fanboy", "user_id" : 2 }
{ "create": { "_index": "us", "_type": "tweet", "_id": "14" }}
{ "date" : "2014-09-24", "name" : "John Smith", "tweet" : "How many more cheesy tweets do I have to write?", "user_id" : 1 }'
```



```shell
curl -XGET '10.250.140.14:9200/_search?pretty'
curl -XGET '10.250.140.14:9200/us/_search?pretty'

curl -XGET '10.250.140.14:9200/us/_search?size=5&pretty'
curl -XGET '10.250.140.14:9200/us/_search?size=5&from=5&pretty'
curl -XGET '10.250.140.14:9200/us/_search?size=5&from=10&pretty'

```

#### [轻量搜索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/search-lite.html)

```shell
# 查询在 tweet 类型中 tweet 字段包含 elasticsearch 单词的所有文档
curl -XGET '10.250.140.14:9200/_all/tweet/_search?q=tweet:elasticsearch&pretty'

# 查询在 name 字段中包含 john 并且在 tweet 字段中包含 mary 的文档
# +name:john +tweet:mary
curl -XGET '10.250.140.14:9200/_search?q=%2Bname%3Ajohn+%2Btweet%3Amary&pretty'

# + 前缀表示必须与查询条件匹配。类似地， - 前缀表示一定不与查询条件匹配。
# 没有 + 或者 - 的所有其他条件都是可选的——匹配的越多，文档就越相关。
```



```shell
# 返回包含 mary 的所有文档
curl -XGET '10.250.140.14:9200/_search?q=mary&pretty'

# 针对tweents类型，并使用以下的条件：
# name 字段中包含 mary 或者 john
# date 值大于 2014-09-10
# _all_ 字段包含 aggregations 或者 geo

# +name:(mary john) +date:>2014-09-10 +(aggregations geo)




```

### [映射和分析](https://www.elastic.co/guide/cn/elasticsearch/guide/current/mapping-analysis.html)

```shell
curl -XGET '10.250.140.14:9200/_search?q=2014              # 12 results&pretty'
curl -XGET '10.250.140.14:9200/_search?q=2014-09-15        # 12 results !&pretty'
curl -XGET '10.250.140.14:9200/_search?q=date:2014-09-15   # 1  result&pretty'
curl -XGET '10.250.140.14:9200/_search?q=date:2014         # 0  results !&pretty'

```

```shell
# 测试分析器
curl -XGET '10.250.140.14:9200/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "analyzer": "standard",
  "text": "Text to analyze"
}
'
```



```shell
# 删除
curl -i -XDELETE '10.250.140.14:9200/gb?pretty'

curl -i -XPUT '10.250.140.14:9200/gb?pretty' -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "tweet" : {
      "properties" : {
        "tweet" : {
          "type" :    "text",
          "analyzer": "english"
        },
        "date" : {
          "type" :   "date"
        },
        "name" : {
          "type" :   "text"
        },
        "user_id" : {
          "type" :   "long"
        }
      }
    }
  }
}
'

# keyword 默认是 not_analyzed, text 默认是 analyzed 
curl -XPUT '10.250.140.14:9200/gb/_mapping/tweet?pretty' -H 'Content-Type: application/json' -d'
{
  "properties" : {
    "tag" : {
      "type" :    "text"
    },
    "color" : {
      "type" :    "keyword"
    }
  }
}
'

# 默认是true ,  会创建索引, 没有创建索引不能被查询
curl -XPUT '10.250.140.14:9200/gb/_mapping/tweet?pretty' -H 'Content-Type: application/json' -d'
{
  "properties" : {
    "ageA" : {
      "type" : "integer",
      "index": false
    },
    "ageB" : {
      "type" : "integer"
    }
  }
}
'

curl -XGET '10.250.140.14:9200/gb/_mapping/tweet?pretty'


curl -XGET '10.250.140.14:9200/gb/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "field": "tweet",
  "text": "Black-cats" 
}
'
curl -XGET '10.250.140.14:9200/gb/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "field": "color",
  "text": "Black-cats" 
}
'

curl -XPUT 'http://10.250.140.14:9200/gb/tweet/14?pretty' -d '
{
   "tweet" : "John Smith",
   "ageA" : 112,
   "ageB" : 11
}
'

# 不能查询，因为未建索引
curl -XGET 'http://10.250.140.14:9200/gb/tweet/_search?pretty' -d '{
    "query" : {
        "match" : {
            "ageA" : 112
        }
    }
}'

# 可以被查询
curl -XGET 'http://10.250.140.14:9200/gb/tweet/_search?pretty' -d '{
    "query" : {
        "match" : {
            "ageB" : 11
        }
    }
}'
```



#### [复杂核心域类型](https://www.elastic.co/guide/cn/elasticsearch/guide/current/complex-core-fields.html)

对于数组，没有特殊的映射需求。任何域都可以包含0、1或者多个值，就像全文域分析得到多个词条。

这暗示 *数组中所有的值必须是相同数据类型的* 。你不能将日期和字符串混在一起。如果你通过索引数组来创建新的域，Elasticsearch 会用数组中第一个值的数据类型作为这个域的 `类型` 。

数组是以多值域 *索引的*—可以搜索，但是无序的。 在搜索的时候，你不能指定 “第一个” 或者 “最后一个”。



Lucene 不理解内部对象。 Lucene 文档是由一组键值对列表组成的。



### [请求体查询](https://www.elastic.co/guide/cn/elasticsearch/guide/current/full-body-search.html)



#### [查询表达式](https://www.elastic.co/guide/cn/elasticsearch/guide/current/query-dsl-intro.html)

```shell
# 空搜索
curl -XGET '10.250.140.14:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "match_all": {}
    }
}'

# 在bool query中minimum_should_match只能紧跟在should的后面，放其他地方会出异常
# "minimum_should_match": 1 表示至少匹配1个
{
    "bool": {
        "must": { "match":   { "email": "business opportunity" }},
        "should": [
            { "match":       { "starred": true }},
            { "bool": {
                "must":      { "match": { "folder": "inbox" }},
                "must_not":  { "match": { "spam": true }}
            }}
        ],
        "minimum_should_match": 1
    }
}


```



#### [最重要的查询](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_most_important_queries.html)

```shell

# match 查询编辑

# 如果你在一个全文字段上使用 match 查询，在执行查询前，它将用正确的分析器去分析查询字符串：

{ "match": { "tweet": "About Search" }}

# 如果在一个精确值的字段上使用它， 例如数字、日期、布尔或者一个 not_analyzed 字符串字段，
# 那么它将会精确匹配给定的值：

{ "match": { "age":    26           }}
{ "match": { "date":   "2014-09-01" }}
{ "match": { "public": true         }}
{ "match": { "tag":    "full_text"  }}

# 对于精确值的查询，你可能需要使用 filter 语句来取代 query，因为 filter 将会被缓存。



# multi_match 查询编辑
#multi_match 查询可以在多个字段上执行相同的 match 查询

{
    "multi_match": {
        "query":    "full text search",
        "fields":   [ "title", "body" ]
    }
}


# range 查询编辑
# range 查询找出那些落在指定区间内的数字或者时间

{
    "range": {
        "age": {
            "gte":  20,
            "lt":   30
        }
    }
}

# gt 大于， gte 大于等于， lt 小于， lte 小于等于



# term 查询
# term 查询被用于精确值 匹配，这些精确值可能是数字、时间、布尔或者那些 not_analyzed 的字符串：

{ "term": { "age":    26           }}
{ "term": { "date":   "2014-09-01" }}
{ "term": { "public": true         }}
{ "term": { "tag":    "full_text"  }}

# term 查询对于输入的文本不 分析 ，所以它将给定的值进行精确查询。

# terms 查询
# terms 查询和 term 查询一样，但它允许你指定多值进行匹配。
# 如果这个字段包含了指定值中的任何一个值，那么这个文档满足条件：

{ "terms": { "tag": [ "search", "full_text", "nosql" ] }}

# 和 term 查询一样，terms 查询对于输入的文本不分析。
# 它查询那些精确匹配的值（包括在大小写、重音、空格等方面的差异）。


# exists 查询和 missing 查询
# exists 查询和 missing 查询被用于查找那些指定字段中有值 (exists) 或无值 (missing) 的文档。
# 这与SQL中的 IS_NULL (missing) 和 NOT IS_NULL (exists) 在本质上具有共性：

{
    "exists":   {
        "field":    "title"
    }
}

# 这些查询经常用于某个字段有值的情况和某个字段缺值的情况。

```





#### [组合多查询](https://www.elastic.co/guide/cn/elasticsearch/guide/current/combining-queries-together.html)

`must`

文档 *必须* 匹配这些条件才能被包含进来。

`must_not`

文档 *必须不* 匹配这些条件才能被包含进来。

`should`

如果满足这些语句中的任意语句，将增加 `_score` ，否则，无任何影响。它们主要用于修正每个文档的相关性得分。

`filter`

*必须* 匹配，但它以不评分、过滤模式来进行。这些语句对评分没有贡献，只是根据过滤标准来排除或包含文档。



```shell
# 查找 title 字段匹配 how to make millions 并且不被标识为 spam 的文档。
# 那些被标识为 starred 或在2014之后的文档，将比另外那些文档拥有更高的排名。
# 如果 _两者_ 都满足，那么它排名将更高
{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }},
            { "range": { "date": { "gte": "2014-01-01" }}}
        ]
    }
}

#  如果没有 must 语句，那么至少需要能够匹配其中的一条 should 语句。
# 但，如果存在至少一条 must 语句，则对 should 语句的匹配没有要求。


# 增加带过滤器（filtering）的查询编辑
# 如果我们不想因为文档的时间而影响得分，可以用 filter 语句来重写前面的例子：

{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }}
        ],
        "filter": {
          "range": { "date": { "gte": "2014-01-01" }} 
        }
    }
}


# 将查询移到 bool 查询的 filter 语句中，这样它就自动的转成一个不评分的 filter 了
{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }}
        ],
        "filter": {
          "bool": { 
              "must": [
                  { "range": { "date": { "gte": "2014-01-01" }}},
                  { "range": { "price": { "lte": 29.99 }}}
              ],
              "must_not": [
                  { "term": { "category": "ebooks" }}
              ]
          }
        }
    }
}


# constant_score 查询编辑
# 它将一个不变的常量评分应用于所有匹配的文档。
# 它被经常用于你只需要执行一个 filter 而没有其它查询（例如，评分查询）的情况下。

# term 查询被放置在 constant_score 中，转成不评分的 filter。
# 这种方式可以用来取代只有 filter 语句的 bool 查询。
{
    "constant_score":   {
        "filter": {
            "term": { "category": "ebooks" } 
        }
    }
}
```







```shell
# 验证查询编辑
curl -XGET '10.250.140.14:9200/gb/tweet/_validate/query?pretty' -H 'Content-Type: application/json' -d'
{
   "query": {
      "tweet" : {
         "match" : "really powerful"
      }
   }
}
'


# 理解错误信息
curl -XGET '10.250.140.14:9200/gb/tweet/_validate/query?explain&pretty' -H 'Content-Type: application/json' -d'
{
   "query": {
      "tweet" : {
         "match" : "really powerful"
      }
   }
}
'



# 理解查询语句
curl -XGET '10.250.140.14:9200/gb,us/_validate/query?explain&pretty' -H 'Content-Type: application/json' -d'
{
   "query": {
      "match" : {
         "tweet" : "really powerful"
      }
   }
}
'

# 不同的分析器结果不同

{
  "valid" : true,
  "_shards" : {
    "total" : 2,
    "successful" : 2,
    "failed" : 0
  },
  "explanations" : [
    {
      "index" : "gb",
      "valid" : true,
      "explanation" : "tweet:realli tweet:power"
    },
    {
      "index" : "us",
      "valid" : true,
      "explanation" : "tweet:really tweet:powerful"
    }
  ]
}

```



### [排序与相关性](https://www.elastic.co/guide/cn/elasticsearch/guide/current/sorting.html)

#### [排序](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_Sorting.html)

```shell
# 使用的是 filter （过滤），没有试图确定这些文档的相关性。 
# 实际上文档将按照随机顺序返回，并且每个文档都会评为零。
curl -XGET '10.250.140.14:9200/_search?pretty' -d '{
    "query" : {
        "bool" : {
            "filter" : {
                "term" : {
                    "user_id" : 1
                }
            }
        }
    }
}'

# 如果评分为零对你造成了困扰，你可以使用 constant_score 查询进行替代
# 这将让所有文档应用一个恒定分数（默认为 1 ）。
curl -XGET '10.250.140.14:9200/_search?pretty' -d '{
    "query" : {
        "constant_score" : {
            "filter" : {
                "term" : {
                    "user_id" : 1
                }
            }
        }
    }
}'



# 对 tweets 进行排序，最新的 tweets 排在最前。 	
# _score 不被计算, 因为它并没有用于排序。
curl -XGET '10.250.140.14:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "bool" : {
            "filter" : { "term" : { "user_id" : 1 }}
        }
    },
    "sort": { "date": { "order": "desc" }}
}
'

#_score 和 max_score 字段都是 null 。 计算 _score 的花销巨大，通常仅用于排序； 
# 我们并不根据相关性排序，所以记录 _score 是没有意义的。如果无论如何你都要计算 _score ， 
# 你可以将 track_scores 参数设置为 true 。


curl -XGET '10.250.140.14:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "bool" : {
            "must":   { "match": { "tweet": "manage text search" }},
            "filter" : { "term" : { "user_id" : 2 }}
        }
    },
    "sort": [
        { "date":   { "order": "desc" }},
        { "_score": { "order": "desc" }}
    ]
}
'
# 多级排序并不一定包含 _score 。你可以根据一些不同的字段进行排序， 如地理距离或是脚本计算的特定值。

# Query-string 搜索 也支持自定义排序，可以在查询字符串中使用 sort 参数：
# GET /_search?sort=date:desc&sort=_score&q=search


# 字段多值的排序编辑

# 对于数字或日期，你可以将多值字段减为单值，这可以通过使用 min 、 max 、 avg 或是 sum 排序模式 。 
# 例如你可以按照每个 date 字段中的最早日期进行排序，通过以下方法：
"sort": {
    "dates": {
        "order": "asc",
        "mode":  "min"
    }
}
```



#### [字符串排序与多字段](https://www.elastic.co/guide/cn/elasticsearch/guide/current/multi-fields.html)

```shell
"tweet": { 
    "type":     "string",
    "analyzer": "english",
    "fields": {
        "raw": { 
            "type":  "string",
            "index": "not_analyzed"
        }
    }
}

# tweet 主字段与之前的一样: 是一个 analyzed 全文字段。
# 新的 tweet.raw 子字段是 not_analyzed.


curl -XGET '10.250.140.14:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "match": {
            "tweet": "elasticsearch"
        }
    },
    "sort": "tweet.raw"
}
'

```



#### [什么是相关性?](https://www.elastic.co/guide/cn/elasticsearch/guide/current/relevance-intro.html)

```shell
# JSON格式
curl -XGET '10.250.140.14:9200/_search?explain&pretty' -H 'Content-Type: application/json' -d'
{
   "query"   : { "match" : { "tweet" : "honeymoon" }}
}
'

# YAML格式
curl -XGET '10.250.140.14:9200/_search?explain&pretty&format=yaml' -H 'Content-Type: application/json' -d'
{
   "query"   : { "match" : { "tweet" : "honeymoon" }}
}
'


# 当 explain 选项加到某一文档上时， explain api 会帮助你理解为何这个文档会被匹配，
# 更重要的是，一个文档为何没有被匹配。
curl -XGET '10.250.140.14:9200/us/tweet/12/_explain?pretty' -H 'Content-Type: application/json' -d'
{
   "query" : {
      "bool" : {
         "filter" : { "term" :  { "user_id" : 2           }},
         "must" :  { "match" : { "tweet" :   "honeymoon" }}
      }
   }
}
'

```



### [索引管理](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index-management.html)

#### [索引设置](https://www.elastic.co/guide/cn/elasticsearch/guide/current/index-settings.html)

```shell
curl -XPUT '10.250.140.14:9200/my_temp_index?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
        "number_of_shards" :   1,
        "number_of_replicas" : 0
    }
}
'

curl -XPUT '10.250.140.14:9200/my_temp_index/_settings?pretty' -H 'Content-Type: application/json' -d'
{
    "number_of_replicas": 1
}
'


```





#### [配置分析器](https://www.elastic.co/guide/cn/elasticsearch/guide/current/configuring-analyzers.html)

```shell
curl -XPUT '10.250.140.14:9200/spanish_docs?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
        "analysis": {
            "analyzer": {
                "es_std": {
                    "type":      "standard",
                    "stopwords": "_spanish_"
                }
            }
        }
    }
}
'

# 10.250.140.14:9200/spanish_docs/_analyze?analyzer=es_std&pretty&text=El veloz zorro marrón
curl -XGET 'http://10.250.140.14:9200/spanish_docs/_analyze?analyzer=es_std&pretty&text=El%20veloz%20zorro%20marr%C3%B3n'





```



#### [自定义分析器](https://www.elastic.co/guide/cn/elasticsearch/guide/current/custom-analyzers.html)

```shell
# 使用 html清除 字符过滤器移除HTML部分。
# 使用一个自定义的 映射 字符过滤器把 & 替换为 " 和 " 。
# 使用 标准 分词器分词。
# 小写词条，使用 小写 词过滤器处理。
# 使用自定义 停止 词过滤器移除自定义的停止词列表中包含的词。
curl -XPUT '10.250.140.14:9200/my_index?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
        "analysis": {
            "char_filter": {
                "&_to_and": {
                    "type":       "mapping",
                    "mappings": [ "&=> and "]
            }},
            "filter": {
                "my_stopwords": {
                    "type":       "stop",
                    "stopwords": [ "the", "a" ]
            }},
            "analyzer": {
                "my_analyzer": {
                    "type":         "custom",
                    "char_filter":  [ "html_strip", "&_to_and" ],
                    "tokenizer":    "standard",
                    "filter":       [ "lowercase", "my_stopwords" ]
            }}
}}}
'


curl -XGET '10.250.140.14:9200/my_index2/_analyze?analyzer=my_analyzer&pretty' -H 'Content-Type: application/json' -d'
{
	"analyzer":	"my_analyzer",
  	"text": "The quick & brown fox"
}
'

# 将分析器应用在一个字段上
curl -XPUT '10.250.140.14:9200/my_index/_mapping/my_type?pretty' -H 'Content-Type: application/json' -d'
{
    "properties": {
        "title": {
            "type":      "text",
            "analyzer":  "my_analyzer"
        }
    }
}
'


```





### [分片内部原理](https://www.elastic.co/guide/cn/elasticsearch/guide/current/inside-a-shard.html)

#### [近实时搜索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/near-real-time.html)

```shell
# 关闭自动刷新
curl -XPUT '10.250.140.14:9200/my_logs/_settings?pretty' -H 'Content-Type: application/json' -d '{ "refresh_interval": -1 }'

# 每秒自动刷新
curl -XPUT '10.250.140.14:9200/my_logs/_settings?pretty' -H 'Content-Type: application/json' -d '{ "refresh_interval": "1s" }'
```



## [深入搜索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/search-in-depth.html)

### [结构化搜索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/structured-search.html)



#### [精确值查找](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_finding_exact_values.html)



```shell
curl -XPOST '10.250.140.14:9200/my_store/products/_bulk?pretty' -H 'Content-Type: application/json' -d'
{ "index": { "_id": 1 }}
{ "price" : 10, "productID" : "XHDK-A-1293-#fJ3" }
{ "index": { "_id": 2 }}
{ "price" : 20, "productID" : "KDKE-B-9947-#kL5" }
{ "index": { "_id": 3 }}
{ "price" : 30, "productID" : "JODL-X-1937-#pV7" }
{ "index": { "_id": 4 }}
{ "price" : 30, "productID" : "QQPX-R-3956-#aD8" }
'


# 会使用 constant_score 查询以非评分模式来执行 term 查询并以一作为统一评分
curl -XGET '10.250.140.14:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : { 
            "filter" : {
                "term" : { 
                    "price" : 20
                }
            }
        }
    }
}
'


# 精确查找
curl -XGET '10.250.140.14:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "term" : {
                    "productID" : "XHDK-A-1293-#fJ3"
                }
            }
        }
    }
}
'



# 匹配 无打分
curl -XGET '10.250.140.14:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "match" : {
                    "productID" : "XHDK-A-1293-#fJ3"
                }
            }
        }
    }
}
'

# 匹配 打分
curl -XGET '10.250.140.14:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
 "query" : {
    "match" : {
    "productID" : "XHDK-A-1293-#fJ3"
    }
  }
}
'

# 
curl -XGET '10.250.140.14:9200/my_store/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "field": "productID",
  "text": "XHDK-A-1293-#fJ3"
}
'


```

#### [组合过滤器](https://www.elastic.co/guide/cn/elasticsearch/guide/current/combining-filters.html)

```shell
curl -XGET 'localhost:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
   "query" : {
      "filtered" : { 
         "filter" : {
            "bool" : {
              "should" : [
                 { "term" : {"price" : 20}}, 
                 { "term" : {"productID" : "XHDK-A-1293-#fJ3"}} 
              ],
              "must_not" : {
                 "term" : {"price" : 30} 
              }
           }
         }
      }
   }
}
'

# 嵌套布尔过滤器
curl -XGET 'localhost:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
   "query" : {
      "filtered" : {
         "filter" : {
            "bool" : {
              "should" : [
                { "term" : {"productID" : "KDKE-B-9947-#kL5"}}, 
                { "bool" : { 
                  "must" : [
                    { "term" : {"productID" : "JODL-X-1937-#pV7"}}, 
                    { "term" : {"price" : 30}} 
                  ]
                }}
              ]
           }
         }
      }
   }
}
'

```



#### [查找多个精确值](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_finding_multiple_exact_values.html)

```shell
curl -XGET 'localhost:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "terms" : { 
                    "price" : [20, 30]
                }
            }
        }
    }
}
'
```





#### [范围](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_ranges.html)



```json
# 数值
curl -XGET '10.250.140.14:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d '{
    "query" : {
        "constant_score" : {
            "filter" : {
                "range" : {
                    "price" : {
                        "gte" : 20,
                        "lt"  : 40
                    }
                }
            }
        }
    }
}'


# 日期范围
"range" : {
    "timestamp" : {
        "gt" : "now-1h"
    }
}


"range" : {
    "timestamp" : {
        "gt" : "2014-01-01 00:00:00",
        "lt" : "2014-01-01 00:00:00||+1M" 
    }
}

# 字符串范围
"range" : {
    "title" : {
        "gte" : "a",
        "lt" :  "b"
    }
}

```



#### [处理 Null 值](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_dealing_with_null_values.html)

```shell
curl -XGET 'localhost:9200/my_index/posts/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "exists" : { "field" : "tags" }
            }
        }
    }
}
'

# 缺失查询
curl -XGET 'localhost:9200/my_index/posts/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : {
            "filter": {
                "missing" : { "field" : "tags" }
            }
        }
    }
}
'

```

### [全文搜索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/full-text-search.html)



#### [基于词项与基于全文](https://www.elastic.co/guide/cn/elasticsearch/guide/current/term-vs-full-text.html)



#### [匹配查询](https://www.elastic.co/guide/cn/elasticsearch/guide/current/match-query.html)

#### [多词查询](https://www.elastic.co/guide/cn/elasticsearch/guide/current/match-multi-word.html)

```shell
# 提高精度
# match 查询还可以接受 operator 操作符作为输入参数，
# 默认情况下该操作符是 or 。我们可以将它修改成 and 让所有指定词项都必须匹配。
curl -XGET '10.250.140.14:9200/my_index/my_type/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "match": {
            "title": {      
                "query":    "BROWN DOG!",
                "operator": "and"
            }
        }
    }
}
'
# 控制精度
# match 查询支持 minimum_should_match 最小匹配参数， 这让我们可以指定必须匹配的词项数用来
# 表示一个文档是否相关。我们可以将其设置为某个具体数字，更常用的做法是将其设置为一个百分数，
# 因为我们无法控制用户搜索时输入的单词数量
curl -XGET 'localhost:9200/my_index/my_type/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "match": {
      "title": {
        "query":                "quick brown dog",
        "minimum_should_match": "75%"
      }
    }
  }
}
'

# 当给定百分比的时候， minimum_should_match 会做合适的事情：在之前三词项的示例中， 75% 会自动被截断成 # 66.6% ，即三个里面两个词。无论这个值设置成什么，至少包含一个词项的文档才会被认为是匹配的。

```



```shell
{
    "match": {
        "title": {
            "query":                "quick brown fox",
            "minimum_should_match": "75%"
        }
    }
}

<===>

{
  "bool": {
    "should": [
      { "term": { "title": "brown" }},
      { "term": { "title": "fox"   }},
      { "term": { "title": "quick" }}
    ],
    "minimum_should_match": 2 
  }
}
```



#### [查询语句提升权重](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_boosting_query_clauses.html)

```shell

# boost 参数被用来提升一个语句的相对权重（ boost 值大于 1 ）或
# 降低相对权重（ boost 值处于 0 到 1 之间），但是这种提升或降低并不是线性的
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "bool": {
            "must": {
                "match": {  
                    "content": {
                        "query":    "full text search",
                        "operator": "and"
                    }
                }
            },
            "should": [
                { "match": {
                    "content": {
                        "query": "Elasticsearch",
                        "boost": 3 
                    }
                }},
                { "match": {
                    "content": {
                        "query": "Lucene",
                        "boost": 2 
                    }
                }}
            ]
        }
    }
}
'

```





#### [控制分析-analysis](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_controlling_analysis.html)



#### [被破坏的相关度！](https://www.elastic.co/guide/cn/elasticsearch/guide/current/relevance-is-broken.html)

```shell
# dfs 是指 分布式频率搜索
curl -XGET '10.250.140.14:9200/alibaba_alias/employee/_search?pretty&search_type=dfs_query_then_fetch' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "match" : {
            "about" : "rock climbing"
        }
    }
}
'
```





### [多字段搜索](https://www.elastic.co/guide/cn/elasticsearch/guide/current/multi-field-search.html)





#### [多字符串查询](https://www.elastic.co/guide/cn/elasticsearch/guide/current/multi-query-strings.html)

```shell
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "should": [
        { "match": { "title":  "War and Peace" }},
        { "match": { "author": "Leo Tolstoy"   }},
        { "bool":  {
          "should": [
            { "match": { "translator": "Constance Garnett" }},
            { "match": { "translator": "Louise Maude"      }}
          ]
        }}
      ]
    }
  }
}
'

# 设置语句的权重
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "should": [
        { "match": { 
            "title":  {
              "query": "War and Peace",
              "boost": 2
        }}},
        { "match": { 
            "author":  {
              "query": "Leo Tolstoy",
              "boost": 2
        }}},
        { "bool":  { 
            "should": [
              { "match": { "translator": "Constance Garnett" }},
              { "match": { "translator": "Louise Maude"      }}
            ]
        }}
      ]
    }
  }
}
'

```



#### [最佳字段](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_best_fields.html)

```shell
# dis_max 查询编辑
{
    "query": {
        "dis_max": {
            "queries": [
                { "match": { "title": "Brown fox" }},
                { "match": { "body":  "Brown fox" }}
            ]
        }
    }
}
```





#### [最佳字段查询调优](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_tuning_best_fields_queries.html)

`tie_breaker` 参数提供了一种 `dis_max` 和 `bool` 之间的折中选择，它的评分方式如下：

1. 获得最佳匹配语句的评分 `_score` 。
2. 将其他匹配语句的评分结果与 `tie_breaker` 相乘。
3. 对以上评分求和并规范化。

```shell
{
    "query": {
        "dis_max": {
            "queries": [
                { "match": { "title": "Quick pets" }},
                { "match": { "body":  "Quick pets" }}
            ],
            "tie_breaker": 0.3
        }
    }
}
```



`tie_breaker` 可以是 `0` 到 `1` 之间的浮点数，其中 `0` 代表使用 `dis_max` 最佳匹配语句的普通逻辑， `1` 表示所有匹配语句同等重要。最佳的精确值需要根据数据与查询调试得出，但是合理值应该与零接近（处于 `0.1 - 0.4` 之间），这样就不会颠覆 `dis_max` 最佳匹配性质的根本。

