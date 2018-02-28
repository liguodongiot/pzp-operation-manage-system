
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





#### [搜索——最基本的工具](https://www.elastic.co/guide/cn/elasticsearch/guide/current/search.html)

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



#### [映射和分析](https://www.elastic.co/guide/cn/elasticsearch/guide/current/mapping-analysis.html)

```shell
curl -XGET 'localhost:9200/_search?q=2014              # 12 results&pretty'
curl -XGET 'localhost:9200/_search?q=2014-09-15        # 12 results !&pretty'
curl -XGET 'localhost:9200/_search?q=date:2014-09-15   # 1  result&pretty'
curl -XGET 'localhost:9200/_search?q=date:2014         # 0  results !&pretty'

```

