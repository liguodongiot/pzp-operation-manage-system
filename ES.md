

### ES安装
```shell
# windows
cd E:\install\elasticsearch-5.5.2
bin\elasticsearch.bat


# linux
cluster.name: es-test
network.host: 10.250.150.231
http.port: 9200
transport.tcp.port: 9393
discovery.zen.ping.unicast.hosts:  ["10.22.150.2"]


# 启动ES
bin/elasticsearch -d


```



#### 报错

**max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]**

```shell
# 修改配置
vi /etc/sysctl.conf 
# 添加配置
vm.max_map_count=655360
# 执行命令
sysctl -p
```





### ES常用查询



```shell
# 查看集群是否启动成功
curl 'http://10.250.140.14:9200/?pretty'

# 查看所有索引库
curl http://10.250.140.14:9200/_cat/indices

# 查看索引库信息
curl -XGET  http://10.250.140.14:9200/user_manage_v1?pretty


# 插入数据 post
curl -XPUT 'http://10.250.140.14:9200/user_manage_v1/user_manage/1' -d '{
  "id" : 1,
  "name" : "李国冬",
  "age" : 25
}'


# 统计文档数 完整请求格式
curl -i -XGET '10.250.140.14:9200/user_info_v1/_count?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "match_all": {}
    }
}'

# 结果
--------------------------------------------------------------------------
HTTP/1.1 200 OK
content-type: application/json; charset=UTF-8
content-length: 95

{
  "count" : 1,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  }
}
--------------------------------------------------------------------------



# 根据ID查询  get
curl http://10.250.140.14:9200/user_manage_v1/user_manage/1

# 根据某个字段类型查询 post
curl -XGET 'http://10.250.140.14:9200/user_manage_v1/user_manage/_search?pretty' -d '{
    "query" : {
        "match" : {
            "name" : "李国冬"
        }
    }
}'


# 使用索引别名查询 post
curl -XGET 'http://10.250.140.14:9200/user_manage_alise/user_manage/_search?pretty' -d '{
    "query" : {
        "match" : {
            "name" : "李国冬"
        }
    }
}'


# 更换索引别名 POST 
# 别名可以指向多个索引，所以我们需要在新索引中添加别名的同时从旧索引中删除它。
# 这个操作需要原子化，所以我们需要用 `_aliases` 操作
curl -XPOST 'http://10.250.140.14:9200/_aliases' -d '
{
    "actions" : [
        { "remove" : { "index" : "user_manage_v1", "alias" : "user_manage_alise" } },
        { "add" : { "index" : "user_manage_v2", "alias" : "user_manage_alise" } }
    ]
}'


# 更新数据
curl -XPUT 'http://10.250.140.14:9200/user_info_v1/user_info/13?pretty' -d '{"id":13,"name":"33dongdong3","motto":"中国好，山河好呀","age":333,"sex":"female"}'

# 删除数据
curl -XDELETE 'http://10.250.140.14:9200/user_info_v1/user_info/13?pretty'
```



#### 排序

```shell
# _score默认是降序，其他字段默认是升序
curl -XGET 'http://10.250.140.14:9200/user_info_v1/user_info/_search?pretty' -d '{
    "sort" : [
        { "name" : {"order" : "asc"}},
        "sex",
        { "age" : "desc" },
        "_score"
    ],
    "query" : {
        "term" : { "name" : "张飞" }
    }
}'
```







### 聚合Aggregations

掌握Aggregations需要理解两个概念：

- 桶(Buckets)：符合条件的文档的集合，相当于SQL中的group by。比如，在users表中，按“地区”聚合，一个人将被分到北京桶或上海桶或其他桶里；按“性别”聚合，一个人将被分到男桶或女桶


- 指标(Metrics)：基于Buckets的基础上进行统计分析，相当于SQL中的count,avg,sum等。比如，按“地区”聚合，计算每个地区的人数，平均年龄等

对照一条SQL来加深我们的理解：

```sql
SELECT COUNT(color) FROM table GROUP BY color
```

GROUP BY相当于做分桶的工作，COUNT是统计指标。



```shell
# 平均年龄, 总年龄，最大年龄，最小年龄，总个数
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{
	"aggs": {
		"avg_age": {
			"avg": {
				"field": "age"
			}
		},
		"sum_age": {
			"sum": {
				"field": "age"
			}
		},
		"max_age": {
			"max": {
				"field": "age"
			}
		},
		"min_age": {
			"min": {
				"field": "age"
			}
		},
		"count_age": {
			"value_count": {
				"field": "age"
			}
		}
	}
}'

# 结果
{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 1.0,
    "hits" : [
      {...},{...},...
    ]
  },
  "aggregations" : {
    "max_age" : {
      "value" : 66.0
    },
    "avg_age" : {
      "value" : 34.5
    },
    "count_age" : {
      "value" : 10
    },
    "sum_age" : {
      "value" : 345.0
    },
    "min_age" : {
      "value" : 23.0
    }
  }
}



```



```shell
# 根据姓名分组
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{
  "aggs": { 
    "all_name": { 
      "terms": { "field": "name" } 
    } 
  } 
}'

# 结果
{
  "took" : 15,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 1.0,
    "hits" : [
      {...},{...},...
    ]
  },
  "aggregations" : {
    "all_name" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "张飞",
          "doc_count" : 4
        },
        {
          "key" : "李元芳",
          "doc_count" : 3
        },
        {
          "key" : "刘飞",
          "doc_count" : 1
        },
        {
          "key" : "李国冬",
          "doc_count" : 1
        },
        {
          "key" : "胡景涛",
          "doc_count" : 1
        }
      ]
    }
  }
}

```



```shell
# 先查询出23岁的文档，再根据姓名分组
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
  "query": { 
    "match": { 
      "age": 23
    } 
  }, 
  "aggs": { 
    "all_name": { 
      "terms": { 
        "field": "name" 
      } 
    } 
  } 
}'

# 结果
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 2,
    "max_score" : 1.0,
    "hits" : [
      {
        "_index" : "user_info_v1",
        "_type" : "user_info",
        "_id" : "2",
        "_score" : 1.0,
        "_source" : {
          "id" : 2,
          "name" : "李国冬",
          "motto" : "滴答滴答滴滴答",
          "age" : 23
        }
      },
      {
        "_index" : "user_info_v1",
        "_type" : "user_info",
        "_id" : "6",
        "_score" : 1.0,
        "_source" : {
          "id" : 6,
          "name" : "李元芳",
          "motto" : "东方红，红太阳，希望在天上",
          "age" : 23
        }
      }
    ]
  },
  "aggregations" : {
    "all_name" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "李元芳",
          "doc_count" : 1
        },
        {
          "key" : "李国冬",
          "doc_count" : 1
        }
      ]
    }
  }
}


```



```shell
# 统计每个姓名下的平均年龄
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
    "aggs" : { 
        "all_name" : { 
            "terms" : { "field" : "name" }, 
            "aggs" : { 
                "avg_age" : { 
                    "avg" : { "field" : "age" } 
                } 
            } 
        } 
    } 
}'

# 结果
{
  "took" : 4,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 1.0,
    "hits" : [
    	{...},{...},...
    ]
  },
  "aggregations" : {
    "all_name" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "张飞",
          "doc_count" : 4,
          "avg_age" : {
            "value" : 35.5
          }
        },
        {
          "key" : "李元芳",
          "doc_count" : 3,
          "avg_age" : {
            "value" : 37.0
          }
        },
        {
          "key" : "刘飞",
          "doc_count" : 1,
          "avg_age" : {
            "value" : 26.0
          }
        },
        {
          "key" : "李国冬",
          "doc_count" : 1,
          "avg_age" : {
            "value" : 23.0
          }
        },
        {
          "key" : "胡景涛",
          "doc_count" : 1,
          "avg_age" : {
            "value" : 43.0
          }
        }
      ]
    }
  }
}


```



```shell
# 因为我们并不关心搜索结果，使用size": 0，它的速度更快。
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{
	"size": 0,
    "aggs" : { 
        "names" : { 
            "terms" : { 
              "field" : "name" 
            } 
        } 
    } 
}'

# 结果
{
  "took" : 9,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "names" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "张飞",
          "doc_count" : 4
        },
        {
          "key" : "李元芳",
          "doc_count" : 3
        },
        {
          "key" : "刘飞",
          "doc_count" : 1
        },
        {
          "key" : "李国冬",
          "doc_count" : 1
        },
        {
          "key" : "胡景涛",
          "doc_count" : 1
        }
      ]
    }
  }
}
```



```shell
# 多重聚合 统计每个姓名下的平均年龄
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
	"size": 0,
   "aggs": { 
      "names": { 
         "terms": { 
            "field": "name" 
         }, 
         "aggs": { 
            "avg_age": { 
               "avg": { 
                  "field": "age" 
               } 
            } 
         } 
      } 
   } 
}'

# 结果
{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "names" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "张飞",
          "doc_count" : 4,
          "avg_age" : {
            "value" : 34.5
          }
        },
        {
          "key" : "李元芳",
          "doc_count" : 3,
          "avg_age" : {
            "value" : 26.333333333333332
          }
        },
        {
          "key" : "刘飞",
          "doc_count" : 1,
          "avg_age" : {
            "value" : 26.0
          }
        },
        {
          "key" : "李国冬",
          "doc_count" : 1,
          "avg_age" : {
            "value" : 23.0
          }
        },
        {
          "key" : "胡景涛",
          "doc_count" : 1,
          "avg_age" : {
            "value" : 43.0
          }
        }
      ]
    }
  }
}
```







```shell
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
   "aggs": { 
      "names": { 
         "terms": { 
            "field": "name" 
         }, 
         "aggs": { 
            "avg_age": { 
               "avg": { 
                  "field": "age" 
               } 
            }, 
            "sexs": { 
                "terms": { 
                    "field": "sex" 
                }, 
                "aggs" : { 
                    "min_age" : { "min": { "field": "age"} }, 
                    "max_age" : { "max": { "field": "age"} } 
                }  
            }  
         } 
      } 
   },
   "size": 0
}'
```





#### Cardinality (排重)

cardinality的作用是先执行类似SQL中的distinct操作，然后再统计排重后集合长度。得到的结果是一个近似值，因为考虑到在大量分片中排重的性能损耗Cardinality算法并不会load所有的数据。

```shell
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{
	"size": 0,
	"aggs": {
		"name_count": {
			"cardinality": {
				"field": "name"
			}
		}
	}
}'

# 结果
{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "name_count" : {
      "value" : 5
    }
  }
}
```









#### Stats(最大，最小，平均，求和)

返回聚合分析后所有有关stat的指标。

```shell
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
	"size": 0,
   "aggs": { 
      "names": { 
         "terms": { 
            "field": "name" 
         }, 
         "aggs": { 
            "avg_stats": { 
               "stats": { 
                  "field": "age" 
               } 
            } 
         } 
      } 
   } 
}'

# 结果
{
  "took" : 3,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "names" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "张飞",
          "doc_count" : 4,
          "avg_stats" : {
            "count" : 4,
            "min" : 23.0,
            "max" : 66.0,
            "avg" : 34.5,
            "sum" : 138.0
          }
        },
        {
          "key" : "李元芳",
          "doc_count" : 3,
          "avg_stats" : {
            "count" : 3,
            "min" : 23.0,
            "max" : 33.0,
            "avg" : 26.333333333333332,
            "sum" : 79.0
          }
        },
        {
          "key" : "刘飞",
          "doc_count" : 1,
          "avg_stats" : {
            "count" : 1,
            "min" : 26.0,
            "max" : 26.0,
            "avg" : 26.0,
            "sum" : 26.0
          }
        },
        {
          "key" : "李国冬",
          "doc_count" : 1,
          "avg_stats" : {
            "count" : 1,
            "min" : 23.0,
            "max" : 23.0,
            "avg" : 23.0,
            "sum" : 23.0
          }
        },
        {
          "key" : "胡景涛",
          "doc_count" : 1,
          "avg_stats" : {
            "count" : 1,
            "min" : 43.0,
            "max" : 43.0,
            "avg" : 43.0,
            "sum" : 43.0
          }
        }
      ]
    }
  }
}
```



#### Extended Stats（标准差，方差）

返回聚合分析后所有指标，比Stats多三个统计结果：平方和、方差、标准差

```shell
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
	"size": 0,
   "aggs": { 
      "names": { 
         "terms": { 
            "field": "name" 
         }, 
         "aggs": { 
            "avg_stats": { 
               "extended_stats": { 
                  "field": "age" 
               } 
            } 
         } 
      } 
   } 
}'

# 结果
{
  "took" : 3,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "names" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "张飞",
          "doc_count" : 4,
          "avg_stats" : {
            "count" : 4,
            "min" : 23.0,
            "max" : 66.0,
            "avg" : 34.5,
            "sum" : 138.0,
            "sum_of_squares" : 6086.0,
            "variance" : 331.25,
            "std_deviation" : 18.200274723201296,
            "std_deviation_bounds" : {
              "upper" : 70.9005494464026,
              "lower" : -1.900549446402593
            }
          }
        },
        {
          "key" : "李元芳",
          "doc_count" : 3,
          "avg_stats" : {
            "count" : 3,
            "min" : 23.0,
            "max" : 33.0,
            "avg" : 26.333333333333332,
            "sum" : 79.0,
            "sum_of_squares" : 2147.0,
            "variance" : 22.22222222222217,
            "std_deviation" : 4.714045207910312,
            "std_deviation_bounds" : {
              "upper" : 35.761423749153955,
              "lower" : 16.90524291751271
            }
          }
        },
        {
          "key" : "刘飞",
          "doc_count" : 1,
          "avg_stats" : {
            "count" : 1,
            "min" : 26.0,
            "max" : 26.0,
            "avg" : 26.0,
            "sum" : 26.0,
            "sum_of_squares" : 676.0,
            "variance" : 0.0,
            "std_deviation" : 0.0,
            "std_deviation_bounds" : {
              "upper" : 26.0,
              "lower" : 26.0
            }
          }
        },
        {
          "key" : "李国冬",
          "doc_count" : 1,
          "avg_stats" : {
            "count" : 1,
            "min" : 23.0,
            "max" : 23.0,
            "avg" : 23.0,
            "sum" : 23.0,
            "sum_of_squares" : 529.0,
            "variance" : 0.0,
            "std_deviation" : 0.0,
            "std_deviation_bounds" : {
              "upper" : 23.0,
              "lower" : 23.0
            }
          }
        },
        {
          "key" : "胡景涛",
          "doc_count" : 1,
          "avg_stats" : {
            "count" : 1,
            "min" : 43.0,
            "max" : 43.0,
            "avg" : 43.0,
            "sum" : 43.0,
            "sum_of_squares" : 1849.0,
            "variance" : 0.0,
            "std_deviation" : 0.0,
            "std_deviation_bounds" : {
              "upper" : 43.0,
              "lower" : 43.0
            }
          }
        }
      ]
    }
  }
}

```



#### Percentiles

百分位法统计，举例，运维人员记录了每次启动系统所需要的时间，或者，网站记录了每次用户访问的页面加载时间，然后对这些时间数据进行百分位法统计。我们在测试报告中经常会看到类似的统计数据

```shell
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
   "size": 0,
   "aggs": { 
      "age_outlier": {
			"percentiles": {
				"field": "age"
			}
		}
   } 
}'

# 结果
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "age_outlier" : {
      "values" : {
        "1.0" : 23.0,
        "5.0" : 23.0,
        "25.0" : 23.0,
        "50.0" : 24.5,
        "75.0" : 31.25,
        "95.0" : 55.64999999999997,
        "99.0" : 63.93000000000001
      }
    }
  }
}


#################################
# 指定百分位的指标，比如只想统计95%、99%、99.9%的年龄
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
   "size": 0,
   "aggs": { 
      "age_outlier": {
			"percentiles": {
				"field": "age",
				"percents": [95, 99, 99.9]
			}
		}
   } 
}'
# 结果
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "age_outlier" : {
      "values" : {
        "95.0" : 55.64999999999997,
        "99.0" : 63.93000000000001,
        "99.9" : 65.79300000000003
      }
    }
  }
}

```



#### Percentile Ranks

Percentile API中，返回结果values中的key是固定的0-100间的值，而Percentile Ranks返回值中的value才是固定的，同样也是0到100。例如，我想知道加载时间是15ms与30ms的数据，在所有记录中处于什么水平，以这种方式反映数据在集合中的排名情况。

```shell
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
   "size": 0,
   "aggs": { 
      "age_outlier": {
			"percentile_ranks": {
				"field": "age",
				 "values": [30, 50]
			}
		}
   } 
}'

# 结果
{
  "took" : 2,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "age_outlier" : {
      "values" : {
        "30.0" : 70.58823529411764,
        "50.0" : 78.04347826086956
      }
    }
  }
}

```

#### Terms聚合

```shell
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
   "size": 0,
   "aggs": { 
      "names": {
			"terms": {
				"field": "name",
				"size":2
			}
		}
   } 
}'

# 结果
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 10,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "names" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 3,
      "buckets" : [
        {
          "key" : "张飞",
          "doc_count" : 4
        },
        {
          "key" : "李元芳",
          "doc_count" : 3
        }
      ]
    }
  }
}
```





#### 数据的不确定性

使用terms聚合，结果可能带有一定的偏差与错误性。

比如：

我们想要获取name字段中出现频率最高的前5个。

此时，客户端向ES发送聚合请求，主节点接收到请求后，会向每个独立的分片发送该请求。
分片独立的计算自己分片上的前5个name，然后返回。当所有的分片结果都返回后，在主节点进行结果的合并，再求出频率最高的前5个，返回给客户端。

这样就会造成一定的误差，比如最后返回的前5个中，有一个叫A的，有50个文档；B有49。 但是由于每个分片独立的保存信息，信息的分布也是不确定的。 有可能第一个分片中B的信息有2个，但是没有排到前5，所以没有在最后合并的结果中出现。 这就导致B的总数少计算了2，本来可能排到第一位，却排到了A的后面。



#### size与shard_size

为了改善上面的问题，就可以使用size和shard_size参数。

- size参数规定了最后返回的term个数(默认是10个)
- shard_size参数规定了每个分片上返回的个数
- 如果shard_size小于size，那么分片也会按照size指定的个数计算

**通过这两个参数，如果我们想要返回前5个，size=5;shard_size可以设置大于5，这样每个分片返回的词条信息就会增多，相应的误差几率也会减小。**



#### order排序

order指定了最后返回结果的排序方式，默认是按照doc_count排序。

```shell
# 按文档数升序
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
   "size": 0,
   "aggs": { 
      "names": {
			"terms": {
				"field": "name",
				"order": { "_count": "asc" }
			}
		}
   } 
}'

# 结果
{
  "took" : 5,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 11,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "names" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "33dongdong3",
          "doc_count" : 1
        },
        {
          "key" : "刘飞",
          "doc_count" : 1
        },
        {
          "key" : "李国冬",
          "doc_count" : 1
        },
        {
          "key" : "胡景涛",
          "doc_count" : 1
        },
        {
          "key" : "李元芳",
          "doc_count" : 3
        },
        {
          "key" : "张飞",
          "doc_count" : 4
        }
      ]
    }
  }
}

###################
# 按照字典方式排序
curl -XGET  'http://10.250.140.14:9200/user_info_v1/_search?pretty' -d '{ 
   "size": 0,
   "aggs": { 
      "names": {
			"terms": {
				"field": "name",
				"order": { "_term": "asc" }
			}
		}
   } 
}'

# 结果
{
  "took" : 1,
  "timed_out" : false,
  "_shards" : {
    "total" : 3,
    "successful" : 3,
    "failed" : 0
  },
  "hits" : {
    "total" : 11,
    "max_score" : 0.0,
    "hits" : [ ]
  },
  "aggregations" : {
    "names" : {
      "doc_count_error_upper_bound" : 0,
      "sum_other_doc_count" : 0,
      "buckets" : [
        {
          "key" : "33dongdong3",
          "doc_count" : 1
        },
        {
          "key" : "刘飞",
          "doc_count" : 1
        },
        {
          "key" : "张飞",
          "doc_count" : 4
        },
        {
          "key" : "李元芳",
          "doc_count" : 3
        },
        {
          "key" : "李国冬",
          "doc_count" : 1
        },
        {
          "key" : "胡景涛",
          "doc_count" : 1
        }
      ]
    }
  }
}
```





### 集群管理

#### 集群和节点信息

```shell
# GET /_cluster/health?pretty
curl -XGET 'http://10.250.140.14:9200/_cluster/health?pretty'

{
  "cluster_name" : "es-cluster",
  "status" : "yellow",
  "timed_out" : false,
  "number_of_nodes" : 1,
  "number_of_data_nodes" : 1,
  "active_primary_shards" : 34,
  "active_shards" : 34,
  "relocating_shards" : 0,
  "initializing_shards" : 0,
  "unassigned_shards" : 34,
  "delayed_unassigned_shards" : 0,
  "number_of_pending_tasks" : 0,
  "number_of_in_flight_fetch" : 0,
  "task_max_waiting_in_queue_millis" : 0,
  "active_shards_percent_as_number" : 50.0
}



# GET /_cluster/health?wait_for_status=yellow&timeout=50s
curl -XGET 'http://10.250.140.14:9200/_cluster/health?wait_for_status=red&timeout=10s&pretty=true'

curl -XGET 'http://10.250.140.14:9200/_cluster/health?wait_for_status=green&timeout=10s&pretty=true'
{
  "cluster_name" : "es-cluster",
  "status" : "yellow",
  "timed_out" : true,
  "number_of_nodes" : 1,
  "number_of_data_nodes" : 1,
  "active_primary_shards" : 34,
  "active_shards" : 34,
  "relocating_shards" : 0,
  "initializing_shards" : 0,
  "unassigned_shards" : 34,
  "delayed_unassigned_shards" : 0,
  "number_of_pending_tasks" : 0,
  "number_of_in_flight_fetch" : 0,
  "task_max_waiting_in_queue_millis" : 0,
  "active_shards_percent_as_number" : 50.0
}

# 一个或多个索引
curl -XGET 'http://10.250.140.14:9200/_cluster/health/user_info_v1,user_manage_v1?pretty'


# GET /_cluster/state
curl -XGET 'http://10.250.140.14:9200/_cluster/state?pretty'

# GET /_cluster/stats?human&pretty
curl -XGET 'http://10.250.140.14:9200/_cluster/stats?human&pretty'

# GET /_cluster/pending_tasks
curl -XGET 'http://10.250.140.14:9200/_cluster/pending_tasks?pretty'

# 元数据和路由表
curl -XGET 'http://10.250.140.14:9200/_cluster/state/metadata,routing_table/user_manage_v1?pretty'


# GET /_nodes
curl -XGET 'http://10.250.140.14:9200/_nodes?pretty'

# GET /_nodes/stats
curl -XGET 'http://10.250.140.14:9200/_nodes/stats?pretty'

# GET /_nodes/nodeId1,nodeId2/stats
curl -XGET 'http://10.250.140.14:9200/_nodes/node-1/stats?pretty'

```



#### 嗅探功能

通过client.transport.sniff启动嗅探功能，这样只需要指定集群中的某一个节点(不一定是主节点)，然后会加载集群中的其他节点，这样只要程序不停即使此节点宕机仍然可以连接到其他节点。



#### 分页压力

我们通过curl和java查询时都可以指定分页，但是页数越往后服务器的压力会越大。大多数搜索引擎都不会提供非常大的页数搜索，原因有两个一是用户习惯一般不会看页数大的搜索结果因为越往后越不准确，二是服务器压力。

比如分片是5分页单位是10查询第10000到10010条记录，es需要在所有分片上进行查询，每个分片会产生10010条排序后的数据然后返回给主节点，主节点接收5个分片的数据一共是50050条然后再进行汇总最后再取其中的10000到10010条数据返回给客户端，这样一来看似只请求了10条数据但实际上es要汇总5万多条数据，所以页码越大服务器的压力就越大。

#### 超时timeout

查询时如果数据量很大，可以指定超时时间即到达此时间后无论查询的结果是什么都会返回并且关闭连接，这样用户体验较好缺点是查询出的数据可能不完整，Java和curl都可以指定超时时间。



### 插件安装

```shell
后台启动ES：
bin/elasticsearch  -d 
bin/elasticsearch  -d  -p /home/hadoop/data/es/pid


后台启动Kibana：
nohup bin/kibana &
bin/kibana -l /home/lgd/install/kibana-5.5.2-linux-x86_64/kibana.log & 



Elasticsearch的默认账户为elastic,默认密码为changme

curl -XPUT -u elastic 'http://localhost:9200/_xpack/security/user/elastic/_password' -d '{
  "password" : "yourpasswd"
}'

curl -XPUT -u elastic 'http://localhost:9200/_xpack/security/user/kibana/_password' -d '{
  "password" : "yourpasswd"
}'
```



#### IK



####murmur3



#### marvel

```shell
bin/elasticsearch-plugin install x-pack

# vim config/elasticsearch.yml
action.auto_create_index: .security,.monitoring*,.watches,.triggered_watches,.watcher-history*

bin/elasticsearch



bin/kibana-plugin install x-pack

bin/kibana

用户名和密码：elastic/changeme

es: http://localhost:9200/
kibana: http://localhost:5601/
```



#### x-pack

Marvel插件：在簇中从每个节点汇集数据。这个插件必须每个节点都得安装。 Marvel是Elasticsearch的管理和监控工具，在开发环境下免费使用。它包含了Sense。
Sense：交互式控制台，使用户方便的通过浏览器直接与Elasticsearch进行交互。
Head：在学习Elasticsearch的过程中，必不可少需要通过一些工具查看es的运行状态以及数据。如果都是通过rest请求，未免太过麻烦，而且也不够人性化。此时，Head插件可以实现基本信息的查看，rest请求的模拟，数据的检索等等。





**启用和禁用X-Pack功能**

默认情况下，所有X-Pack功能都已启用。 可以在elasticsearch.yml，kibana.yml和logstash.yml配置文件中启用或禁用特定的X-Pack功能

```shell
#设置                  #描述
xpack.graph.enabled    #设置为false以禁用X-Pack图形功能。
xpack.ml.enabled       #设置为false以禁用X-Pack机器学习功能。
xpack.monitoring.enabled  #设置为false以禁用X-Pack监视功能。
xpack.reporting.enabled   #设置为false以禁用X-Pack报告功能。
xpack.security.enabled    #设置为false以禁用X-Pack安全功能。
xpack.watcher.enabled    #设置为false以禁用Watcher。
```



**内置角色**


```shell
ingest_admin  #授予访问权限以管理所有索引模板和所有摄取管道配置。这个角色不能提供创建索引的能力; 这些特权必须在一个单独的角色中定义。
kibana_dashboard_only_user  #授予对Kibana仪表板的访问权限以及对.kibana索引的只读权限。 这个角色无法访问Kibana中的编辑工具。
kibana_system  #授予Kibana系统用户读取和写入Kibana索引所需的访问权限，管理索引模板并检查Elasticsearch集群的可用性。 此角色授予对.monitoring- *索引的读取访问权限以及对.reporting- *索引的读取和写入访问权限。
kibana_user   #授予Kibana用户所需的最低权限。 此角色授予访问集群的Kibana索引和授予监视权限。
logstash_admin  #授予访问用于管理配置的.logstash *索引的权限。
logstash_system  #授予Logstash系统用户所需的访问权限，以将系统级别的数据（如监视）发送给Elasticsearch。不应将此角色分配给用户，因为授予的权限可能会在不同版本之间发生变化。此角色不提供对logstash索引的访问权限，不适合在Logstash管道中使用。
machine_learning_admin  #授予manage_ml群集权限并读取.ml- *索引的访问权限。
machine_learning_user   #授予查看X-Pack机器学习配置，状态和结果所需的最低权限。此角色授予monitor_ml集群特权，并可以读取.ml-notifications和.ml-anomalies *索引，以存储机器学习结果。
monitoring_user  #授予除使用Kibana所需的X-Pack监视用户以外的任何用户所需的最低权限。 这个角色允许访问监控指标。 监控用户也应该分配kibana_user角色。
remote_monitoring_agent  #授予远程监视代理程序将数据写入此群集所需的最低权限。
reporting_user  #授予使用Kibana所需的X-Pack报告用户所需的特定权限。 这个角色允许访问报告指数。 还应该为报告用户分配kibana_user角色和一个授予他们访问将用于生成报告的数据的角色。
superuser   #授予对群集的完全访问权限，包括所有索引和数据。 具有超级用户角色的用户还可以管理用户和角色，并模拟系统中的任何其他用户。 由于此角色的宽容性质，在将其分配给用户时要格外小心。
transport_client  #通过Java传输客户端授予访问集群所需的权限。 Java传输客户端使用节点活性API和群集状态API（当启用嗅探时）获取有关群集中节点的信息。 如果他们使用传输客户端，请为您的用户分配此角色。
watcher_admin  #授予对.watches索引的写入权限，读取对监视历史记录的访问权限和触发的监视索引，并允许执行所有监视器操作。
watcher_user  #授予读取.watches索引，获取观看动作和观察者统计信息的权限。
```





### 参考文档

**Elasticsearch官方文档：**<https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html>

**Elasticsearch官方Java文档：**<https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html>

**Elasticsearch 服务配置属性：**<https://www.ibm.com/support/knowledgecenter/zh/SSFPJS_8.5.6/com.ibm.wbpm.main.doc/topics/rfps_esearch_configoptions.html>

**elasticsearch 查询数据 | 分页查询(java)：** <http://www.sojson.com/blog/90.html>

**Elasticsearch: 权威指南：**<https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html>

