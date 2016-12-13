# GreenDaoDemo

#步骤：
一、在module的build.gradle文件中配置
1.apply plugin: 'org.greenrobot.greendao'

2. buildscript {
      repositories {
          mavenCentral()
      }
      dependencies {
          //依赖greendao插件
           classpath 'org.greenrobot:greendao-gradle-plugin:3.2.1'
      }
  }
  
3.greendao {
    schemaVersion 1
    //生成实体类的包路径
    daoPackage 'com.rudy.demo.greendao'
    //生成实体类的根目录
    targetGenDir 'src/main/java'
}
  
4.dependencies {
    compile 'org.greenrobot:greendao:3.2.0'
  }
  
二、使用注解方式创建实体类（举例）
@Entity
public class Student {
//Id 默认为主键，自增方式
    @Id
    private Long id;
    private String name;
    private int age;
    private String sex;
    private String grade;
    }
    
三、在AndroidStudio的Build菜单中进行Make Project后，在目标包中生成DaoMaster、DaoSession、StudentDao（有多少个实体类就有多少个Dao文件）

四、在Activity中对数据库进行操作
  
