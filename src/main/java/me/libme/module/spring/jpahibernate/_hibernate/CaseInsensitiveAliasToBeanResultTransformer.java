package me.libme.module.spring.jpahibernate._hibernate;

import me.libme.kernel._c.bean.SimpleFieldOnClassFinder;
import org.hibernate.HibernateException;
import org.hibernate.transform.AliasedTupleSubsetResultTransformer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by J on 2017/10/15.
 */
public class CaseInsensitiveAliasToBeanResultTransformer extends AliasedTupleSubsetResultTransformer {

    private final Class resultClass;

    private Map<String,SetterMethodImpl> setters=new HashMap<>();

    private static Map<Class,CaseInsensitiveAliasToBeanResultTransformer> transformers=new HashMap<>();

    public static CaseInsensitiveAliasToBeanResultTransformer get(Class resultClass){
        if(transformers.containsKey(resultClass)){
            return transformers.get(resultClass);
        }else{
            CaseInsensitiveAliasToBeanResultTransformer transformer=
                    new CaseInsensitiveAliasToBeanResultTransformer(resultClass);
            transformers.put(resultClass,transformer);
            return transformer;
        }

    }


    private CaseInsensitiveAliasToBeanResultTransformer(Class resultClass) {
        if ( resultClass == null ) {
            throw new IllegalArgumentException( "resultClass cannot be null" );
        }
        this.resultClass = resultClass;
        initialize();
    }

    private void initialize() {

        SimpleFieldOnClassFinder simpleFieldOnClassFinder=new
                SimpleFieldOnClassFinder(resultClass);
        Method[] methods=resultClass.getMethods();
        simpleFieldOnClassFinder.find()
                .forEach(defaultFieldMeta -> {
                    String property=defaultFieldMeta.getFieldName();
                    String setterName=defaultFieldMeta.getSetterMethodName();
                    Class containerClass=defaultFieldMeta.getClazz();
                    Method setterMethod=null;
                    for(Method method :methods){
                        if(setterName.equals(method.getName())){
                            setterMethod=method;
                            break;
                        }
                    }
                    if(setterMethod==null){
                        throw new IllegalStateException("field["+property+"] miss setter method.");
                    }

                    SetterMethodImpl setterMethodImpl=new SetterMethodImpl(containerClass,property,setterMethod);
                    setters.put(property.toUpperCase(),setterMethodImpl);
                });


    }



    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {

        Object result;

        try {

            result = resultClass.newInstance();

            for ( int i = 0; i < aliases.length; i++ ) {
                String aliase=aliases[i];
                SetterMethodImpl setterMethod=setters.get(aliase.toUpperCase());
                if(setterMethod==null){
                    throw new IllegalStateException("can not find appropriate property to column ["+aliase+"]");
                }
                setterMethod.set(result,tuple[i]);
            }
        }
        catch ( InstantiationException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }
        catch ( IllegalAccessException e ) {
            throw new HibernateException( "Could not instantiate resultclass: " + resultClass.getName() );
        }

        return result;


    }

    @Override
    public List transformList(List list) {
        return super.transformList(list);
    }


    @Override
    public boolean isTransformedValueATupleElement(String[] aliases, int tupleLength) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if ( this == o ) {
            return true;
        }
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        CaseInsensitiveAliasToBeanResultTransformer that = ( CaseInsensitiveAliasToBeanResultTransformer ) o;

        if ( ! resultClass.equals( that.resultClass ) ) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = resultClass.hashCode();
        result = 31 * result;
        return result;
    }


}
