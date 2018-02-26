
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
10.250.140.14:9200/alibaba_alias/employee/_search?q=last_name:哈登&pretty
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













