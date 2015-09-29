package be.foreseegroup.micro.resourceservice.unitsidecomposite.service;

import org.mockito.Mockito;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by Kaj on 29/09/15.
 */
public class MockitoFactoryBean<T> implements FactoryBean<T> {
    private Class<T> classToBeMocked;

    public MockitoFactoryBean(Class<T> classToBeMocked) {
        this.classToBeMocked = classToBeMocked;
    }


    @Override
    public T getObject() throws Exception {
        return Mockito.mock(classToBeMocked);
    }

    @Override
    public Class<?> getObjectType() {
        return classToBeMocked;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
