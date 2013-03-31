/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.javarebel.research.commons.dao;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 *
 * @author naveen
 */
public class DAOProxyfier {
   
    private static class DAOInterceptor implements MethodInterceptor {

        private Class<?> daoClass;

        public DAOInterceptor(Class<?> daoClass) {
            this.daoClass = daoClass;
        }
        
        public Object intercept(Object arg0, Method arg1, Object[] arg2,
                MethodProxy arg3) throws Throwable {

            if (arg1.isAnnotationPresent(Query.class)) {

                ConnectionInfo con = this.daoClass.getAnnotation(ConnectionInfo.class);
                String dsName = con.datasource();
                if (dsName.equals("NULL")) {
                    dsName = con.value();
                }
                System.out.println("Datasource is " + dsName);

                DataSource ds = null;
                Context ctx = new InitialContext();
                Object obj = ctx.lookup(dsName);
                if (obj != null) {
                    ds = DataSource.class.cast(obj);
                }

                Query qry = arg1.getAnnotation(Query.class);
                System.out.println("Query is " + qry.value());

                List<Object> params = Arrays.asList(arg2);

                Type type = arg1.getGenericReturnType();
                System.out.println("Type is " + type);
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;

                    Type[] actArgs = pType.getActualTypeArguments();
                    if (actArgs != null && actArgs.length == 1) {
                        Class<?> rtnType = (Class<?>) actArgs[0];
                        System.out.println("Return List is of type " + rtnType);

                        System.out.println("RAW Type " + pType.getRawType());

                        if (qry.value().toUpperCase().startsWith("SELECT ")) {
                            return AirFrameworkDBUtil.toList(ds, qry.value(), params, rtnType);
                        }
                    }
                } else {
                    if (type == boolean.class || type == void.class) {
                        if (qry.value().toUpperCase().startsWith("INSERT")) {
                            return AirFrameworkDBUtil.insert(ds, qry.value(), params);
                        }
                        if (qry.value().toUpperCase().startsWith("UPDATE")) {
                            return AirFrameworkDBUtil.update(ds, qry.value(), params);
                        }
                        if (qry.value().toUpperCase().startsWith("DELETE")) {
                            return AirFrameworkDBUtil.delete(ds, qry.value(), params);
                        }
                    } else if (qry.value().toUpperCase().startsWith("SELECT ")) {
                        if (type == Long.class || type == long.class) {
                            return AirFrameworkDBUtil.toObject(ds, qry.value(), params, Long.class);
                        }
                        if (type == Integer.class || type == int.class) {
                            return AirFrameworkDBUtil.toObject(ds, qry.value(), params, Integer.class);
                        }
                        if (type == String.class) {
                            return AirFrameworkDBUtil.toObject(ds, qry.value(), params, String.class);
                        }
                        if (type == Double.class || type == double.class) {
                            return AirFrameworkDBUtil.toObject(ds, qry.value(), params, Double.class);
                        }
                        if (type == Float.class || type == float.class) {
                            return AirFrameworkDBUtil.toObject(ds, qry.value(), params, Float.class);
                        }
                        return AirFrameworkDBUtil.toObject(ds, qry.value(), params, arg1.getReturnType());
                    }
                }
            }

            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz) {
        return (T)Enhancer.create(clazz, new DAOInterceptor(clazz));
    }
   
}
