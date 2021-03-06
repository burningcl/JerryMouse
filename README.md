# 什么是JerryMouse?
  
JerryMouse一个完整的OrMapping(Ojbect-Relation Mapping)与DAO(Data Access Object)框架。JerryMouse为Android与Java设计。目前仅支持Android环境下SQLite数据库。  
  
本人在使用以往DAO框架时，所诸多不是很满意的地方：  
1. 配置方式比较复杂，常常需要配置文件与多行Java代码，才能实现数据库连接的配置。  
2. 表结构(Schema)与类结构(Class)之间的对应关系，需要较多的代码来描述。  
3. 通过类SQL的API来拼接数据访问操作。尤其在查询条件非常复杂的情况下，类SQL API的拼接操作，显得非常复杂，代码的可读取性也不高。  
4. 列(Column)与字段(Field)之间，往往需要类型一致。  
  
因此，JerryMouse的设计初衷如下：  
1. 配置方式简单，一行代码实现数据库连接的配置。  
2. 表结构与类结构的对应关系中，一行代码描述一个列与它将对应的字段的对应关系。  
3. 不使用类SQL的API操作数据库，使用SQL语句注解(Annotation)的方式来操作数据库。一个数据库操作，一行代码申明。  
4. 列与字段之间，类型无需一致。
  
# 为什么叫JerryMouse
  
1. 有一个Java Web服务器叫TomCat。  
2. 有一本动画片，叫Tom and Jerry。Tom是那只Cat，Jerry是那只Mouse。  
  
因此，脑洞大开，叫JerryMouse。
  
# 表结构(Schema)与类结构(Class)的定义
  
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
  
	* 功能  
		定义该Class相对应的Table
	* 参数列表  
	
		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	name          | String        | 是            | -             | 表名          |

+ @DbField注解
  
	* 功能  
		定义该Field相对应的Column
	* 参数列表  
	
		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	name 				| String        | 否            | -            		| 列名，当为空时，取Field的名字作为列名						|
		|	defaultValue  		| String        | 否            | -             		| 默认值													|
		|	index 			| `SortType`  	| 否            | `SortType.NULL`	| 是否需要创建索引，取值`SortType.ASC`、`SortType.DESC`		|
		|	unique       		| boolean       | 否            | false         		| 是否需要创建唯一索引										|
		|	notNull      		| boolean       | 否            | false          		| 是否允许为空											|
		|	mapper     		| `Class<? extends ITypeMapper>`| 否 | `MapperNull.Class` | 如果设置mapper，则允许该Field与Column的类型不一致，需要mapper来进行转换 |
		|	primaryKey          | `@PrimaryKey` | 否            | `@PrimaryKey(primaryKey = false, autoIncrement = false)` | 是否为主键 |
  
+ @PrimaryKey注解
  
	* 功能  
		定义该Field相对应的Column是否为主键
	* 参数列表  

		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	primaryKey 		| boolean       | 否            | false           	| 是否为主键		|
		|	autoIncrement  		| boolean       | 否            | false           	| 是否自增		|
  
# 数据库连接初始化
  
+ 范例代码
```Java
String dbName = "TestDatabase";
SQLiteDatabase.CursorFactory factory = null;
int version = 1;
DatabaseErrorHandler errorHandler = null;
List<Class<?>> metaCalzzes = new ArrayList<>();
metaCalzzes.add(Note.class);
DataSourceInitCallBack initCallBack = new DataSourceInitCallBack() {
	@Override
	public void beforeCreateTable() {
		Log.i(LOG_TAG, "beforeCreateTable");
	}
	@Override
	public void afterCreateTable(SQLiteDatabase database, SQLiteDataSource dataSource) {
		Log.i(LOG_TAG, "afterCreateTable");
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion, SQLiteDataSource dataSource) {
		Log.i(LOG_TAG, "onUpgrade, oldVersion: " + oldVersion + ", newVersion: " + newVersion);
	}
};

SQLiteOpenHelper sQLiteOpenHelper = (SQLiteOpenHelper) SQLiteDataSource.init(context, dbName, factory, version,	errorHandler, metaCalzzes, initCallBack);
```
  
+ 范例代码说明
	* 在以上代码中，我们建立了一个SQLite的连接；
	* 数据库名字为TestDatabase；
	* 数据库版本为1；
	* 在初始化数据库或者升级数据库时，系统会自动创建metaCalzzes列表中的类所对应的表；
  
# Insert操作
  
+ 范例代码  
第一步，DAO接口需要继承接口`Dao<Meta>`
```Java
public interface NoteDao extends Dao<Note> 
```  
第二步，调用接口  
```Java
Note note = new Note();
note.title = "title " + System.currentTimeMillis();
note.text = "text " + System.currentTimeMillis();
note.deleted = false;
note.createTime = System.currentTimeMillis();
note.modifyTime = System.currentTimeMillis();
long id = DaoProxy.getDao(NoteDao.class).add(note);
```
  
+ 范例代码说明
	* 在接口`Dao<Meta>`中已经定义了`add(Meta)`方法（Meta为泛型）
	```Java
	@Sql(type = SqlType.INSERT)
	long add(Meta meta);
	```
	* 在只需要申明interface，并在method加上`@Sql(type = SqlType.INSERT)`注解就可以实现对Meta的插入（JerryMouse自动将Meta对象map成记录，将插入数据库中）。
	* 接口调用，采用[Java动态代理技术](http://www.ibm.com/developerworks/library/j-jtp08305/ "")实现。
	* insert成功后，返回该记录的id。

# Delete操作(Delete Item)
  
+ 范例代码  
第一步，DAO接口需要继承接口`Dao<Meta>`  
第二步，调用接口
```Java
int deleteItemNum = DaoProxy.getDao(NoteDao.class).delete(note);
```

+ 范例代码说明
  
	* 在接口`Dao<Meta>`中已经定义了`delete(Meta)`方法（Meta为泛型）
	```Java
	@Sql(type = SqlType.DELETE)
	long delete(Meta... meta);
	```	
	* 在只需要申明interface，并在method加上`@Sql(type = SqlType.DELETE)`注解就可以实现对Meta的删除（JerryMouse自动解析Meta对象，提取主键及其值，转为whereCaluse，从数据库中将相应的记录删除）。
	* delete成功后，返回删除的记录数。

# Delete操作(Delete with whereCaluse)
上一章节中的Delete操作有很强的局限性，在更多的应用场景中，我们更需要根据条件删除。因此JerryMouse也提供whereCaluse删除的接口。
  
+ 范例代码  
第一步，定义接口
```Java
@Sql(type = SqlType.DELETE, delete = @DeleteSql(whereClause = "createTime=?"))
int delete(@Param long createTime);
```
第二步，调用接口
```Java
int deleItemNum = DaoProxy.getDao(NoteDao.class).delete(note.createTime);
```

+ `@DeleteSql`注解
  
	* 功能  
		用于描述删除条件
	* 参数列表  
	
		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	tableName 		| String       	| 否            | ""           	| 所需删除的数据所在的表	|
		|	whereClause  		| String       	| 是            |            	| 删除条件			|

+ `@Param`注解
  
	* 功能  
		用于描述描述所需要的参数
	* 参数列表  
	
		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	mapper     		| `Class<? extends ITypeMapper>`| 否 | `MapperNull.Class` | 如果设置mapper，则允许该Field与Column的类型不一致，需要mapper来进行转换 |

+ 范例代码说明
	* `@DeleteSql`注解用于描述删除条件。
	* 当`@DeleteSql`的tableName没有设置时，使用`Note<Meta>`中泛型的Meta中的所描述的tableName。
	* delete成功后，返回删除的记录数。
  
# Update操作(Update Items)

+ 范例代码  
第一步，DAO接口需要继承接口`Dao<Meta>`  
第二步，调用接口
```Java
int updateItemNum = DaoProxy.getDao(NoteDao.class).update(note);
```

+ 范例代码说明
  
	* 在接口`Dao<Meta>`中已经定义了`update(Meta)`方法（Meta为泛型）
	```Java
	@Sql(type = SqlType.UPDATE)
	int update(Meta... meta);
	```	
	* 在只需要申明interface，并在method加上`@Sql(type = SqlType.UPDATE)`注解就可以实现对Meta的修改（JerryMouse自动解析Meta对象，提取主键及其值，转为whereCaluse，提取其它cloumn及其value，从数据库修改相应的数据）。
	* update成功后，返回修改的记录数。
  
# Update操作(Update with whereCaluse)

+ 范例代码  
第一步，定义接口
```Java
@Sql(type = SqlType.UPDATE, update = @UpdateSql(whereClause = "createTime=?"))
int update(@Value(columnName = "title") String title, @Param long createTime);
```
第二步，调用接口
```Java
int updateItemNum = DaoProxy.getDao(NoteDao.class).update("new title 1", note.createTime);
```

+ `@UpdateSql`注解
	* 功能  
		用于描述修改条件
	* 参数列表  
	
		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	tableName 		| String       	| 否            | ""           	| 所需修改的数据所在的表	|
		|	whereClause  		| String       	| 是            |            	| 修改条件			|
+ 范例代码说明
	* `@UpdateSql`注解用于描述修改条件。
	* 当`@UpdateSql`的tableName没有设置时，使用`Note<Meta>`中泛型的Meta中的所描述的tableName。
	* 修改成功后，返回修改的记录数。

# Select操作(Select Item)

+ 范例代码  
第一步，DAO接口需要继承接口`Dao<Meta>`  
第二步，定义查询SQL语句  
```Java
@Sql(type = SqlType.SELECT, select = @SelectSql(sql = "select * from " + Note.TABLE_NAME + " where deleted = ?"))
Note selectItem(@Param(mapper = BooleanMapper.class) boolean deleted);
```
第二步，调用接口  
```Java
Note note = DaoProxy.getDao(NoteDao.class).selectItem(false);
```

+ `@SelectSql`注解
	* 功能  
		用于描述查询SQL
	* 参数列表  
	
		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	sql 		| String       	| 是            |            	| 查询SQ语句			|
		|	mapper  	| `@Mapper`     | 否            |            	| OrMapping所需要的mapper。 	|

+ `@Mapper`注解
	* 功能  
		OrMapping所需要的mapper，将Relation mapping成 Object。	
	* 参数列表  
	
		| 参数 | 类型 | 是否必须 | 默认值 | 含义 |
		| ------------- | ------------- | ------------- | ------------- | ------------- |
		|	raw 		| boolean      	| 否            | false     	| 是否是原始查询，如果是，则直接返回Cursor，不进行OrMapping			|
		|	mapper  	| `Class<? extends IOrMapper>`     | 否            | `MapperNull.class`     | Mapper的class	|

+ 范例代码说明
	* 在未指定mapper的情况下：
		1. 返回类型为Item， 则Select操作的OrMapping使用DefaultOrMapper；
		2. 返回类型为List，则Select操作的OrMapping使用DefaultOrListMapper；
		3. 返回类型为原始数据类型(Primitive Data Type)，则Select操作的OrMapping使用PrimitiveDataMapper；
  
# Select操作(Select List)

+ 范例代码  
第一步，DAO接口需要继承接口`Dao<Meta>`  
第二步，定义查询SQL语句  
```Java
@Sql(type = SqlType.SELECT, select = @SelectSql(sql = "select * from " + Note.TABLE_NAME + " where deleted = ?"))
List<Note> select(@Param(mapper = BooleanMapper.class) boolean deleted);
```
第二步，调用接口  
```Java
List<Note> notes = DaoProxy.getDao(NoteDao.class).select(false);
```

+ 范例代码说明
	* 与《Select操作(Select Item)》唯一不同的是，接口的返回值类型不再是Item而是List；

# Select操作(Count、Sum、Max、Min...)

+ 范例代码  
第一步，DAO接口需要继承接口`Dao<Meta>`  
第二步，定义查询SQL语句  
```Java
@Sql(type = SqlType.SELECT, select = @SelectSql(sql = "select count(1) from " + Note.TABLE_NAME + " where deleted = ?"))
int count(@Param(mapper = BooleanMapper.class) boolean deleted);
```
第二步，调用接口  
```Java
int cnt = DaoProxy.getDao(NoteDao.class).count(false);
```

+ 范例代码说明
	* 与《Select操作(Select Item)》唯一不同的是，接口的返回值类型不再是Item，而是基础数据类型或者其包装类型；
