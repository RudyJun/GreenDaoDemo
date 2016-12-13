# GreenDaoDemo

#步骤：
##一、在module的build.gradle文件中配置
<pre><code>1.apply plugin: 'org.greenrobot.greendao'</code></pre>

<pre><code>2. buildscript {
      repositories {
          mavenCentral()
      }<br/>
      dependencies {
          //依赖greendao插件
           classpath 'org.greenrobot:greendao-gradle-plugin:3.2.1'<br />
      }
  }</code></pre>
  
<pre><code>3.greendao {
    //数据库版本
    schemaVersion 1
    //生成实体类的包路径
    daoPackage 'com.rudy.demo.greendao'
    //生成实体类的根目录
    targetGenDir 'src/main/java'
}</code></pre>
  
<pre><code>4.dependencies {
    compile 'org.greenrobot:greendao:3.2.0'
  }</code></pre>
  
##二、使用注解方式创建实体类（举例）
<pre><code>@Entity
public class Student {
//Id 默认为主键，自增方式
    @Id
    private Long id;
    private String name;
    private int age;
    private String sex;
    private String grade;
    }</code></pre>
    
##三、在AndroidStudio的Build菜单中进行Make Project后，在目标包中生成DaoMaster、DaoSession、StudentDao（有多少个实体类就有多少个Dao文件）

##四、在Activity中对数据库进行操作
  
