package me.codalot.core.gui;

@SuppressWarnings("unused")
public interface Scroll {

    int getPage();
    int getPages();
    int getPageSize();

    void setPage(int page);
    void nextPage();
    void prevPage();

}
