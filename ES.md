

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




```



### 参考文档

**Elasticsearch官方文档：**<https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html>

**Elasticsearch官方Java文档：**<https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/index.html>