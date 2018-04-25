## [地理位置](https://www.elastic.co/guide/cn/elasticsearch/guide/current/geoloc.html)







## [管理、监控和部署](https://www.elastic.co/guide/cn/elasticsearch/guide/current/administration.html)

### [监控](https://www.elastic.co/guide/cn/elasticsearch/guide/current/cluster-admin.html)

#### [Marvel 监控](https://www.elastic.co/guide/cn/elasticsearch/guide/current/marvel.html)

#### [集群健康](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_cluster_health.html) 

```shell
curl -XGET "http://10.250.140.14:9200/_cluster/health"

# 集群信息里添加一个索引清单
curl -XGET "http://10.250.140.14:9200/_cluster/health?level=indices"

# shards 选项会提供一个详细得多的输出，列出每个索引里每个分片的状态和位置。
curl -XGET "http://10.250.140.14:9200/_cluster/health?level=shards"
# 阻塞等待状态变化编辑
curl -XGET "http://10.250.140.14:9200/_cluster/health?wait_for_status=yellow"
```



#### [监控单个节点](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_monitoring_individual_nodes.html)

```shell
# 节点统计值
curl -XGET "http://10.250.140.14:9200/_nodes/stats"

```



**索引(indices)部分**

```json
"indices": {
	"docs": {
	  "count": 589845,  //节点内存有多少文档
	  "deleted": 19447  //还没有从段里清除的已删除文档数量
	},
	"store": {
	  "size_in_bytes": 404392698, 	//节点耗用了多少物理存储。这个指标包括主分片和副本分片在内
	  "throttle_time_in_millis": 0 	//限流时间很大，那可能表明你的磁盘限流设置得过低
	},
	"indexing": { 	//已经索引了多少文档,这个值是一个累加计数器。在文档被删除的时候，数值不会下降。
					//还要注意的是，在发生内部 索引 操作的时候，这个值也会增加，比如说文档更新。
	  "index_total": 2360602,
	  "index_time_in_millis": 440766, //索引操作耗费的时间
	  "index_current": 0,  //正在索引的文档数量
	  "index_failed": 0,
	  "delete_total": 5336, //删除操作
	  "delete_time_in_millis": 326,
	  "delete_current": 0,
	  "noop_update_total": 0,
	  "is_throttled": false,
	  "throttle_time_in_millis": 0
	},
	"get": { //通过 ID 获取文档的接口相关的统计值。包括对单个文档的 GET 和 HEAD 请求。
	  "total": 37217,
	  "time_in_millis": 2498,
	  "exists_total": 37118,
	  "exists_time_in_millis": 2495,
	  "missing_total": 99,
	  "missing_time_in_millis": 3,
	  "current": 0
	},
	"search": { //在活跃中的搜索（ open_contexts ）数量、查询的总数量、
				//以及自节点启动以来在查询上消耗的总时间。用 query_time_in_millis / query_total 计算的比值，
				//可以用来粗略的评价你的查询有多高效。比值越大，每个查询花费的时间越多
	  "open_contexts": 0,
	  "query_total": 75678,
	  "query_time_in_millis": 8489,
	  "query_current": 0,
	  "fetch_total": 70097, //查询处理的后一半流程（query-then-fetch 里的 fetch ）。
							//如果 fetch 耗时比 query 还多，说明磁盘较慢，或者获取了太多文档，
							//或者可能搜索请求设置了太大的分页（比如， size: 10000 ）。
	  "fetch_time_in_millis": 2571,
	  "fetch_current": 0,
	  "scroll_total": 7726,
	  "scroll_time_in_millis": 2604,
	  "scroll_current": 0,
	  "suggest_total": 0,
	  "suggest_time_in_millis": 0,
	  "suggest_current": 0
	},
	"merges": { //Lucene 段合并相关的信息。它会告诉你目前在运行几个合并，合并涉及的文档数量，
				//正在合并的段的总大小，以及在合并操作上消耗的总时间。
				//在你的集群写入压力很大时，合并统计值非常重要。
				//合并要消耗大量的磁盘 I/O 和 CPU 资源。
				//如果你的索引有大量的写入，同时又发现大量的合并数.
				//注意：文档更新和删除也会导致大量的合并数，因为它们会产生最终需要被合并的段 碎片 。
	  "current": 0,
	  "current_docs": 0,
	  "current_size_in_bytes": 0,
	  "total": 2115,
	  "total_time_in_millis": 836818,
	  "total_docs": 10260577,
	  "total_size_in_bytes": 5951942556,
	  "total_stopped_time_in_millis": 0,
	  "total_throttled_time_in_millis": 415,
	  "total_auto_throttle_in_bytes": 2680541556
	},
	"refresh": {
	  "total": 22778,
	  "total_time_in_millis": 360677,
	  "listeners": 0
	},
	"flush": {
	  "total": 124,
	  "total_time_in_millis": 746
	},
	"warmer": {
	  "current": 0,
	  "total": 22908,
	  "total_time_in_millis": 6705
	},
	"query_cache": {
	  "memory_size_in_bytes": 130079,
	  "total_count": 18897,
	  "hit_count": 17545,
	  "miss_count": 1352,
	  "cache_size": 40,
	  "cache_count": 419,
	  "evictions": 379
	},
	"fielddata": {
	  "memory_size_in_bytes": 1416,
	  "evictions": 0
	},
	"completion": {
	  "size_in_bytes": 0
	},
	"segments": { 	//这个节点目前正在服务中的 Lucene 段的数量。
					//这是一个重要的数字。大多数索引会有大概 50–150 个段，
					//哪怕它们存有 TB 级别的数十亿条文档。
					//段数量过大表明合并出现了问题（比如，合并速度跟不上段的创建）。
					//注意这个统计值是节点上所有索引的汇聚总数。
	  "count": 152,
	  "memory_in_bytes": 2588220,
	  "terms_memory_in_bytes": 1219255,
	  "stored_fields_memory_in_bytes": 100192,
	  "term_vectors_memory_in_bytes": 0,
	  "norms_memory_in_bytes": 37504,
	  "points_memory_in_bytes": 147925,
	  "doc_values_memory_in_bytes": 1083344,
	  "index_writer_memory_in_bytes": 13119602,
	  "version_map_memory_in_bytes": 1296298,
	  "fixed_bit_set_memory_in_bytes": 2288,
	  "max_unsafe_auto_id_timestamp": -1,
	  "file_sizes": {}
	},
	"translog": {
	  "operations": 726339,
	  "size_in_bytes": 467955330
	},
	"request_cache": {
	  "memory_size_in_bytes": 4258,
	  "evictions": 0,
	  "hit_count": 23545,
	  "miss_count": 14
	},
	"recovery": {
	  "current_as_source": 0,
	  "current_as_target": 0,
	  "throttle_time_in_millis": 0
	}
}
```



**JVM部分**

```shell
"jvm": {
	"timestamp": 1523601257737,
	"uptime_in_millis": 80099642,
	"mem": {
	  "heap_used_in_bytes": 667677744, //有多少 heap 被使用了
	  "heap_used_percent": 32, 	//Elasticsearch 被配置为当 heap 达到 75% 的时候开始 GC。
								//如果你的节点一直 >= 75%，你的节点正处于 内存压力 状态。
								//这是个危险信号，不远的未来可能就有慢 GC 要出现了。
								//如果 heap 使用率一直 >=85%，你就麻烦了。Heap 在 90–95% 之间，
								//则面临可怕的性能风险，此时最好的情况是长达 10–30s 的 GC，
								//最差的情况就是内存溢出（OOM）异常。
	  "heap_committed_in_bytes": 2077753344,  //多少被指派了（当前被分配给进程的）
	  "heap_max_in_bytes": 2077753344, //heap 被允许分配的最大值
	  "non_heap_used_in_bytes": 158566152,
	  "non_heap_committed_in_bytes": 167346176,
	  "pools": {
		"young": {
		  "used_in_bytes": 73469112,
		  "max_in_bytes": 558432256,
		  "peak_used_in_bytes": 558432256,
		  "peak_max_in_bytes": 558432256
		},
		"survivor": {
		  "used_in_bytes": 11587168,
		  "max_in_bytes": 69730304,
		  "peak_used_in_bytes": 69730304,
		  "peak_max_in_bytes": 69730304
		},
		"old": {
		  "used_in_bytes": 582621464,
		  "max_in_bytes": 1449590784,
		  "peak_used_in_bytes": 582621464,
		  "peak_max_in_bytes": 1449590784
		}
	  }
	},
	"threads": {
	  "count": 128,
	  "peak_count": 131
	},
	"gc": { //gc 部分显示新生代和老生代的垃圾回收次数和累积时间。
			//大多数时候你可以忽略掉新生代的次数：这个数字通常都很大。这是正常的。
			//老生代的次数应该很小，而且 collection_time_in_millis 也应该很小。
			//这些是累积值，所以很难给出一个阈值表示你要开始操心了
	  "collectors": {
		"young": {
		  "collection_count": 1648,
		  "collection_time_in_millis": 18468
		},
		"old": {
		  "collection_count": 1,
		  "collection_time_in_millis": 58
		}
	  }
	},
	"buffer_pools": {
	  "direct": {
		"count": 81,
		"used_in_bytes": 269739275,
		"total_capacity_in_bytes": 269739274
	  },
	  "mapped": {
		"count": 266,
		"used_in_bytes": 402695060,
		"total_capacity_in_bytes": 402695060
	  }
	},
	"classes": {
	  "current_loaded_count": 14792,
	  "total_loaded_count": 14792,
	  "total_unloaded_count": 0
	}
}
```



**线程池部分**

```shell
"thread_pool": {
	"bulk": { //批量请求，和单条的索引请求不同的线程池
	  "threads": 8, //已配置的线程数量（ threads ），
	  "queue": 0, 	//在队列中等待处理的任务单元数量（ queue ）。
	  "active": 0, 	//当前在处理任务的线程数量（ active ），
	  "rejected": 0,	//如果队列中任务单元数达到了极限，新的任务单元会开始被拒绝，
						//你会在 rejected 统计值上看到它反映出来。这通常是你的集群在某些资源上碰到瓶颈的信号。
						//因为队列满意味着你的节点或集群在用最高速度运行，但依然跟不上工作的蜂拥而入。
	  "largest": 8,
	  "completed": 50607
	},
	"fetch_shard_started": {
	  "threads": 1,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 16,
	  "completed": 124
	},
	"fetch_shard_store": {
	  "threads": 0,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 0,
	  "completed": 0
	},
	"flush": {
	  "threads": 1,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 4,
	  "completed": 247
	},
	"force_merge": {
	  "threads": 0,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 0,
	  "completed": 0
	},
	"generic": {
	  "threads": 5,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 5,
	  "completed": 104242
	},
	"get": { //Get-by-ID 操作
	  "threads": 8,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 8,
	  "completed": 31881
	},
	"index": { //普通的索引请求的线程池
	  "threads": 8,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 8,
	  "completed": 5336
	},
	"listener": {
	  "threads": 0,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 0,
	  "completed": 0
	},
	"management": {
	  "threads": 4,
	  "queue": 0,
	  "active": 1,
	  "rejected": 0,
	  "largest": 4,
	  "completed": 201432
	},
	"ml_autodetect": {
	  "threads": 0,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 0,
	  "completed": 0
	},
	"ml_datafeed": {
	  "threads": 0,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 0,
	  "completed": 0
	},
	"ml_utility": {
	  "threads": 1,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 1,
	  "completed": 1
	},
	"refresh": {
	  "threads": 4,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 4,
	  "completed": 3110228
	},
	"search": { //所有的搜索和查询请求
	  "threads": 13,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 13,
	  "completed": 148834
	},
	"security-token-key": {
	  "threads": 0,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 0,
	  "completed": 0
	},
	"snapshot": {
	  "threads": 0,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 0,
	  "completed": 0
	},
	"warmer": {
	  "threads": 4,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 4,
	  "completed": 32843
	},
	"watcher": {
	  "threads": 40,
	  "queue": 0,
	  "active": 0,
	  "rejected": 0,
	  "largest": 40,
	  "completed": 5336
	}
}

```

**文件系统和网络部分**

```shell
"fs": {
	"timestamp": 1523601257738,
	"total": {
	  "total_in_bytes": 211242950656,
	  "free_in_bytes": 148575768576,
	  "available_in_bytes": 137821626368,
	  "spins": "true"
	},
	"data": [
	  {
		"path": "/home/lgd/install/elasticsearch-5.5.2/data/nodes/0",
		"mount": "/home (/dev/vdb1)",
		"type": "ext4",
		"total_in_bytes": 211242950656,
		"free_in_bytes": 148575768576,
		"available_in_bytes": 137821626368,
		"spins": "true"
	  }
	],
	"io_stats": {
	  "devices": [
		{
		  "device_name": "vdb1",
		  "operations": 377770,
		  "read_operations": 16,
		  "write_operations": 377754,
		  "read_kilobytes": 104,
		  "write_kilobytes": 11102676
		}
	  ],
	  "total": {
		"operations": 377770,
		"read_operations": 16,
		"write_operations": 377754,
		"read_kilobytes": 104,
		"write_kilobytes": 11102676
	  }
	}
},
"transport": {	//transport 显示和 传输地址 相关的一些基础统计值。
				//包括节点间的通信（通常是 9300 端口）以及任意传输客户端或者节点客户端的连接。
				//如果看到这里有很多连接数不要担心；Elasticsearch 在节点之间维护了大量的连接。
	"server_open": 0,
	"rx_count": 0,
	"rx_size_in_bytes": 0,
	"tx_count": 0,
	"tx_size_in_bytes": 0
},
"http": { 	//显示 HTTP 端口（通常是 9200）的统计值。如果你看到 total_opened 数很大而且还在一直上涨，
			//这是一个明确信号，说明你的 HTTP 客户端里有没启用 keep-alive 长连接的。
			//持续的 keep-alive 长连接对性能很重要，因为连接、
			//断开套接字是很昂贵的（而且浪费文件描述符）。请确认你的客户端都配置正确。
	"current_open": 13,
	"total_opened": 2838
}
```

**断路器**

```json
"breakers": {
	"request": {
	  "limit_size_in_bytes": 1246652006,
	  "limit_size": "1.1gb",
	  "estimated_size_in_bytes": 0,
	  "estimated_size": "0b",
	  "overhead": 1,
	  "tripped": 0
	},
	"fielddata": {
	  "limit_size_in_bytes": 1246652006, //断路器的最大值
	  "limit_size": "1.1gb",
	  "estimated_size_in_bytes": 1416,
	  "estimated_size": "1.3kb",
	  "overhead": 1.03,
	  "tripped": 0 		//如果这个数字很大或者持续上涨，这是一个信号，
						//说明你的请求需要优化，或者你需要添加更多内存
						//（单机上添加，或者通过添加新节点的方式）
	},
	"in_flight_requests": {
	  "limit_size_in_bytes": 2077753344,
	  "limit_size": "1.9gb",
	  "estimated_size_in_bytes": 0,
	  "estimated_size": "0b",
	  "overhead": 1,
	  "tripped": 0
	},
	"parent": {
	  "limit_size_in_bytes": 1454427340,
	  "limit_size": "1.3gb",
	  "estimated_size_in_bytes": 1416,
	  "estimated_size": "1.3kb",
	  "overhead": 1,
	  "tripped": 0
	}
}
```



#### [集群统计](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_cluster_stats.html)

```shell
curl -XGET "http://10.250.140.14:9200/_cluster/stats"
```



#### [索引统计](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_index_stats.html)

```shell
# 统计 my_index 索引
curl -XGET "http://10.250.140.14:9200/my_index/_stats"

# 使用逗号分隔索引名可以请求多个索引统计值。
curl -XGET "http://10.250.140.14:9200/my_index,my_index_v2/_stats"

# 使用特定的 _all 可以请求全部索引的统计值
curl -XGET "http://10.250.140.14:9200/_all/_stats"
```





#### [等待中的任务](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_pending_tasks.html)

```shell
curl -XGET "http://10.250.140.14:9200/_cluster/pending_tasks"
```



#### [cat API](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_cat_api.html)

```shell
curl -XGET "http://10.250.140.14:9200/_cat"

# 健康检查 
curl -XGET "http://10.250.140.14:9200/_cat/health"

# 启动表头
curl -XGET "http://10.250.140.14:9200/_cat/health?v"

# 节点统计
curl -XGET "http://10.250.140.14:9200/_cat/nodes?v"

# 对任意 API 添加 ?help 参数来做到这点
curl -XGET "http://10.250.140.14:9200/_cat/nodes?help"

# 用 ?h 参数来明确指定显示这些指标
curl -XGET "http://10.250.140.14:9200/_cat/nodes?v&h=ip,port,heapPercent,heapMax"

# 因为 cat API 试图像 *nix 工具一样工作，你可以使用管道命令将结果传递给其他工具，
# 比如 sort 、 grep 或者 awk 。
curl -XGET "http://10.250.140.14:9200/_cat/indices?bytes=b"


curl -XGET -u liguodong:liguodong "http://10.250.140.14:9200/_cat/indices?bytes=b" | sort -rnk8

# 排除marvel索引
curl -XGET -u liguodong:liguodong "http://10.250.140.14:9200/_cat/indices?bytes=b" | sort -rnk8| grep -v marvel
```



#### [硬件](https://www.elastic.co/guide/cn/elasticsearch/guide/current/hardware.html)



内存

64 GB 内存的机器是非常理想的， 但是32 GB 和16 GB 机器也是很常见的。少于8 GB 会适得其反（你最终需要很多很多的小机器），大于64 GB 的机器也会有问题。



CPUs

常见的集群使用两到八个核的机器。

多个内核提供的额外并发远胜过稍微快一点点的时钟频率。



硬盘

 基于 SSD 的节点，查询和索引性能都有提升。如果你负担得起，SSD 是一个好的选择。

网络

快速可靠的网络显然对分布式系统的性能是很重要的 。 低延时能帮助确保节点间能容易的通讯，大带宽能帮助分片移动和恢复。现代数据中心网络（1 GbE, 10 GbE）对绝大多数集群都是足够的。

即使数据中心们近在咫尺，也要避免集群跨越多个数据中心。绝对要避免集群跨越大的地理距离。



#### [Java 虚拟机](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_java_virtual_machine.html)





#### [Transport Client 与 Node Client](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_transport_client_versus_node_client.html)

#### [配置管理](https://www.elastic.co/guide/cn/elasticsearch/guide/current/_configuration_management.html)

配置管理工具（ Puppet，Chef，Ansible）



#### [重要配置的修改](https://www.elastic.co/guide/cn/elasticsearch/guide/current/important-configuration-changes.html)

```shell
curl -XPUT "http://10.250.140.14:9200/_cluster/settings" -H 'Content-Type: application/json' -d'
{
    "persistent" : {
        "discovery.zen.minimum_master_nodes" : 2
    }
}'


gateway.recover_after_nodes: 8
gateway.expected_nodes: 10
gateway.recover_after_time: 5m
Elasticsearch 会采取如下操作：
等待集群至少存在 8 个节点
等待 5 分钟，或者10 个节点上线后，才进行数据恢复，这取决于哪个条件先达到。

注意：这些配置只能设置在 config/elasticsearch.yml 文件中或者是在命令行里（它们不能动态更新）它们只在整个集群重启的时候有实质性作用。


你的单播列表不需要包含你的集群中的所有节点， 它只是需要足够的节点，当一个新节点联系上其中一个并且说上话就可以了。如果你使用 master 候选节点作为单播列表，你只要列出三个就可以了。 这个配置在 elasticsearch.yml 文件中：
discovery.zen.ping.unicast.hosts: ["host1", "host2:port"]
```



#### [不要触碰这些配置！](https://www.elastic.co/guide/cn/elasticsearch/guide/current/dont-touch-these-settings.html)

垃圾收集器

线程池



#### [堆内存:大小和交换](https://www.elastic.co/guide/cn/elasticsearch/guide/current/heap-sizing.html)

```shell
export ES_HEAP_SIZE=10g
./bin/elasticsearch -Xmx10g -Xms10g 
```







### [部署](https://www.elastic.co/guide/cn/elasticsearch/guide/current/deploy.html)











### [部署后](https://www.elastic.co/guide/cn/elasticsearch/guide/current/post_deploy.html)









**Elasticsearch: 权威指南：**<https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html>





































































**Elasticsearch: 权威指南：**<https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html>