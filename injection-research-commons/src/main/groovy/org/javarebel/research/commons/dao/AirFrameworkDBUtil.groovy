package org.javarebel.research.commons.dao

import groovy.util.logging.Log4j
import javax.sql.DataSource
import javax.naming.InitialContext
import groovy.sql.Sql
import java.lang.reflect.Field;

/**
 *
 * @author naveen
 */
@Log4j
class AirFrameworkDBUtil<T> {
	def static final primitives = [Integer.class, String.class, Long.class, Double.class, Float.class]
	def static final tsTypes = [92,93,-102,-100,-101]
        
        private static def executeQuery = {ds, query, params, clazz ->
		def objList = [], obj, colName = "", field, sql, colValue, clob, colType
		try {
			sql = Sql.newInstance(ds)
			if(primitives.find {it == clazz}) {
				if(!params) params = []
				sql.eachRow(query, params) {row ->
					colName = row.getMetaData().getColumnName(1)
					colType = row.getMetaData().getColumnType(1)
					colValue = row."$colName";
					if(colValue instanceof java.sql.Clob) {
						clob = (java.sql.Clob) colValue
						objList << clob.getAsciiStream()?.getText()
					} else {
						if(tsTypes.count { it == colType } > 0)
							objList << row.getTimestamp(colName)
						else
							objList << convertColumnValue(colValue, clazz)
					}
				}
			} else {
				def fieldMap = mapFieldNames(clazz)
				if(!params) params = []
				sql.eachRow(query, params) {row -> 
					obj = clazz.metaClass.invokeConstructor()
					(0..row.getMetaData().columnCount-1).each {
						colName = row.getMetaData().getColumnName(it+1)
						colType = row.getMetaData().getColumnType(it+1)
						field = fieldMap.find {it.key.compareToIgnoreCase(colName) == 0}
						if(!field) {
							log.debug "Field match by column name failed, matching with column label"
							colName = row.getMetaData().getColumnLabel(it+1)
							log.debug "Colum Label ${colName}"
							field = fieldMap.find {it.key.compareToIgnoreCase(colName) == 0}
							log.debug "Field is ${field}"
						}
						if(field) {
							colValue = row."$colName";
							if(colValue instanceof java.sql.Clob) {
								clob = (java.sql.Clob) colValue
								obj."$field.value" = clob.getAsciiStream()?.getText()
							} else {
								if(tsTypes.count { it == colType } > 0) 
									obj."$field.value" = row.getTimestamp(colName)
								else
									obj."$field.value" = colValue
							}
						}
					}
					objList << obj
				}
				def calAnn, clsExpr, clsure
				def calFlds = findCalculatedFields(clazz)
				log.debug "There are ${calFlds?.size()} fields available for calculation" 
				if(!calFlds.empty) {
					def shell = new GroovyShell()
					calFlds.each {calFld ->
						log.debug "${calFld.name} is a calculated field"
						calAnn = calFld.getAnnotation(Calculated.class)
						clsExpr = "{it -> it.${calFld.name} = ${calAnn.expr()}}"
						clsure = shell.evaluate(clsExpr)
						objList.collect(clsure)
						clsure = null
					}
					calFlds = null
					shell = null
				}
			}
		} catch(Exception e) {
			log.error e.message, e
			throw new AirFrameworkDAOException(e)
		} finally {
			sql?.close()
			sql = null
		}
		objList
	} 
	
	/**
	 * Execute given update query
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param query - Update Query
	 * @param params - List of parameters in query
	 * @return
	 */
	static def boolean update(DataSource dataSource, String query, List<?> params) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		def sql
		try {
			sql = Sql.newInstance(dataSource)
			query = query?.trim()
			if(query.toLowerCase().startsWith('update')) 
				return sql.executeUpdate(query, params) > 0
			else 
				throw new IllegalArgumentException("Not an update statement");
		} catch(Exception e) {
			log.error e.message, e
			throw new AirFrameworkDAOException(e)
		} finally {
			sql?.close()
			sql = null
		}
	}
	
	/**
	 * Execute given update query
	 * 
	 * @param dataSource - JNDI name of the datasource for executing query 
	 * @param query - Update Query
	 * @param params - List of parameters in query
	 * @return
	 */
	static def boolean update(String dataSource, String query, List<?> params) throws AirFrameworkDAOException {
		return update(lookup(dataSource), query, params);
	}
	
	/**
	 * Insert not null member values to corresponding table
	 * 
	 * @param dataSource - JNDI name of the datasource for executing query 
	 * @param object - Object to insert
	 * @return
	 */	
	static def boolean insert(String dataSource, def object) {
		return insert(lookup(dataSource), object);
	}

	/**
	 * Insert not null member values to corresponding table
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param object - Object to insert
	 * @return
	 */		
	static def boolean insert(DataSource dataSource, def object) throws AirFrameworkDAOException {
		def flds = [], fld
		def clazz = object.class
		def metaProps = clazz.metaClass.properties
		def fields = [], decFlds
		while(clazz != Object.class) {
			decFlds = clazz.getDeclaredFields() as List
			fields.addAll decFlds
			clazz = clazz.superclass
		}
		metaProps.each {metaProp ->
			fld = fields.find {metaProp.name == it.name}
			if(fld && "metaClass" != fld.name && !fld.isAnnotationPresent(javax.persistence.Transient.class)) {
				if(object."$fld.name")
					flds << fld
				else {
					if(fld.isAnnotationPresent(Column.class)) {
						def colAnn = fld.getAnnotation(Column.class)
						if(colAnn.defaultValue())
							flds << fld
					}
				}
			}
		}
		
		def fldNames = [], fldParams = [], fldValues = []
		flds.each { it ->
			if(it.isAnnotationPresent(Column.class)) {
				def colAnn = it.getAnnotation(Column.class)
				fldNames << colAnn.name()?.trim()
			} else fldNames << it.name
		
			
			if(object."$it.name") {
				fldValues << object."$it.name"
				fldParams << "?"
			} else {
				if(it.isAnnotationPresent(Column.class)) {
					def colAnn = it.getAnnotation(Column.class)
					if(colAnn.defaultValue())
						fldParams << colAnn.defaultValue()
				}
			}
		}
		def tableName = object.class.simpleName;
		if(object.class.isAnnotationPresent(javax.persistence.Table.class)) {
			def tableAnn = object.class.getAnnotation(javax.persistence.Table.class)
			def schema = tableAnn.schema()?.trim()
			def name = tableAnn.name()?.trim()
			tableName = schema ? "${schema}.${name}" : name
		}
		def query = "insert into ${tableName} (${fldNames.join(',')}) values (${fldParams.join(',')})"
		log.debug "Inserting object with query ${query} and values ${fldValues}"
		
		def sql
		try {
			sql = Sql.newInstance(dataSource)
			return sql.executeUpdate(query, fldValues) > 0
		} catch(Exception e) {
			log.error e.message, e
			throw new AirFrameworkDAOException(e)
		} finally {
			sql?.close()
			sql = null
		}
	}
	
	/**
	 * Execute given insert query
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param query - Insert Query
	 * @param params - List of parameters in query
	 * @return
	 */
	static def boolean insert(DataSource dataSource, String query, List<?> params) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		def sql
		try {
			sql = Sql.newInstance(dataSource)
			query = query?.trim()
			if(query.toLowerCase().startsWith('insert')) 
				return sql.executeUpdate(query, params) > 0
			else 
				throw new IllegalArgumentException("Not an insert statement");
		} catch(Exception e) {
			log.error e.message, e
			throw new AirFrameworkDAOException(e)
		} finally {
			sql?.close()
			sql = null
		}
	}
	
	/**
	 * Execute given insert query
	 * 
	 * @param dataSource - JNDI name of the datasource for executing query 
	 * @param query - Insert Query
	 * @param params - List of parameters in query
	 * @return
	 */
	static def boolean insert(String dataSource, String query, List<?> params) throws AirFrameworkDAOException {
		return insert(lookup(dataSource), query, params);
	}
	
	/**
	 * Execute given delete query
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param query - Delete Query
	 * @param params - List of parameters in query
	 * @return
	 */
	static def boolean delete(DataSource dataSource, String query, List<?> params) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		def sql
		try {
			sql = Sql.newInstance(dataSource)
			query = query?.trim()
			if(query.toLowerCase().startsWith('delete')) 
				return sql.executeUpdate(query, params) > 0
			else 
				throw new IllegalArgumentException("Not a delete statement");
		} catch(Exception e) {
			log.error e.message, e
			throw new AirFrameworkDAOException(e)
		} finally {
			sql?.close()
			sql = null
		}
	}
	
	/**
	 * Execute given delete query
	 * 
	 * @param dataSource - JNDI name of the datasource for executing query 
	 * @param query - Delete Query
	 * @param params - List of parameters in query
	 * @return
	 */
	static def boolean delete(String dataSource, String query, List<?> params) throws AirFrameworkDAOException {
		return delete(lookup(dataSource), query, params);
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return List of objects of clazz argument type
	 */
	static def toObject(DataSource dataSource, String query, List<?> params, Class clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		def objects = executeQuery(dataSource, query, params, clazz)
		if(objects) return objects[0]
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return List of objects of clazz argument type
	 */
	static def <T> T toGenericOject(DataSource dataSource, String query, List<?> params, Class<T> clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		def objects = executeQuery(dataSource, query, params, clazz)
		if(objects) return objects[0]
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return List of objects of clazz argument type
	 */
	static def <T> T toGenericOject(String dataSource, String query, List<?> params, Class<T> clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		def objects = executeQuery(lookup(dataSource), query, params, clazz)
		if(objects) return objects[0]
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - JNDI Name of the Datasource 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return List of objects of clazz argument type
	 */
	static def toObject(String dataSource, String query, List<?> params, Class clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		def objects = executeQuery(lookup(dataSource), query, params, clazz)
		if(objects) return objects[0]
	}
	
	private static def convertColumnValue(Object colVal, Class<?> clazz) {
		if(colVal != null && colVal.class == BigDecimal.class) {
			if(clazz == Long.class) return colVal.longValue()
			if(clazz == Integer.class) return colVal.intValue()
			if(clazz == Float.class) return colVal.floatValue()
			if(clazz == Double.class) return colVal.doubleValue()
		} else return colVal
	}
	
	private static def findPrimitives = {ds, query, params, clazz ->
		def objList = [], colName = "", field, sql, colValue, clob
		try {
			sql = Sql.newInstance(ds)
			if(primitives.find {it == clazz}) {
				if(!params) params = []
				sql.eachRow(query, params) {row ->
					colName = row.getMetaData().getColumnName(1)
					colValue = row."$colName";
					if(colValue instanceof java.sql.Clob) {
						clob = (java.sql.Clob) colValue
						objList << clob.getAsciiStream()?.getText()
					} else 
						objList << convertColumnValue(colValue, clazz)
				}
			} else throw new IllegalArgumentException("clazz is not a Primitive/Wrapper type");
		} catch(Exception e) {
			log.error e.message, e
			throw new AirFrameworkDAOException(e)
		} finally {
			sql?.close()
			sql = null
		}
		objList
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return Object of type mentioned as clazz argument
	 */
	static def <T> T toPrimitive(DataSource dataSource, String query, List<?> params, Class<T> clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		def objs = findPrimitives(dataSource, query, params, clazz)
		if(objs.size() > 0) objs[0]
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return List of Objects of type mentioned as clazz argument
	 */
	static def <T> List<T> toPrimitives(DataSource dataSource, String query, List<?> params, Class<T> clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		findPrimitives(dataSource, query, params, clazz)
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - JNDI name of Datasource 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return Object of type mentioned as clazz argument
	 */
	static def <T> T toPrimitive(String dataSource, String query, List<?> params, Class<T> clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		def objs = findPrimitive(lookup(dataSource), query, params, clazz)
		if(objs.size() > 0) objs[0]
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - JNDI name of Datasource 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return List of Objects of type mentioned as clazz argument
	 */
	static def <T> T toPrimitives(String dataSource, String query, List<?> params, Class<T> clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		findPrimitive(lookup(dataSource), query, params, clazz)
	}
	
	private static def findCollection = {ds, query, params, clazz ->
		def objList = [], obj, colName = "", field, sql, colValue, clob, colType
		try {
			sql = Sql.newInstance(ds)
			def fieldMap = mapFieldNames(clazz)
			if(!params) params = []
			sql.eachRow(query, params) {row ->
				obj = clazz.metaClass.invokeConstructor()
				(0..row.getMetaData().columnCount-1).each {
					colName = row.getMetaData().getColumnName(it+1)
					colType = row.getMetaData().getColumnName(it+1)
					field = fieldMap.find {it.key.compareToIgnoreCase(colName) == 0}
					if(!field) {
						log.debug "Field match by column name failed, matching with column label"
						colName = row.getMetaData().getColumnLabel(it+1)
						field = fieldMap.find {it.key.compareToIgnoreCase(colName) == 0}
					}
					if(field) {
						colValue = row."$colName";
						if(colValue instanceof java.sql.Clob) {
							clob = (java.sql.Clob) colValue
							obj."$field.value" = clob.getAsciiStream()?.getText()
						} else {
							if(tsTypes.count { it == colType } > 0) 
								obj."$field.value" = row.getTimestamp(colName)
							else
								obj."$field.value" = colValue
						}
					} 
				}
				objList << obj
			}
			def calAnn, clsExpr, clsure
			def calFlds = findCalculatedFields(clazz) 
			if(!calFlds.empty) {
				def shell = new GroovyShell()
				log.debug "There are ${calFlds?.size()} fields available for calculation" 
				calFlds.each {calFld ->
					log.debug "${calFld.name} is a calculated field"
					calAnn = calFld.getAnnotation(Calculated.class)
					clsExpr = "{it -> it.${calFld.name} = ${calAnn.expr()}}"
					clsure = shell.evaluate(clsExpr)
					objList.collect(clsure)
					clsure = null
				}
				calFlds = null
				shell = null
			}
		} catch(Exception e) {
			log.error e.message, e
			throw new AirFrameworkDAOException(e)
		} finally {
			sql?.close()
			sql = null
		}
		objList
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - Datasource for executing query 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return List of Objects of type mentioned as clazz argument
	 */
	static def <T> List<T> toList(DataSource dataSource, String query, List<?> params, Class clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		findCollection(dataSource, query, params, clazz)
	}
	
	/**
	 * Will execute given query and convert the result to object of specified class.
	 * 
	 * @param dataSource - JNDI name of Datasource 
	 * @param query - Select query
	 * @param params - List of parameters in query
	 * @param clazz - Return type
	 * @return List of Objects of type mentioned as clazz argument
	 */
	static def <T> List<T> toList(String dataSource, String query, List<?> params, Class clazz) throws AirFrameworkDAOException {
		log.debug "Executing query ${query} with parameters ${params} using datasource ${dataSource}"
		findCollection(lookup(dataSource), query, params, clazz)
	}
	
	private static List findCalculatedFields(Class<?> clazz) {
		def fields = clazz.getDeclaredFields()
		fields.grep{it.isAnnotationPresent(Calculated.class)}
	}
	
	private static Map mapFieldNames(Class<?> clazz) {
		def flds = [], fld, fldMap = [:]
		def metaProps = clazz.metaClass.properties
		def fields = [], decFlds
		while(clazz != Object.class) {
			decFlds = clazz.getDeclaredFields() as List
			fields.addAll decFlds
			clazz = clazz.superclass
		}
		metaProps.each {metaProp -> 
			fld = fields.find {'serialVersionUID' != it.name && metaProp.name.toLowerCase() == it.name.toLowerCase()}
			if(fld) flds << fld
		}
		flds.each{it-> 
			def fldName = it.name
			if(fldName != 'metaClass') {
				if(it.isAnnotationPresent(Column.class)) {
					def colAnn = it.getAnnotation(Column.class)
					fldMap[colAnn.name()] = fldName
				} else fldMap[fldName] = fldName
			}
		}
		log.debug "Fields available for mapping ${fldMap}"
		fldMap
	}
	
	private static def lookup = {jndiName ->
		def ctx, ds
		try {
			log.debug "Looking up for a datasource with JNDI name ${jndiName}"
			ctx = new InitialContext()
			ds = ctx.lookup(jndiName)
		} finally {
			ctx?.close()
		}
		return ds
	}.memoize()
}

