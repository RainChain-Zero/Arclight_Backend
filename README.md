# Arclight

```text
    本手册是 Arclight 的接口文档，本计划旨在以骰娘为载体实现在一定范围内的跑团信息共享，例如开团时间、主持人、模组详细信息等，方便玩家寻找合适的团加入，一定程度上解决“无团可跑”“信息更新不及时”等问题，其中所有骰娘连接在组成一幅繁星绘卷，绽放出闪耀的弧光（Arclight）。
    Dice!下的骰娘可通过嵌入 Lua 脚本中提供的http 函数来非常简单地完成请求，本文档中的接口在设计时也参考了Dice!嵌入Lua 脚本开发中的情况。
    需要说明的是，本项目不会收取您的任何费用，一切使用均遵循自愿原则，不得滥用或将此项目用于商业行为，若发现有恶意刷接口、上传不良信息等攻击性行为，将封禁 api key 并列入黑名单，不排除直接跑路的可能性，希望大家都对世界保持一份善意×
    上传新团或修改团信息均会采用腾讯内容安全API 进行审核，审核未通过将删除有关信息，敬请谅解。
```

目录:

+ **[注册](#注册)**
+ **[找回](#找回)**
+ **[查询](#查询)**
+ **[增加](#增加)**
+ **[更改](#更改)**
+ **[删除](#删除)**

## 状态

所有 `API` 返回的数据包含一个 `succ` 字段的状态,代表操作是否成功,同时会根据具体错误信息返回`err_msg`字段，如无错误则`err_msg`为null

| 状态 | 原因                                |
| ------ | ----------------------------------- |
| true          | 操作成功                       |
| false         | 操作失败                      |

------

## 注册

### url

[Post] <http://arclight.space:45445/register>

#### 请求

```json5
{
    "qq":114514,  
    "password":"1234567" 
 }
```

| 参数 | 类型 | 必须 | 说明                            |
| ------ | ---------|----|----------------------        |
| qq        | 操作成功 |Long/String | 是 | 将要注册的QQ号  |
| password  | 操作失败 |String      | 是 | 注册的密码      |

#### 响应

```json5
{ 
    "succ":true,  
    "err_msg":null,  
    "data":"114514"  
}
```

| 参数      | 类型           | 说明               |
| ------    | ------------- |----------------------        |
| succ      | Boolean       | 是否成功  |
| err_msg   | String        | 错误信息，成功时为空值      |
| data      | String        | 唯一的api key，后续请求均需带此key，失败时为空值|

同一ip下，最多注册10个qq号。

如果丢失了自己的api key,可以通过找回接口找回

## 找回

### url

[Get] <http://arclight.space:45445/searchKey>

#### 请求

| 参数 | 类型 | 必须 | 说明                            |
| ------ | ---------|----|----------------------        |
| qq        | 操作成功 |Long/String | 是 | 查询的QQ号  |
| password  | 操作失败 |String      | 是 | 注册的密码      |

#### 响应

```json5
{  
    "succ":true,  
    "err_msg":null,  
    "data":"114514"  
}  
```

| 参数      | 类型           | 说明               |
| ------    | ------------- |----------------------        |
| succ      | Boolean       | 是否成功  |
| err_msg   | String        | 错误信息，成功时为空值      |
| data      | String        | 查找到的api key|

## 查询

### url

[Get] <http://arclight.space:45445/search>

#### 请求

|参数 |必需| 举例 |说明|
| ------| ------------- |----------------|------        |
|api_key |是|114514|你的api_key|
|maxnum|否|20|一次最大返回数量，默认100|
|id|否|1|按id精确查找团，使用此参数会忽略以下所有参数|
|kp_qq|否|114514|查找对应QQ主持人创建的所有团，使用此参数会忽略以下所有参数|
|all|否|false|直接查询所有团数据，默认false，为true时忽略以下所有参数|
|title|否|龍門的泥灰|按照团的名称查询|
|start_time|否|2022-06-20|按开团时间查询，格式为yyyy-MM-dd，返回开团时间在此及之|前的团，默认为当日30天后|
|last_time|否|14h|按团持续时间查询，数字后为h为小时，d为天，返回持续时间小于等于|此的团|
|minper|否|1|按团最少人数查询，默认为1|
|maxper|否|6|按团最多人数查询，默认为30|

#### 响应

```json5
{  
    "succ": true,  
    "err_msg": null,  
    "data": [  
        {  
             "id": 1
            "title": "这是团名",  
            "kp_name": "主持人昵称",  
            "kp_qq": "123456",  
            "start_time": "2022-06-01",  
            "last_time": "1d",  
             "last_timeh": "24",
            "minper": 4,  
            "maxper": 6,  
            "isfull": false,  
            "tags": "团的标签",  
            "des": "团的详细介绍、描述"  
        }  
    ]  
}  
```

|参数|类型|说明|
|--|--|--|
|succ|Boolean|是否成功|
|err_msg|String|错误信息，成功时为空值|
|data|Array|由团信息组成的数组，失败时为空值，可能为空数组|
|id|Int|团的唯一编号|
|title|String|团名|
|kp_name|String|主持人昵称|
|kp_qq|String|主持人QQ|
|start_time|String|开始时间，yyyy-MM-dd|
|last_time|String|持续时间，后缀d为天，h为小时|
|last_timeh|String|持续时间，换算为小时单位|
|minper|Int|参团最少人数|
|maxper|Int|参团最多人数|
|isfull|Boolean|团是否满人|
|tags|String|团的标签，如COC、秘密团.etc|
|des|String|团的详细介绍|

## 增加

### url

[Post] <http://arclight.space:45445/add?api_key>=

|参数| 必需| 举例| 说明|
|--|--|--|--|
|api_key| 是| Arclight0620| 你的api_key|

#### 请求

```json5
{      
    "title":"这是团名",      
    "kp_name":"主持人昵称",      
    "kp_qq":123456,      
    "start_time":"yyyy-MM-dd",      
    "last_time":"12h",      
    "minper":4,    
    "maxper":6,  
    "isfull":false,    
    "tags":"团的标签",      
    "des":"团的详细介绍、描述"      
}   
```

|参数| 类型| 必需| 说明|
| ------| ------------- |----------------|------        |
|title| String| 是| 团的名称|
|kp_name| String| 是| 主持人的昵称|
|kp_qq| Long/String| 是| 主持人QQ号|
|start_time| String| 是| 开团日期，格式为“yyyy-MM-dd”(需要前导0,如2022-06-01)|
|last_time| String| 是| 团持续时间，后缀d为天，h为小时|
|minper| Int| 是| 团的最少人数|
|maxper| Int| 是| 团的最多人数|
|isfull| Boolean| 否| 团是否满人，默认false|
|tags| String| 是| 团的标签，如COC|
|des| String| 是| 团的详细介绍|

#### 响应

```json5
{  
    "succ":true,  
    "err_msg":null,  
    "data":null    
}
```

|参数|类型|说明|
|--|--|--|
|succ| Boolean| 是否成功|
|err_msg| String| 错误信息，成功时为空值|
|data| Int| 团的id号|

## 更改

### url

[Post] <http://arclight.space:45445/update?api_key>=
|参数| 必需| 举例| 说明|
|--|--|--|--|
|api_key| 是| Arclight0620| 你的api_key|

#### 请求

```json5
{  
    "id": 1,  
    "title": "修改后的团名",  
    "kp_name": "修改后的kp名",  
    "kp_qq": "kp的QQ不能更改！",  
    "start_time": "2022-06-30",  
    "last_time": "12h",  
    "minper": 4,  
    "maxper": 6,  
    "isfull": true,  
    "tags": "修改后的团标签",  
    "des": "修改后的团描述"  
}  
```

|参数| 类型| 必需| 说明|
| ------| ------------- |----------------|------        |
|id| Int| 是| 团的id，用于定位团|
|title| String| 否| 修改后的团名|
|kp_name| String| 否| 修改后的主持人昵称|
|kp_qq| Long/String| 是| 主持人QQ号，不能更改|
|start_time| String| 否| 修改后的开团日期，格式同上|
|last_time| String| 否| 修改后的团持续时间|
|minper| Int| 否| 修改后的团的最少人数|
|maxper| Int| 否| 修改后团的最多人数|
|isfull| Boolean| 否| 修改团是否满人|
|tags| String| 否| 修改后的团标签|
|des| String| 否| 修改后的团的详细介绍|

注：除id和kp_qq是必选外，其余字段如未修改，则不传输或设为空值。

#### 响应

```json5
{  
    "succ": true,  
    "err_msg": null,  
    "data": [  
        {  
            "id": 1,  
            "title": "原先的团名",  
            "kp_name": "原先的kp名",  
            "kp_qq": "kp的QQ",  
            "start_time": "2022-06-30",  
            "last_time": "12h",  
            "last_timeh": 12,  
            "minper": 4,  
            "maxper": 6,  
            "isfull": false,  
            "tags": "原先的团标签",  
            "des": "原先的团描述"  
        },  
        {  
            "id": 1,  
            "title": "修改后的团名",  
            "kp_name": "修改后的kp名",  
            "kp_qq": "kp的QQ不能更改！",  
            "start_time": "2022-06-30",  
            "last_time": "12h",  
            "last_timeh": 12,  
            "minper": 4,  
            "maxper": 6,  
            "isfull": true,  
            "tags": "修改后的团标签",  
            "des": "修改后的团描述"  
        }  
    ]  
}  
```

|参数|类型|说明|
|--|--|--|
|succ| Boolean| 是否成功|
|err_msg| String| 错误信息，成功时为空值|
|data| Array| 长度为2的数组，第一个元素为原先信息，第二个元素为修改后的信息，失败时为空值|
注：data字段内容见[查询响应](#查询)

## 删除

### url

[Post] <http://arclight.space:45445/delete?api_key>=
|参数| 必需| 举例| 说明|
|--|--|--|--|
|api_key| 是| Arclight0620| 你的api_key|

#### 请求

```json5
{  
    "id":1,  
    "qq":123456  
}
```

|参数| 类型| 必需| 说明|
| ------| ------------- |----------------|------        |
|参数| 类型| 必需| 说明|
|id| Int| 是| 目标团id|
|qq| Long/String| 是| 执行者qq|

Warning：请做好id的类型检查

#### 响应

```json5
{  
    "succ":true,  
    "err_msg":null,  
    "data":null  
}  
```

|参数|类型|说明|
|--|--|--|
|succ| Boolean| 是否成功|
| err_msg| String| 错误信息，成功时为空值|
| data| Object| 恒为空值|
