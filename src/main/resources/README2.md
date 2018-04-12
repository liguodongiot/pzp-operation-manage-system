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







### [部署](https://www.elastic.co/guide/cn/elasticsearch/guide/current/deploy.html)











### [部署后](https://www.elastic.co/guide/cn/elasticsearch/guide/current/post_deploy.html)















































































**Elasticsearch: 权威指南：**<https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html>