package agh.cs.po.szjan.project1;

import javax.print.Doc;

/**
 * Created by szjan on 29.12.2017.
 */
public enum Level {
    Document,
    Article,
    Paragraph,
    Point,
    Letter;

    public Level previous(){
        switch(this){
            case Article:
                return Document;
            case Paragraph:
                return Article;
            case Point:
                return Paragraph;
            case Letter:
                return Point;

        }
        return null;
    }

    public Level next() {
        switch (this) {
            case Document:
                return Article;
            case Article:
                return Paragraph;
            case Paragraph:
                return Point;
            case Point:
                return Letter;
        }
        return null;
    }
}
