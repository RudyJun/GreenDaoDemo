package com.rudy.demo.entity;

import com.rudy.demo.greendao.DaoSession;
import com.rudy.demo.greendao.FatherDao;
import com.rudy.demo.greendao.SonDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by RudyJun on 2016/12/13.
 */

@Entity
public class Son {
    @Id
    private Long id;
    private String name;
    private String sex;
    private int age;
    @ToOne(joinProperty = "id")
    private Father father;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1926509084)
    private transient SonDao myDao;
    @Generated(hash = 417898651)
    public Son(Long id, String name, String sex, int age) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.age = age;
    }
    @Generated(hash = 1259336981)
    public Son() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSex() {
        return this.sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    @Generated(hash = 2100996716)
    private transient Long father__resolvedKey;
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 600097342)
    public Father getFather() {
        Long __key = this.id;
        if (father__resolvedKey == null || !father__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            FatherDao targetDao = daoSession.getFatherDao();
            Father fatherNew = targetDao.load(__key);
            synchronized (this) {
                father = fatherNew;
                father__resolvedKey = __key;
            }
        }
        return father;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1337826754)
    public void setFather(Father father) {
        synchronized (this) {
            this.father = father;
            id = father == null ? null : father.getId();
            father__resolvedKey = id;
        }
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 838735897)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSonDao() : null;
    }
}
