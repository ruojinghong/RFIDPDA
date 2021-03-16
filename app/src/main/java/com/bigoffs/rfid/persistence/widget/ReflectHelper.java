package com.bigoffs.rfid.persistence.widget;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectHelper
{
	public static final String TAG = "ReflectHelper";

	/**
	 * ���ö���Ĺ��з������������෽��
	 * 
	 * @param object �����߶���
	 * @param methodName ���õķ���������object����һ��Ϊ object.methodName
	 * @param types ���÷����Ĳ�������
	 * @param values ���÷����Ĳ���ֵ
	 * @return ���� methodName�����صĶ���
	 */
	public static Object invokePublicMethod(Object object, String methodName, Class<?>[] types, Object[] values)
	{
		// ע����������Ϊ:��������+[].class,��String[]д�� new Class<?>[]{String[].class}
		Object result = null;
		try
		{
			Class<?> classz = object.getClass();
			Method method = classz.getMethod(methodName, types);
			result = method.invoke(object, values);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���ö���ķ��������Ե��÷ǹ��з�����������������ķ���
	 * 
	 * @param object �����߶���
	 * @param methodName ���õķ���������object����һ��Ϊ object.methodName
	 * @param types ���÷����Ĳ�������
	 * @param values ���÷����Ĳ���ֵ
	 * @return ���� methodName�����صĶ���
	 */
	public static Object invokeDeclaredMethod(Object object, String methodName, Class<?>[] types, Object[] values)
	{
		Object result = null;
		try
		{
			Class<?> classz = object.getClass();
			Method method = classz.getDeclaredMethod(methodName, types);
			method.setAccessible(true);// ���ð�ȫ��飬��Ϊtrueʹ�ÿ��Է���˽�з���
			result = method.invoke(object, values);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���ö���ķ��������Ե��÷ǹ��з�������������������
	 * 
	 * @param object �����߶���
	 * @param className ����������ָ��Ϊ������ʱ���ɵ��ø��෽��
	 * @param methodName ���õķ���������object����һ��Ϊ object.methodName
	 * @param types ���÷����Ĳ�������
	 * @param values ���÷����Ĳ���ֵ
	 * @return ���� methodName�����صĶ���
	 */
	public static Object invokeDeclaredMethod(Object object, String className, String methodName, Class<?>[] types,
			Object[] values)
	{
		Object result = null;
		try
		{
			Class<?> classz = Class.forName(className);
			Method method = classz.getDeclaredMethod(methodName, types);
			method.setAccessible(true);// ���ð�ȫ��飬��Ϊtrueʹ�ÿ��Է���˽�з���
			result = method.invoke(object, values);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ������ľ�̬����
	 * 
	 * @param classz ���õ���
	 * @param methodName ���õķ�����
	 * @param types ���÷����Ĳ�������
	 * @param values ���÷����Ĳ���ֵ
	 * @return ���� methodName�����صĶ���
	 */
	public static Object invokeStaticMethod(Class<?> classz, String methodName, Class<?>[] types, Object[] values)
	{
		Object result = null;
		try
		{
			Method method = classz.getDeclaredMethod(methodName, types);
			method.setAccessible(true);// ���ð�ȫ��飬��Ϊtrueʹ�ÿ��Է���˽�з���
			result = method.invoke(null, values);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ������ľ�̬����
	 * 
	 * @param className ���õ����������������
	 * @param methodName ���õķ�����
	 * @param types ���÷����Ĳ�������
	 * @param values ���÷����Ĳ���ֵ
	 * @return ���� methodName�����صĶ���
	 */
	public static Object invokeStaticMethod(String className, String methodName, Class<?>[] types, Object[] values)
	{
		try
		{
			Class<?> classz = Class.forName(className);
			return invokeStaticMethod(classz, methodName, types, values);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ö���Ĺ��г�Ա�����������������
	 * 
	 * @param object �����߶���
	 * @param fieldName ���г�Ա������
	 * @return ���� ��Ա����
	 */
	public static Object getPublicFieldValue(Object object, String fieldName)
	{
		Object result = null;
		try
		{
			Class<?> classz = object.getClass();
			Field field = classz.getField(fieldName);
			result = field.get(object);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ��ö���ĳ�Ա���������Ի�÷ǹ��г�Ա���������������Ա
	 * 
	 * @param object �����߶���
	 * @param fieldName ��Ա������
	 * @return ���� ��Ա����
	 */
	public static Object getDeclaredFieldValue(Object object, String fieldname)
	{
		Object result = null;
		try
		{
			Class<?> classz = object.getClass();
			Field field = classz.getDeclaredField(fieldname);
			field.setAccessible(true);
			result = field.get(object);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ��ö���ı�����Ա�����Ի�÷ǹ��г�Ա������������
	 * 
	 * @param object �����߶���
	 * @param className ����������ָ��Ϊ������ʱ���ɻ�ȡ�����Ա����
	 * @param fieldName ��Ա������
	 * @return ���� ��Ա����
	 */
	public static Object getDeclaredFieldValue(Object object, String className, String fieldname)
	{
		Object result = null;
		try
		{
			Class<?> classz = Class.forName(className);
			Field field = classz.getDeclaredField(fieldname);
			field.setAccessible(true);
			result = field.get(object);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * �����ľ�̬��Ա���������Ի�÷ǹ��г�Ա���������������Ա
	 * 
	 * @param classz ���õ���
	 * @param fieldName ��Ա������
	 * @return ���� ��Ա����
	 */
	public static Object getStaticFieldValue(Class<?> classz, String fieldName)
	{
		Object result = null;
		try
		{
			Field field = classz.getDeclaredField(fieldName);
			field.setAccessible(true);
			result = field.get(null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * �����ľ�̬��Ա���������Ի�÷ǹ��г�Ա���������������Ա
	 * 
	 * @param className ���õ����������������
	 * @param fieldName ��Ա������
	 * @return ���� ��Ա����
	 */
	public static Object getStaticFieldValue(String className, String fieldName)
	{
		try
		{
			Class<?> classz = Class.forName(className);
			return getStaticFieldValue(classz, fieldName);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ��ȡ�������ڲ����ӿ�
	 * */
	public static Class<?> getInnerClass(Class<?> classz, String innerClassName)
	{
		Class<?>[] classes = classz.getClasses();
		for (Class<?> innerClass : classes)
		{
			if (innerClass.getSimpleName().equals(innerClassName))
			{
				return innerClass;
			}
		}
		return null;
	}

	public static Class<?> getInnerClass(Object object, String innerClassName)
	{
		return getInnerClass(object.getClass(), innerClassName);
	}

	/**
	 * ��ȡ�����ڲ��࣬���Ի�ȡ�ǹ����ڲ���
	 * */
	public static Class<?> getDeclaredInnerClass(Class<?> classz, String innerClassName)
	{
		Class<?>[] classes = classz.getDeclaredClasses();
		for (Class<?> innerClass : classes)
		{
			if (innerClass.getSimpleName().equals(innerClassName))
			{
				return innerClass;
			}
		}
		return null;
	}

	public static Class<?> getDeclaredInnerClass(Object object, String innerClassName)
	{
		return getDeclaredInnerClass(object.getClass(), innerClassName);
	}

	/**
	 * ��ֵ����Ĺ��г�Ա���������������Ա
	 * 
	 * @param object �����߶���
	 * @param fieldName ��Ա������
	 * @param value ��ֵ
	 * @return �����Ƿ�ɹ�
	 */
	public static boolean setFieldValue(Object object, String fieldName, Object value)
	{
		try
		{
			Class<?> classz = object.getClass();
			Field field = classz.getField(fieldName);
			field.set(object, value);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��ֵ����ĳ�Ա�����������÷ǹ��г�Ա���������������Ա
	 * 
	 * @param object �����߶���
	 * @param fieldName ��Ա������
	 * @param value ��ֵ
	 * @return �����Ƿ�ɹ�
	 */
	public static boolean setDeclaredFieldValue(Object object, String fieldName, Object value)
	{
		try
		{
			Class<?> classz = object.getClass();
			Field field = classz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��ֵ����ĳ�Ա�����������÷ǹ��г�Ա����ָ������
	 * 
	 * @param object �����߶���
	 * @param className ������������ָ��Ϊ������ʱ�������ø����Ա����
	 * @param fieldName ��Ա������
	 * @param value ��ֵ
	 * @return �����Ƿ�ɹ�
	 */
	public static boolean setDeclaredFieldValue(Object object, String className, String fieldName, Object value)
	{
		try
		{
			Class<?> classz = Class.forName(className);
			Field field = classz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��ֵ��ľ�̬��Ա����������˽�г�Ա���������������Ա
	 * 
	 * @param classz ���õ���
	 * @param fieldName ��Ա������
	 * @param value ��ֵ
	 * @return �����Ƿ�ɹ�
	 */
	public static boolean setStaticFieldValue(Class<?> classz, String fieldName, Object value)
	{
		try
		{
			Field field = classz.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(null, value);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ��ֵ��ľ�̬��Ա����������˽�г�Ա���������������Ա
	 * 
	 * @param className ���õ����������������
	 * @param fieldName ��Ա������
	 * @param value ��ֵ
	 * @return �����Ƿ�ɹ�
	 */
	public static boolean setStaticFieldValue(String className, String fieldName, Object value)
	{
		try
		{
			Class<?> classz = Class.forName(className);
			return setStaticFieldValue(classz, fieldName, value);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ������ʵ��
	 * 
	 * @param classz ��
	 * @param types �����������
	 * @param values �������ֵ
	 * @return ������ʵ��
	 */
	public static Object newInstance(Class<?> classz, Class<?>[] types, Object[] values)
	{
		Object result = null;
		try
		{
			Constructor<?> constructor = classz.getDeclaredConstructor(types);// ��λ���췽��
			constructor.setAccessible(true);// ���ð�ȫ��飬�ܹ�����˽�й��캯��
			result = constructor.newInstance(values);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ������ʵ��
	 * 
	 * @param classz ��
	 * @return ������ʵ��
	 */
	public static Object newInstance(Class<?> classz)
	{
		try
		{
			return classz.newInstance();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ������ʵ��
	 * 
	 * @param className ���������������
	 * @param types �����������
	 * @param values �������ֵ
	 * @return ������ʵ��
	 */
	public static Object newInstance(String className, Class<?>[] types, Object[] values)
	{
		try
		{
			Class<?> classz = Class.forName(className);
			return newInstance(classz, types, values);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ������ʵ��
	 * 
	 * @param className ���������������
	 * @return ������ʵ��
	 */
	public static Object newInstance(String className)
	{
		try
		{
			Class<?> classz = Class.forName(className);
			return newInstance(classz);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static Object newArrayInstance(Class<?> classz, int length)
	{
		return Array.newInstance(classz, length);
	}

	public static Object newArrayInstance(String className, int length)
	{
		Class<?> classz;
		try
		{
			classz = Class.forName(className);
			return newArrayInstance(classz, length);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * �ж����Ƿ�֧��ָ���ķ���
	 * 
	 * @param classz ָ������
	 * @param methodName ָ���ķ�����
	 * @param types �����Ĳ�������
	 * @return boolean
	 */
	public static boolean isMethodSupported(Class<?> classz, String methodName, Class<?>[] types)
	{
		try
		{
			classz.getDeclaredMethod(methodName, types);
			return true;
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �ж����Ƿ�֧��ָ���ķ���
	 * 
	 * @param className ָ�������������������
	 * @param methodName ָ���ķ�����
	 * @param types �����Ĳ�������
	 * @return boolean
	 */
	public static boolean isMethodSupported(String className, String methodName, Class<?>[] types)
	{
		try
		{
			Class<?> classz = Class.forName(className);
			return isMethodSupported(classz, methodName, types);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �ж϶����Ƿ�֧��ָ���ķ���
	 * 
	 * @param object ָ���Ķ���
	 * @param methodName ָ���ķ�����
	 * @param argClass �����Ĳ�������
	 * @return boolean
	 */
	public static boolean isMethodSupported(Object object, String methodName, Class<?>[] types)
	{
		try
		{
			Class<?> classz = object.getClass();
			return isMethodSupported(classz, methodName, types);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * �ж�ָ�������Ƿ����
	 * 
	 * @param className ָ�������������������
	 * @return boolean
	 */
	public static boolean isClassExistsed(String className)
	{
		try
		{
			Class.forName(className);
			return true;
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isInstanceOf(Class<?> classz, Class<?> instanceClass)
	{
		do
		{
			if (classz.equals(instanceClass))
			{
				return true;
			}
		}
		while ((classz = classz.getSuperclass()) != null);
		return false;
	}

	public static boolean isInstanceOf(String className, String instanceClassName)
	{
		try
		{
			Class<?> classz = Class.forName(className);
			Class<?> parentClass = Class.forName(instanceClassName);
			return isInstanceOf(classz, parentClass);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isInstanceOf(Object object, String instanceClassName)
	{
		try
		{
			Class<?> classz = object.getClass();
			Class<?> parentClass = Class.forName(instanceClassName);
			return isInstanceOf(classz, parentClass);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
