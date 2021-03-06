package com.smash.revolance.process.monitoring.jvm;

import com.smash.revolance.process.monitoring.jvm.filter.By;
import com.smash.revolance.process.monitoring.jvm.filter.JvmSearchCriteria;
import com.smash.revolance.process.monitoring.jvm.filter.JvmSearchCriterias;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wsmash on 16/11/13.
 */
public class Jvms
{
    private List<Jvm> jvms = new ArrayList();

    public List<Jvm> getAll()
    {
        return jvms;
    }

    public static boolean isUnknown(Jvms jvms, Jvm jvm)
    {
        return isUnknown(jvms.getAll(), jvm);
    }

    public static boolean isUnknown(List<Jvm> jvms, Jvm jvm)
    {
        if(jvms.isEmpty())
        {
            return true;
        }

        JvmSearchCriterias criterias = new JvmSearchCriterias();
        criterias.add(By.VMID, jvm.getId());

        for(Jvm knownJvm : jvms)
        {
            if(Jvm.matches(knownJvm, criterias))
            {
                return false;
            }
        }

        return true;
    }

    public static List<Jvm> find(List<Jvm> jvmWatchers, JvmSearchCriteria criteria)
    {
        JvmSearchCriterias criterias = new JvmSearchCriterias();
        criterias.add(criteria);
        return find(jvmWatchers, criterias);
    }

    public static List<Jvm> find(List<Jvm> jvms, JvmSearchCriterias criterias)
    {
        List<Jvm> jvmList = new ArrayList<Jvm>();

        for(Jvm jvm : jvms)
        {
            if(Jvm.matches(jvm, criterias))
            {
                jvmList.add(jvm);
            }
        }

        return jvmList;
    }

    public boolean isUnknown(Jvm jvm)
    {
        return Jvms.isUnknown(this, jvm);
    }

    public void addJvm(Jvm jvm)
    {
        if(!jvms.contains(jvm))
            jvms.add(jvm);
    }

}
