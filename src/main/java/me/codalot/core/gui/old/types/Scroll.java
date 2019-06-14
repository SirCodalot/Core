package me.codalot.core.gui.old.types;

@SuppressWarnings("unused")
public interface Scroll {

    int getPage();
    int getPages();
    int getPageSize();

    void setPage(int page);
    void nextPage();
    void prevPage();

}
