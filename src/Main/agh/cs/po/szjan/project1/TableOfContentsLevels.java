package agh.cs.po.szjan.project1;

/**
 * Created by szjan on 03.01.2018.
 */
public enum TableOfContentsLevels {
    TableOfContents,
    Section,
    Chapter,
    Title;

    public TableOfContentsLevels previous(){
        switch(this){
            case Section:
                return TableOfContents;
            case Chapter:
                return Section;
            case Title:
                return Chapter;
        }
        return null;
    }

    public TableOfContentsLevels next() {
        switch (this) {
            case TableOfContents:
                return Section;
            case Section:
                return Chapter;
            case Chapter:
                return Title;
        }
        return null;
    }
}
