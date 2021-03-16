package com.bigoffs.rfid.util;

import android.database.Cursor;

import com.bigoffs.rfid.database.greendao.DaoSession;
import com.bigoffs.rfid.mvp.bean.AllocationError;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * @author okbuy-001
 * @desc
 * @time 2020/4/28 12:01
 */
public class DBOperator<T extends AbstractDao,E> {

    public T mCustomDao;
    public DaoSession mDaoSession;
    private String sql;
    public DBOperator(T customDao, DaoSession daoSession) {
        this.mCustomDao = customDao;
        this.mDaoSession = daoSession;
    }

    /**
     * 插入一条数据
     */
    public long insertData(E e) {
        return mCustomDao.insert(e);
    }
    /**
     * 插入一条数据
     */
    public void quicklyInsertData(E e) {
        mCustomDao.insert(e);
        mCustomDao.detachAll();
    }
    /**
     * 插入多条数据
     */
    public void insertDatas(final List<E> e) {
        mDaoSession.runInTx(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < e.size(); i++) {
                    E item = e.get(i);
                    mCustomDao.insert(item);
                }
            }
        });

    }

    /**
     * 修改一条数据,根据id来匹配的
     */
    public void updateData(List<E> e) {
        mCustomDao.updateInTx(e);
    }
    /**
     * 修改一条数据,根据id来匹配的
     */
    public void updateData(E e) {
        mCustomDao.updateInTx(e);
    }

    /**
     * 根据某个字段获取单条数据
     */
    public List<E> getItemsByParameters(String orderRaw, WhereCondition... whereCondition) {
        QueryBuilder qb = mCustomDao.queryBuilder();

        switch (whereCondition.length) {
            case 1:
                qb.where(whereCondition[0]);
                break;
            case 2:
                qb.where(whereCondition[0], whereCondition[1]);
                break;
            case 3:
                qb.where(whereCondition[0], whereCondition[1], whereCondition[2]);
                break;
            case 4:
                qb.where(whereCondition[0], whereCondition[1], whereCondition[2], whereCondition[3]);
                break;
        }
        if (orderRaw != null)
            qb.orderRaw(orderRaw);

        return qb.list();
    }

    public List<E> getItemsByCondition(WhereCondition whereCondition) {
        QueryBuilder qb = mCustomDao.queryBuilder();
        qb.where(whereCondition);
        return qb.list();
    }
    //删除一条数据
    public List<E> deleteItemsByCondition(WhereCondition whereCondition) {
        mCustomDao.queryBuilder().where(whereCondition)

                .buildDelete().executeDeleteWithoutDetachingEntities();


        QueryBuilder qb = mCustomDao.queryBuilder();
        qb.where(whereCondition);
        return qb.list();
    }

    public List<E> getItemByParameter(Property fieldName, String fieldValue) {
        QueryBuilder qb = mCustomDao.queryBuilder();
        qb.where(fieldName.eq(fieldValue));
        List list = qb.list();
        if (list != null) {
            return list;
        }
        return null;
    }
    public List<E> getAll() {

        return mCustomDao.queryBuilder().list();
    }
    public int rawQueryForSum(String[] selectionArgs, String condition, String column, String table) {
        int epcOperateSum = 0;
        Cursor cursor = mDaoSession.getDatabase().rawQuery("SELECT SUM(" + column + ") FROM " + table + " WHERE " + condition, selectionArgs);
        if (cursor.moveToNext()) {
            epcOperateSum = cursor.getInt(0);
        }
        cursor.close();
        return epcOperateSum;
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return mDaoSession.getDatabase().rawQuery(sql, selectionArgs);
    }

    /**
     * 根据ID获取一条数据
     *
     * @param id
     * @return
     */
    public E getItemByID(long id) {
        return (E) mCustomDao.load(id);
    }


    public void deleteDatas(List<E> datas) {
        mCustomDao.deleteInTx(datas);
    }
    public void deleteData(E data) {
        mCustomDao.delete(data);
    }


    public static <T extends AbstractDao, E> DBOperator<T, E> getOperator(T t, Class<E> e) {
        return new DBOperator<T, E>(t, ((DaoSession) t.getSession()));
    }


}
