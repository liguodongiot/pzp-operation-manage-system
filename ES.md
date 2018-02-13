

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







### 参考文档

**Elasticsearch官方文档：**<https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html>

**Elasticsearch官方Java文档：**<https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html>

**Elasticsearch 服务配置属性：**<https://www.ibm.com/support/knowledgecenter/zh/SSFPJS_8.5.6/com.ibm.wbpm.main.doc/topics/rfps_esearch_configoptions.html>

**elasticsearch 查询数据 | 分页查询(java)** <http://www.sojson.com/blog/90.html>

