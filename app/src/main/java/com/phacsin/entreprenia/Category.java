package com.phacsin.entreprenia;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.List;

/**
 * Created by GD on 9/6/2016.
 */
public class Category implements ParentListItem {

    // a recipe contains several ingredients
    private List events;
    String name;

    public Category(List events,String name)
    {
        this.events = events;
        this.name = name;
    }

    @Override
    public List getChildItemList() {
        return events;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getName()
    {
        return name;
    }
}