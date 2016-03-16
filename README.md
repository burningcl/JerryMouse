# 什么是JerryMouse?
<br>
JerryMouse一个完整的OrMapping(Ojbect-Relation Mapping)与DAO(Data Access Object)框架。JerryMouse为Android与Java设计。目前仅支持Android环境下SQLite数据库。<br>
本人在使用以往DAO框架时，所诸多不是很满意的地方：<br>
1. 配置方式比较复杂，常常需要配置文件与多行Java代码，才能实现数据库连接的配置。<br>
2. 表结构(Schema)与类结构(Class)之间的对应关系，需要较多的代码来描述。<br>
3. 通过类SQL的API来拼接数据访问操作。尤其在查询条件非常复杂的情况下，类SQL API的拼接操作，显得非常复杂，代码的可读取性也不高。<br>
4. 列(Column)与字段(Field)之间，往往需要类型一致。<br>
因此，JerryMouse的设计初衷如下：<br>
1. 配置方式简单，一行代码实现数据库连接的配置。<br>
2. 表结构与类结构的对应关系中，一行代码描述一个列与它将对应的字段的对应关系。<br>
3. 不使用类SQL的API操作数据库，使用SQL语句注解(Annotation)的方式来操作数据库。一个数据库操作，一行代码申明。<br>
4. 列与字段之间，类型无需一致。<br>
# 为什么叫JerryMouse
<br>
1. 有一个Java Web服务器叫TomCat。<br>
2. 有一本动画片，叫Tom and Jerry。Tom是那只Cat，Jerry是那只Mouse。<br>
因此，脑洞大开，叫JerryMouse。<br>

# 表结构(Schema)与类结构(Class)的声明
+ 范例代码
```java
@DbTable(name = Note.TABLE_NAME)
public class Note {

	public static final String TABLE_NAME = "TABLE_TEST_NOTE";

	@DbField(
			primaryKey =
			@PrimaryKey(
					primaryKey = true,
					autoIncrement = true
			)
	)
	public long id;

	@DbField(notNull = true)
	public String title;

	@DbField(name = "content")
	public String text;

	@DbField(mapper = BooleanMapper.class, defaultValue = "0")
	public boolean deleted;

	@DbField(index = SortType.DESC)
	public long createTime;

	@DbField
	public long modifyTime;
}
```
+ @DbTable注解
	* 功能<br>
		定义该Class相对应的Table
	* 参数列表<br>
	
		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	name          | String        | 是            | -             | 表名          |
+ @DbField注解
	* 功能<br>
		定义该Field相对应的Column
	* 参数列表<br>
	
		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	name 				| String        | 否            | -            		| 列名，当为空时，取Field的名字作为列名						|
		|	defaultValue  		| String        | 否            | -             		| 默认值													|
		|	index 				| `SortType`  	| 否            | `SortType.NULL`	| 是否需要创建索引，取值`SortType.ASC`、`SortType.DESC`		|
		|	unique       		| boolean       | 否            | false         		| 是否需要创建唯一索引										|
		|	notNull      		| boolean       | 否            | false          		| 是否允许为空											|
		|	mapper     			| `Class<? extends ITypeMapper>`| 否 | `MapperNull.Class` | 如果设置mapper，则允许该Field与Column的类型不一致，需要mapper来进行转换 |
		|	primaryKey          | `@PrimaryKey` | 否            | `@PrimaryKey(primaryKey = false, autoIncrement = false)` | 是否为主键 |
+ @PrimaryKey注解
	* 功能<br>
		定义该Field相对应的Column是否为主键
	* 参数列表<br>

		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	primaryKey 			| boolean       | 否            | false           	| 是否为主键						|
		|	autoIncrement  		| boolean       | 否            | false           	| 是否自增						|
