/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.sysinforekamedis.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import me.gaestra.sysinforekamedis.extras.Field;
import me.gaestra.sysinforekamedis.extras.annotations.MethodIgnorable;

/**
 *
 * @author Ganesha
 */
public class UtilityMiscHelper {
    
    public static String tableOf(Object obj) {
        return obj.getClass().getSimpleName().toLowerCase();
    }
    
    public static List<Field> getFields(Method[] methods, Object invoker, boolean request) { 
        return getFields(methods, invoker, request, true);
    }
    
    public static List<Field> getFields(Method[] methods, Object invoker, boolean request, boolean checkIgnorable) {
        List<Field> fields = new ArrayList<>();
        try {
            for (int i = methods.length - 1; i >= 0; i--) {
                if (!Modifier.isStatic(methods[i].getModifiers()) &&
                        methods[i].getName().contains("get")) {
                    if (checkIgnorable && methods[i].isAnnotationPresent(MethodIgnorable.class))
                        continue;
                    
                    String name = toFieldName(methods[i].getName());
                    Object value = methods[i].invoke(invoker, (Object[]) null);
                    Class<?> type = methods[i].getReturnType();

                    if (request || value != null) {
                        fields.add(new Field(name, value, type.getSimpleName()));
                    }
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            Logger.log(Logger.Level.Error, "Failed to get name of fields");
            e.printStackTrace();
        }

        return fields;
    }

    public static String toFieldName(String getterMethod) {
        // eliminate get** Prefix
        char[] field = getterMethod.substring(3).toCharArray();
        field[0] = getterMethod.toLowerCase().charAt(3);
        return new String(field);
    }
    
    public static String[] slice (String str, int perIndex) {
        LinkedList<String> arr = new LinkedList();
        if (str.length() <= perIndex || perIndex == 0) {
            arr.push(str);
            return (String[])arr.toArray();
        }
        
        for (var i = 0; i < str.length(); i += perIndex) {
            arr.addFirst(str.substring(i, i + perIndex));
        }
        
        return (String[])arr.toArray();
    }
    
    public static String formatCurrency(String amount) {
        if (amount == null || amount.isEmpty())
            return "";
        
        DecimalFormat formatter = new DecimalFormat("###,###,##0");
        return formatter.format(Double.parseDouble(amount));
    }
    
    public static int formatToNumber(String amount) {
        return Integer.valueOf(amount.replace(",", ""));
    }
    
    public static LocalDate formatToDate(String propValue) {
        Date date = null;

        try {
            date = new SimpleDateFormat(DateFormat.DATE_STRING_ISO_8601).parse(propValue);
        } catch (Exception e) { }
        
        try {
            date = new SimpleDateFormat(DateFormat.DATE_STRING_ISO_8601_DATE).parse(propValue);
        } catch (Exception e) { }
        
        try {
            date = new SimpleDateFormat(DateFormat.DATE_STRING_FORMAT_1).parse(propValue);
        } catch (Exception e) { }

        try {
            date = new SimpleDateFormat(DateFormat.DATE_STRING_FORMAT_2).parse(propValue);
        } catch (Exception e) { }
        
        try {
            date = new SimpleDateFormat(DateFormat.DATE_STRING_FORMAT_3).parse(propValue);
        } catch (Exception e) { }
         
        try {
            date = new SimpleDateFormat(DateFormat.DATE_STRING_FORMAT_3_REVERSED).parse(propValue);
        } catch (Exception e) { }

        return date != null ? LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault()) : null;
    }
    
    public interface DateFormat {
        public static final String DATE_STRING_ISO_8601             = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String DATE_STRING_ISO_8601_DATE        = "yyyy-MM-dd";
        public static final String DATE_STRING_FORMAT_1             = "dd.MM.yyyy";
        public static final String DATE_STRING_FORMAT_2             = "dd.MM.yyyy HH:mm:ss";
        public static final String DATE_STRING_FORMAT_3             = "yyyy/MM/dd";
        public static final String DATE_STRING_FORMAT_3_REVERSED    = "dd/MM/yyyy";
    }
}
