package com.project.databases.generalisation;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.project.databases.MyConnection;
import com.project.databases.generalisation.annotation.AttributDb;
import com.project.databases.generalisation.annotation.IdDb;
import com.project.databases.generalisation.annotation.TableDb;
import com.project.databases.generalisation.utils.InfoObject;



public class DB {

    public static boolean isComplexObject(Class<?> type) throws Exception {
        if (type.isPrimitive())
            return false;

        if (type == String.class)
            return false;
        if (Number.class.isAssignableFrom(type))
            return false;
        if (type == Boolean.class || type == Character.class)
            return false;

        if (java.util.Date.class.isAssignableFrom(type))
            return false;
        if (java.time.temporal.Temporal.class.isAssignableFrom(type))
            return false;

        if (Collection.class.isAssignableFrom(type))
            return false;
        if (Map.class.isAssignableFrom(type))
            return false;

        Package pkg = type.getPackage();
        if (pkg != null && pkg.getName().startsWith("java."))
            return false;

        return true;
    }


    public static InfoObject getAllInformationsObjects(Object table) throws Exception {
        InfoObject infoObject = new InfoObject();
        Class<?> classTable = table.getClass();

        if (!classTable.isAnnotationPresent(TableDb.class)) 
            throw new Exception("Le classe '" + classTable.getSimpleName() + "' n'est pas relier a une table");

        TableDb annotation = classTable.getAnnotation(TableDb.class);
        infoObject.nameTable = annotation.name();
        int i = 0;

        List<String> nameAttributs = new ArrayList<>();
         List<String> nameAttributsInTable = new ArrayList<>();
         List<String> typeAttributs = new ArrayList<>();
         String typeIdAttribut = "";
        for (Field field : classTable.getDeclaredFields()) {
            if (field.isAnnotationPresent(AttributDb.class))
                i++;
        }

        if (i == 0)
            throw new Exception("Il n'y a aucun attribut dans votre classe '" + classTable.getSimpleName()
            + "' qui se relie a un table '" + infoObject.nameTable + "'");
        i = 0;
        for (Field field : classTable.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(AttributDb.class)) {
                nameAttributs.add(field.getName());
                nameAttributsInTable.add(field.getAnnotation(AttributDb.class).name());
                typeAttributs.add(field.getType().getName());
            }
            if (field.isAnnotationPresent(IdDb.class)) {
                infoObject.nameIdAttributs = (field.getName());
                infoObject.nameIdAttributsInTable = field.getAnnotation(AttributDb.class).name();
                infoObject.objectIdAttribut = field.getType();
                infoObject.valueId = field.get(table);
                infoObject.typeIdAttributs = field.getType().getName();
            }
        } 

       

        infoObject.nameAttributs = nameAttributs;
        infoObject.nameAttributsInTable = nameAttributsInTable;
        return infoObject;
    }


    public static Object getAll(Object table, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j)); 

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }


     public static Object getAllWhere(Object table, String where,Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }


    public static Object getAllOrder(Object table, String order ,Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " ORDER BY " + order;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }

    public static Object getAllOrderAndLimit(Object table, String order , String limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " ORDER BY " + order + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }

    public static Object getAllOrderAndLimit(Object table, String order , int limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " ORDER BY " + order + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }


    public static Object getAllLimit(Object table, int limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int") || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }

    public static Object getAllLimit(Object table, String limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int" )  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }
    public static Object getAllOrderAndWhere(Object table,String where , String order ,Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM "  + nameTable + " WHERE " + where + "  " + " ORDER BY " + order;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }

    public static Object getAllOrderAndLimitAndWhere(Object table,String where, String order , String limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM "  + nameTable + " WHERE " + where + "  " + " ORDER BY " + order + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }

    public static Object getAllOrderAndLimitAndWhere(Object table,String where, String order , int limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " + " ORDER BY " + order + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }


    public static Object getAllLimitAndWhere(Object table,String where, int limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " +" LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }

    public static Object getAllLimitAndWhere(Object table,String where, String limit, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        i = 0;

        Class<?> classTable = table.getClass();
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE " + where + "  " + " LIMIT " + limit;
        List<Object> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                Object data =  constructor.newInstance();
                for (Field field : data.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int")  || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(data, value);
                            } else if (isComplexObject(field.getType())) { 
                                if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))  
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(data, getById(classField.getConstructor().newInstance(), valeurId, connection));

                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
                result.add(data);
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }




    public static Object getById(Object table, Object id, Connection connection) throws Exception {
        InfoObject infoObject = DB.getAllInformationsObjects(table);
        String nameTable = infoObject.nameTable;
        String attrs = "";
        int i = 0;
        for (String attrTable : infoObject.nameAttributsInTable) {
            if (i > 0)
                attrs = attrs + " , ";
            attrs = attrs + attrTable;
            i++;
        }
        
        String idValue;
        if (id instanceof String) {
            idValue = "'" + ((String) id).replace("'", "''") + "'";
        } else if (id instanceof Integer || id instanceof Long || id instanceof Double || id instanceof Float) {
            idValue = id.toString();
        } else if (id == null) { 
            throw new Exception("L'ID du get by id ne peut pas être null");
        } else {
            idValue = "'" + id.toString().replace("'", "''") + "'";
        }
        
        String query = "SELECT " + attrs + " FROM " + nameTable + " WHERE "+infoObject.nameIdAttributsInTable+" = " + idValue;
        PreparedStatement statement = null;
        ResultSet resultset = null;
        boolean statementOpen = false;
        boolean resultsetOpen = false;
        
        Class<?> classTable = table.getClass();
        Object result = null;
        try {
            if (connection == null) 
                throw new Exception("Le connexion qui prend les '"+nameTable+"' n'est pas ouvert");
            
            statement = connection.prepareStatement(query);
            statementOpen = true;
           
            resultset = statement.executeQuery();
            resultsetOpen = true;
            int j = 0;
            while (resultset.next()) {
                Constructor<?> constructor = classTable.getConstructor();
                result =  constructor.newInstance();
                for (Field field : result.getClass().getDeclaredFields()) { 
                    j = 0;
                    for (String nameAttribut : infoObject.nameAttributs) {
                        if (field.getName().compareToIgnoreCase(nameAttribut) == 0) {
                            field.setAccessible(true);

                            if (field.getType().getName().equals("int") || field.getType().getName().equals("java.lang.Integer")) {
                                int value = resultset.getInt(infoObject.nameAttributsInTable.get(j));
                                field.set(result, value);
                            } else if (field.getType().getName().equals("java.lang.String")) {
                                String value = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                field.set(result, value);
                            } else if (field.getType().getName().equals("boolean")) {
                                boolean value = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                field.set(result, value);
                            } else if (field.getType().getName().equals("double")) {
                                double value = resultset.getDouble(infoObject.nameAttributsInTable.get(j));
                                field.set(result, value);
                            } else if (isComplexObject(field.getType())) { 
                                 if (field.getType().isArray()) {

                                } else {
                                    Class<?> classField = field.getType();  
                                    InfoObject infoObjectField = DB.getAllInformationsObjects(classField.getConstructor().newInstance());
                                    Object valeurId = null;
                                    if (infoObjectField.nameIdAttributs != null) {
                                        if (infoObjectField.objectIdAttribut.getName().equals("int") || infoObjectField.objectIdAttribut.getName().equals("java.lang.Integer")) {
                                            valeurId = resultset.getInt(infoObject.nameAttributsInTable.get(j)); 
                                        }
                                        else if (infoObjectField.objectIdAttribut.getName().equals("java.lang.String")) 
                                            valeurId = resultset.getString(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("boolean")) 
                                            valeurId = resultset.getBoolean(infoObject.nameAttributsInTable.get(j));
                                        else if (infoObjectField.objectIdAttribut.getName().equals("double"))   
                                            valeurId = resultset.getDouble(infoObject.nameAttributsInTable.get(j));

                                    }else{
                                        throw new Exception("L'attribut '" + infoObject.nameAttributs.get(j) + "' n'est pas annoter IdDb dans son class");
                                    }
                                    field.set(result, getById(classField.getConstructor().newInstance(), valeurId, connection));
                                }
                            }
                             else {
                                throw new Exception("Type non pris en charge pour le champ " + field.getName());
                            }
                        }
                        j++;
                    }
                }
            }
            
            statement.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (statementOpen) {
                statement.close();
            }
            if (resultsetOpen) {
                resultset.close();
            }
        }

        
        return result; 
    }

    public static void save(Object table, Connection connection)
			throws Exception {
		InfoObject infoObject = DB.getAllInformationsObjects(table);
        boolean isInsert = true;
        if (infoObject.valueId != null) {
            isInsert = false;
        }
        String query = "";

        if (isInsert) {
            String attrs = "";
            String data = "";
            int i = 0;
            for (String attrTable : infoObject.nameAttributsInTable) {
                if (!attrTable.equals(infoObject.nameIdAttributs)) {
                    if (i > 0){
                        attrs = attrs + " , ";
                        data = data + " , ";
                    }
                    attrs = attrs + attrTable;
                    data = data + " ? ";
                    i++;
                }
            }
            query = "INSERT INTO "+infoObject.nameTable+"("+attrs+") VALUES ("+data+")";
        }else{
            String attrs = "";
            int i = 0;
            for (String attrTable : infoObject.nameAttributsInTable) {
                if (!attrTable.equals(infoObject.nameIdAttributs)) {
                    if (i > 0){
                        attrs = attrs + " , ";
                    }
                    attrs = attrs + attrTable + " = ? ";
                    i++;
                }
            }
            query = "UPDATE "+infoObject.nameTable+" SET " + attrs + " WHERE " + infoObject.nameIdAttributsInTable + " = ?";
        }

        System.out.println(query + " query "); 
		PreparedStatement statement = null;
		ResultSet resultset = null;
		boolean statementOpen = false;
		boolean resultsetOpen = false;
		boolean closeable = false;
		
		try {
			if (connection == null) 
                throw new Exception("Le connexion qui sauvegarde le '"+infoObject.nameTable+"' n'est pas ouvert");

			statement = connection.prepareStatement(query);
            int j = 1;
            for (int i = 0; i < infoObject.nameAttributs.size(); i++) {
                if (!infoObject.nameAttributs.get(i).equals(infoObject.nameIdAttributs)) {
                for (Field field : table.getClass().getDeclaredFields()) { 
                    if (field.getName().compareToIgnoreCase(infoObject.nameAttributs.get(i)) == 0) {
                        field.setAccessible(true);
                        Class<?> type = field.getType();
                        Object value = field.get(table);

                        if (type.getName().equals("int") || type.getName().equals("java.lang.Integer")) {
                            if (value != null) {
                                statement.setInt(j, (Integer) value);
                            } else {
                                statement.setNull(j, java.sql.Types.INTEGER);
                            }

                        } else if (type.getName().equals("long") || type.getName().equals("java.lang.Long")) {
                            if (value != null) {
                                statement.setLong(j, (Long) value);
                            } else {
                                statement.setNull(j, java.sql.Types.BIGINT);
                            }

                        } else if (type.getName().equals("double") || type.getName().equals("java.lang.Double")) {
                            if (value != null) {
                                statement.setDouble(j, (Double) value);
                            } else {
                                statement.setNull(j, java.sql.Types.DOUBLE);
                            }

                        } else if (type.getName().equals("float") || type.getName().equals("java.lang.Float")) {
                            if (value != null) {
                                statement.setFloat(j, (Float) value);
                            } else {
                                statement.setNull(j, java.sql.Types.FLOAT);
                            }

                        } else if (type.getName().equals("boolean") || type.getName().equals("java.lang.Boolean")) {
                            if (value != null) {
                                statement.setBoolean(j, (Boolean) value);
                            } else {
                                statement.setNull(j, java.sql.Types.BOOLEAN);
                            }

                        } else if (type.getName().equals("java.lang.String")) {
                            statement.setString(j, (String) value);

                        } else if (type.getName().equals("java.sql.Date")) {
                            statement.setDate(j, (java.sql.Date) value);

                        } else if (type.getName().equals("java.sql.Time")) {
                            statement.setTime(j, (java.sql.Time) value);

                        } else if (type.getName().equals("java.sql.Timestamp")) {
                            statement.setTimestamp(j, (java.sql.Timestamp) value);

                        } else if (type.getName().equals("java.util.Date")) {
                            if (value != null) {
                                statement.setTimestamp(
                                    j,
                                    new java.sql.Timestamp(((java.util.Date) value).getTime())
                                );
                            } else {
                                statement.setNull(j, java.sql.Types.TIMESTAMP);
                            }

                        } else if (isComplexObject(type)) {

                            // Cas tableau / collection → ignoré ici
                            if (type.isArray() || java.util.Collection.class.isAssignableFrom(type)) {
                                statement.setNull(j, java.sql.Types.INTEGER);
                            }
                            else {
                                // Objet complexe (ex: Voiture)
                                if (value == null) {
                                    statement.setNull(j, java.sql.Types.INTEGER);
                                } else {

                                    // Charger les infos ORM de la classe (Voiture)
                                    Class<?> classField = type;
                                    InfoObject infoObjectField =
                                        DB.getAllInformationsObjects(value);

                                    if (infoObjectField.objectIdAttribut == null) {
                                        throw new Exception(
                                            "La classe " + classField.getSimpleName() +
                                            " n'a pas d'attribut annoté @IdDb"
                                        );
                                    }

                                    
                                    Object valeurId = infoObjectField.valueId;

                                    if (valeurId == null) {
                                        statement.setNull(j, java.sql.Types.INTEGER);
                                    }
                                    else {
                                        Class<?> idType = valeurId.getClass();

                                        if (idType == int.class || idType == Integer.class) {
                                            statement.setInt(j, (Integer) valeurId);

                                        } else if (idType == long.class || idType == Long.class) {
                                            statement.setLong(j, (Long) valeurId);

                                        } else if (idType == String.class) {
                                            statement.setString(j, (String) valeurId);

                                        } else {
                                            throw new Exception(
                                                "Type d'ID non supporté : " + idType.getName()
                                            );
                                        }
                                    }
                                }
                            }
                        }

                            else {
                            throw new Exception("Type non pris en charge pour le champ " + field.getName());
                        }
                    j++;
                    } 
                }
                    
                }
            }

            if (!isInsert) {
                Object value = infoObject.valueId;
                Class<?> type = value.getClass();

                        if (type.getName().equals("int") || type.getName().equals("java.lang.Integer")) {
                            if (value != null) {
                                statement.setInt(j, (Integer) value);
                            } else {
                                statement.setNull(j, java.sql.Types.INTEGER);
                            }

                        } else if (type.getName().equals("long") || type.getName().equals("java.lang.Long")) {
                            if (value != null) {
                                statement.setLong(j, (Long) value);
                            } else {
                                statement.setNull(j, java.sql.Types.BIGINT);
                            }

                        } else if (type.getName().equals("double") || type.getName().equals("java.lang.Double")) {
                            if (value != null) {
                                statement.setDouble(j, (Double) value);
                            } else {
                                statement.setNull(j, java.sql.Types.DOUBLE);
                            }

                        } else if (type.getName().equals("float") || type.getName().equals("java.lang.Float")) {
                            if (value != null) {
                                statement.setFloat(j, (Float) value);
                            } else {
                                statement.setNull(j, java.sql.Types.FLOAT);
                            }

                        } else if (type.getName().equals("boolean") || type.getName().equals("java.lang.Boolean")) {
                            if (value != null) {
                                statement.setBoolean(j, (Boolean) value);
                            } else {
                                statement.setNull(j, java.sql.Types.BOOLEAN);
                            }

                        } else if (type.getName().equals("java.lang.String")) {
                            statement.setString(j, (String) value);

                        } else if (type.getName().equals("java.sql.Date")) {
                            statement.setDate(j, (java.sql.Date) value);

                        } else if (type.getName().equals("java.sql.Time")) {
                            statement.setTime(j, (java.sql.Time) value);

                        } else if (type.getName().equals("java.sql.Timestamp")) {
                            statement.setTimestamp(j, (java.sql.Timestamp) value);

                        } else if (type.getName().equals("java.util.Date")) {
                            if (value != null) {
                                statement.setTimestamp(
                                    j,
                                    new java.sql.Timestamp(((java.util.Date) value).getTime())
                                );
                            } else {
                                statement.setNull(j, java.sql.Types.TIMESTAMP);
                            }

                        }
            }

			statementOpen = true;
			statement.executeUpdate();
			statement.close();
          

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw e;
		} finally {
			if (statementOpen) {
				statement.close();
			}
			
			if (closeable) {
				connection.close();
			}
		}
    }
    
}
