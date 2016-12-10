package se.mattec.design.interfaces;

public interface ViewPagerHolder
{

    void registerViewPagerListener(int position, ViewPagerListener listener);

    void unregisterViewPagerListener(int position);

}
